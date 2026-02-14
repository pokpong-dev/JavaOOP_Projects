package com.pomodoro.ui.components;

import com.pomodoro.model.Priority;
import com.pomodoro.model.Task;
import com.pomodoro.model.TaskStatus;
import com.pomodoro.ui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;

public class TableRenderers {

    public static class IndexRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
            setForeground(Theme.TEXT_SECONDARY);
            setHorizontalAlignment(CENTER);
            return this;
        }
    }

    public static class DateRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
            if (v instanceof Task) {
                Task task = (Task) v;
                if (task.getDueDate() == null) {
                    setText("-");
                } else {
                    String d = new SimpleDateFormat("MMM dd HH:mm").format(task.getDueDate());
                    if (task.getReminderTime() != null)
                        d += " 🔔";
                    setText(d);
                }
            } else {
                setText(v != null ? v.toString() : "-");
            }
            return this;
        }
    }

    public static class TimeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            super.getTableCellRendererComponent(t, v, isS, hasF, r, c);
            setFont(Theme.FONT_Bold);
            setForeground(Theme.BLUE_600);
            return this;
        }
    }

    public static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            TaskStatus s = (v instanceof TaskStatus) ? (TaskStatus) v : TaskStatus.TODO;

            JLabel l = new JLabel(s.toString());
            l.setOpaque(true);
            l.setFont(new Font("SansSerif", Font.BOLD, 10));
            l.setHorizontalAlignment(CENTER);
            l.setBorder(new EmptyBorder(4, 8, 4, 8));

            if (s == TaskStatus.IN_PROGRESS) {
                l.setBackground(new Color(254, 243, 199));
                l.setForeground(Theme.AMBER_500);
                l.setText("⚡ DOING");
            } else if (s == TaskStatus.DONE) {
                l.setBackground(new Color(220, 252, 231));
                l.setForeground(Theme.GREEN_500);
                l.setText("✓ DONE");
            } else {
                l.setBackground(new Color(241, 245, 249));
                l.setForeground(Theme.TEXT_SECONDARY);
            }

            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
            p.setBackground(isS ? t.getSelectionBackground() : Theme.BG_APP);
            p.add(l);
            return p;
        }
    }

    public static class PriorityRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            Priority p = (v instanceof Priority) ? (Priority) v : Priority.LOW;

            JLabel l = new JLabel(p.toString());
            l.setFont(Theme.FONT_Bold);

            if (p == Priority.HIGH)
                l.setForeground(Theme.RED_500);
            else if (p == Priority.MEDIUM)
                l.setForeground(Theme.AMBER_500);
            else
                l.setForeground(Theme.GREEN_500);

            JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
            pnl.setBackground(isS ? t.getSelectionBackground() : Theme.BG_APP);
            pnl.add(l);
            return pnl;
        }
    }
}
