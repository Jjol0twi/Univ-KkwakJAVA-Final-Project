class AssignmentProgressManager {
    // 과목 완료 여부를 관리하는 클래스입니다.

    // 사용자가 입력한 과목명과 같은 미완료 과목을 찾아 완료 처리합니다.
    boolean markCompletedByTitle(AssignmentList assignmentList, String title) {
        for (int i = 0; i < assignmentList.getCount(); i++) {
            Assignment assignment = assignmentList.get(i);

            if (!assignment.completed && assignment.title.equals(title)) {
                assignment.completed = true;
                return true;
            }
        }

        return false;
    }

    // 현재 그리디 계획의 1순위 과제를 완료 처리합니다.
    boolean markCompletedByPlan(GreedyScheduleItem[] schedule) {
        if (schedule.length == 0) {
            return false;
        }

        schedule[0].assignment.completed = true;
        return true;
    }

    // 등록된 모든 과제가 완료되었는지 확인합니다.
    boolean isAllCompleted(AssignmentList assignmentList) {
        if (assignmentList.isEmpty()) {
            return false;
        }

        for (int i = 0; i < assignmentList.getCount(); i++) {
            if (!assignmentList.get(i).completed) {
                return false;
            }
        }

        return true;
    }
}
