package com.pomodoro.ui;

import com.pomodoro.logic.PomodoroTimer;
import com.pomodoro.logic.TaskManager;
import com.pomodoro.ui.panels.SettingsPanel;
import com.pomodoro.ui.panels.Sidebar;
import com.pomodoro.ui.panels.TaskPanel;
import com.pomodoro.ui.panels.TimerPanel;

import javax.swing.*;
import java.awt.*;

public class MainLayout extends JPanel {
    private CardLayout centerCardLayout;
    private JPanel centerPanel;

    private Sidebar sidebar;
    private TaskPanel taskPanel;
    private SettingsPanel settingsPanel;
    private TimerPanel timerPanel;

    public MainLayout(TaskManager taskManager, PomodoroTimer timer) {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_APP);

        // Init Panels
        timerPanel = new TimerPanel();
        timerPanel.setTimerLogic(timer);

        taskPanel = new TaskPanel(taskManager, task -> {
            timerPanel.updateTaskDetails(task);
        });

        settingsPanel = new SettingsPanel(timer, taskManager);

        sidebar = new Sidebar(
                viewName -> {
                    if (viewName.equals("SETTINGS")) {
                        centerCardLayout.show(centerPanel, "SETTINGS");
                    } else {
                        centerCardLayout.show(centerPanel, "TASKS");
                    }
                },
                filterName -> taskPanel.setFilter(filterName));

        // Center Content (Card Layout)
        centerCardLayout = new CardLayout();
        centerPanel = new JPanel(centerCardLayout);
        centerPanel.setBackground(Theme.BG_APP);

        centerPanel.add(taskPanel, "TASKS");
        centerPanel.add(settingsPanel, "SETTINGS");

        // Assemble
        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(timerPanel, BorderLayout.EAST);
    }

    public TimerPanel getTimerPanel() {
        return timerPanel;
    }

    public TaskPanel getTaskPanel() {
        return taskPanel;
    }
}
