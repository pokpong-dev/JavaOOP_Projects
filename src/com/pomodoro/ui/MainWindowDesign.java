package com.pomodoro.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.pomodoro.logic.TaskManager;
import com.pomodoro.ui.MenuUI.trashMenuDesign;
import com.pomodoro.ui.MenuUI.homeMenuDesign;
import com.pomodoro.ui.MenuUI.settingMenuDesign;
import com.pomodoro.ui.MenuUI.taskMenuDesign;
import javax.swing.BoxLayout;

public class MainWindowDesign extends JFrame {

    private static final long serialVersionUID = 1L;

    // --- Fields (WindowBuilder) ---
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainWindowDesign frame = new MainWindowDesign();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainWindowDesign() {

        // เปลี่ยนหน้าตาให้รองรับภาษาไทย
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

        UIManager.put("Label.font", thaiFont);
        UIManager.put("Panel.font", thaiFont);
        UIManager.put("Button.font", thaiFont);
        UIManager.put("TextField.font", thaiFont);
        UIManager.put("TextArea.font", thaiFont);
        UIManager.put("ComboBox.font", thaiFont);
        UIManager.put("List.font", thaiFont);
        UIManager.put("CheckBox.font", thaiFont);
        UIManager.put("MenuItem.font", thaiFont);
        UIManager.put("ToolTip.font", thaiFont);
        UIManager.put("RadioButton.font", thaiFont);
        UIManager.put("TableHeader.font", thaiFont);

        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);

        // ตั้งค่าหน้าต่างโปรแกรมหลัก
        setTitle("TaskMaster");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 1200, 600);
        setResizable(false); // ล็อคขนาดหน้าต่างห้ามผู้ใช้ขยาย
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // พื้นที่แสดงผลส่วนกลาง
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // ระบบสลับหน้าต่างการใช้งาน
        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(113, 5, 780, 551);
        contentPanel.setPreferredSize(new Dimension(780, 0));
        contentPanel.setBorder(BorderFactory.createTitledBorder("Home"));
        contentPane.add(contentPanel, BorderLayout.CENTER);

        // ใช้งานตัวจัดการงานร่วมกันในทุกหน้าย่อย
        TaskManager sharedTaskManager = new TaskManager();
        taskMenuDesign taskMenu = new taskMenuDesign(sharedTaskManager);
        trashMenuDesign trashMenu = new trashMenuDesign(sharedTaskManager);
        homeMenuDesign home = new homeMenuDesign(sharedTaskManager);

        // แถบเมนูซ้ายมือ
        JPanel menuPanel = new JPanel();
        menuPanel.setBounds(5, 5, 160, 551);
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        menuPanel.setPreferredSize(new Dimension(160, 0));
        contentPane.add(menuPanel, BorderLayout.WEST);

        JPanel Menu = new JPanel();

        // ปุ่มหน้าหลัก
        JButton btnHome = new JButton("Home");
        btnHome.setHorizontalAlignment(SwingConstants.LEFT);
        btnHome.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHome.getPreferredSize().height));

        // กดปุ่มเพื่อสลับหน้า
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "HOME");
                contentPanel.setBorder(BorderFactory.createTitledBorder("Home"));
                home.reloadHome();
            }
        });
        menuPanel.setLayout(new BorderLayout(0, 0));
        Menu.setLayout(new BoxLayout(Menu, BoxLayout.Y_AXIS));
        Menu.add(btnHome);

        // ปุ่มหน้างาน
        JButton btnTask = new JButton("Task");
        btnTask.setHorizontalAlignment(SwingConstants.LEFT);
        btnTask.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHome.getPreferredSize().height));

        // กดปุ่มเพื่อสลับหน้า
        btnTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "TASK");
                contentPanel.setBorder(BorderFactory.createTitledBorder("Task"));
                taskMenu.reloadTask(); // โหลดข้อมูลตารางใหม่ทุกรอบที่กลับมาหน้านี้
            }
        });
        Menu.add(btnTask);

        // ปุ่มหน้าถังขยะ
        JButton btnTrash = new JButton("Trash");
        btnTrash.setHorizontalAlignment(SwingConstants.LEFT);
        btnTrash.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHome.getPreferredSize().height));

        btnTrash.addActionListener(new ActionListener() {
            // คำสั่งกดปุ่มเพื่อสลับไปหน้าถังขยะ
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "TRASH");
                contentPanel.setBorder(BorderFactory.createTitledBorder("Trash"));
                trashMenu.reloadTrash(); // ข้อมูลถังขยะจะถูกโหลดใหม่ตอนเปิดหน้านี้เสมอ
            }
        });
        Menu.add(btnTrash);

        // ปุ่มหน้าตั้งค่า
        JButton btnSetting = new JButton("Setting");
        btnSetting.setHorizontalAlignment(SwingConstants.LEFT);
        btnSetting.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnHome.getPreferredSize().height));

        // กดปุ่มเพื่อสลับหน้า
        btnSetting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "SETTING");
                contentPanel.setBorder(BorderFactory.createTitledBorder("Setting"));
            }
        });
        Menu.add(btnSetting);

        menuPanel.add(Menu, BorderLayout.NORTH);

        // สร้างแต่ละหน้าจอผูกกับข้อความ
        contentPanel.add(taskMenu, "TASK");
        contentPanel.add(home, "HOME");

        contentPanel.add(trashMenu, "TRASH");

        settingMenuDesign settingMenu = new settingMenuDesign();
        contentPanel.add(settingMenu, "SETTING");

        // แถบจับเวลาด้านขวา
        pomodoroTimer pomodoro = new pomodoroTimer();

        JPanel timerPanel = new JPanel();
        contentPane.add(timerPanel, BorderLayout.EAST);
        timerPanel.setBounds(719, 5, 260, 551);
        timerPanel.setPreferredSize(new Dimension(260, 0));
        timerPanel.setBorder(BorderFactory.createTitledBorder("Pomodoro Timer"));
        timerPanel.setLayout(new BorderLayout(0, 0));
        timerPanel.add(pomodoro);

        // เริ่มระบบจับเวลาแจ้งเตือนในพื้นหลัง ให้เช็คเดดไลน์ต่อเนื่อง
        com.pomodoro.logic.DeadlineChecker.startBackgroundChecker(sharedTaskManager);

        // แสดงหน้า Home เป็นหน้าเริ่มต้นตอนเปิดแอปพลิเคชัน
        btnHome.doClick();
    }

}
