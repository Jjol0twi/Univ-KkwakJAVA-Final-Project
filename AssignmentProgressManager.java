class AssignmentProgressManager {
    // 과제 완료, 되돌리기, 전체 완료 여부를 관리하는 클래스입니다.
    Assignment[] completionHistory;
    int completionHistoryCount;

    // 완료 기록을 저장할 배열을 준비합니다.
    AssignmentProgressManager() {
        completionHistory = new Assignment[50];
        completionHistoryCount = 0;
    }

    // 새 주차가 시작될 때 완료 기록을 비웁니다.
    void resetHistory() {
        for (int i = 0; i < completionHistoryCount; i++) {
            completionHistory[i] = null;
        }

        completionHistoryCount = 0;
    }

    // 현재 표에서 가장 위에 있는 진행중 과제를 완료 처리합니다.
    Assignment markNextCompleted(AssignmentList assignmentList) {
        return markCompleted(findNextAssignment(assignmentList));
    }

    // 화면에 보이는 순위 번호로 과제를 찾아 완료 처리합니다.
    Assignment markCompletedByDisplayRank(
        AssignmentList assignmentList,
        int displayRank
    ) {
        return markCompleted(
            findCompletableAssignmentByDisplayRank(assignmentList, displayRank)
        );
    }

    // 사용자가 입력한 과목명과 같은 미완료 과목을 찾아 완료 처리합니다.
    Assignment markCompletedByTitle(AssignmentList assignmentList, String title) {
        return markCompleted(
            findCompletableAssignmentByTitle(assignmentList, title)
        );
    }

    // 과제를 완료 처리하고 되돌릴 수 있도록 기록합니다.
    Assignment markCompleted(Assignment assignment) {
        if (assignment == null) {
            return null;
        }

        assignment.completed = true;
        rememberCompletion(assignment);
        return assignment;
    }

    // 가장 최근에 완료한 과제를 다시 진행중으로 되돌립니다.
    Assignment undoLastCompletion() {
        while (completionHistoryCount > 0) {
            completionHistoryCount--;
            Assignment assignment = completionHistory[completionHistoryCount];
            completionHistory[completionHistoryCount] = null;

            if (assignment != null && assignment.completed && !assignment.isOverdue()) {
                assignment.completed = false;
                return assignment;
            }
        }

        return null;
    }

    // 되돌릴 완료 기록이 있는지 확인합니다.
    boolean hasCompletionHistory() {
        return completionHistoryCount > 0;
    }

    // 등록된 모든 진행 대상 과제가 완료되었는지 확인합니다.
    boolean isAllCompleted(AssignmentList assignmentList) {
        if (assignmentList.isEmpty()) {
            return false;
        }

        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);

            if (!assignment.completed && !assignment.isOverdue()) {
                return false;
            }
        }

        return true;
    }

    // 현재 표에서 가장 위에 있는 진행중 과제를 찾습니다.
    Assignment findNextAssignment(AssignmentList assignmentList) {
        Assignment best = null;

        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);

            if (isCompletable(assignment)) {
                if (best == null || assignment.displayRank < best.displayRank) {
                    best = assignment;
                }
            }
        }

        return best;
    }

    // 화면에 보이는 순위로 완료 가능한 과제를 찾습니다.
    Assignment findCompletableAssignmentByDisplayRank(
        AssignmentList assignmentList,
        int displayRank
    ) {
        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);

            if (isCompletable(assignment) && assignment.displayRank == displayRank) {
                return assignment;
            }
        }

        return null;
    }

    // 과목명으로 완료 가능한 과제를 찾습니다.
    Assignment findCompletableAssignmentByTitle(
        AssignmentList assignmentList,
        String title
    ) {
        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);

            if (isCompletable(assignment) && assignment.title.equals(title)) {
                return assignment;
            }
        }

        return null;
    }

    // 완료 가능한 진행중 과제인지 확인합니다.
    boolean isCompletable(Assignment assignment) {
        return assignment != null && !assignment.completed && !assignment.isOverdue();
    }

    // 완료한 과제를 되돌릴 수 있도록 기록합니다.
    void rememberCompletion(Assignment assignment) {
        if (completionHistoryCount < completionHistory.length) {
            completionHistory[completionHistoryCount] = assignment;
            completionHistoryCount++;
        }
    }
}
