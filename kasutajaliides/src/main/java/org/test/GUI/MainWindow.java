package org.test.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.test.GUI.tagid.TeadmusbaasPanel;
import org.test.GUI.tagid.puu.TreePanel;
import org.test.parser.InvalidInputException;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import java.lang.Exception;

public class MainWindow extends JFrame{
	
	private JTabbedPane tabbedPane;

	public TreePanel puuPanel;
	private TeadmusbaasPanel teadmusbaasPanel;

	
	private final int WIDTH = 1000;

	public MainWindow(){
		
		Dimension screenSize = new Dimension(WIDTH, 700);//Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(0,0,screenSize.width, screenSize.height);
	    
	    
	    // menu bar
		MenuBar menubar = new MenuBar(this);
		setJMenuBar(menubar);

		// teadmusbaas panel
		 teadmusbaasPanel = new TeadmusbaasPanel();
		
		// puu panel
		puuPanel = new TreePanel(this);

		// tabs
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Teadmusbaas",teadmusbaasPanel);
		tabbedPane.addTab("Puu",puuPanel);
		add(tabbedPane);



	    setVisible(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    
	    
	}

	public void switchToTreeTab(){
		tabbedPane.setSelectedIndex(1);
	}

	public void switchToTeadmusbaas(){
		tabbedPane.setSelectedIndex(0);
	}

	public void displayOutput(String input, String endStates){
		try{
			System.out.println("display output");
			puuPanel.displayOutput(input, endStates);
		}
		catch(InvalidInputException e){
			e.printStackTrace();
		}
	}

	public TeadmusbaasPanel getTeadmusbaasPanel() {
		return teadmusbaasPanel;
	}


}
