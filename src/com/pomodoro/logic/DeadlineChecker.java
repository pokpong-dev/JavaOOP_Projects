package com.pomodoro.logic;

import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// ตัวตรวจสอบและแจ้งเตือนเวลางานถึงกำหนด แยกออกมาเพื่อให้หน้าตั้งค่าเปิดปิดได้ง่าย
public class DeadlineChecker {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final List<String> alertedTaskIds = new ArrayList<String>();

    // เปลี่ยนให้คืนค่าข้อมูลงาน พร้อมกับ "ระยะเวลา (Stage)" ที่แจ้งเตือน
    public static class AlertData {
        public Task task;
        public String stageMessage;
        public AlertData(Task task, String stageMessage) {
            this.task = task;
            this.stageMessage = stageMessage;
        }
    }

    public static List<AlertData> getOverdueTasks(List<Task> tasks) {
        List<AlertData> result = new ArrayList<AlertData>();
        long nowMillis = System.currentTimeMillis();

        for (Task t : tasks) {
            // ไม่แจ้งเตือนถ้าลบหรือเสร็จแล้ว
            if (TaskStatus.DONE.equals(t.getStatus()) || TaskStatus.TRASH.equals(t.getStatus())) {
                continue;
            }

            if (t.getReminderTime() == null) {
                // ถ้าไม่มี ReminderTime ให้เช็คข้ามวันจาก DueDate (โค้ดเดิม)
                if (t.getDueDate() != null) {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(t.getDueDate());
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
                    cal.set(java.util.Calendar.MINUTE, 59);
                    cal.set(java.util.Calendar.SECOND, 59);
                    
                    if (cal.getTimeInMillis() < nowMillis) {
                        String alertKey = t.getId() + "_LATE_DUE";
                        if (!alertedTaskIds.contains(alertKey)) {
                            result.add(new AlertData(t, "(เลยกำหนดส่ง)"));
                            alertedTaskIds.add(alertKey);
                        }
                    }
                }
                continue;
            }

            // ถ้าระบุ ReminderTime ให้ทำการเช็คแบบ 3 ระยะ (3 stages)
            long reminderMillis = t.getReminderTime().getTime();
            long diffMillis = reminderMillis - nowMillis;
            
            // แปลงความห่างเป็นนาที (ค่าบวก = ยังไม่ถึงเวลา, ค่าลบ = เลยเวลามาแล้ว)
            int diffMinutes = (int) (diffMillis / (60 * 1000));
            int beforeMin = com.pomodoro.ui.MenuUI.settingMenuDesign.DEFAULT_NOTIFY_BEFORE_DEADLINE_MINUTES;
            
            String stage = null;
            String stageMessage = null;

            if (diffMinutes == beforeMin) {
                // ระยะก่อน (Before): เหลือเวลาตามที่ตั้งไว้เป๊ะ
                stage = "BEFORE_" + beforeMin + "M";
                stageMessage = "(อีก " + beforeMin + " นาที)";
            } else if (diffMinutes == 0 && diffMillis <= 0) {
                // ระยะตรงเวลา (Exact): ถึงเวลาเป๊ะๆ (นาทีที่ 0 และเลยเข้าแดนลบไปนิดหน่อย)
                stage = "EXACT";
                stageMessage = "(ถึงเวลาแล้ว!)";
            } else if (diffMinutes == -(beforeMin)) {
                // ระยะหลัง (After): เลยเวลามาตามที่ตั้งไว้
                stage = "AFTER_" + beforeMin + "M";
                stageMessage = "(เลยเวลามา " + beforeMin + " นาทีแล้ว!)";
            } else if (diffMinutes < -(beforeMin)) {
                 // **Smart Cache Logic**: ถ้าเพิ่งเปิดโปรแกรมขึ้นมาแล้วมันเลยเวลาไปไกลเกินกำหนดมากแล้ว 
                 // เราจะรวบยอดแจ้งเตือนเตือนว่า "เลยเวลา" แค่ครั้งเดียวพอ แล้วข้ามอดีตไปเลย
                 stage = "AFTER_LONG";
                 stageMessage = "(เลยเวลามาแล้ว!)";
            }

            // ถ้าตกอยู่ในระยะเวลาที่ต้องแจ้งเตือน
            if (stage != null) {
                String alertKey = t.getId() + "_" + reminderMillis + "_" + stage;
                
                // ถ้าระยะตกมาอยู่ที่ AFTER_LONG แปลว่าข้ามเวลามาไกลมาก 
                // ให้เช็คว่างานนี้เคยแจ้งเตือนรอบ "เลยเวลา" อันไหนมาก่อนรึเปล่า ค่อยแจ้งทีเดียว
                if ("AFTER_LONG".equals(stage)) {
                     String lateKey = t.getId() + "_" + reminderMillis + "_LATEDONE";
                     if (!alertedTaskIds.contains(lateKey)) {
                         result.add(new AlertData(t, stageMessage));
                         alertedTaskIds.add(lateKey); // เหมาว่าเตือนข้ามเวลาไปแล้ว
                         
                         // Block ไม่ให้ไปเตือนย้อนหลัง Before/Exact แล้ว
                         alertedTaskIds.add(t.getId() + "_" + reminderMillis + "_BEFORE_" + beforeMin + "M");
                         alertedTaskIds.add(t.getId() + "_" + reminderMillis + "_EXACT");
                         alertedTaskIds.add(t.getId() + "_" + reminderMillis + "_AFTER_" + beforeMin + "M");
                     }
                } else {
                     // โหมดแจ้งเตือนปกติทีละสเต็ป
                     if (!alertedTaskIds.contains(alertKey)) {
                         result.add(new AlertData(t, stageMessage));
                         alertedTaskIds.add(alertKey);
                         
                         if (("AFTER_" + beforeMin + "M").equals(stage)) {
                              // ถ้าเตือน After แล้ว ให้ปิด LATEDONE ดักไว้ด้วยเลย
                              alertedTaskIds.add(t.getId() + "_" + reminderMillis + "_LATEDONE");
                         }
                     }
                }
            }
        }
        return result;
    }

