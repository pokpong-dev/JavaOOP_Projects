package com.pomodoro.ui;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class pomodoroTimer extends JPanel {

	public pomodoroTimer() {

		setBounds(719, 5, 260, 551);

		JPanel TimerPanel = new JPanel();
		add(TimerPanel);

		JLabel lblNewLabel = new JLabel("Timer");
		TimerPanel.add(lblNewLabel);

	}

}
