package com.pomodoro.logic;

import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;
import com.pomodoro.model.Category;
import com.pomodoro.model.Priority;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskManager {

    private List<Task> tasks;
    private List<Category> categories;
    private DatabaseManager database;

    public TaskManager() {
        this.database = new DatabaseManager();
        this.categories = database.loadAllCategories();

        // ถ้าตารางหมวดหมู่ยังว่างอยู่ก็ใส่ค่าตั้งต้นให้มัน
        if (categories.isEmpty()) {
            Category general = new Category("General", "#2ECC71");
            Category work = new Category("Work", "#3498DB");
            categories.add(general);
            categories.add(work);
            database.saveCategory(general);
            database.saveCategory(work);
        }

        this.tasks = database.loadAllTasks(categories);
    }
    
    public void addTask(Task task) {
        tasks.add(task);
        database.saveTask(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        database.deleteTask(task.getId());
    }
    
    public List<Task> getAllTasks() {
        return tasks;
    }
    
    public List<Task> getActiveTasks() {
        List<Task> result = new ArrayList<Task>();
        for (Task t : tasks) {
            if (!TaskStatus.TRASH.equals(t.getStatus())) {
                result.add(t);
            }
        }
        return result;
    }
    
    public List<Task> getTrashTasks() {
        List<Task> result = new ArrayList<Task>();
        for (Task t : tasks) {
            if (TaskStatus.TRASH.equals(t.getStatus())) {
                result.add(t);
            }
        }
        return result;
    }
    
    
    public void moveToTrash(Task task) {
        task.setStatus(TaskStatus.TRASH);
        database.saveTask(task); // เปลี่ยนข้อมูลบนฐานข้อมูลตามไปด้วย
    }
    
    public void restoreTask(Task task) {
        task.setStatus(TaskStatus.TODO);
    }
    
    // ลบงานทิ้งถาวรเลยทั้งในระบบและฐานข้อมูล
    public void deleteForever(Task task) {
        tasks.remove(task);
        database.deleteTask(task.getId()); // ลบในฐานข้อมูลทิ้งด้วย
    }
    
    public List<Category> getCategories() {
        return categories;
    }

    // ค้นหาหมวดหมู่ผ่านชื่อแบบไม่แคร์ตัวพิมเล็กพิมใหญ่
    public Category getCategoryByName(String name) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    public void addCategory(Category category) {
        categories.add(category);
        database.saveCategory(category);
    }
    
    
    // จัดเรียงงานตามตัวอักษร
    public void sortByName() {
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
    }

    // จัดเรียงงานความสำคัญมากไปน้อย
    public void sortByPriority() {
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return Integer.compare(Priority.getLevel(a.getPriority()), Priority.getLevel(b.getPriority()));
            }
        });
    }

    // จัดเรียงงานตามวันครบกำหนด อันไหนไม่ใส่วันก็เอาไว้หลังสุด
    public void sortByDueDate() {
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                long aTime;
                if (a.getDueDate() != null) {
                    aTime = a.getDueDate().getTime();
                } else {
                    aTime = Long.MAX_VALUE;
                }

                long bTime;
                if (b.getDueDate() != null) {
                    bTime = b.getDueDate().getTime();
                } else {
                    bTime = Long.MAX_VALUE;
                }

                return Long.compare(aTime, bTime);
            }
        });
    }

    // จัดเรียงงานตามหมวดหมู่ (ตัวอักษร)
    public void sortByCategory() {
        tasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                String catA = (a.getCategory() != null) ? a.getCategory().getName() : "ZZZ";
                String catB = (b.getCategory() != null) ? b.getCategory().getName() : "ZZZ";
                return catA.compareToIgnoreCase(catB);
            }
        });
    }

    
    // คัดลอกส่งออกงานทั้งหมดเป็นไฟล์ข้อความ
    public boolean exportAllToFile(String filepath) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filepath));
            try {
                for (Task task : tasks) {
                    writer.println(task.exportToText());
                    writer.println("---");
                }
            } finally {
                writer.close();
            }
            return true;
        } catch (IOException e) {
            System.err.println("ส่งออกไฟล์ไม่ได้: " + e.getMessage());
            return false;
        }
    }
    
    
    // บันทึกที่แก้ในงานลงฐานข้อมูล
    public void updateTask(Task task) {
        database.saveTask(task);
    }
    
    // ปิดการเชื่อมต่อฐานข้อมูลตอนปิดระบบทิ้ง
    public void close() {
        database.close();
    }
}