package dev.wtamaso.todo.constants;

import java.io.Serializable;

public enum TaskStatus implements Serializable{
    LISTED,
    COMPLETED,
    DELETED;

    public static TaskStatus fromValue(String s) {
        if (s == null){
            return null;
        }
        s = s.trim().toUpperCase();
        for (TaskStatus ts : TaskStatus.values()) {
            if (ts.name().toUpperCase().equals(s)) {
                return ts;
            }
        }
        return null;
    }
}