    // แสดงหน้าต่างแจงเตือนทีละหลายๆงานรวมกันเรียงตามความสำคัญ
    public static void showCombinedAlerts(List<AlertData> alerts) {
        if (alerts == null || alerts.isEmpty()) return;

        // Sorting by Priority (High = 0, Medium = 1, Low = 2)
        alerts.sort(new java.util.Comparator<AlertData>() {
            @Override
            public int compare(AlertData a, AlertData b) {
                int levelA = com.pomodoro.model.Priority.getLevel(a.task.getPriority());
                int levelB = com.pomodoro.model.Priority.getLevel(b.task.getPriority());
                return Integer.compare(levelA, levelB);
            }
        });

        StringBuilder msg = new StringBuilder("⚠️ แจ้งเตือนงานที่ถึงเวลา!\n\n");
        for (AlertData alert : alerts) {
             String pri = alert.task.getPriority();
             String icon = "🟢"; // Low
             if ("HIGH".equals(pri)) icon = "🔴";
             else if ("MEDIUM".equals(pri)) icon = "🟡";

             msg.append(icon).append(" [").append(pri).append("] ")
                .append(alert.task.getTitle()).append(" ")
                .append(alert.stageMessage).append("\n");
        }

        final String finalMsg = msg.toString();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(
                        null,
                        finalMsg,
                        "การแจ้งเตือนเส้นตาย",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // ตรวจสอบและแสดงแจ้งเตือน
    public static void checkAndAlert(List<Task> tasks, boolean enabled) {
        if (!enabled) return;
        List<AlertData> overdue = getOverdueTasks(tasks);
        if (!overdue.isEmpty()) {
            showCombinedAlerts(overdue);
        }
    }

    // เริ่มการทำงานระบบตรวจจับเวลาในพื้นหลัง (Background) ถี่ยิบทุก 5 วิ
    public static void startBackgroundChecker(final TaskManager taskManager) {
        javax.swing.Timer timer = new javax.swing.Timer(5000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                checkAndAlert(taskManager.getActiveTasks(), true);
            }
        });
        timer.setInitialDelay(2000); 
        timer.start();
    }
}
