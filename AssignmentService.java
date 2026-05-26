class AssignmentService {
    // 프로그램의 전체 흐름만 담당합니다.
    int lastWeek;
    InputReader inputReader;
    AssignmentInputHandler inputHandler;
    AssignmentView view;
    GreedyAssignmentScheduler greedyAssignmentScheduler;
    AssignmentProgressManager progressManager;

    // 프로그램 실행에 필요한 입력, 출력, 계산 담당 객체를 연결합니다.
    AssignmentService(InputReader inputReader, AssignmentInputHandler inputHandler, AssignmentView view, GreedyAssignmentScheduler greedyAssignmentScheduler, AssignmentProgressManager progressManager) {
        lastWeek = 16;
        this.inputReader = inputReader;
        this.inputHandler = inputHandler;
        this.view = view;
        this.greedyAssignmentScheduler = greedyAssignmentScheduler;
        this.progressManager = progressManager;
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
        System.out.println("       _/\\_                         _/\\_");
        System.out.println("    <(o )___   한 학기 과제 완료   ___( o)>");
        System.out.println("     ( ._> /      수고했습니다!      \\ <_. )");
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
    boolean handleInputMenu(AssignmentList assignmentList, boolean canContinueInput) {
        view.printInputMenu(canContinueInput);
        int maxMenu = 2;

        if (canContinueInput) {
            maxMenu = 3;
        }

        int menu = inputReader.readIntInRange("번호 선택: ", 1, maxMenu);

        switch (menu) {
            case 1:
                editAssignment(assignmentList);
                break;
            case 2:
                System.out.println("입력을 종료하고 우선순위를 정리합니다.");
                return true;
            case 3:
                System.out.println("과목 입력을 계속합니다.");
                break;
        }

        return false;
    }

    // 새 과제를 입력받아 목록에 추가하고 중요도 순위를 정리합니다.
    boolean addAssignment(AssignmentList assignmentList, String title) {
        if (assignmentList.isFull()) {
            System.out.println("최대 50개까지만 입력할 수 있습니다.");
            return false;
        }

        Assignment assignment = inputHandler.readAssignment(assignmentList.getCount() + 1, title);
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

        int number = inputReader.readIntInRange("수정할 중요도 순위: ", 1, assignmentList.getCount());

        int index = assignmentList.findIndexByImportance(number);
        Assignment oldAssignment = assignmentList.get(index);
        int oldImportance = assignmentList.get(index).importance;
        assignmentList.removeAt(index);
        assignmentList.pullForwardLowerImportance(oldImportance);

        System.out.println();
        System.out.println("중요도 " + number + "순위 항목을 새 내용으로 다시 입력합니다.");
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
        // 완료되지 않은 과목이 남아 있는 동안 그리디 우선순위를 계속 다시 계산합니다.
        while (!progressManager.isAllCompleted(assignmentList)) {
            GreedyScheduleItem[] schedule = greedyAssignmentScheduler.schedule(assignmentList);
            view.printSchedule(week, schedule);

            String command = inputReader.readLine("완료한 과목명 또는 '계획대로 했음' 입력(exit 종료): ").trim();

            if (isExitCommand(command)) {
                System.out.println("프로그램을 종료합니다.");
                return false;
            } else if (isPlanDoneCommand(command)) {
                completeByPlan(schedule);
            } else {
                completeByTitle(assignmentList, command);
            }
        }

        System.out.println();
        System.out.println(week + "주차 과제가 모두 완료되었습니다.");
        return true;
    }

    // "계획대로 했음" 입력 시 현재 1순위 과제를 완료 처리합니다.
    void completeByPlan(GreedyScheduleItem[] schedule) {
        if (progressManager.markCompletedByPlan(schedule)) {
            System.out.println(schedule[0].assignment.title + " 과목을 완료 처리했습니다.");
        } else {
            System.out.println("완료 처리할 과목이 없습니다.");
        }
    }

    // 사용자가 직접 입력한 과목명을 찾아 완료 처리합니다.
    void completeByTitle(AssignmentList assignmentList, String title) {
        if (progressManager.markCompletedByTitle(assignmentList, title)) {
            System.out.println(title + " 과목을 완료 처리했습니다.");
        } else {
            System.out.println("해당 과목을 찾지 못했습니다.");
        }
    }

    // 사용자가 계획 완료 의미로 입력한 문장인지 확인합니다.
    boolean isPlanDoneCommand(String command) {
        return command.equals("계획대로 했음") || command.equals("계획대로");
    }

    // exit 명령어인지 대소문자를 구분하지 않고 확인합니다.
    boolean isExitCommand(String title) {
        return title.equalsIgnoreCase("exit");
    }
}
