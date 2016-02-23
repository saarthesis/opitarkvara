package org.test;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class MainWindow extends JFrame{
	
	protected JTextArea teadmusbaasText;
	private JTabbedPane tabbedPane;

	protected JTextArea puuText;

	MainWindow(){
	    
	    // menu bar
		MenuBar menubar = new MenuBar(this);
		setJMenuBar(menubar);

		// teadmusbaas panel
		JPanel teadmusbaasPanel = new JPanel();
		teadmusbaasPanel.setLayout(new GridLayout(0, 1));
		teadmusbaasText = new JTextArea("Write here");
		JScrollPane scrollPane = new JScrollPane(teadmusbaasText);
		teadmusbaasPanel.add(scrollPane);

		// puu panel
		JPanel puuPanel = new JPanel();
		puuPanel.setLayout(new GridLayout(0, 1));
		puuText = new JTextArea("Puu tuleb siia");
		JScrollPane scrollPane2 = new JScrollPane(puuText);
		puuPanel.add(scrollPane2);


		// tabs
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Teadmusbaas",teadmusbaasPanel);
		tabbedPane.addTab("Puu",puuPanel);
		add(tabbedPane);


		Dimension screenSize = new Dimension(500, 500);//Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(0,0,screenSize.width, screenSize.height);
	    setVisible(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    
	}

	public void switchToTreeTab(){
		tabbedPane.setSelectedIndex(1);
	}

	public void switchToTeadmusbaas(){
		tabbedPane.setSelectedIndex(0);
	}



}
