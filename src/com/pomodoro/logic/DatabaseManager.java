package com.pomodoro.logic;

import com.pomodoro.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:pomodoro.db";
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            System.err.println("เชื่อมต่อ Database ไม่ได้: " + e.getMessage());
        }
    }

    // สร้างตารางข้อมูลถ้ามันยังไม่มี
    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();

        // ตารางหมวดหมู่
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS categories (
                        id TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        color_code TEXT
                    )
                """);

        // ตารางงาน
        stmt.execute("""
                    CREATE TABLE IF NOT EXISTS tasks (
                        id TEXT PRIMARY KEY,
                        title TEXT NOT NULL,
                        description TEXT,
                        status TEXT,
                        priority TEXT,
                        category_id TEXT,
                        due_date INTEGER,
                        reminder_time INTEGER,
                        total_focus_minutes INTEGER DEFAULT 0,
                        task_type TEXT NOT NULL,
                        interval_days INTEGER,
                        next_occurrence INTEGER,
                        remaining_seconds INTEGER DEFAULT -1,
                        FOREIGN KEY (category_id) REFERENCES categories(id)
                    )
                """);

        stmt.close();
    }

    public void saveCategory(Category category) {
        String sql = "INSERT OR REPLACE INTO categories (id, name, color_code) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, category.getId());
            ps.setString(2, category.getName());
            ps.setString(3, category.getColorCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("บันทึก Category ไม่ได้: " + e.getMessage());
        }
    }

    public List<Category> loadAllCategories() {
        List<Category> list = new ArrayList<Category>();
        String sql = "SELECT * FROM categories";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // ดึงข้อมูลเดิมมาสร้างใหม่ผ่านไอดีเก่า
                Category cat = new Category(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("color_code"));
                list.add(cat);
            }
        } catch (SQLException e) {
            System.err.println("โหลด Category ไม่ได้: " + e.getMessage());
        }
        return list;
    }

    public void saveTask(Task task) {
        String sql = """
                    INSERT OR REPLACE INTO tasks
                    (id, title, description, status, priority, category_id, due_date, reminder_time, total_focus_minutes, task_type, interval_days, next_occurrence, remaining_seconds)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, task.getId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus());
            ps.setString(5, task.getPriority());

            if (task.getCategory() != null) {
                ps.setString(6, task.getCategory().getId());
            } else {
                ps.setString(6, null);
            }

            if (task.getDueDate() != null) {
                ps.setLong(7, task.getDueDate().getTime());
            } else {
                ps.setLong(7, 0);
            }

            if (task.getReminderTime() != null) {
                ps.setLong(8, task.getReminderTime().getTime());
            } else {
                ps.setLong(8, 0);
            }

            ps.setInt(9, task.getTotalFocusMinutes());

            if (task instanceof RepeatingTask) {
                RepeatingTask repeatingTask = (RepeatingTask) task;
                ps.setString(10, "REPEATING");
                ps.setInt(11, repeatingTask.getIntervalDays());
                if (repeatingTask.getNextOccurrence() != null) {
                    ps.setLong(12, repeatingTask.getNextOccurrence().getTime());
                } else {
                    ps.setLong(12, 0);
                }
            } else {
                ps.setString(10, "SINGLE");
                ps.setNull(11, Types.INTEGER);
                ps.setNull(12, Types.INTEGER);
            }

            ps.setInt(13, task.getRemainingSeconds());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("บันทึก Task ไม่ได้: " + e.getMessage());
        }
    }

    public void deleteTask(String taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, taskId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("ลบ Task ไม่ได้: " + e.getMessage());
        }
    }

    public List<Task> loadAllTasks(List<Category> categories) {
        List<Task> list = new ArrayList<Task>();
        String sql = "SELECT * FROM tasks";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String type = rs.getString("task_type");
                String title = rs.getString("title");
                String priority = rs.getString("priority");

                // ค้นหาหมวดหมู่ผ่านไอดี
                String catId = rs.getString("category_id");
                Category category = null;
                for (Category c : categories) {
                    if (c.getId().equals(catId)) {
                        category = c;
                        break;
                    }
                }

                Task task;
                if ("REPEATING".equals(type)) {
                    int intervalDays = rs.getInt("interval_days");
                    long nextOccMs = rs.getLong("next_occurrence");
                    Date nextOcc;
                    if (nextOccMs != 0) {
                        nextOcc = new Date(nextOccMs);
                    } else {
                        nextOcc = null;
                    }
                    task = new RepeatingTask(title, priority, category, intervalDays, nextOcc);
                } else {
                    task = new SingleTask(title, priority, category);
                }

                task.setId(rs.getString("id"));
                task.setDescription(rs.getString("description"));
                String status = rs.getString("status");
                if ("TRUSH".equals(status)) status = TaskStatus.TRASH;
                task.setStatus(status);

                long dueMs = rs.getLong("due_date");
                if (dueMs != 0) {
                    task.setDueDate(new Date(dueMs));
                }

                long reminderMs = rs.getLong("reminder_time");
                if (reminderMs != 0) {
                    task.setReminderTime(new Date(reminderMs));
                }

                task.addFocusMinutes(rs.getInt("total_focus_minutes"));
                task.setRemainingSeconds(rs.getInt("remaining_seconds"));

                list.add(task);
            }
        } catch (SQLException e) {
            System.err.println("โหลด Task ไม่ได้: " + e.getMessage());
        }
        return list;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("ปิด Database ไม่ได้: " + e.getMessage());
        }
    }
}