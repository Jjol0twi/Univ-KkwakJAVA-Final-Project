import java.util.Scanner;

class InputReader {
    // Scanner 입력 처리를 한 곳에서 담당합니다.
    Scanner scanner;

    // 외부에서 만든 Scanner를 받아 입력 도구로 사용합니다.
    InputReader(Scanner scanner) {
        this.scanner = scanner;
    }

    // 정수 입력을 받을 때 사용합니다.
    // 빈 Enter나 숫자가 아닌 값이 들어오면 다시 입력받습니다.
    int readInt(String message) {
        while (true) {
            String text = readLine(message).trim();

            if (text.equals("")) {
                System.out.println("숫자를 입력해주세요.");
            } else if (!isOnlyDigits(text)) {
                System.out.println("숫자로 입력해주세요.");
            } else {
                return Integer.parseInt(text);
            }
        }
    }

    // 정수 입력 중 exit가 들어오면 현재 입력 취소를 뜻하는 -1을 돌려줍니다.
    int readIntOrCancel(String message) {
        while (true) {
            String text = readLine(message).trim();

            if (isExitCommand(text)) {
                return -1;
            } else if (text.equals("")) {
                System.out.println("숫자를 입력해주세요.");
            } else if (!isOnlyDigits(text)) {
                System.out.println("숫자로 입력해주세요.");
            } else {
                return Integer.parseInt(text);
            }
        }
    }

    // 최소값과 최대값 사이의 정수만 입력받습니다.
    // 메뉴 번호, 중요도 순위, 수정할 순위처럼 범위가 정해진 입력에 사용합니다.
    int readIntInRange(String message, int min, int max) {
        while (true) {
            int number = readInt(message);

            if (number >= min && number <= max) {
                return number;
            }

            System.out.println(min + "부터 " + max + "까지 입력해주세요.");
        }
    }

    // 범위가 있는 정수 입력 중 exit가 들어오면 현재 입력 취소를 뜻하는 -1을 돌려줍니다.
    int readIntInRangeOrCancel(String message, int min, int max) {
        while (true) {
            int number = readIntOrCancel(message);

            if (number == -1) {
                return -1;
            } else if (number >= min && number <= max) {
                return number;
            }

            System.out.println(min + "부터 " + max + "까지 입력해주세요.");
        }
    }

    // 범위가 있는 정수 입력에서 빈 Enter는 기본값으로 처리하고, exit는 입력 취소로 처리합니다.
    int readIntInRangeOrDefaultOrCancel(String message, int min, int max, int defaultNumber) {
        while (true) {
            String text = readLine(message).trim();

            if (isExitCommand(text)) {
                return -1;
            } else if (text.equals("")) {
                return defaultNumber;
            } else if (!isOnlyDigits(text)) {
                System.out.println("숫자로 입력해주세요.");
            } else {
                int number = Integer.parseInt(text);

                if (number >= min && number <= max) {
                    return number;
                }

                System.out.println(min + "부터 " + max + "까지 입력해주세요.");
            }
        }
    }

    // 한 줄 전체를 문자열로 입력받습니다.
    String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    // 문자열 입력 중 exit가 들어오면 현재 입력 취소를 뜻하는 null을 돌려줍니다.
    String readLineOrCancel(String message) {
        String text = readLine(message).trim();

        if (isExitCommand(text)) {
            return null;
        }

        return text;
    }

    // 문자열이 숫자로만 이루어져 있는지 확인합니다.
    // "abc", "1번" 같은 값을 걸러낼 때 사용합니다.
    boolean isOnlyDigits(String text) {
        int i;

        for (i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    // exit 명령어인지 대소문자를 구분하지 않고 확인합니다.
    boolean isExitCommand(String text) {
        return text.equalsIgnoreCase("exit");
    }
}
