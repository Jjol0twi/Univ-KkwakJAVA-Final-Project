class StudyTimeCalculator {
    // 강의 1개 60분, 제출 과제 1개 60분, 퀴즈 과제 1개 30분으로 계산합니다.

    // 과제 하나를 끝내는 데 필요한 전체 시간을 분 단위로 계산합니다.
    int getRequiredMinutes(Assignment assignment) {
        int lectureMinutes = assignment.onlineLectureCount * 60;
        int submitMinutes = assignment.submitAssignmentCount * 60;
        int quizMinutes = assignment.quizAssignmentCount * 30;

        return lectureMinutes + submitMinutes + quizMinutes;
    }

    // 오늘부터 마감일까지 확보할 수 있는 공부 시간을 분 단위로 계산합니다.
    int getAvailableMinutes(int remainingDays) {
        int availableMinutes = 0;

        // 오늘부터 마감일까지 평일은 3시간, 주말은 5시간 공부할 수 있다고 가정합니다.
        for (int i = 0; i <= remainingDays; i++) {
            if (isWeekend(i)) {
                availableMinutes += 300;
            } else {
                availableMinutes += 180;
            }
        }

        return availableMinutes;
    }

    // 오늘로부터 며칠 뒤의 날짜가 주말인지 확인합니다.
    boolean isWeekend(int daysAfterToday) {
        int day = java.time.LocalDate.now().plusDays(daysAfterToday).getDayOfWeek().getValue();

        return day == 6 || day == 7;
    }
}
