package com.pomodoro.ui;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;

public class TaskTableModel extends JScrollPane {

    private JTable table;
    private DefaultTableModel model;
    private TaskManager taskManager;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public TaskTableModel(TaskManager taskManager) {
        this.taskManager = taskManager;

        model = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "ชื่องาน", "คำอธิบาย", "ความสำคัญ", "หมวดหมู่", "วันครบกำหนด", "แจ้งเตือน", "สถานะ"
                }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ห้ามแก้ไขข้อมูลในตารางโดยตรง
            }
        };

        table = new JTable(model);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        setViewportView(table);
    }

    // ส่งตารางไปให้หน้าอื่นใช้งานต่อ
    public JTable getTable() {
        return table;
    }

    // ดึงข้อมูลงานจากแถวที่เลือกล่าสุด
    public Task getTaskAt(int row) {
        List<Task> activeTasks = taskManager.getActiveTasks();
        if (row >= 0 && row < activeTasks.size()) {
            return activeTasks.get(row);
        }
        return null;
    }

    // ดึงข้อมูลงานจากแถวที่อยู่ในถังขยะ
    public Task getTrashTaskAt(int row) {
        List<Task> trashTasks = taskManager.getTrashTasks();
        if (row >= 0 && row < trashTasks.size()) {
            return trashTasks.get(row);
        }
        return null;
    }

    // โหลดข้อมูลตารางใหม่โดยเอาเฉพาะงานที่ยังไม่ถูกทิ้ง
    public void reloadTable() {
        model.setRowCount(0);
        for (Task t : taskManager.getActiveTasks()) {
            String categoryName = "-";
            if (t.getCategory() != null) {
                categoryName = t.getCategory().getName();
            }
            
            String dueDateStr = "-";
            if (t.getDueDate() != null) {
                dueDateStr = DATE_FORMAT.format(t.getDueDate());
            }
            
            String reminderTimeStr = "-";
            if (t.getReminderTime() != null) {
                reminderTimeStr = DATE_TIME_FORMAT.format(t.getReminderTime());
            }

            model.addRow(new Object[] {
                    t.getTitle(),
                    t.getDescription(),
                    t.getPriority(),
                    categoryName,
                    dueDateStr,
                    reminderTimeStr,
                    TaskStatus.getDisplayLabel(t.getStatus())
            });
        }
    }

    // โหลดข้อมูลตารางใหม่โดยเอาเฉพาะงานที่อยู่ในถังขยะ
    public void reloadTrashTable() {
        model.setRowCount(0);
        for (Task t : taskManager.getTrashTasks()) {
            String categoryName = "-";
            if (t.getCategory() != null) {
                categoryName = t.getCategory().getName();
            }
            
            String dueDateStr = "-";
            if (t.getDueDate() != null) {
                dueDateStr = DATE_FORMAT.format(t.getDueDate());
            }
            
            String reminderTimeStr = "-";
            if (t.getReminderTime() != null) {
                reminderTimeStr = DATE_TIME_FORMAT.format(t.getReminderTime());
            }

            model.addRow(new Object[] {
                    t.getTitle(),
                    t.getDescription(),
                    t.getPriority(),
                    categoryName,
                    dueDateStr,
                    reminderTimeStr,
                    TaskStatus.getDisplayLabel(t.getStatus())
            });
        }
    }
}