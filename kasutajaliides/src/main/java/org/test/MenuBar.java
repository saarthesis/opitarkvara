package org.test;

import javax.swing.JMenuBar;

public class MenuBar extends JMenuBar {
	
	MenuBar(MainWindow mw){
		
		FileMenu fileMenu = new FileMenu(mw);
		add(fileMenu);
	}

}
