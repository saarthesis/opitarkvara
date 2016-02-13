package org.test;

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
	}

	private void startNewFile(){

		mw.textArea.setText("");
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
				mw.textArea.setText(content);

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

	}

	private void saveFile(){

		if(selectedFile == null){

			saveAsFile();
		}
		else{

			String text = mw.textArea.getText();

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
			String text = mw.textArea.getText();

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
