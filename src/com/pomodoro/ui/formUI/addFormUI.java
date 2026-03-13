package com.pomodoro.ui.formUI;

import java.awt.Frame;
import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Category;
import com.pomodoro.model.Priority;
import com.pomodoro.model.SingleTask;
import com.pomodoro.model.Task;

public class addFormUI extends JDialog {

    private JTextField nameField;
    private JTextField descripField;
    private JTextField dueDateField; // ใส่ทั้งวันที่และเวลาไว้ช่องเดียว
    private JTextField notiTimeField; // เวลาสำหรับแจ้งเตือน

    private TaskManager taskManager;
    private Runnable onSave;
    private Task taskToEdit; // ตรวจสอบว่ากำลังใช้โหมดเพิ่มหรือแก้ไขงาน

    private static final SimpleDateFormat DATE_FMT    = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat TIME_FMT    = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat COMBINE_FMT = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    // โหมดกดเพิ่มงาน
    public addFormUI(Frame parent, TaskManager taskManager, Runnable onSave) {
        super(parent, "เพิ่มงาน", true);
        this.taskManager = taskManager;
        this.onSave      = onSave;
        this.taskToEdit  = null;
        buildUI(parent);
    }

    // โหมดกดแก้ไขงานโดยจะดึงข้อมูลเก่ามาใส่ในช่องข้อความให้ก่อน
    public addFormUI(Frame parent, TaskManager taskManager, Task existingTask, Runnable onSave) {
        super(parent, "แก้ไขงาน", true);
        this.taskManager = taskManager;
        this.onSave      = onSave;
        this.taskToEdit  = existingTask;
        buildUI(parent);
    }

    private void buildUI(Frame parent) {
        setSize(540, 430);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // แบบฟอร์มกรอกรายละเอียด
        JPanel formPanel = new JPanel();
        getContentPane().add(formPanel);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setLayout(new GridLayout(0, 2, 10, 10));

        // ชื่อ
        formPanel.add(new JLabel("ชื่องาน"));
        nameField = new JTextField();
        nameField.setColumns(10);
        formPanel.add(nameField);

        // คำอธิบาย
        formPanel.add(new JLabel("คำอธิบาย"));
        descripField = new JTextField();
        descripField.setColumns(10);
        formPanel.add(descripField);

        // ความสำคัญ
        formPanel.add(new JLabel("ความสำคัญ"));
        JComboBox<String> prioComboBox = new JComboBox<>();
        for (String p : Priority.values()) {
            prioComboBox.addItem(p);
        }
        formPanel.add(prioComboBox);

        // หมวดหมู่
        formPanel.add(new JLabel("หมวดหมู่"));
        JComboBox<String> cateComboBox = new JComboBox<>();
        if (taskManager != null) {
            for (Category c : taskManager.getCategories()) {
                cateComboBox.addItem(c.getName());
            }
        } else {
            cateComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"General", "Work"}));
        }
        formPanel.add(cateComboBox);

        // ใช้วันครบกำหนดเป็นวันที่แจ้งเตือนด้วย
        formPanel.add(new JLabel("วันครบกำหนด (dd/MM/yyyy)"));
        dueDateField = new JTextField();
        dueDateField.setColumns(10);
        formPanel.add(dueDateField);

        // เวลาสำหรับตั้งแจ้งเตือน
        formPanel.add(new JLabel("เวลาแจ้งเตือน (HH:mm)"));
        notiTimeField = new JTextField();
        notiTimeField.setColumns(10);
        formPanel.add(notiTimeField);

        // นำข้อมูลเก่ามาใส่ฟอร์มถ้ากำลังแก้ไขอยู่
        if (taskToEdit != null) {
            nameField.setText(taskToEdit.getTitle());
            if (taskToEdit.getDescription() != null) {
                descripField.setText(taskToEdit.getDescription());
            } else {
                descripField.setText("");
            }
            if (taskToEdit.getPriority() != null) {
                prioComboBox.setSelectedItem(taskToEdit.getPriority());
            }
            if (taskToEdit.getCategory() != null) {
                cateComboBox.setSelectedItem(taskToEdit.getCategory().getName());
            }
            if (taskToEdit.getDueDate() != null) {
                dueDateField.setText(DATE_FMT.format(taskToEdit.getDueDate()));
            }
            if (taskToEdit.getReminderTime() != null) {
                notiTimeField.setText(TIME_FMT.format(taskToEdit.getReminderTime()));
            }
        }

        // ปุ่มต่างๆ
        JPanel btnPanel = new JPanel();
        getContentPane().add(btnPanel);

        JButton saveBtn = new JButton("บันทึก");
        btnPanel.add(saveBtn);

        JButton cancelBtn = new JButton("ยกเลิก");
        btnPanel.add(cancelBtn);

        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                dispose();
            }
        });

        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            String title = nameField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(addFormUI.this, "กรุณาใส่ชื่องาน", "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String priority = (String) prioComboBox.getSelectedItem();
            String cateName = (String) cateComboBox.getSelectedItem();
            Category category = null;
            if (taskManager != null) {
                category = taskManager.getCategoryByName(cateName);
            }

            // แปลงวันที่
            java.util.Date dueDate = null;
            String dueDateStr = dueDateField.getText().trim();
            if (!dueDateStr.isEmpty()) {
                try {
                    DATE_FMT.setLenient(false);
                    dueDate = DATE_FMT.parse(dueDateStr);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(addFormUI.this,
                            "รูปแบบวันที่ไม่ถูกต้อง ใช้ dd/MM/yyyy\nเช่น 20/03/2026",
                            "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // รวมวันที่และเวลาแจ้งเตือนเข้าด้วยกัน
            java.util.Date reminder = null;
            String notiTimeStr = notiTimeField.getText().trim();
            if (!notiTimeStr.isEmpty()) {
                if (dueDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(addFormUI.this,
                            "ถ้าจะตั้งเวลาแจ้งเตือน ต้องใส่วันครบกำหนดด้วย",
                            "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                String combined = notiTimeStr + " " + dueDateStr;
                try {
                    COMBINE_FMT.setLenient(false);
                    reminder = COMBINE_FMT.parse(combined);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(addFormUI.this,
                            "รูปแบบเวลาแจ้งเตือนไม่ถูกต้อง ใช้ HH:mm เช่น 09:30",
                            "แจ้งเตือน", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            if (taskToEdit != null) {
                // โหมดสำหรับแก้ไขงานเก่า
                taskToEdit.setTitle(title);
                taskToEdit.setDescription(descripField.getText().trim());
                taskToEdit.setPriority(priority);
                taskToEdit.setCategory(category);
                taskToEdit.setDueDate(dueDate);
                taskToEdit.setReminderTime(reminder);
                if (taskManager != null) taskManager.updateTask(taskToEdit);
            } else {
                // โหมดสำหรับเพิ่มงานใหม่เข้าไป
                Task task = new SingleTask(title, priority, category);
                task.setDescription(descripField.getText().trim());
                task.setDueDate(dueDate);
                task.setReminderTime(reminder);
                if (taskManager != null) taskManager.addTask(task);
            }

            if (onSave != null) onSave.run();
            dispose();
            }
        });
    }
}
