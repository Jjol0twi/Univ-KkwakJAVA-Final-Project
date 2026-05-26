class AssignmentList {
    // 과제 배열과 개수를 함께 관리하는 클래스입니다.
    Assignment[] assignments;
    int count;

    // 지정한 크기만큼 과제 배열을 만들고 개수를 0으로 시작합니다.
    AssignmentList(int size) {
        assignments = new Assignment[size];
        count = 0;
    }

    // 등록된 과제가 하나도 없는지 확인합니다.
    boolean isEmpty() {
        return count == 0;
    }

    // 배열이 꽉 차서 더 이상 과제를 넣을 수 없는지 확인합니다.
    boolean isFull() {
        return count >= assignments.length;
    }

    // 현재 등록된 과제 개수를 돌려줍니다.
    int getCount() {
        return count;
    }

    // 배열의 index 위치에 있는 과제를 돌려줍니다.
    Assignment get(int index) {
        return assignments[index];
    }

    // 새 과제를 배열의 마지막 위치에 추가합니다.
    void add(Assignment assignment) {
        assignments[count] = assignment;
        count++;
    }

    // index 위치의 과제를 삭제하고 뒤의 과제들을 앞으로 당깁니다.
    void removeAt(int index) {
        for (int i = index; i < count - 1; i++) {
            assignments[i] = assignments[i + 1];
        }

        assignments[count - 1] = null;
        count--;
    }

    // 중요도 순위가 일치하는 과제의 배열 위치를 찾습니다.
    int findIndexByImportance(int importance) {
        for (int i = 0; i < count; i++) {
            if (assignments[i].importance == importance) {
                return i;
            }
        }

        return 0;
    }

    // 중요도 순위를 기준으로 과제 객체를 찾습니다.
    Assignment findByImportance(int importance) {
        return assignments[findIndexByImportance(importance)];
    }

    // 새 과목의 중요도와 같거나 낮은 기존 과목들을 한 칸씩 뒤로 밉니다.
    void pushBackSameOrLowerImportance(int newImportance) {
        for (int i = 0; i < count; i++) {
            if (assignments[i].importance >= newImportance) {
                assignments[i].importance++;
            }
        }
    }

    // 과목 삭제 후 뒤쪽 중요도 순위를 한 칸씩 앞으로 당깁니다.
    void pullForwardLowerImportance(int oldImportance) {
        for (int i = 0; i < count; i++) {
            if (assignments[i].importance > oldImportance) {
                assignments[i].importance--;
            }
        }
    }
}
