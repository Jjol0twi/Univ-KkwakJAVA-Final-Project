class AssignmentView {
    // 표, 메뉴, 결과 출력만 담당하는 클래스입니다.

    // 해당 주차의 과제 입력을 시작한다는 안내 문구를 출력합니다.
    void printWeekInputHeader(int week) {
        System.out.println();
        System.out.println("[" + week + "주차 과제 입력]");
        System.out.println("과목명을 계속 입력하세요.");
        System.out.println("과목명에 exit를 입력하면 수정 또는 입력 종료를 선택할 수 있습니다.");
        System.out.println("프로그램을 바로 끝내려면 Ctrl + C를 누르면 됩니다.");
    }

    // 현재 입력된 과제 목록을 중요도 순위 기준 표 형태로 출력합니다.
    void printAssignmentTable(AssignmentList assignmentList) {
        System.out.println();
        System.out.println("[현재 입력된 과제 목록]");

        if (assignmentList.isEmpty()) {
            System.out.println("입력된 과제가 없습니다.");
            return;
        }

        printTableHeader();

        for (int importance = 1; importance <= assignmentList.getCount(); importance++) {
            Assignment assignment = assignmentList.findByImportance(importance);
            printTableRow(importance, assignment);
        }
    }

    // 과목명 입력을 멈췄을 때 보여줄 수정/종료/추가 입력 메뉴를 출력합니다.
    void printInputMenu(boolean canContinueInput) {
        System.out.println();

        if (canContinueInput) {
            System.out.println("과목명이 비어 있습니다. 원하는 작업을 선택해주세요.");
        }

        System.out.println("1. 수정");
        System.out.println("2. 입력 종료");

        if (canContinueInput) {
            System.out.println("3. 추가 입력");
        }
    }

    // 그리디 알고리즘으로 정리된 과제 수행 순서를 출력합니다.
    void printSchedule(int week, GreedyScheduleItem[] schedule) {
        System.out.println();
        System.out.println("[" + week + "주차 남은 과제 먼저 해야 할 순서]");

        if (schedule.length == 0) {
            System.out.println("남은 과제가 없습니다.");
            return;
        }

        for (int i = 0; i < schedule.length; i++) {
            schedule[i].printOrder(i + 1);
        }
    }

    // 과제 목록 표의 제목 줄과 구분선을 출력합니다.
    void printTableHeader() {
        System.out.println(formatTableRow("순위", "과목명", "강의", "제출", "퀴즈", "남은기한", "상태", "메모"));
        System.out.println(repeat("-", 86));
    }

    // 과제 하나를 표의 한 줄로 출력합니다.
    void printTableRow(int importance, Assignment assignment) {
        System.out.println(formatTableRow(
                String.valueOf(importance),
                assignment.title,
                assignment.onlineLectureCount + "개",
                assignment.submitAssignmentCount + "개",
                assignment.quizAssignmentCount + "개",
                assignment.remainingDays + "일",
                assignment.getStatusText(),
                assignment.getMemoText()));
    }

    // 표의 각 칸 너비를 맞춰 한 줄 문자열을 만듭니다.
    String formatTableRow(String rank, String title, String lecture, String submit, String quiz, String remainingDays, String status, String memo) {
        return padLeft(rank, 4) + "  "
                + padRight(shortenText(title, 16), 16) + "  "
                + padLeft(lecture, 6) + "  "
                + padLeft(submit, 6) + "  "
                + padLeft(quiz, 6) + "  "
                + padLeft(remainingDays, 8) + "  "
                + padRight(status, 8) + "  "
                + memo;
    }

    // 문자열을 오른쪽에 공백을 붙여 지정한 화면 너비에 맞춥니다.
    String padRight(String text, int width) {
        String result = text;

        while (getDisplayWidth(result) < width) {
            result += " ";
        }

        return result;
    }

    // 문자열을 왼쪽에 공백을 붙여 지정한 화면 너비에 맞춥니다.
    String padLeft(String text, int width) {
        String result = text;

        while (getDisplayWidth(result) < width) {
            result = " " + result;
        }

        return result;
    }

    // 정해진 화면 너비보다 긴 문자열은 뒤에 ...을 붙여 줄입니다.
    String shortenText(String text, int width) {
        if (getDisplayWidth(text) <= width) {
            return text;
        }

        String result = "";
        int currentWidth = 0;
        int maxTextWidth = width - 3;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            int charWidth = getCharDisplayWidth(ch);

            if (currentWidth + charWidth > maxTextWidth) {
                break;
            }

            result += ch;
            currentWidth += charWidth;
        }

        return result + "...";
    }

    // 한글은 보통 터미널에서 2칸을 차지하므로 화면 표시 너비를 따로 계산합니다.
    int getDisplayWidth(String text) {
        int width = 0;

        for (int i = 0; i < text.length(); i++) {
            width += getCharDisplayWidth(text.charAt(i));
        }

        return width;
    }

    // 한 글자가 터미널에서 차지하는 칸 수를 계산합니다.
    int getCharDisplayWidth(char ch) {
        if (ch > 127) {
            return 2;
        }

        return 1;
    }

    // 같은 문자를 count번 반복한 문자열을 만듭니다.
    String repeat(String text, int count) {
        String result = "";

        for (int i = 0; i < count; i++) {
            result += text;
        }

        return result;
    }
}
