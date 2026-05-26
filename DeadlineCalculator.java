import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class DeadlineCalculator {
    // 사용자가 입력한 날짜 문장을 실제 날짜와 남은 일수로 바꿉니다.
    TextNumberParser textNumberParser;

    // 날짜 문장에서 숫자를 뽑기 위한 파서를 저장합니다.
    DeadlineCalculator(TextNumberParser textNumberParser) {
        this.textNumberParser = textNumberParser;
    }

    // 제출 기한 문장을 날짜로 바꾼 뒤 오늘부터 며칠 남았는지 계산합니다.
    int calculateRemainingDays(String deadlineText) {
        LocalDate today = LocalDate.now();
        LocalDate deadline = parseDeadline(deadlineText, today);

        return (int) ChronoUnit.DAYS.between(today, deadline);
    }

    // 제출 기한 입력값이 날짜로 변환 가능한지 확인합니다.
    boolean isValidDeadline(String deadlineText) {
        try {
            parseDeadline(deadlineText, LocalDate.now());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 2026-05-30, 5/30, 30 같은 입력을 LocalDate 객체로 변환합니다.
    LocalDate parseDeadline(String deadlineText, LocalDate today) {
        int[] numbers = new int[3];
        int count = textNumberParser.extractNumbers(deadlineText, numbers);

        if (count >= 3) {
            return LocalDate.of(numbers[0], numbers[1], numbers[2]);
        } else if (count == 2) {
            return LocalDate.of(today.getYear(), numbers[0], numbers[1]);
        } else if (count == 1) {
            return LocalDate.of(today.getYear(), today.getMonthValue(), numbers[0]);
        }

        throw new IllegalArgumentException();
    }
}
