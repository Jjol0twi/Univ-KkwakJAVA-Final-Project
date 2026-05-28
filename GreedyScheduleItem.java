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
}
