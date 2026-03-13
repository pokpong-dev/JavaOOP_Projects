package com.pomodoro.model;

import java.util.Date;
import java.util.UUID;

public abstract class Task implements Exportable {

    private String id;
    private String title;
    private String description;
    private Date dueDate;
    private String status;
    private String priority;
    private Date reminderTime;
    private Category category;
    private int totalFocusMinutes;
    // บันทึกเวลาที่เหลือไว้เผื่อกลับมาทำต่อ ถ้าเป็นลบหนึ่งแปลว่ายังไม่ได้เริ่มทำ
    private int remainingSeconds = -1;

    public Task(String title, String priority, Category category) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.status = TaskStatus.TODO;
        this.totalFocusMinutes = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return TaskStatus.DONE.equals(status);
    }

    public void setCompleted(boolean completed) {
        if (completed) {
            this.status = TaskStatus.DONE;
        } else {
            this.status = TaskStatus.TODO;
        }
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(Date reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getTotalFocusMinutes() {
        return totalFocusMinutes;
    }

    public void addFocusMinutes(int minutes) {
        this.totalFocusMinutes += minutes;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public abstract String getSummary();

    public abstract String getTaskType();
}