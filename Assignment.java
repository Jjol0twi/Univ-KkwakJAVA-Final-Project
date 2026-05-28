class Assignment {
    // 과목 하나의 과제 정보를 저장하는 데이터 클래스입니다.
    String title;
    int onlineLectureCount;
    int submitAssignmentCount;
    int quizAssignmentCount;
    String deadlineText;
    int remainingDays;
    String memo;
    int importance;
    int displayRank;
    boolean completed;

    // 입력받은 과제 정보를 하나의 객체로 묶어서 저장합니다.
    Assignment(String title, int onlineLectureCount, int submitAssignmentCount, int quizAssignmentCount, String deadlineText, int remainingDays, String memo, int importance) {
        this.title = title;
        this.onlineLectureCount = onlineLectureCount;
        this.submitAssignmentCount = submitAssignmentCount;
        this.quizAssignmentCount = quizAssignmentCount;
        this.deadlineText = deadlineText;
        this.remainingDays = remainingDays;
        this.memo = memo;
        this.importance = importance;
        displayRank = 0;
        completed = false;
    }

    // 메모가 비어 있으면 출력용으로 "없음"을 돌려줍니다.
    String getMemoText() {
        if (memo.equals("")) {
            return "없음";
        }

        return memo;
    }

    // 완료 여부를 출력용 상태 문구로 바꿉니다.
    String getStatusText() {
        if (isOverdue()) {
            return "기한지남";
        } else if (completed) {
            return "완료";
        }

        return "진행중";
    }

    // 제출 기한이 이미 지났는지 확인합니다.
    boolean isOverdue() {
        return remainingDays < 0;
    }
}
