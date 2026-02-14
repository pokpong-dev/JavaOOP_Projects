package com.pomodoro.logic;

import com.pomodoro.model.Task;
import com.pomodoro.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskManager {

    private List<Task> tasks; // Placeholder
    private List<Category> categories; // Placeholder

    // ถ้าจะใช้sqlควรเขียนเพิ่มเด้อ

    public TaskManager() {
        // เขียนเพิ่ม
        this.tasks = new ArrayList<>();
        this.categories = new ArrayList<>();

        categories.add(new Category("General", "#000000"));
        categories.add(new Category("Work", "#0000FF"));
    }

    public void addTask(Task t) {
        // เขียนเพิ่ม
        tasks.add(t); // Basic implementation for UI testing
    }

    public void removeTask(Task t) {
        // เขียนเพิ่ม
        tasks.remove(t); // Basic implementation for UI testing
    }

    public List<Task> getAllTasks() {
        // เขียนเพิ่ม
        return tasks;
    }

    public List<Category> getCategories() {
        // เขียนเพิ่ม
        return categories;
    }

    public Category getCategoryByName(String name) {
        // เขียนเพิ่ม
        return categories.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void addCategory(Category c) {
        // เขียนเพิ่ม
        categories.add(c);
    }

    public void sortByName() {
        // เขียนเพิ่ม
    }

    public void sortByPriority() {
        // เขียนเพิ่ม
    }

    public void sortByDueDate() {
        // เขียนเพิ่ม
    }

    public boolean exportAllToFile(String filepath) {
        // เขียนเพิ่ม

        System.out.println("Exporting PDF to: " + filepath);
        return true; // Mock
    }
}
