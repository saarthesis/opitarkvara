package org.test.GUI.tagid.puu;

import javax.swing.JPanel;

import org.test.node.Node;
import org.test.node.NodeSortAscId;
import org.test.parser.HaskellOutputParser;
import org.test.parser.InvalidInputException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
	
	private SelectBox selectBox;
	
	private final int START_X; // mid of jframe. init when constructor! later need to change it, so it adjusts
	private final int START_Y = 20;

	public DrawPanel(TreePanel treePanel){
		this.treePanel = treePanel;
		START_X = 0;//(int) Math.round(treePanel.mw.getSize().getWidth()/1.5);
		setBackground(Color.WHITE);
		// if you want to have scrollpane working you need to adjust the size
		// if you do not adjust the size, then scroll does not know that it has room to scroll
		setPreferredSize(new Dimension(500,1000)); 
		
		MoveBoxesMouse move = new MoveBoxesMouse();
		
		this.addMouseListener(move);
		this.addMouseMotionListener(move);

	}
	
	class SelectBox {
		
		private Integer startX;
		private Integer startY;
		private Color color = new Color(17,103,252); // same as node selected
		
		private Integer endX;
		private Integer endY;
		
		public SelectBox(int x, int y) {
			// these are beginning coordinates
			this.startX = x;
			this.startY = y;
		}
		
		public void setCoordinates(int x, int y){
			endX = x;
			endY = y;
		}
		
		public void paint(Graphics g){
			g.setColor(color);
			if(endX != null){
				
				int lowestX = endX < startX ? endX : startX;
				int lowestY = endY < startY ? endY : startY;
				

				g.drawRect(lowestX, lowestY, Math.abs(endX - startX),  Math.abs(endY - startY));

				
			}
		}

		public HashSet<Node> getNodesInLoc() {
			HashSet<Node> ns = new HashSet<Node>();
			
			for(Node n : TREE_NODES){
				
				if(isInLoc(n)) ns.add(n);
				
			}
				
			return ns;
			
		}
		
		private boolean isInLoc(Node n){
			
			int lowestX = endX < startX ? endX : startX;
			int lowestY = endY < startY ? endY : startY;
			
			int highestX = endX > startX ? endX: startX;
			int highestY = endY > startY ? endY : startY;

			boolean xb = n.getX()>= lowestX && n.getX()<=highestX;
			boolean yb = n.getY()>= lowestY && n.getY()<=highestY;
			
			return xb && yb;
			//if(xb && yb) return true;
			//else return	n.onLocation(startX, startY);
		
		}
	}

	class MoveBoxesMouse implements MouseListener, MouseMotionListener{
		
		Integer startDragX =null;
		Integer startDragY = null;
		
		Integer pressedX = null;
		Integer pressedY = null; 
		
		boolean dragging =false;
		
		HashSet<Node> nodesInSelectBoxArea = new HashSet<Node>();
		
		/*
		Integer startX = null;
		Integer startY = null;
		Node nodeOnLoc;
		*/
		
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println("mouse pressed");
			// meil ei ole ühtegi märgistatud
			if(nodesInSelectBoxArea.size() == 0){
				//System.out.println("mouse pressed: no selected boxes");
				pressedX = e.getX();
				pressedY = e.getY();
				selectBox = new SelectBox(pressedX, pressedY);			
			}
			// meil on mõned märgistatud
			else if(nodesInSelectBoxArea.size() > 0){
				//System.out.println("mouse pressed: some selected");
				Node n = DrawPanel.onLocation(e.getX(),e.getY(), null);
				
				// vajutas kastist mööda
				if(n == null || n.isSelected == false){
					//System.out.println("mouse pressed: vajutas kastist mööda");
					for(Node nn : nodesInSelectBoxArea){
						nn.isSelected = false;
						nn.pressedX = null;
						nn.pressedY = null;
					}
					//System.out.println("starDragX made null");
					startDragX = null;
					startDragY = null;
					nodesInSelectBoxArea.clear();
					selectBox = null;
				}
				// vajutas kasti peale
				else{
					// dragging started
				}
			}
			repaint();
			

			
			/*
			System.out.println("mouse pressed!");

			System.out.println(x + " " + y);
			nodeOnLoc = DrawPanel.onLocation(x, y, null);

			if(nodeOnLoc != null){
				startX = nodeOnLoc.getX();
				startY = nodeOnLoc.getY();
				nodeOnLoc.isSelected = true;
			}
			
			if(nodeOnLoc == null){
				
			}
			*/
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//System.out.println("mouse released");
			
			// meil on selectbox
			if(selectBox!= null){
				//System.out.println("mouse released: have selectBox");
				
				// selectBoxi ei ole tõmmatud, ehk klikitakse ühele kohale
				if(!dragging){
				//	System.out.println("mouse released: no startDragX");
					
					Node n = DrawPanel.onLocation(e.getX(), e.getY(), null);
					n.isSelected = true;
					n.pressedX = n.getX();
					n.pressedY = n.getY();
					nodesInSelectBoxArea.add(n);
					
					selectBox = null;
				}
				else{
					// märgistame
				//	System.out.println("mouse released märgistame");
					nodesInSelectBoxArea.addAll(selectBox.getNodesInLoc());
		
					for(Node n : nodesInSelectBoxArea){
						n.isSelected = true;
						n.pressedX = n.getX();
						n.pressedY = n.getY();
					}
					
					selectBox = null;
				}		
			}
			// meil ei ole select box
			else{
				
			}
			
			

			dragging= false;
			repaint();
			
			


			
			/*
			pressedX = null;
			pressedY = null;
			if(nodeOnLoc != null){
				nodeOnLoc.isSelected = false;
				nodeOnLoc = null;	
			}
			startX = null;
			startY = null;
			
			if(selectBox!= null){
				
				selectAllNodes();
				
				selectBox = null;
			}
			
			
			repaint();
			*/
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// (teame kas on märgistatud või mitte)
			//System.out.println("mouse dragged");
			dragging = true;
			if(selectBox != null){
				
		
				selectBox.setCoordinates(e.getX(), e.getY());


			}
			
			// on märgistatuid
			if(nodesInSelectBoxArea.size() > 0){
		//		System.out.println("mouse dragged: have selected boxes");
				//esmakordne drag
				if(startDragX == null){
			//		System.out.println("mouse dragged: startDragX init");
					startDragX = e.getX();
					startDragY = e.getY();
				}
				//juba drag alustatud
				else{

					for(Node n : nodesInSelectBoxArea){
						
						int abs = Math.abs(startDragY  - n.pressedY);
						int newY = e.getY() - abs;
				/*		
						System.out.println("dragging node: "+ n.toSimpleString() 
						+ " pressedX " + n.pressedX 
						+ " pressedY " + n.pressedY
						+ " startDragX, startDragY: " + startDragX + ", " + startDragY
						+ " \nthis is Subtracted From previous: startDragY - n.pressedY =" + abs
						+ " \ne.getY() " + e.getY()
						+ " \nthis is Subtracted From previous: startDragX - n.pressedX =" + Math.abs(startDragX - n.pressedX)
						+ " \ne.getX() " + e.getX()
						);*/
	
						int absx = Math.abs(startDragX - n.pressedX);
						//n.setX(e.getX() - absx);
						
						if(n.pressedX > startDragX){
							n.setX(e.getX() + absx);
						}

						if(n.pressedX < startDragX){
							n.setX(e.getX() - absx);
						}
						
						

						
						if(n.pressedY > startDragY){
							n.setY(e.getY() + abs );
						}

						if(n.pressedY < startDragY){
							n.setY(e.getY() - abs);
						}

						
									
					}
				}
				

			}
			// ei ole märgistatuid
			else{
				
			}
			
			repaint();
			
			
			/*
			if(nodeOnLoc != null){
				System.out.println("not null drag");
				int dX = e.getX();
				int dY = e.getY();
				
				System.out.println(dX + " " + dY);
				
				
				nodeOnLoc.setX(dX - Math.abs(startX - pressedX));

				nodeOnLoc.setY(dY - Math.abs(startY  - pressedY));
				
				

								
				System.out.println(nodeOnLoc.simpleToString());
				
				// reset
				repaint();
				
		
				
			}
			else if(selectBox != null){
				//System.out.println("null drag");
				
				selectBox.setCoordinates(e.getX(), e.getY());
				repaint();

			}
			*/
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	/**
	 * 
	 * For testing purpose
	 * 
	 */
	public static void printAllTreeNodes(){
		for(Node n : TREE_NODES){
		//	System.out.println(n);
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
			//System.out.println("OnLocation, looking node: " + n.simpleToString());
			if(notThis==null && n.onLocation(x, y)){
				return n;
			}
			else if(!n.equals(notThis) && n.onLocation(x,y)){
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
			case "Täispuu":
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
	    
	    if(selectBox != null){
	    	selectBox.paint(g);
	    }
	    
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
					System.out.println(stack_node.toSimpleString());
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
					System.out.println(stack_node.toSimpleString());
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
