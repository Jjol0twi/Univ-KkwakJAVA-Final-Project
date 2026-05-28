class GreedyAssignmentScheduler {
    // 남은 과제 중 지금 가장 먼저 할 과목을 하나씩 고르는 그리디 스케줄러입니다.
    StudyTimeCalculator studyTimeCalculator;

    // 과제별 필요 시간과 가능한 시간을 계산할 객체를 저장합니다.
    GreedyAssignmentScheduler(StudyTimeCalculator studyTimeCalculator) {
        this.studyTimeCalculator = studyTimeCalculator;
    }

    // 미완료 과제를 대상으로 매번 가장 좋은 선택을 골라 전체 순서를 만듭니다.
    GreedyScheduleItem[] schedule(AssignmentList assignmentList) {
        int incompleteCount = countIncompleteAssignments(assignmentList);
        GreedyScheduleItem[] result = new GreedyScheduleItem[incompleteCount];
        boolean[] selected = new boolean[assignmentList.getCount()];

        // 매 단계마다 아직 선택되지 않은 과목 중 가장 좋은 과목을 하나 고릅니다.
        for (int order = 0; order < incompleteCount; order++) {
            int bestIndex = findBestAssignment(assignmentList, selected);
            selected[bestIndex] = true;
            result[order] = makeScheduleItem(assignmentList.get(bestIndex));
        }

        return result;
    }

    // 아직 완료되지 않은 과제가 몇 개인지 셉니다.
    int countIncompleteAssignments(AssignmentList assignmentList) {
        int incompleteCount = 0;

        for (int i = 0; i < assignmentList.getCount(); i++) {
            if (!assignmentList.get(i).completed && !assignmentList.get(i).isOverdue()) {
                incompleteCount++;
            }
        }

        return incompleteCount;
    }

    // 아직 선택되지 않은 과제 중 현재 기준에서 가장 먼저 해야 할 과제를 찾습니다.
    int findBestAssignment(AssignmentList assignmentList, boolean[] selected) {
        int bestIndex = -1;
        GreedyScheduleItem bestItem = null;

        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);

            if (!selected[i] && !assignment.completed && !assignment.isOverdue()) {
                GreedyScheduleItem item = makeScheduleItem(assignment);

                if (bestIndex == -1 || isBetter(item, bestItem)) {
                    bestIndex = i;
                    bestItem = item;
                }
            }
        }

        return bestIndex;
    }

    // 두 과제를 비교해서 current가 best보다 먼저 해야 할 과제인지 판단합니다.
    boolean isBetter(GreedyScheduleItem current, GreedyScheduleItem best) {
        // 비교 순서: 마감 초과 위험 -> 중요도 -> 긴급도 -> 작업량 -> 제출 과제 수
        if (current.deadlineRiskScore > best.deadlineRiskScore) {
            return true;
        } else if (current.deadlineRiskScore < best.deadlineRiskScore) {
            return false;
        }

        if (current.assignment.importance < best.assignment.importance) {
            return true;
        } else if (current.assignment.importance > best.assignment.importance) {
            return false;
        }

        if (current.urgencyPercent > best.urgencyPercent) {
            return true;
        } else if (current.urgencyPercent < best.urgencyPercent) {
            return false;
        }

        if (current.requiredMinutes > best.requiredMinutes) {
            return true;
        } else if (current.requiredMinutes < best.requiredMinutes) {
            return false;
        }

        return getTypeScore(current.assignment) > getTypeScore(best.assignment);
    }

    // 과제 하나에 대해 시간, 긴급도, 위험도를 계산해 일정 항목으로 만듭니다.
    GreedyScheduleItem makeScheduleItem(Assignment assignment) {
        int requiredMinutes = studyTimeCalculator.getRequiredMinutes(assignment);
        int availableMinutes = studyTimeCalculator.getAvailableMinutes(assignment.remainingDays);
        int urgencyPercent = getUrgencyPercent(requiredMinutes, availableMinutes);
        int deadlineRiskScore = getDeadlineRiskScore(assignment, requiredMinutes, availableMinutes);

        return new GreedyScheduleItem(assignment, requiredMinutes, urgencyPercent, deadlineRiskScore);
    }

    // 가능한 시간 중 필요한 시간이 차지하는 비율을 퍼센트로 계산합니다.
    int getUrgencyPercent(int requiredMinutes, int availableMinutes) {
        if (availableMinutes <= 0) {
            return 999;
        }

        return requiredMinutes * 100 / availableMinutes;
    }

    // 마감 초과 가능성과 남은 기간을 기준으로 위험 점수를 계산합니다.
    int getDeadlineRiskScore(Assignment assignment, int requiredMinutes, int availableMinutes) {
        if (requiredMinutes > availableMinutes) {
            return 3;
        } else if (assignment.remainingDays <= 1) {
            return 2;
        } else if (assignment.remainingDays <= 3) {
            return 1;
        }

        return 0;
    }

    // 제출 과제가 많을수록 더 먼저 처리하도록 유형 점수를 계산합니다.
    int getTypeScore(Assignment assignment) {
        return assignment.submitAssignmentCount * 5;
    }

}
