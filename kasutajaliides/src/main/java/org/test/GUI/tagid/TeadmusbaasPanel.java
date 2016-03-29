package org.test.GUI.tagid;

import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TeadmusbaasPanel extends JPanel{
	
	JTextArea teadmusbaasText;
	
	public TeadmusbaasPanel() {
		
		setLayout(new GridLayout(0, 1));
		teadmusbaasText = new JTextArea("Write here");
		JScrollPane scrollPane = new JScrollPane(teadmusbaasText);
		add(scrollPane);
	}

	public JTextArea getAreaText() {
		return teadmusbaasText;
	}

}
