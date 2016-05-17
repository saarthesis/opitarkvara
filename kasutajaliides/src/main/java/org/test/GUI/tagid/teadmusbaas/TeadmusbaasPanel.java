package org.test.GUI.tagid.teadmusbaas;

import java.awt.GridLayout;

import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This is the area where is located teadmusbaas text.
 * This panel is inside of tag.
 *
 */
public class TeadmusbaasPanel extends JPanel{
	
	private JTextArea teadmusbaasText;
	
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
