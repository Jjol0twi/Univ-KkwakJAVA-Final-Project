import java.time.LocalDate;

class AssignmentService {

    // 프로그램의 전체 흐름만 담당합니다.
    int lastWeek;
    InputReader inputReader;
    AssignmentInputHandler inputHandler;
    AssignmentView view;
    GreedyAssignmentScheduler greedyAssignmentScheduler;
    AssignmentProgressManager progressManager;
    DeadlineCalculator deadlineCalculator;
    LocalDate lastAdjustedDate;

    // 프로그램 실행에 필요한 입력, 출력, 계산 담당 객체를 연결합니다.
    AssignmentService(
        InputReader inputReader,
        AssignmentInputHandler inputHandler,
        AssignmentView view,
        GreedyAssignmentScheduler greedyAssignmentScheduler,
        AssignmentProgressManager progressManager,
        DeadlineCalculator deadlineCalculator
    ) {
        lastWeek = 16;
        this.inputReader = inputReader;
        this.inputHandler = inputHandler;
        this.view = view;
        this.greedyAssignmentScheduler = greedyAssignmentScheduler;
        this.progressManager = progressManager;
        this.deadlineCalculator = deadlineCalculator;
        lastAdjustedDate = null;
    }

    // 처음 주차를 정하고, 한 주차가 끝날 때마다 다음 주차를 실행합니다.
    void run(String startWeekText) {
        System.out.println();
        System.out.println("[이번주 과제 우선순위 정리]");
        int week = inputHandler.readWeek(startWeekText);
        boolean running = true;

        // 한 주차가 모두 완료되면 다음 주차로 넘어갑니다.
        while (running) {
            running = runWeek(week);

            if (running) {
                if (week >= lastWeek) {
                    printLastWeekEvent();
                    return;
                }

                week++;
                System.out.println();
                System.out.println(week + "주차로 자동 이동합니다.");
            }
        }
    }

    // 마지막 주차를 완료했을 때 축하 메시지를 출력하고 프로그램을 마무리합니다.
    void printLastWeekEvent() {
        System.out.println();
        System.out.println("        *       .        *        .       *");
        System.out.println("   .        *        .        *        .");
        System.out.println("     _/\\_                             _/\\_");
        System.out.println("    <(o )___   한 학기 과제 완료   ___( o)>");
        System.out.println("     ( ._> /      수고했습니다!    \\ <_. )");
        System.out.println("      `---'                         `---'");
        System.out.println();
        System.out.println("KkwakJAVA가 과제 관리를 마무리합니다.");
    }

    // 한 주차의 과제 입력, 우선순위 출력, 완료 처리 흐름을 실행합니다.
    boolean runWeek(int week) {
        AssignmentList assignmentList = new AssignmentList(50);

        inputAssignments(week, assignmentList);

        if (assignmentList.isEmpty()) {
            System.out.println("입력된 과제가 없어 프로그램을 종료합니다.");
            return false;
        }

        return processCompletion(assignmentList, week);
    }

    // 과목명을 반복 입력받고, 빈 입력이나 exit가 들어오면 메뉴를 보여줍니다.
    void inputAssignments(int week, AssignmentList assignmentList) {
        boolean finished = false;

        view.printWeekInputHeader(week);

        while (!finished) {
            view.printAssignmentTable(assignmentList);
            String title = inputReader.readLine("과목명: ").trim();

            if (title.equals("")) {
                finished = handleInputMenu(assignmentList, true);
            } else if (isExitCommand(title)) {
                finished = handleInputMenu(assignmentList, false);
            } else {
                addAssignment(assignmentList, title);
            }
        }
    }

    // 입력 중단 상황에서 수정, 입력 종료, 추가 입력 메뉴를 처리합니다.
    boolean handleInputMenu(
        AssignmentList assignmentList,
        boolean canContinueInput
    ) {
        boolean selected = false;

        while (!selected) {
            view.printInputMenu(canContinueInput);
            String menu = inputReader.readLine("메뉴 입력: ").trim();

            switch (menu) {
                case "1":
                case "수정":
                    editAssignment(assignmentList);
                    selected = true;
                    break;
                case "2":
                case "입력 종료":
                case "입력종료":
                case "종료":
                    System.out.println("입력을 종료하고 우선순위를 정리합니다.");
                    return true;
                case "3":
                case "추가 입력":
                case "추가입력":
                case "추가":
                    if (canContinueInput) {
                        System.out.println("과목 입력을 계속합니다.");
                        selected = true;
                    } else {
                        System.out.println("지금은 추가 입력 메뉴를 사용할 수 없습니다.");
                    }
                    break;
                default:
                    System.out.println("1 또는 수정, 2 또는 입력 종료 중 하나를 입력해주세요.");

                    if (canContinueInput) {
                        System.out.println("3 또는 추가 입력도 사용할 수 있습니다.");
                    }
                    break;
            }
        }

        return false;
    }

