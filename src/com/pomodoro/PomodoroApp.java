package com.pomodoro;

import com.pomodoro.logic.PomodoroTimer;
import com.pomodoro.logic.TaskManager;
import com.pomodoro.model.Category;
import com.pomodoro.model.Priority;
import com.pomodoro.model.SingleTask;
import com.pomodoro.ui.MainLayout;
import com.pomodoro.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Calendar;

public class PomodoroApp {
    private TaskManager taskManager;
    private PomodoroTimer timer;
    private MainLayout mainLayout;

    public PomodoroApp() {
        // 1. Logic
        taskManager = new TaskManager();
        initData();

        // 2. Timer
        timer = new PomodoroTimer(
                timeStr -> { // onTick
                    if (mainLayout != null) {
                        mainLayout.getTimerPanel().updateTime(timeStr);

                    }
                },
                () -> { // onFinish
                    if (mainLayout != null) {
                        mainLayout.getTimerPanel().updateButtons();
                        mainLayout.getTaskPanel().refreshTable();
                        JOptionPane.showMessageDialog(mainLayout, "Pomodoro Completed!");
                    }
                },
                () -> { // onStatusChange
                    if (mainLayout != null) {
                        mainLayout.getTimerPanel().updateButtons();
                        mainLayout.getTaskPanel().refreshTable();
                    }
                });

        // 3. UI
        mainLayout = new MainLayout(taskManager, timer);

        // 4. Frame
        JFrame frame = new JFrame("Pomodoro Professional");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 850);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(mainLayout);
        frame.setVisible(true);
    }

    private void initData() {
        Category work = taskManager.getCategoryByName("Work");
        Category general = taskManager.getCategoryByName("General");

        SingleTask t1 = new SingleTask("Complete UI Design", Priority.HIGH, work);
        t1.setDescription("Finish Figma design for the new dashboard.");
        t1.setDueDate(new Date());
        taskManager.addTask(t1);

        SingleTask t2 = new SingleTask("Weekly Review", Priority.MEDIUM, general);
        t2.setDescription("Review all tasks for the week.");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, 2);
        t2.setDueDate(c.getTime());
        taskManager.addTask(t2);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(PomodoroApp::new);
    }
}
