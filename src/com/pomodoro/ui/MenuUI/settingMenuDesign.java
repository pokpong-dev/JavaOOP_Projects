package com.pomodoro.ui.MenuUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class settingMenuDesign extends JPanel{
	private JTextField focusField;
	private JTextField breakField;

	// จำนวนนาทีตั้งต้นแจ้งเตือนก่อนถึงกำหนดส่ง โดยสามารถเปลี่ยนตัวเลขเอาได้เลย
	public static final int DEFAULT_NOTIFY_BEFORE_DEADLINE_MINUTES = 1;

	public settingMenuDesign() {
		setBounds(113, 5, 601, 551);
		setLayout(null);
		
		JPanel settingPanel = new JPanel();
		settingPanel.setBounds(10, 11, 568, 284);
		add(settingPanel);
		settingPanel.setLayout(null);
		
		JPanel pomoPanel = new JPanel();
		pomoPanel.setBounds(0, 0, 568, 152);
		settingPanel.add(pomoPanel);
		pomoPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel pomorodoLabel = new JLabel("Pomodoro Timer");
		pomorodoLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pomorodoLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		pomorodoLabel.setBounds(6, 4, 164, 14);
		pomoPanel.add(pomorodoLabel, BorderLayout.NORTH);
		
		JPanel pomoRadio = new JPanel();
		pomoRadio.setBounds(31, 27, 139, 69);
		pomoPanel.add(pomoRadio, BorderLayout.CENTER);
		pomoRadio.setLayout(new BoxLayout(pomoRadio, BoxLayout.Y_AXIS));
		
		JRadioButton p_Radio1 = new JRadioButton("25 : 5");
		pomoRadio.add(p_Radio1);
		
		JRadioButton p_Radio2 = new JRadioButton("50 : 10");
		pomoRadio.add(p_Radio2);
		
		JRadioButton p_Radio3 = new JRadioButton("Custom");
		pomoRadio.add(p_Radio3);

		// ทำให้เลือกได้ตัวเลือกเดียว
		javax.swing.ButtonGroup pomoGroup = new javax.swing.ButtonGroup();
		pomoGroup.add(p_Radio1);
		pomoGroup.add(p_Radio2);
		pomoGroup.add(p_Radio3);
		p_Radio1.setSelected(true); // default
		
		JPanel customSetting = new JPanel();
		customSetting.setBounds(0, 103, 151, 56);
		pomoPanel.add(customSetting, BorderLayout.SOUTH);
		customSetting.setLayout(new BorderLayout(0, 0));
		
		JPanel Setting = new JPanel();
		customSetting.add(Setting, BorderLayout.WEST);
		Setting.setLayout(new BoxLayout(Setting, BoxLayout.Y_AXIS));
		
		JPanel focusSetting = new JPanel();
		Setting.add(focusSetting);
		
		JLabel focusLabel = new JLabel("Focus : ");
		focusLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		focusSetting.add(focusLabel);
		
		focusField = new JTextField();
		focusSetting.add(focusField);
		focusField.setColumns(10);
		
		JPanel breakSetting = new JPanel();
		Setting.add(breakSetting);
		
		JLabel breakLabel = new JLabel("Break : ");
		breakLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		breakSetting.add(breakLabel);
		
		breakField = new JTextField();
		breakSetting.add(breakField);
		breakField.setColumns(10);
		
		
		JPanel notiPanel = new JPanel();
		notiPanel.setBounds(0, 168, 568, 134);
		settingPanel.add(notiPanel);
		notiPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JPanel notiSetting = new JPanel();
		notiPanel.add(notiSetting);
		notiSetting.setLayout(new BoxLayout(notiSetting, BoxLayout.Y_AXIS));
		
		JLabel notiLabel = new JLabel("Notification Setting");
		notiLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		notiSetting.add(notiLabel);
		
		JPanel notiRadio = new JPanel();
		notiSetting.add(notiRadio);
		notiRadio.setLayout(new BoxLayout(notiRadio, BoxLayout.Y_AXIS));
		
		JRadioButton n_Radio1 = new JRadioButton("แจ้งเตือนเมื่อ Pomodoro จบ");
		notiRadio.add(n_Radio1);

		// ดึงค่าแจ้งเตือนเริ่มต้นมาแสดงผล
		JRadioButton n_Radio2 = new JRadioButton(
				"แจ้งเตือนก่อนถึง Deadline (" + DEFAULT_NOTIFY_BEFORE_DEADLINE_MINUTES + " นาที)");
		notiRadio.add(n_Radio2);

		// บังคับให้เลือกได้เฉพาะตัวเลือกเดียวและตั้งค่าเริ่มต้นไว้ที่การแจ้งก่อนกำหนด
		javax.swing.ButtonGroup notiGroup = new javax.swing.ButtonGroup();
		notiGroup.add(n_Radio1);
		notiGroup.add(n_Radio2);
		n_Radio2.setSelected(true); // default

	}
}
