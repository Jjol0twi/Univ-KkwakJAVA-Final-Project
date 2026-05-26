class TextNumberParser {
    // "11주차", "5월 30일"처럼 글자가 섞인 입력에서 숫자만 뽑습니다.

    // 문장 안에서 가장 먼저 나온 숫자 하나를 찾아 돌려줍니다.
    int extractFirstNumber(String text) {
        int number = -1;
        int current = 0;
        boolean readingNumber = false;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch >= '0' && ch <= '9') {
                current = current * 10 + (ch - '0');
                readingNumber = true;
            } else if (readingNumber) {
                number = current;
                return number;
            }
        }

        if (readingNumber) {
            number = current;
        }

        return number;
    }

    // 문장 안에 있는 숫자들을 순서대로 배열에 저장하고 개수를 돌려줍니다.
    int extractNumbers(String text, int[] numbers) {
        int count = 0;
        int current = 0;
        boolean readingNumber = false;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            if (ch >= '0' && ch <= '9') {
                current = current * 10 + (ch - '0');
                readingNumber = true;
            } else if (readingNumber) {
                if (count < numbers.length) {
                    numbers[count] = current;
                    count++;
                }
                current = 0;
                readingNumber = false;
            }
        }

        if (readingNumber && count < numbers.length) {
            numbers[count] = current;
            count++;
        }

        return count;
    }
}
