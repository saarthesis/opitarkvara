package org.test.GUI;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.util.HashSet;

public class AlgorithmPanel extends JPanel{

	private HashSet<JCheckBox> boxes = new HashSet<JCheckBox>();

	public AlgorithmPanel(){
		createCheckBox("Süvitsi", true);
		createCheckBox("Laiuti", false);
		createCheckBox("Full Tree", false);
	}

	private void createCheckBox(String title, boolean isSelected ){
		JCheckBox box = new JCheckBox(title, isSelected);
		boxes.add(box);
		box.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent ev){
				int state = ev.getStateChange();
		        if (state == ItemEvent.SELECTED) {
		        	unselectEveryElse(box);
		        }
			}
		});
		add(box);
	}

	private void unselectEveryElse(JCheckBox box){
		for(JCheckBox b : boxes){
			if(!b.equals(box) && b.isSelected()) b.setSelected(false);
		}
	}

	public String algoSelected(){
		for(JCheckBox b : boxes){
			if(b.isSelected()) return b.getText();
		}
		return null;
	}



}