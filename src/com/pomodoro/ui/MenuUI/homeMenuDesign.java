package com.pomodoro.ui.MenuUI;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;

public class homeMenuDesign extends JPanel {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy");
    private TaskManager taskManager;

    private DefaultTableModel todayModel;
    private DefaultTableModel upModel;

    private JLabel valTotalLabel;
    private JLabel valDoneLabel;
    private JLabel valActiveLabel;
    private JLabel valTrashLabel;

    public homeMenuDesign(TaskManager taskManager) {
        this.taskManager = taskManager;

        setLayout(null);
        setBounds(113, 5, 780, 551);

        // วันนี้
        JLabel todayLabel = new JLabel("📋  Today's Tasks");
        todayLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        todayLabel.setBounds(10, 10, 300, 28);
        add(todayLabel);

        String[] cols = {"ชื่องาน", "ความสำคัญ", "สถานะ"};
        todayModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable todayTable = new JTable(todayModel);
        JScrollPane todaySP = new JScrollPane(todayTable);
        todaySP.setBounds(10, 45, 740, 140);
        add(todaySP);

        // เร็วๆ นี้
        JLabel deadlineLabel = new JLabel("⏰  Upcoming Deadlines (7 วันข้างหน้า)");
        deadlineLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        deadlineLabel.setBounds(10, 200, 400, 28);
        add(deadlineLabel);

        String[] upCols = {"ชื่องาน", "ความสำคัญ", "วันครบกำหนด"};
        upModel = new DefaultTableModel(upCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable upTable = new JTable(upModel);
        JScrollPane upSP = new JScrollPane(upTable);
        upSP.setBounds(10, 235, 740, 140);
        add(upSP);

        // สถิติ
        JLabel focusLabel = new JLabel("📊  Focus Statistics");
        focusLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        focusLabel.setBounds(10, 390, 300, 28);
        add(focusLabel);

        JPanel statsPanel = new JPanel();
        statsPanel.setBorder(BorderFactory.createEtchedBorder());
        statsPanel.setBounds(10, 425, 740, 100);
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
        add(statsPanel);

        valTotalLabel = createStatValueLabel();
        valDoneLabel = createStatValueLabel();
        valActiveLabel = createStatValueLabel();
        valTrashLabel = createStatValueLabel();

        addStat(statsPanel, "งานทั้งหมด", valTotalLabel);
        addStat(statsPanel, "เสร็จแล้ว ✅", valDoneLabel);
        addStat(statsPanel, "ยังค้างอยู่ 🔄", valActiveLabel);
        addStat(statsPanel, "ใน Trash 🗑️", valTrashLabel);

        reloadHome();
    }

    private JLabel createStatValueLabel() {
        JLabel valLabel = new JLabel("0");
        valLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        valLabel.setForeground(new Color(70, 130, 180));
        valLabel.setAlignmentX(CENTER_ALIGNMENT);
        return valLabel;
    }

    private void addStat(JPanel parent, String label, JLabel valLabel) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lbl.setAlignmentX(CENTER_ALIGNMENT);

        card.add(valLabel);
        card.add(lbl);
        parent.add(card);
    }

    public void reloadHome() {
        todayModel.setRowCount(0);
        upModel.setRowCount(0);

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfToday = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfToday = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date sevenDays = cal.getTime();

        List<Task> todayTasks = new java.util.ArrayList<Task>();
        List<Task> upcomingTasks = new java.util.ArrayList<Task>();

        long totalActiveCount = 0;
        long totalDoneCount = 0;

        for (Task t : taskManager.getActiveTasks()) {
            if (!TaskStatus.DONE.equals(t.getStatus())) {
                totalActiveCount++;
                
                if (t.getDueDate() != null) {
                    if (!t.getDueDate().before(startOfToday) && !t.getDueDate().after(endOfToday)) {
                        todayTasks.add(t);
                    } else if (!t.getDueDate().before(today) && !t.getDueDate().after(sevenDays)) {
                        upcomingTasks.add(t);
                    }
                }
            } else {
                totalDoneCount++;
            }
        }

        upcomingTasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getDueDate().compareTo(o2.getDueDate());
            }
        });

        for (Task t : todayTasks) {
            todayModel.addRow(new Object[]{
                t.getTitle(), t.getPriority(), TaskStatus.getDisplayLabel(t.getStatus())
            });
        }
        if (todayTasks.isEmpty()) {
            todayModel.addRow(new Object[]{"  ✅  ไม่มีงานที่ต้องทำวันนี้", "", ""});
        }

        for (Task t : upcomingTasks) {
            upModel.addRow(new Object[]{
                t.getTitle(), t.getPriority(), DATE_FMT.format(t.getDueDate())
            });
        }
        if (upcomingTasks.isEmpty()) {
            upModel.addRow(new Object[]{"✅  ไม่มี deadline ใน 7 วันข้างหน้า", "", ""});
        }

        valTotalLabel.setText(String.valueOf(totalActiveCount + totalDoneCount));
        valDoneLabel.setText(String.valueOf(totalDoneCount));
        valActiveLabel.setText(String.valueOf(totalActiveCount));
        valTrashLabel.setText(String.valueOf(taskManager.getTrashTasks().size()));
    }
}