    // 새 과제를 입력받아 목록에 추가하고 중요도 순위를 정리합니다.
    boolean addAssignment(AssignmentList assignmentList, String title) {
        if (assignmentList.isFull()) {
            System.out.println("최대 50개까지만 입력할 수 있습니다.");
            return false;
        }

        if (assignmentList.hasSameTitle(title)) {
            System.out.println(
                "이미 등록된 과목명입니다. 다른 과목명을 입력해주세요."
            );
            return false;
        }

        Assignment assignment = inputHandler.readAssignment(
            assignmentList.getActiveCount() + 1,
            title
        );
        if (assignment == null) {
            return false;
        }

        assignmentList.pushBackSameOrLowerImportance(assignment.importance);
        assignmentList.add(assignment);
        return true;
    }

    // 중요도 순위로 기존 과제를 선택한 뒤 새 내용으로 다시 입력받습니다.
    void editAssignment(AssignmentList assignmentList) {
        if (assignmentList.isEmpty()) {
            System.out.println("수정할 과목이 없습니다.");
            return;
        }

        int activeCount = assignmentList.getActiveCount();

        if (activeCount == 0) {
            System.out.println("수정할 과목이 없습니다.");
            return;
        }

        int number = inputReader.readIntInRange(
            "수정할 중요도 순위: ",
            1,
            activeCount
        );

        int index = assignmentList.findIndexByImportance(number);
        Assignment oldAssignment = assignmentList.get(index);
        int oldImportance = assignmentList.get(index).importance;
        assignmentList.removeAt(index);
        assignmentList.pullForwardLowerImportance(oldImportance);

        System.out.println();
        System.out.println(
            "중요도 " + number + "순위 항목을 새 내용으로 다시 입력합니다."
        );
        String title = inputHandler.readSubjectTitle();
        boolean edited = addAssignment(assignmentList, title);

        if (!edited) {
            assignmentList.pushBackSameOrLowerImportance(oldImportance);
            assignmentList.add(oldAssignment);
            System.out.println("수정이 취소되어 기존 과제를 유지합니다.");
        }
    }

    // 그리디 순서를 보여주고 사용자의 완료 입력을 처리합니다.
    boolean processCompletion(AssignmentList assignmentList, int week) {
        refreshRemainingDays(assignmentList);
        updatePriorityRanks(assignmentList);
        progressManager.resetHistory();

        // 완료되지 않은 과목이 남아 있는 동안 현재 순위를 유지하며 완료 입력을 받습니다.
        while (!progressManager.isAllCompleted(assignmentList)) {
            if (refreshRemainingDaysIfDateChanged(assignmentList)) {
                updatePriorityRanks(assignmentList);
            }

            view.printPriorityTable(week, assignmentList);
            view.printCompletionMenu(progressManager.hasCompletionHistory());

            String command = inputReader
                .readLine(
                    "완료 입력: "
                )
                .trim();

            if (isCompletionExitCommand(command)) {
                System.out.println("프로그램을 종료합니다.");
                return false;
            } else if (command.equals("")) {
                completeNextAssignment(assignmentList);
            } else if (isUndoCommand(command) && progressManager.hasCompletionHistory()) {
                undoLastCompletion();
            } else if (command.equals("되돌리기")) {
                System.out.println("되돌릴 완료 기록이 없습니다.");
            } else if (isRecalculateCommand(command)) {
                refreshRemainingDays(assignmentList);
                updatePriorityRanks(assignmentList);
                System.out.println("우선순위를 다시 계산합니다.");
            } else if (isPlanDoneCommand(command)) {
                completeNextAssignment(assignmentList);
            } else if (completeByScheduleOrder(assignmentList, command)) {
                // 순위 번호로 완료 처리된 경우입니다.
            } else {
                completeByTitle(assignmentList, command);
            }
        }

        view.printPriorityTable(week, assignmentList);
        System.out.println();
        System.out.println(week + "주차 과제가 모두 완료되었습니다.");
        return true;
    }

    // 프로그램 실행 중 날짜가 바뀌면 남은 기한을 자동으로 다시 계산합니다.
    boolean refreshRemainingDaysIfDateChanged(AssignmentList assignmentList) {
        LocalDate today = LocalDate.now();

        if (lastAdjustedDate == null) {
            refreshRemainingDays(assignmentList);
            return true;
        } else if (!lastAdjustedDate.equals(today)) {
            refreshRemainingDays(assignmentList);
            System.out.println("날짜가 바뀌어 남은 기한을 다시 계산했습니다.");
            return true;
        }

        return false;
    }

