package com.pomodoro.ui.panels;

import com.pomodoro.ui.Theme;
import com.pomodoro.ui.components.TailwindButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class Sidebar extends JPanel {
    private Consumer<String> onViewChange;
    private Consumer<String> onFilterChange;

    public Sidebar(Consumer<String> onViewChange, Consumer<String> onFilterChange) {
        this.onViewChange = onViewChange;
        this.onFilterChange = onFilterChange;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.BG_WHITE);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));

        JLabel logo = new JLabel("⚡ FocusFlow");
        logo.setFont(Theme.FONT_Header);
        logo.setForeground(Theme.TEXT_PRIMARY);
        logo.setBorder(new EmptyBorder(30, 24, 40, 24));
        add(logo);

        add(createNavBtn("All Tasks", "ALL", true));
        add(createNavBtn("Today", "TODAY", true));
        add(createNavBtn("Scheduled", "SCHEDULED", true));
        add(createNavBtn("Completed", "COMPLETED", true));

        add(Box.createVerticalStrut(20));
        add(new JSeparator());
        add(Box.createVerticalStrut(20));

        add(createNavBtn("Settings", "SETTINGS", false));

        add(Box.createVerticalGlue());
    }

    private JButton createNavBtn(String text, String actionKey, boolean isFilter) {
        JButton btn = new JButton(text);
        btn.setFont(Theme.FONT_Body);
        btn.setForeground(Theme.TEXT_SECONDARY);
        if (isFilter && actionKey.equals("ALL"))
            btn.setForeground(Theme.BLUE_600);

        btn.setBackground(Theme.BG_WHITE);
        btn.setBorder(new EmptyBorder(12, 24, 12, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(240, 45));

        btn.addActionListener(e -> {
            for (Component c : getComponents()) {
                if (c instanceof JButton)
                    c.setForeground(Theme.TEXT_SECONDARY);
            }
            btn.setForeground(Theme.BLUE_600);

            if (isFilter) {
                if (onFilterChange != null)
                    onFilterChange.accept(actionKey);
                if (onViewChange != null)
                    onViewChange.accept("TASKS");
            } else {
                if (onViewChange != null)
                    onViewChange.accept(actionKey);
            }
        });
        return btn;
    }
}
