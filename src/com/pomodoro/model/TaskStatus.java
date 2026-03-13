package com.pomodoro.model;

public class TaskStatus {

    public static final String TODO        = "TODO";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String DONE        = "DONE";
    public static final String TRASH       = "TRASH";

    public static String[] values() {
        return new String[] { TODO, IN_PROGRESS, DONE, TRASH };
    }

    public static String getDisplayLabel(String status) {
        if (TODO.equals(status)) return "To Do";
        if (IN_PROGRESS.equals(status)) return "In Progress";
        if (DONE.equals(status)) return "Done";
        if (TRASH.equals(status)) return "Trash";
        return status;
    }
}