    // 제출 기한 날짜를 기준으로 모든 과제의 남은 기한을 다시 계산합니다.
    void refreshRemainingDays(AssignmentList assignmentList) {
        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);
            assignment.remainingDays = deadlineCalculator.calculateRemainingDays(
                assignment.deadlineText
            );

            if (assignment.isOverdue()) {
                assignment.completed = true;
                assignment.displayRank = 0;

                if (assignment.importance > 0) {
                    assignmentList.pullForwardLowerImportance(assignment.importance);
                    assignment.importance = 0;
                }
            }
        }

        lastAdjustedDate = LocalDate.now();
    }

    // 그리디 기준으로 순위를 다시 계산해 진행중인 과제에 저장합니다.
    void updatePriorityRanks(AssignmentList assignmentList) {
        GreedyScheduleItem[] schedule = greedyAssignmentScheduler.schedule(
            assignmentList
        );

        for (int i = 0; i < schedule.length; i++) {
            schedule[i].assignment.displayRank = i + 1;
        }
    }

    // Enter 또는 "계획대로 했음" 입력 시 현재 표의 가장 위 과제를 완료 처리합니다.
    void completeNextAssignment(AssignmentList assignmentList) {
        printCompletionResult(
            progressManager.markNextCompleted(assignmentList)
        );
    }

    // 표의 순위 번호를 입력했는지 확인하고, 맞으면 해당 과목을 완료 처리합니다.
    boolean completeByScheduleOrder(AssignmentList assignmentList, String command) {
        int order = getScheduleOrder(command);

        if (order == -1) {
            return false;
        }

        Assignment assignment = progressManager.markCompletedByDisplayRank(
            assignmentList,
            order
        );

        if (assignment != null) {
            System.out.println(
                assignment.title + " 과목을 완료 처리했습니다."
            );
        } else {
            System.out.println("진행중인 과제의 순위를 입력해주세요.");
        }

        return true;
    }

    // 사용자가 직접 입력한 과목명을 찾아 완료 처리합니다.
    void completeByTitle(AssignmentList assignmentList, String title) {
        Assignment assignment = progressManager.markCompletedByTitle(
            assignmentList,
            title
        );

        if (assignment != null) {
            System.out.println(title + " 과목을 완료 처리했습니다.");
        } else {
            System.out.println("해당 과목을 찾지 못했습니다.");
        }
    }

    // 가장 최근에 완료한 과제를 다시 진행중으로 되돌립니다.
    void undoLastCompletion() {
        Assignment assignment = progressManager.undoLastCompletion();

        if (assignment != null) {
            System.out.println(assignment.title + " 과목을 진행중으로 되돌렸습니다.");
        } else {
            System.out.println("되돌릴 완료 기록이 없습니다.");
        }
    }

    // 완료 처리 결과를 공통 문구로 출력합니다.
    void printCompletionResult(Assignment assignment) {
        if (assignment != null) {
            System.out.println(assignment.title + " 과목을 완료 처리했습니다.");
        } else {
            System.out.println("완료 처리할 과목이 없습니다.");
        }
    }

    // 사용자가 계획 완료 의미로 입력한 문장인지 확인합니다.
    boolean isPlanDoneCommand(String command) {
        return command.equals("계획대로 했음") || command.equals("계획대로");
    }

    // 재계산 명령어인지 확인합니다.
    boolean isRecalculateCommand(String command) {
        return command.equals("재계산")
            || command.equals("재계산?")
            || command.equals("재조정")
            || command.equals("0");
    }

    // 완료 메뉴에서 직전 완료를 되돌리는 입력인지 확인합니다.
    boolean isUndoCommand(String command) {
        return command.equals("되돌리기") || command.equals("1");
    }

    // 완료 메뉴에서 프로그램 종료를 뜻하는 입력인지 확인합니다.
    boolean isCompletionExitCommand(String command) {
        return isExitCommand(command) || command.equals("종료") || command.equals("99");
    }

    // exit 명령어인지 대소문자를 구분하지 않고 확인합니다.
    boolean isExitCommand(String title) {
        return title.equalsIgnoreCase("exit");
    }

    // "1", "1번", "1순위"처럼 순위를 뜻하는 입력에서 숫자를 뽑습니다.
    int getScheduleOrder(String command) {
        String orderText = command;

        if (orderText.endsWith("순위")) {
            orderText = orderText.substring(0, orderText.length() - 2).trim();
        } else if (orderText.endsWith("번")) {
            orderText = orderText.substring(0, orderText.length() - 1).trim();
        }

        if (orderText.equals("") || !inputReader.isOnlyDigits(orderText)) {
            return -1;
        }

        return Integer.parseInt(orderText);
    }
}
