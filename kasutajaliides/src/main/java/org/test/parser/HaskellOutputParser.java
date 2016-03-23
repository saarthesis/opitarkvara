package org.test.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.test.node.Node;


public class HaskellOutputParser{

	static int countParse = 0;

	/**
	*
	*
	* Node {state = State [1,1,0,0], parents = [], children = []}
	*
	*
	* returns back Node, parent node, which has all the children necessary
	 * @param end_states 
	* 
	*
	*/
	public static Node parse(String input, List<String> end_states){
		System.out.println("----input for parse method: " + input);

		countParse++;
		//System.out.println("we have parsed "+countParse);
		String state = getState(input);
		LinkedList<Node> children = childrenNodes(input, end_states);
		
		return new Node(state, children,end_states.contains(state));

	}

	public static String getState(String input){
		String state = input.replaceAll("^Node \\{state = State \\[|\\].*$","");
		return state;	
	}

	/**
	*
	*
	* input: "Node {stuff},Node {stuff},Node {stuff}"
	*
	* return: 3 elments,sepearated with commas
	*/
	public static LinkedList<String> splitNodesByComma(String input){
		LinkedList<String> result = new LinkedList<String>();

		// Here we add all the time... 
		// unless depth is 0 AND comma comes
		int curly_depth = 0;
		String node = ""; 
		
		LinkedList<Character> bufferLast1 = new LinkedList<Character>();

		for(char c : input.toCharArray()){

			/*
			System.out.println("-----new loop-----");
			System.out.println("char to check: " + c);
			System.out.println("curly depth: " + curly_depth);
			System.out.println("node: " + node);
			System.out.println("buffer: " + getBuffer(bufferLast1));
			System.out.println("buffer boolean: " + getBuffer(bufferLast1).equals(" "));
			*/
			
			if( c == ',' && curly_depth == 0){
				// do nothing
				addBuffer(c,bufferLast1, 1);
				continue;
			}
			// SHOULD NOT GET HERE
			//else if(c == ' ' && curly_depth == 0 && getBuffer(bufferLast1).equals(",")){
				// do nothing
			//	continue;
			//}
			else if(c == 'N' && curly_depth == 0 && getBuffer(bufferLast1).equals(",")){
				result.add(node);
				node = "";
			}
			else if(c =='{'){
				curly_depth++;
			} 
			else if(c == '}'){
				curly_depth--;
			}	
			else{
				// why it even should be here
			}

			node +="" +c;
			addBuffer(c,bufferLast1, 1);
		}

		if(node.trim().length() >0) result.add(node);
		
		return result;
	}


	/**
	*
	* 
	* Gets original input,and parses children nodes from it
	*
	* Node {somestuff, children = [children are here]}
	*
	*/
	public static LinkedList<Node> childrenNodes(String input, List<String> end_states){
		String childrenPart = getChildren(input);
		LinkedList<String> childrenStrings = splitNodesByComma(childrenPart);

		LinkedList<Node> childrenNodes = new LinkedList<Node>();
		for(String child : childrenStrings){
			childrenNodes.add(parse(child,end_states));
		}

		return childrenNodes;
	}

	/**
	*
	* 
	* Node {state = State [1,1,0,0], parents = [], children = [this stuff inside]}
	*
	* returns "this stuff inside"
	*
	*/
	public static String getChildren(String input){
		// buffer last is 12

		// this is for understanding the depth of nodes
		// for example, where is children, that we are looking for
		// is in depth 1
		// outside of parent is 0, that is when it starts
		int curly_depth = 0;
		String childrenStr = ""; 

		boolean nowOnlyAdd = false;

		LinkedList<Character> bufferLast12 = new LinkedList<Character>();
		
		for(char c : input.toCharArray()){

			addBuffer(c,bufferLast12, 12);

			if(nowOnlyAdd){
				childrenStr +=""+c;
			}
			else{
				if(c =='{'){
					curly_depth++;	
				} 
				else if(c == '}'){
					curly_depth--;
				}	
				// we found our goal!
				else if(curly_depth == 1 && getBuffer(bufferLast12).equals("children = [")){
					nowOnlyAdd = true;
				}
				else{
				// must think we should be even here
				// currently do nothing
				}
			}

		}
		String s = childrenStr.replaceAll("\\]\\}$","");
		return s;
	}

	private static String getBuffer(LinkedList<Character> buffer){
		String str = "";
		for(char c : buffer) str +=""+c;
		return str;
	}

	private static void addBuffer(char c, LinkedList<Character> buffer, int size){

		if(buffer.size() == size){
			buffer.removeFirst();
			buffer.add(c);
		}
		else{
			buffer.add(c);
		}

	}

	/**
	 * 
	 * "[State [2,0],State [2,1],State [2,2],State [2,3]]"
	 * 
	 * returns list of "2,0" "2,1" ...
	 * 
	 * @param endStates
	 * @return
	 */
	public static List<String> parseEndStates(String endStates) {
		//System.out.println("------inside of parse end states!!!! input: "+endStates);
		
		String[] ps = endStates.split("\\[State \\[|\\],State \\[|\\]\\]");
	
		List<String> states = new ArrayList<String>();
		
		for(String p : ps){
			if(p.trim().equals("")){
				
			}
			else{
				states.add(p);
				//System.out.println(p);
			}
		}
		
		//System.out.println("------end endstaes pares-----");

		return states;
	}



}