package com.pomodoro.model;

import java.util.Calendar;
import java.util.Date;

// ต้นแบบสำหรับงานที่ต้องทำซ้ำหลายรอบ
public class RepeatingTask extends Task {

    // จำนวนวันที่ต้องเว้นระยะก่อนทำรอบถัดไป
    private int intervalDays;
    // วันที่ต้องทำงานนี้ในรอบหน้า
    private Date nextOccurrence;

    public RepeatingTask(String title, String priority, Category category, int intervalDays, Date firstOccurrence) {
        super(title, priority, category);
        this.intervalDays = intervalDays;
        this.nextOccurrence = firstOccurrence;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

    public void setIntervalDays(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public Date getNextOccurrence() {
        return nextOccurrence;
    }

    public void setNextOccurrence(Date nextOccurrence) {
        this.nextOccurrence = nextOccurrence;
    }

    // คำนวณหาวันทำงานรอบถัดไปโดยบวกจำนวนวันเว้นระยะเข้าไป
    public void calculateNextOccurrence() {
        if (nextOccurrence != null && intervalDays > 0) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(nextOccurrence);
            cal.add(Calendar.DATE, intervalDays);
            this.nextOccurrence = cal.getTime();
        }
    }

    @Override
    public String getSummary() {
        return "งานซ้ำ: " + getTitle() + " (ครั้งถัดไป: " + nextOccurrence + ")";
    }

    @Override
    public String getTaskType() {
        return "งานซ้ำ";
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

        String nextOccurrenceText;
        if (nextOccurrence != null) {
            nextOccurrenceText = nextOccurrence.toString();
        } else {
            nextOccurrenceText = "-";
        }

        return "[งานซ้ำ]" +
                "\nชื่องาน: " + getTitle() +
                "\nสถานะ: " + getStatus() +
                "\nความสำคัญ: " + getPriority() +
                "\nหมวดหมู่: " + categoryName +
                "\nคำอธิบาย: " + description +
                "\nทำซ้ำทุก: " + intervalDays + " วัน" +
                "\nครั้งถัดไป: " + nextOccurrenceText +
                "\nเวลาโฟกัสรวม: " + getTotalFocusMinutes() + " นาที";
    }
}