class AssignmentInputHandler {
    // 사용자 입력을 Assignment 객체로 바꾸는 클래스입니다.
    InputReader inputReader;
    TextNumberParser textNumberParser;
    DeadlineCalculator deadlineCalculator;

    // 입력 처리에 필요한 도구들을 받아서 저장합니다.
    AssignmentInputHandler(InputReader inputReader, TextNumberParser textNumberParser, DeadlineCalculator deadlineCalculator) {
        this.inputReader = inputReader;
        this.textNumberParser = textNumberParser;
        this.deadlineCalculator = deadlineCalculator;
    }

    // 실행 인자로 주차가 들어오면 그 값을 쓰고, 없으면 직접 입력받습니다.
    int readWeek(String startWeekText) {
        int week = parseWeek(startWeekText);

        if (week > 0) {
            System.out.println(week + "주차로 시작합니다.");
            return week;
        }

        if (!startWeekText.equals("")) {
            System.out.println("시작 주차를 인식하지 못해 직접 입력받습니다.");
        }

        return readWeek();
    }

    // 사용자가 직접 입력한 "11주차" 같은 문장에서 주차 숫자를 뽑습니다.
    int readWeek() {
        String weekText = inputReader.readLine("이번주는 몇 주차인가요? ");
        int week = parseWeek(weekText);

        while (week <= 0) {
            System.out.println("예: 11주차 처럼 숫자가 들어가게 입력해주세요.");
            weekText = inputReader.readLine("이번주는 몇 주차인가요? ");
            week = parseWeek(weekText);
        }

        return week;
    }

    // 주차 문장 안에서 실제 주차 숫자만 추출합니다.
    int parseWeek(String weekText) {
        return textNumberParser.extractFirstNumber(weekText);
    }

    // 과목명 외의 과제 정보를 입력받아 Assignment 객체를 만듭니다.
    Assignment readAssignment(int maxImportance, String title) {
        int onlineLectureCount = inputReader.readIntOrCancel("온라인 강의 개수: ");
        if (onlineLectureCount == -1) {
            return cancelAssignmentInput();
        }

        int submitAssignmentCount = inputReader.readIntOrCancel("제출 과제 개수: ");
        if (submitAssignmentCount == -1) {
            return cancelAssignmentInput();
        }

        int quizAssignmentCount = inputReader.readIntOrCancel("퀴즈 과제 개수: ");
        if (quizAssignmentCount == -1) {
            return cancelAssignmentInput();
        }

        String deadlineText = readDeadlineText();
        if (deadlineText == null) {
            return cancelAssignmentInput();
        }

        int remainingDays = deadlineCalculator.calculateRemainingDays(deadlineText);
        String memo = inputReader.readLineOrCancel("추가 메모(강의/퀴즈 주제): ");
        if (memo == null) {
            return cancelAssignmentInput();
        }

        int importance = readImportance(maxImportance);
        if (importance == -1) {
            return cancelAssignmentInput();
        }

        return new Assignment(title, onlineLectureCount, submitAssignmentCount, quizAssignmentCount, deadlineText, remainingDays, memo, importance);
    }

    // 과목명 외의 입력에서 exit가 들어오면 현재 과제 등록을 취소합니다.
    Assignment cancelAssignmentInput() {
        System.out.println("현재 과제 입력을 취소하고 과목명부터 다시 입력합니다.");
        return null;
    }

    // 수정할 때 사용할 과목명을 입력받고, 빈 과목명은 다시 입력받습니다.
    String readSubjectTitle() {
        String title = inputReader.readLine("과목명: ").trim();

        while (title.equals("")) {
            System.out.println("과목명은 비워둘 수 없습니다.");
            title = inputReader.readLine("과목명: ").trim();
        }

        return title;
    }

    // 제출 기한 날짜를 입력받고, 날짜로 바꿀 수 있을 때까지 다시 묻습니다.
    String readDeadlineText() {
        String deadlineText = inputReader.readLineOrCancel("제출 기한 날짜(예: 5월 30일, 5/30, 2026-05-30): ");

        while (deadlineText != null && !deadlineCalculator.isValidDeadline(deadlineText)) {
            System.out.println("날짜를 다시 입력해주세요. 예: 5월 30일, 5/30, 2026-05-30");
            deadlineText = inputReader.readLineOrCancel("제출 기한 날짜: ");
        }

        return deadlineText;
    }

    // 현재 등록 가능한 범위 안에서 중요도 순위를 입력받습니다.
    int readImportance(int maxImportance) {
        if (maxImportance == 1) {
            System.out.println("중요도 순위는 1순위로 자동 입력됩니다.");
            return 1;
        }

        return inputReader.readIntInRangeOrDefaultOrCancel("중요도 순위(숫자가 낮을수록 중요, 입력 가능: 1~" + maxImportance + ", Enter 입력 시 " + maxImportance + "순위): ", 1, maxImportance, maxImportance);
    }
}
