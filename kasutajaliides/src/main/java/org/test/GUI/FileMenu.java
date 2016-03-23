package org.test.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


import java.lang.Runtime;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.lang.Exception;


import org.apache.commons.io.IOUtils;

public class FileMenu extends JMenu{

	private final MainWindow mw;

	private File selectedFile;

	FileMenu(MainWindow mw){
		super("File");
		this.mw = mw;
		
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem saveas = new JMenuItem("Save as");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem run = new JMenuItem("Run");

		JMenuItem test = new JMenuItem("TEST");


		test.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				String input = "Node {state = State [0,0], children = [Node {state = State [0,3], children = [Node {state = State [3,0], children = [Node {state = State [3,3], children = [Node {state = State [4,2], children = [Node {state = State [0,2], children = [Node {state = State [2,0], children = []}]},Node {state = State [4,0], children = [Node {state = State [1,3], children = [Node {state = State [1,0], children = [Node {state = State [0,1], children = [Node {state = State [4,1], children = [Node {state = State [2,3], children = []},Node {state = State [4,3], children = []}]}]}]},Node {state = State [4,3], children = []}]},Node {state = State [4,3], children = []}]},Node {state = State [4,3], children = [Node {state = State [4,0], children = [Node {state = State [1,3], children = [Node {state = State [1,0], children = [Node {state = State [0,1], children = [Node {state = State [4,1], children = [Node {state = State [2,3], children = []}]}]}]}]}]}]}]},Node {state = State [4,3], children = [Node {state = State [4,0], children = [Node {state = State [1,3], children = [Node {state = State [1,0], children = [Node {state = State [0,1], children = [Node {state = State [4,1], children = [Node {state = State [2,3], children = []}]}]}]}]}]}]}]},Node {state = State [4,0], children = [Node {state = State [1,3], children = [Node {state = State [1,0], children = [Node {state = State [0,1], children = [Node {state = State [4,1], children = [Node {state = State [2,3], children = []},Node {state = State [4,3], children = []}]}]}]},Node {state = State [4,3], children = []}]},Node {state = State [4,3], children = []}]}]},Node {state = State [4,3], children = [Node {state = State [4,0], children = [Node {state = State [1,3], children = [Node {state = State [1,0], children = [Node {state = State [0,1], children = [Node {state = State [4,1], children = [Node {state = State [2,3], children = []}]}]}]}]}]}]}]},Node {state = State [4,0], children = [Node {state = State [1,3], children = [Node {state = State [0,3], children = [Node {state = State [3,0], children = [Node {state = State [3,3], children = [Node {state = State [4,2], children = [Node {state = State [0,2], children = [Node {state = State [2,0], children = []}]},Node {state = State [4,3], children = []}]},Node {state = State [4,3], children = []}]}]},Node {state = State [4,3], children = []}]},Node {state = State [1,0], children = [Node {state = State [0,1], children = [Node {state = State [0,3], children = [Node {state = State [3,0], children = [Node {state = State [3,3], children = [Node {state = State [4,2], children = [Node {state = State [0,2], children = [Node {state = State [2,0], children = []}]},Node {state = State [4,3], children = []}]},Node {state = State [4,3], children = []}]}]},Node {state = State [4,3], children = []}]},Node {state = State [4,1], children = [Node {state = State [2,3], children = []},Node {state = State [4,3], children = [Node {state = State [0,3], children = [Node {state = State [3,0], children = [Node {state = State [3,3], children = [Node {state = State [4,2], children = [Node {state = State [0,2], children = [Node {state = State [2,0], children = []}]}]}]}]}]}]}]}]}]},Node {state = State [4,3], children = [Node {state = State [0,3], children = [Node {state = State [3,0], children = [Node {state = State [3,3], children = [Node {state = State [4,2], children = [Node {state = State [0,2], children = [Node {state = State [2,0], children = []}]}]}]}]}]}]}]},Node {state = State [4,3], children = [Node {state = State [0,3], children = [Node {state = State [3,0], children = [Node {state = State [3,3], children = [Node {state = State [4,2], children = [Node {state = State [0,2], children = [Node {state = State [2,0], children = []}]}]}]}]}]}]}]}]}";
				mw.puuPanel.drawArea.displayFull(input, "");
			}
		});

		run.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				runFile();

			}
		});

		newItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				startNewFile();

			}
		});

		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				saveFile();
				
			}
		});

		saveas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				saveAsFile();
				
			}
		});
		
		load.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				loadFile();
				
			}
		});
		
		add(newItem);
		add(save);
		add(saveas);
		add(load);
		add(run);
		add(test);
	}

	private void runFile(){
		// 1. save current teadmusbaas
		saveFile();
		// 2. get teadmusbaas location
		String p = selectedFile.getAbsolutePath();
		// 3. switch tabs
		mw.switchToTreeTab();
		// 4. run haskell program with teadmusbaas location
		String output = getHaskellOutputForFullTreeNodes(p);
		// 5. display output in tree tab

		System.out.println("WE got haskell output length " + output.length());
		
		String endStates = getHaskellOutputForEndStates(p);
		
		mw.displayOutput(output, endStates);
	}

	private String getHaskellOutputForEndStates(String p) {
		String teadmusbaas = p; // "teadmusbaas/armukadedad_purjetajad_PROD.txt";
		 
		String programm = "./ProductionsSolver"; // jar peab asuma samas failis, kus haskell

		String execStr = programm + " endstates " + teadmusbaas;

		String output = "";

		try {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(execStr);

			try{
		    BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
 
                String line=null;
 
                while((line=input.readLine()) != null) {
                	output += line + "\n";
                }
 
                int exitVal = pr.waitFor();
                System.out.println("Exited with error code "+exitVal);
 
        	} catch(Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }


		}
		catch(IOException e){
			e.printStackTrace();
		}
		return output;
	}

	private String getHaskellOutputForFullTreeNodes(String teadmusbaasPath){
		String teadmusbaas = teadmusbaasPath; // "teadmusbaas/armukadedad_purjetajad_PROD.txt";
		 
		String programm = "./ProductionsSolver"; // jar peab asuma samas failis, kus haskell

		String execStr = programm + " " + teadmusbaas;

		String output = "";

		try {
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(execStr);

			try{
		    BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
 
                String line=null;
 
                while((line=input.readLine()) != null) {
                	output += line + "\n";
                }
 
                int exitVal = pr.waitFor();
                System.out.println("Exited with error code "+exitVal);
 
        	} catch(Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }


		}
		catch(IOException e){
			e.printStackTrace();
		}
		return output;
	}
	
	private void setTeadmusbaasPanelText(String input){
		mw.getTeadmusbaasPanel().getAreaText().setText(input);
	}
	
	private String getTeadmusbaasText(){
		return mw.getTeadmusbaasPanel().getAreaText().getText();	
	}

	private void startNewFile(){

		setTeadmusbaasPanelText("");
		selectedFile = null;

	}

	private void loadFile(){
		
		String path = FileMenu.class.getProtectionDomain().getCodeSource().getLocation().getPath() ; 

		JFileChooser fileChooser = new JFileChooser(path);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			selectedFile = file;

			try {

				String content = IOUtils.toString(new FileReader(file));
				setTeadmusbaasPanelText(content);

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

		mw.switchToTeadmusbaas();

	}

	private void saveFile(){

		if(selectedFile == null){

			saveAsFile();
		}
		else{

			String text = getTeadmusbaasText();

			try {
				PrintWriter pw = new PrintWriter(selectedFile);
				pw.write(text);
				pw.close();
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}

				JOptionPane.showMessageDialog(mw, "Saved over existing file!");
		}

	}

	private void saveAsFile(){

		String path = FileMenu.class.getProtectionDomain().getCodeSource().getLocation().getPath() ; 

		JFileChooser fileChooser = new JFileChooser(path);

		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			String text = getTeadmusbaasText();

			try{
				new FileReader(file);
				selectedFile = file;
				JOptionPane.showMessageDialog(mw, "Saved over existing file!");

			}catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(mw, "Made new file!");
			}

			try {
				PrintWriter pw = new PrintWriter(file);
				pw.write(text);
				pw.close();

			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}

			


		}
		

	}
	
}
