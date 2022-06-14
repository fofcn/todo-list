package com.epam.todo.task.domain.ability;

import com.epam.todo.task.infrastructure.model.StateEnum;

public class TaskStateAbility {

    public static boolean isValidTransition(int start, int end) {
        if (start == StateEnum.TODO.getCode() && end != StateEnum.DONE.getCode()) {
            return false;
        }

        if (start == StateEnum.DONE.getCode()) {
            return false;
        }

        return true;
    }
}
