package org.test.node;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.test.GUI.tagid.puu.DrawPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class Node {

	public static int ID= 0;
	private int id;

	private String state;
	private LinkedList<Node> children;
	
	public boolean isSelected = false;
	public Integer pressedX;
	public Integer pressedY;

	/*based on state size. Initilaly it is not init.. but after first*/
	int width_of_box = 0;
	static final int DISTANCE_BETWEEN_X = 50;
	static final int FONT_SIZE = 15;
	public static final int HEIGHT_OF_BOX = FONT_SIZE*2;

	// for every depth, it goes lower by that amount
	static final int BETWEEN_LEVELS = 50;

	static final int OFFSET = 3;

	// these are init from calculateNodePosition*()
	private Integer x;
	private Integer y;
	
	// necessary for drawing arrows
	private Node parent;
	
	private boolean isAccepting;

	public Node(String state, LinkedList<Node> children, boolean isAccepting){
		ID++;
		setId(ID);
		this.state = state;
		this.isAccepting = isAccepting;
		this.children = children;
		// init var
		if(width_of_box == 0){
			width_of_box = state.length() * FONT_SIZE;
		}
	}

	public String toString(){
		return "ID: "+getId()+" x: "+x+" y: "+y+" State: "+state +" "
				+ "children: " + children; 
	}

	public String toSimpleString(){
		return "ID: "+getId()+" x: "+x+" y: "+y+" State: "+state;	
	}

	//static int COUNT_NODE_PAINTS =0;

	/**
	 * 
	 * Checks if that node is within those coordinates.
	 * 
	 */
	public boolean onLocation(int x, int y){
		try {

			boolean xb = x >= this.x && x <= getEndX();
			boolean yb = this.y <= y && y <= getEndY();
			//
			boolean b =  xb && yb;
			//System.out.println("x >= this.x && x <= endX" + xb);
			return b;
		}
		catch(NullPointerException e){
			return false;
		}
	}

	private int getEndX() {
		return this.x + width_of_box;
	}

	private int getEndY() {
		return this.y + HEIGHT_OF_BOX;
	}

	/**
	*
	* Moves current node to the left
	* If it is moved onto someone else, then this new node is also moved to the left, etc..
	*
	*/
	public void moveLeft(){
		x = x - (width_of_box + DISTANCE_BETWEEN_X);

		// check if it moved onto another node
		// if, then move that also
		Node onLocationNode =  DrawPanel.onLocation(x,y,this);
		if(onLocationNode != null){
			onLocationNode.moveLeft();
		}

	}

	public void paintNode(Graphics g){	
		
		// draw arrow
		// not drawn, if main node
		if(parent != null) paintArrow(g);
		
		paintBox(g);
		paintString(g);


	}

	private void paintBox(Graphics g) {
		Color border_color = new Color(174,174,174);
		Color border_color_selected = new Color(17,103,252);
		Color box_color = new Color(236,236,236);
		
		if(isAccepting){
			box_color = new Color(0,255,26);	
		}
	
		if(isSelected){
			g.setColor(border_color_selected);
			g.fillRect(x-2, y-2, (4 +width_of_box), (4+ HEIGHT_OF_BOX));
		}else{
			g.setColor(border_color);
			g.fillRect(x-1, y-1, (2 +width_of_box), (2+ HEIGHT_OF_BOX));
		}
		
		
		
		g.setColor(box_color);
		g.fillRect(x, y, width_of_box, HEIGHT_OF_BOX);
	}

	private void paintString(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE)); 
		g.drawString(state,x + OFFSET, y + (HEIGHT_OF_BOX/2) + ((FONT_SIZE/2)-(FONT_SIZE/2)/2));	
		g.setColor(Color.RED);
		int endX = getEndX();

		g.setFont(new Font("TimesRoman", Font.BOLD, 8)); 
		g.drawString(""+id,endX - 20, y + (HEIGHT_OF_BOX/2) + ((FONT_SIZE/2)-(FONT_SIZE/2)/2));	
	}

	/**
	 * 
	 * CHILD MIDDLE TOP
	 * to
	 * PARENT MIDDLE BOT
	 * 
	 * @param g
	 */
	private void paintArrow(Graphics g) {
		g.setColor(Color.BLACK);
		
		LinkedList<Double> thisMiddleTop = middleTop();
		LinkedList<Double> parentMiddleBot = parent.middleBot();
		
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
        g2.setStroke(new BasicStroke(2));
        g2.draw(new Line2D.Float((int) Math.round(parentMiddleBot.getFirst()), 
				(int) Math.round(parentMiddleBot.getLast()),
				(int) Math.round(thisMiddleTop.getFirst()), 
				(int) Math.round(thisMiddleTop.getLast())));

	}
	
	/**
	 * 
	 * There are no tuples in Java.
	 * I will use linked list
	 * 
	 * @return
	 */
	private LinkedList<Double> middleTop(){
		
		LinkedList<Double> xy = new LinkedList<Double>();
		
		double top_y = y;
		
		double toMiddle = (width_of_box/2);
		
		double top_x = x + toMiddle;
		
		xy.add(top_x);
		xy.add(top_y);
		
		return xy;
		
	}
	
	private LinkedList<Double> middleBot(){
		
		LinkedList<Double> xy = new LinkedList<Double>();
		
		double bot_y = y + HEIGHT_OF_BOX;
		
		double toMiddle = (width_of_box/2);
		
		double bot_x = x + toMiddle;
		
		xy.add(bot_x);
		xy.add(bot_y);
		
		return xy;
		
	}

	/**
	 * 
	 * First it start with START_X and START_Y
	 * <<< these are from drawPanel
	 * 
	 * This is called only once for each node.
	 * 
	 * 
	 * @param x
	 * @param y
	 */
	public void calculateNodePositionOnTree(int x, int y){
		//HashSet<Node> test_treenode = DrawPanel.TREE_NODES;
		String state2 = state;
		// Now this node is in drawPanel tree
		DrawPanel.TREE_NODES.add(this);
		
		// we init. 
		this.x = x;
		this.y = y;		
		
		// leiame node, mis on samas kohas ja liigutame teda edasi
		Node onLocationNode =  DrawPanel.onLocation(x,y,this);
		if(onLocationNode != null){
			onLocationNode.moveLeft();
		}

		// children are moved lower in tree
		y += BETWEEN_LEVELS;
		for(Node child: children){
			child.setParent(this);
			child.calculateNodePositionOnTree(x, y);
		}
	}

	private void setParent(Node node) {
		this.parent = node;
	}



	/**
	 * 
	 * We want parent to be in the middle of children
	 * 
	 * Currently moves onto... Need to add check
	 * 
	 */
	public void adjustLocation() {
		if(children.size() > 1){
			
			Node f = children.getFirst();
			Node l = children.getLast();
			
			int distance = l.x - f.x;
			int middle = f.x + (distance/2);
			
			// check if it is moved onto someone
			x = middle;

			while(DrawPanel.onLocation(x, y, this) != null){
				x -= 1;
			}
			
		}	
	}

	
	public Integer getX(){
		return x;
	}

	public void setX(int x){
		System.out.println("node setX arg: " + x );

		this.x = x;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isAccepting(){
		return isAccepting;
	}

	public List<Node> getChildren(){
		return children;
	}

	public Integer getY() {
		return y;
	}

	public void setY(int y) {
		System.out.println("node setY arg: " + y );
		this.y = y;
		
	}
	
}