package com.pomodoro.model;

public class SingleTask extends Task {

    public SingleTask(String title, String priority, Category category) {
        super(title, priority, category);
    }

    @Override
    public String getSummary() {
        return "Single Task: " + getTitle();
    }

    @Override
    public String getTaskType() {
        return "Single";
    }

    @Override
    public String exportToText() {
        String categoryName;
        if (getCategory() != null) {
            categoryName = getCategory().toString();
        } else {
            categoryName = "-";
        }

        String description;
        if (getDescription() != null) {
            description = getDescription();
        } else {
            description = "-";
        }

        return "[งานเดี่ยว]" +
                "\nชื่องาน: " + getTitle() +
                "\nสถานะ: " + getStatus() +
                "\nความสำคัญ: " + getPriority() +
                "\nหมวดหมู่: " + categoryName +
                "\nคำอธิบาย: " + description +
                "\nเวลาโฟกัสรวม: " + getTotalFocusMinutes() + " นาที";
    }
}
