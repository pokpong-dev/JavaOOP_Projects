package com.pomodoro.model;

import java.text.SimpleDateFormat;

//เขียนพวกระบบเตือนซ้ำได้ไรงี้
public class SingleTask extends Task {

    public SingleTask(String title, Priority priority, Category category) {
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
        // อันนี้เทสเฉยๆมันน่าจะต้องมีวิธีในการทำเปนpdf
        return "MOCK_EXPORT_STRING";
    }
}
