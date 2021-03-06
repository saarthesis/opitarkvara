package org.test.GUI.tagid.puu;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.test.GUI.MainWindow;
import org.test.parser.InvalidInputException;

/**
 * Inside of "puu" tag
 *
 */
public class TreePanel extends JPanel{

	public DrawPanel drawArea;
	private AlgorithmPanel algorithmArea;
	public MainWindow mw;
	
	public TreePanel(MainWindow mw){
		this.mw = mw;
		setLayout(new BorderLayout());
		
		drawArea = new DrawPanel(this);

		// algorithm area
		algorithmArea = new AlgorithmPanel();
		
		// scrolli suurus sõltub draw area suurusest
		JScrollPane scroll = new JScrollPane(drawArea);
		
		add("North", algorithmArea);
		add("Center", scroll);
	}

	public void displayOutput(String input, String endStates) throws InvalidInputException{
		drawArea.displayOutput(input, endStates);
	}

	public String getAlgo(){
		return algorithmArea.getSelected();
	}






}