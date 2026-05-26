import java.util.Scanner;

public class KkwakJAVA {
    // 프로그램 시작점입니다. 필요한 객체를 만들고 과제 서비스를 실행합니다.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InputReader inputReader = new InputReader(scanner);
        String startWeekText = makeStartWeekText(args);

        // 프로그램에서 사용할 객체들을 먼저 만들고 서로 연결합니다.
        TextNumberParser textNumberParser = new TextNumberParser();
        DeadlineCalculator deadlineCalculator = new DeadlineCalculator(textNumberParser);
        StudyTimeCalculator studyTimeCalculator = new StudyTimeCalculator();
        GreedyAssignmentScheduler greedyAssignmentScheduler = new GreedyAssignmentScheduler(studyTimeCalculator);
        AssignmentProgressManager progressManager = new AssignmentProgressManager();
        AssignmentInputHandler inputHandler = new AssignmentInputHandler(inputReader, textNumberParser, deadlineCalculator);
        AssignmentView assignmentView = new AssignmentView();
        AssignmentService assignmentService = new AssignmentService(inputReader, inputHandler, assignmentView, greedyAssignmentScheduler, progressManager);

        System.out.println("KkwakJAVA를 시작합니다.");
        assignmentService.run(startWeekText);

        scanner.close();
    }

    // 실행할 때 같이 입력한 값을 하나의 주차 문장으로 합칩니다.
    static String makeStartWeekText(String[] args) {
        String result = "";

        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                result += " ";
            }

            result += args[i];
        }

        return result;
    }
}
