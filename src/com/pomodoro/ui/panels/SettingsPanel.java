package com.pomodoro.ui.panels;

import com.pomodoro.logic.PomodoroTimer;
import com.pomodoro.logic.TaskManager;
import com.pomodoro.ui.Theme;
import com.pomodoro.ui.components.TailwindButton;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class SettingsPanel extends JPanel {
    private PomodoroTimer timerLogic;
    private TaskManager taskManager;
    private int currentDuration = 25;

    public SettingsPanel(PomodoroTimer timer, TaskManager tm) {
        this.timerLogic = timer;
        this.taskManager = tm;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel header = new JLabel("Settings");
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setForeground(Theme.TEXT_PRIMARY);
        add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Theme.BG_WHITE);
        content.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER_COLOR), new EmptyBorder(30, 30, 30, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(0, 0, 15, 0);

        // 1. Timer Duration
        JLabel lblDuration = new JLabel("Pomodoro Duration (Minutes)");
        lblDuration.setFont(Theme.FONT_Bold);
        content.add(lblDuration, gbc);

        // Spinner
        gbc.gridy = 1;
        JSpinner durSpin = new JSpinner(new SpinnerNumberModel(currentDuration, 1, 120, 1));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) durSpin.getEditor();
        editor.getTextField().setEditable(false);
        editor.getTextField().setBackground(Theme.BG_WHITE);
        editor.getTextField().setBorder(Theme.INPUT_BORDER);

        durSpin.addChangeListener(e -> {
            currentDuration = (int) durSpin.getValue();
            timerLogic.setDuration(currentDuration);
        });
        content.add(durSpin, gbc);

        // --- PRESET BUTTONS ---
        gbc.gridy = 2;
        JPanel presets = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        presets.setBackground(Theme.BG_WHITE);

        TailwindButton btnShort = new TailwindButton("Quick (15m)", Theme.GREEN_500, Color.WHITE, false);
        btnShort.addActionListener(e -> durSpin.setValue(15));

        TailwindButton btnStd = new TailwindButton("Standard (25m)", Theme.BLUE_600, Color.WHITE, false);
        btnStd.addActionListener(e -> durSpin.setValue(25));

        TailwindButton btnLong = new TailwindButton("Deep (50m)", Theme.INDIGO_500, Color.WHITE, false);
        btnLong.addActionListener(e -> durSpin.setValue(50));

        presets.add(btnShort);
        presets.add(btnStd);
        presets.add(btnLong);
        content.add(presets, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 20, 0);
        content.add(new JSeparator(), gbc);

        // 2. Data Management
        JLabel lblData = new JLabel("Data Management");
        lblData.setFont(Theme.FONT_Bold);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        content.add(lblData, gbc);

        TailwindButton exportBtn = new TailwindButton("Export Data to CSV", Theme.BLUE_600, Color.WHITE, false);
        exportBtn.addActionListener(e -> exportData());

        gbc.gridy = 5;
        content.add(exportBtn, gbc);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Theme.BG_APP);
        wrapper.add(content, BorderLayout.NORTH);

        add(wrapper, BorderLayout.CENTER);
    }

    private void exportData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Tasks");
        chooser.setFileFilter(new FileNameExtensionFilter("CSV File", "csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getParentFile(), file.getName() + ".csv");
            }
            if (taskManager.exportAllToFile(file.getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Export Successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Export Failed!");
            }
        }
    }
}
