package org.test.GUI.menu;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


import java.lang.Runtime;

import java.io.InputStreamReader;
import java.lang.Exception;


import org.apache.commons.io.IOUtils;
import org.test.GUI.MainWindow;
import org.test.GUI.tagid.puu.DrawPanel;



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

		JMenuItem snap_shot = new JMenuItem("SnapShot");

		snap_shot.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				snapShot();

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
		add(snap_shot);
	}
	
	private void snapShot(){
		String name = JOptionPane.showInputDialog(mw, "Sisesta väljastava pildi nimi:");
		DrawPanel panel = mw.puuPanel.drawArea;
		BufferedImage bi = new BufferedImage(panel.getSize().width, panel.getSize().height, BufferedImage.TYPE_INT_ARGB); 
		Graphics g = bi.createGraphics();
		panel.paint(g);  //this == JComponent
		g.dispose();
		try{
			ImageIO.write(bi,"png",new File(name+".png"));
			JOptionPane.showMessageDialog(mw, "Success!");
		}catch (Exception e) {
			JOptionPane.showMessageDialog(mw, "FAILED!");
		}
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

		//System.out.println("WE got haskell output length " + output.length());
		
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
               // System.out.println("Exited with error code "+exitVal);
 
        	} catch(Exception e) {
          //      System.out.println(e.toString());
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
            //    System.out.println("Exited with error code "+exitVal);
 
        	} catch(Exception e) {
              //  System.out.println(e.toString());
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
			mw.setTitle(file.getName());

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
		//System.out.println("savaeFile");

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
		//System.out.println("Save as file");
		String path = FileMenu.class.getProtectionDomain().getCodeSource().getLocation().getPath() ; 

		JFileChooser fileChooser = new JFileChooser(path);

		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {

			File file = fileChooser.getSelectedFile();
			//System.out.println("selected file" + file.getName());
			String text = getTeadmusbaasText();

			try{
				new FileReader(file);
			//	System.out.println("this file already existed, therefore we write that over");
				JOptionPane.showMessageDialog(mw, "Saved over existing file!");

			}catch(FileNotFoundException e){
			//	System.out.println("we did not find that file!");
				JOptionPane.showMessageDialog(mw, "Made new file!");
			}

			try {
				PrintWriter pw = new PrintWriter(file);
				pw.write(text);
				pw.close();
				selectedFile = file;
				mw.setTitle(file.getName());

			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}

			


		}
		

	}
	
}
