package com.pomodoro.ui.components;

import com.pomodoro.ui.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TailwindButton extends JButton {
    Color bg, fg;
    boolean isOutline;

    public TailwindButton(String text, Color bg, Color fg, boolean outline) {
        super(text);
        this.bg = bg;
        this.fg = fg;
        this.isOutline = outline;

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setFont(Theme.FONT_Bold);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isOutline) {
            setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER_COLOR),
                new EmptyBorder(8, 16, 8, 16)
            ));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isOutline) {
            g2.setColor(Theme.BG_WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
            super.paintComponent(g);
        } else {
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
            g2.setColor(fg);
            FontMetrics fm = g2.getFontMetrics();
            Rectangle r = fm.getStringBounds(getText(), g2).getBounds();
            g2.drawString(getText(), (getWidth() - r.width) / 2, (getHeight() - r.height) / 2 + fm.getAscent());
        }
    }
}
