package com.pomodoro.ui.panels;

import com.pomodoro.logic.PomodoroTimer;
import com.pomodoro.model.Task;
import com.pomodoro.ui.Theme;
import com.pomodoro.ui.components.TailwindButton;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class TimerPanel extends JPanel {
    private JLabel timerLabel;
    private JLabel currentTaskLabel;
    private JTextArea descArea;
    private JPanel btnContainer;

    private PomodoroTimer timerLogic;
    private Task displayedTask; // The task currently selected in the table

    public TimerPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.BG_WHITE);
        setPreferredSize(new Dimension(340, 0));
        setBorder(new CompoundBorder(
                new MatteBorder(0, 1, 0, 0, Theme.BORDER_COLOR),
                new EmptyBorder(32, 24, 32, 24)
        ));

        timerLabel = new JLabel("25:00", SwingConstants.CENTER);
        timerLabel.setFont(Theme.FONT_Display);
        timerLabel.setForeground(Theme.TEXT_PRIMARY);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(timerLabel);

        add(Box.createVerticalStrut(8));

        currentTaskLabel = new JLabel("Select a task...", SwingConstants.CENTER);
        currentTaskLabel.setFont(Theme.FONT_Body);
        currentTaskLabel.setForeground(Theme.TEXT_SECONDARY);
        currentTaskLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(currentTaskLabel);

        add(Box.createVerticalStrut(32));

        btnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnContainer.setBackground(Theme.BG_WHITE);
        add(btnContainer);

        add(Box.createVerticalStrut(32));
        add(new JSeparator());
        add(Box.createVerticalStrut(24));

        JPanel headerWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerWrap.setBackground(Theme.BG_WHITE);
        JLabel descHeader = new JLabel("Description");
        descHeader.setFont(Theme.FONT_Header);
        headerWrap.add(descHeader);
        headerWrap.setMaximumSize(new Dimension(1000, 30));
        add(headerWrap);

        add(Box.createVerticalStrut(12));

        descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(Theme.FONT_Body);
        descArea.setForeground(Theme.TEXT_SECONDARY);
        descArea.setBackground(Theme.BG_WHITE);
        add(descArea);
    }

    public void setTimerLogic(PomodoroTimer timer) {
        this.timerLogic = timer;
        updateButtons();
    }

    public void updateTime(String time) {
        timerLabel.setText(time);
    }

    // Called when user selects a task in the table
    public void updateTaskDetails(Task task) {
        this.displayedTask = task;
        if (task == null) {
            currentTaskLabel.setText("Select a task...");
            descArea.setText("");
        } else {
            currentTaskLabel.setText(task.getTitle());
            descArea.setText((task.getDescription() == null || task.getDescription().isEmpty()) ? "No description." : task.getDescription());
        }
        updateButtons();
    }

    public void updateButtons() {
        btnContainer.removeAll();

        if (timerLogic == null) return;

        boolean running = timerLogic.isRunning();
        boolean paused = timerLogic.isPaused();

        // If not running, show START.
        // BUT if we are paused, show RESUME/STOP.

        if (!running && !paused) {
            TailwindButton start = new TailwindButton("START FOCUS", Theme.GREEN_500, Color.WHITE, false);
            start.setPreferredSize(new Dimension(160, 48));
            start.addActionListener(e -> {
                if (displayedTask == null) {
                    JOptionPane.showMessageDialog(this, "Please select a task first!");
                    return;
                }
                timerLogic.start(displayedTask);
                updateButtons();
            });
            btnContainer.add(start);
        } else {
            if (running) {
                TailwindButton pause = new TailwindButton("PAUSE", Theme.AMBER_500, Color.WHITE, false);
                pause.addActionListener(e -> {
                    timerLogic.pause();
                    updateButtons();
                });
                btnContainer.add(pause);
            } else {
                TailwindButton resume = new TailwindButton("RESUME", Theme.GREEN_500, Color.WHITE, false);
                resume.addActionListener(e -> {
                    timerLogic.resume();
                    updateButtons();
                });
                btnContainer.add(resume);
            }
            TailwindButton stop = new TailwindButton("STOP", Theme.RED_500, Color.WHITE, false);
            stop.addActionListener(e -> {
                timerLogic.stop();
                updateButtons();
            });
            btnContainer.add(stop);
        }
        btnContainer.revalidate();
        btnContainer.repaint();
    }
}
