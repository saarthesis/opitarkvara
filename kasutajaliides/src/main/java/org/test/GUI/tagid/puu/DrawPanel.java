package org.test.GUI.tagid.puu;

import javax.swing.JPanel;

import org.test.node.Node;
import org.test.node.NodeSortAscId;
import org.test.parser.HaskellOutputParser;
import org.test.parser.InvalidInputException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class DrawPanel extends JPanel{

	// everything that is inside of this list is painted on screen
	// so if you want to adjust what is on tree, you should modify this list
	public static HashSet<Node> TREE_NODES = new HashSet<Node>(); 

	private TreePanel treePanel;
	private Node node; // rootnode
	
	final int START_X; // mid of jframe. init when constructor! later need to change it, so it adjusts
	final int START_Y = 20;

	public DrawPanel(TreePanel treePanel){
		this.treePanel = treePanel;
		START_X = 0;//(int) Math.round(treePanel.mw.getSize().getWidth()/1.5);
		setBackground(Color.WHITE);
		// if you want to have scrollpane working you need to adjust the size
		// if you do not adjust the size, then scroll does not know that it has room to scroll
		setPreferredSize(new Dimension(500,1000)); 

	}

	/**
	 * 
	 * For testing purpose
	 * 
	 */
	public static void printAllTreeNodes(){
		for(Node n : TREE_NODES){
			System.out.println(n);
		}
	}

	/**
	*
	* Checks if there is some node on this location. 
	* draw tree nodes..
	*
	*
	*/
	public static Node onLocation(int x, int y, Node notThis){
		for(Node n : TREE_NODES){
			if(!n.equals(notThis) && n.onLocation(x,y)){
				return n;
			} 
		}
		return null;
	}


	/**
	*
	* This gets haskell output.
	*
	* nodesInput is full tree nodes with syntax like following:
	* Node {state = State [], parents = [], children = []}
	* 
	* endStates is string of endstates. This is necessary for showing different algos
	* and for painting end states
	* ex: "[State [2,0],State [2,1],State [2,2],State [2,3]]"
	* 
	*/
	public void displayOutput(String nodesInput, String endStates) throws InvalidInputException{

		//System.out.println("Inside draw panel display output!");
		
		// RESTART
		TREE_NODES.clear();
		repaint();
		Node.ID = 0;

		//System.out.println("endstates were " + endStates);

		switch(treePanel.getAlgo()){
			case "Süvitsi":
				createFullTreeNode(nodesInput, endStates);
				displayDepth(nodesInput, endStates);
				break;
			case "Laiuti":
				createFullTreeNode(nodesInput, endStates);
				displayWidth(nodesInput, endStates);
				break;
			case "Full Tree":
				createFullTreeNode(nodesInput, endStates);
				repaint();
				break;
			default:
				throw new InvalidInputException("No algo defined!");
		}
	}

	/**
	 * 
	 * Vastavalt puu suurusele peab olema ka "joonistav ala", et
	 * ikka kogu puu oleks ekraanil. Erinevatel puudel on erinev "joonistava ala" 
	 * suurus.
	 * 
	 */
	private Integer adjustScrollArea() {
		
		
		if(TREE_NODES.size()== 0){
			//do nothing
			return null;
		} 
		else{
			// kohenda "joonistava ala" laiust
			Integer min = null;
			Integer max = null;
			
			Integer maxY = null;
			
			for(Node n : TREE_NODES){
				
				if(maxY == null) maxY = n.getY();
				else if(maxY < n.getY()){
					maxY = n.getY();
				}
				
				if(min == null){
					min = n.getX();
				}else if (n.getX() < min){
					min = n.getX();
				}
				
				if(max == null){
					max = n.getX();
				}
				else if(n.getX() > max){
					max = n.getX();
				}
			}
			
			//System.out.println("------min is: --- " + min);			
			//System.out.println("------max is: --- " + max);

			int newWidth = ((0 + max) + (Math.abs(0 - min)))*2;
			
			for(Node n: TREE_NODES){
				n.setX(n.getX() + newWidth/2 + 100);
			} 
			
			int newHeight = maxY + 200;
				
			//System.out.println("-------UUUS WIDTH ON----- " + newWidth);
			this.setPreferredSize(new Dimension(newWidth, newHeight));
			this.updateUI();
			
			return newWidth;
		}
		
		
		
		
		
	}

	/**
	 * 
	 * This is painting the drawPanel
	 * 
	 * node is init from display*() 
	 * 
	 * node.calculateNodePositionOnTree*
	 * <<< basically calculates all x and y-s for nodes
	 * <<< after calculation we will paint them paintTree(*)
	 * 
	 * adjustTreeStructure()
	 * <<< fixes some mistakes from calculate node Position
	 * <<< in ideal world, i think it should calculate correctly in the beginning
	 * 
	 */
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    
	    paintTree(g);
	}  


	/**
	*
	* Takes all nodes from TREE_NODES and paints them
	* 
	* TREE_NODES
	* <<< contains all the nodes in the tree
	* 
	* node.paintNode(*)
	* <<< node has necessary coordinates and parent
	* <<< it paint the box and arrow
	*
	*/
	public void paintTree(Graphics g){
		
		if( node != null){
			for(Node node : DrawPanel.TREE_NODES){
				node.paintNode(g);
			}
		}

	}

	/**
	 * Basically moves boxes around to make it look nicer
	 */
	private void adjustTreeStructure() {
		ArrayList<Node> ns = new ArrayList<Node>(DrawPanel.TREE_NODES);
		Collections.sort(ns, new NodeSortAscId()); // nodes that are created first, are first
		
		for(Node node : ns){		
			node.adjustLocation();
		}
	}




	/**
	 * 
	 * Pay attention that this method adds nodes to TREE_NODES.
	 * That means if you run other algorithms, you probably want to clear list
	 * 
	 * @param input
	 * @param endStates
	 */
	private void createFullTreeNode(String input, String endStates) {
		List<String> end_states = HaskellOutputParser.parseEndStates(endStates);
		Node node = HaskellOutputParser.parse(input, end_states);

		this.node = node;

		// see lisab samuti TREE_NODESi kõik need tipud
    	this.node.calculateNodePositionOnTree(START_X, START_Y);
    	
    	//see kohendab
    	adjustTreeStructure();
    	
    	adjustScrollArea();
	
    	// see kohendab ala
    	// üks variant oleks liigutada neid keskele
    	// teine variant on uuesti kõik arvutada.
    	// millegi pärast praegu ma ei mõtle välja, et mida vaja teha on.
	//Integer newWidth = adjustScrollArea();
    	//this.node.calculateNodePositionOnTree(newWidth/2, START_Y);
    	//adjustTreeStructure();
	
	}

	
	class AnimationThread extends Thread{
		String input;
		String endstates;
		String algo;
		public AnimationThread(String input, String endstates, String algo) {
			// TODO Auto-generated constructor stub
			this.input = input;
			this.endstates = endstates;
			this.algo = algo;
		}
		
		public void run(){
			
			if(algo.equals("width")) displayWidth2(input, endstates,this);
			else displayDepth2(input, endstates,this);
			
		}
		
	}
	
	public void displayWidth(String input, String endStates){
		AnimationThread animation = new AnimationThread(input,endStates, "width");
		animation.start();
	}
	
	public void displayWidth2(String input, String endStates, AnimationThread animation){

		
		//System.out.println("Display width!");
		//createFullTreeNode(input, endStates); // liigutasime selle ülemisse meetodi

		TREE_NODES.clear(); // we need to empty it, because otherways there is full tree
		// but note this.node still stays!! ;) so we can use that
		
		LinkedList<Node> df = new LinkedList<Node>();
		df.add(this.node);
		
		TREE_NODES.add(this.node);
		
		boolean jump_out = false;
		
		while(true){
			//System.out.println("loooooooooooop süvitsi -----------");
			if(df.size()== 0){
				System.out.println("size is 0 we jumped out!");
				break; // lahendit ei leidunud
			}
			
			Node n = df.pop();
			//System.out.println("we are looking node: " + n.simpleToString());
			
			

			
				// lisame vahetud järglased stacki algusesse
				// pane tähele, et teem seda tagurpidi, sest siis on õiges järjekorras
				
				// print for testing
				System.out.println("before adding to stack");
				for(Node stack_node : df){
					System.out.println(stack_node.simpleToString());
				}
				
				for(Node nn : n.getChildren()){ // hmm miks tagurpidi
					//System.out.println("we added node");
					TREE_NODES.add(nn);
					df.addLast(nn);
					
				}
				
			
				
				
				try {
					repaint();
					animation.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(anyAccepting(n.getChildren())) break;

				//System.out.println("after adding to stack");
				for(Node stack_node : df){
					System.out.println(stack_node.simpleToString());
				}
			}
			
		
		
	}
	
	private boolean anyAccepting(List<Node> children) {
		for(Node n : children){
			if(n.isAccepting()) return true;
		}
		return false;
	}

	public void displayDepth(String input, String endStates){
		AnimationThread animation = new AnimationThread(input,endStates, "depth");
		animation.start();		
	}


	public void displayDepth2(String input, String endStates, AnimationThread animation){
	//	System.out.println("DEPTH tree display!");
		
		//createFullTreeNode(input, endStates); //liigutasime selle ülemisse meetodi.

		TREE_NODES.clear(); // we need to empty it, because otherways there is full tree
		// but note this.node still stays!! ;) so we can use that
		
		Stack<Node> df = new Stack<Node>();
		df.push(this.node);
		
		TREE_NODES.add(this.node);
		
		while(true){
		//	System.out.println("loooooooooooop-----------");
			if(df.size()== 0){
			//	System.out.println("size is 0 we jumped out!");
				break; // lahendit ei leidunud
			}
			
			Node n = df.pop();
		//	System.out.println("we are looking node: " + n.simpleToString());
			
			
			
				// lisame vahetud järglased stacki algusesse
				// pane tähele, et teem seda tagurpidi, sest siis on õiges järjekorras
				for(int i = n.getChildren().size()-1; i >= 0; i--){
			//		System.out.println("we added node");
					TREE_NODES.add(n.getChildren().get(i));
					df.push(n.getChildren().get(i));
				}
				


				try {
					repaint();
					animation.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				if(anyAccepting(n.getChildren())) break;
			
			
		}
		

		
	}

}
