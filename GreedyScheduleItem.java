class GreedyScheduleItem {
    // 그리디 결과로 출력할 과제 하나의 계산 정보를 저장합니다.
    Assignment assignment;
    int requiredMinutes;
    int urgencyPercent;
    int deadlineRiskScore;

    // 과제와 계산된 우선순위 정보를 하나의 출력 항목으로 묶습니다.
    GreedyScheduleItem(Assignment assignment, int requiredMinutes, int urgencyPercent, int deadlineRiskScore) {
        this.assignment = assignment;
        this.requiredMinutes = requiredMinutes;
        this.urgencyPercent = urgencyPercent;
        this.deadlineRiskScore = deadlineRiskScore;
    }

    // 화면에 보이는 한 과제의 우선순위 상세 내용을 출력합니다.
    void printOrder(int order) {
        System.out.println(order + "순위. " + assignment.title);
        System.out.println("   제출 기한: " + assignment.deadlineText + ", 남은 기간: " + assignment.remainingDays + "일, 예상 필요 시간: " + getRequiredTimeText());
        System.out.println("   온라인 강의: " + assignment.onlineLectureCount + "개, 제출 과제: " + assignment.submitAssignmentCount + "개, 퀴즈 과제: " + assignment.quizAssignmentCount + "개");
        System.out.println("   마감 초과 위험: " + getRiskText());
        System.out.println("   메모: " + assignment.getMemoText());
    }

    // 필요한 시간을 분 단위에서 "시간/분" 형태로 바꿉니다.
    String getRequiredTimeText() {
        int hours = requiredMinutes / 60;
        int minutes = requiredMinutes % 60;

        if (hours == 0) {
            return minutes + "분";
        } else if (minutes == 0) {
            return hours + "시간";
        }

        return hours + "시간 " + minutes + "분";
    }

    // 마감 위험 점수를 사용자가 이해하기 쉬운 문구로 바꿉니다.
    String getRiskText() {
        if (deadlineRiskScore == 3) {
            return "높음";
        } else if (deadlineRiskScore == 2) {
            return "중간";
        } else if (deadlineRiskScore == 1) {
            return "낮음";
        }

        return "없음";
    }
}
