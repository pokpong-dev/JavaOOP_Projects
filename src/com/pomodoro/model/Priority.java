package com.pomodoro.model;

public class Priority {

    public static final String HIGH = "HIGH";
    public static final String MEDIUM = "MEDIUM";
    public static final String LOW = "LOW";

    public static String[] values() {
        return new String[] { HIGH, MEDIUM, LOW };
    }

    public static int getLevel(String priority) {
        if (HIGH.equals(priority)) {
            return 0;
        } else if (MEDIUM.equals(priority)) {
            return 1;
        } else if (LOW.equals(priority)) {
            return 2;
        }
        return 3;
    }
}
