package com.pomodoro.model;

import java.util.Date;
import java.util.UUID;

public abstract class Task implements Exportable {
    private String id;
    private String title;
    private String description;
    private Date dueDate;
    private TaskStatus status; // Changed from boolean isCompleted
    private Priority priority;
    private Date reminderTime;
    private Category category;
    private int totalFocusMinutes;

    public Task(String title, Priority priority, Category category) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.status = TaskStatus.TODO;
        this.totalFocusMinutes = 0;
    }

    // Getters and Setters
    public String getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public boolean isCompleted() { return status == TaskStatus.DONE; }
    public void setCompleted(boolean completed) { this.status = completed ? TaskStatus.DONE : TaskStatus.TODO; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Date getReminderTime() { return reminderTime; }
    public void setReminderTime(Date reminderTime) { this.reminderTime = reminderTime; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public int getTotalFocusMinutes() { return totalFocusMinutes; }
    public void addFocusMinutes(int minutes) { this.totalFocusMinutes += minutes; }

    // Abstract methods for subclasses
    public abstract String getSummary();
    public abstract String getTaskType();
}
