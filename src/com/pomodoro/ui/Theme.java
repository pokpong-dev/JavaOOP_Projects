package com.pomodoro.ui;

import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Theme {
    // ==========================================
    // TAILWIND DESIGN SYSTEM
    // ==========================================
    public static final Color BG_APP = new Color(248, 250, 252);
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(226, 232, 240);

    public static final Color TEXT_PRIMARY = new Color(15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100, 116, 139);

    public static final Color BLUE_600 = new Color(37, 99, 235);
    public static final Color BLUE_50 = new Color(239, 246, 255);
    public static final Color GREEN_500 = new Color(34, 197, 94);
    public static final Color AMBER_500 = new Color(245, 158, 11);
    public static final Color RED_500 = new Color(239, 68, 68);
    public static final Color INDIGO_500 = new Color(99, 102, 241);

    public static final Font FONT_Display = new Font("SansSerif", Font.BOLD, 54);
    public static final Font FONT_Header = new Font("SansSerif", Font.BOLD, 16);
    public static final Font FONT_Body = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_Bold = new Font("SansSerif", Font.BOLD, 13);

    public static final Border INPUT_BORDER = new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10));
}
