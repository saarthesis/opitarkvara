package org.test;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class MainWindow extends JFrame{
	
	protected JTextArea textArea;

	MainWindow(){
	    
		MenuBar menubar = new MenuBar(this);
		setJMenuBar(menubar);
		textArea = new JTextArea("jaksdfjkasdf");
		JScrollPane scrollPane = new JScrollPane(textArea);
		Dimension screenSize = new Dimension(300, 300);//Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(0,0,screenSize.width, screenSize.height);
		add(scrollPane);
		

	    setVisible(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    
	}


}
