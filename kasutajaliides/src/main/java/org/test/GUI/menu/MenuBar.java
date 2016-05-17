package org.test.GUI.menu;

import javax.swing.JMenuBar;

import org.test.GUI.MainWindow;

public class MenuBar extends JMenuBar {
	
	public MenuBar(MainWindow mw){
		
		FileMenu fileMenu = new FileMenu(mw);
		add(fileMenu);
	}

}
