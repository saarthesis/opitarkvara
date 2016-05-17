package org.test.GUI.tagid.puu;

import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import java.util.HashSet;

public class AlgorithmPanel extends JPanel{

	private HashSet<JCheckBox> boxes = new HashSet<JCheckBox>();

	public AlgorithmPanel(){
		createCheckBox("Süvitsi", true);
		createCheckBox("Laiuti", false);
		createCheckBox("Täispuu", false);
	}

	private void createCheckBox(String title, boolean isSelected ){
		JCheckBox box = new JCheckBox(title, isSelected);
		boxes.add(box);
		box.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent ev){
				int state = ev.getStateChange();
		        if (state == ItemEvent.SELECTED) {
		        	deselectEveryoneElse(box);
		        }
			}
		});
		add(box);
	}

	private void deselectEveryoneElse(JCheckBox box){
		for(JCheckBox b : boxes){
			if(!b.equals(box) && b.isSelected()) b.setSelected(false);
		}
	}

	public String getSelected(){
		for(JCheckBox b : boxes){
			if(b.isSelected()) return b.getText();
		}
		return null;
	}



}