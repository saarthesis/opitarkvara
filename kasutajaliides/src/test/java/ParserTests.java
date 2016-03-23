package org.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.LinkedList;


public class ParserTests{

  @Test
  public void testParse(){
    String n1 = "Node {state = State [0,0,0,0], parents = [], children = [Node {state = State [1,1,0,0], parents = [], children = []}]}";

    assertEquals("", HaskellOutputParser.parse(n1).toString());
  }

  @Test
  public void testTopLevelSeparateNodes(){
    String n1 = "Node {state = State [1,1,0,0], parents = [], children = []}";
    String n2 = "Node {state = State [1,1,0,5], parents = [], children = []}";

    assertEquals("", HaskellOutputParser.splitNodesByComma(n1 +", " +n2).toString());
  }

  @Test
  public void testState01() {
   	String n1 = "Node {state = State [1,1,0,0], parents = [], children = []}";
    assertEquals("1,1,0,0",HaskellOutputParser.getState(n1) );
 	  assertEquals("1,1,0,1",HaskellOutputParser.getState("Node {state = State [1,1,0,1], parents = ["+n1+"], children = []}") );
  }

  @Test
  public void getChildren_test01() {
    String n1 = "Node {state = State [1,1,0,0], parents = [], children = []}";
    String n2 = "Node {state = State [1,1,0,5], parents = [], children = []}";

      //assertEquals("",HaskellOutputParser.getChildren(n1) );
   	assertEquals(n2,HaskellOutputParser.getChildren("Node {state = State [1,1,0,1], parents = ["+n1+"], children = ["+n2+"]}") );
    
    String n = "Node {state = State [1,1,1,1], children = [Node {state = State [0,1,0,1], children = [Node {state = State [1,1,0,1], children = [Node {state = State [0,0,0,1], children = [Node {state = State [1,0,1,1], children = [Node {state = State [0,0,1,0], children = [Node {state = State [1,0,1,0], children = [Node {state = State [0,0,0,0], children = []}]},Node {state = State [1,1,1,0], children = [Node {state = State [0,1,0,0], children = []}]}]}]}]},Node {state = State [0,1,0,0], children = [Node {state = State [1,1,1,0], children = [Node {state = State [0,0,1,0], children = [Node {state = State [1,0,1,0], children = [Node {state = State [0,0,0,0], children = []}]},Node {state = State [1,0,1,1], children = [Node {state = State [0,0,0,1], children = []}]}]}]}]}]}]}]}";
    String n_expected = "Node {state = State [0,1,0,1], children = [Node {state = State [1,1,0,1], children = [Node {state = State [0,0,0,1], children = [Node {state = State [1,0,1,1], children = [Node {state = State [0,0,1,0], children = [Node {state = State [1,0,1,0], children = [Node {state = State [0,0,0,0], children = []}]},Node {state = State [1,1,1,0], children = [Node {state = State [0,1,0,0], children = []}]}]}]}]},Node {state = State [0,1,0,0], children = [Node {state = State [1,1,1,0], children = [Node {state = State [0,0,1,0], children = [Node {state = State [1,0,1,0], children = [Node {state = State [0,0,0,0], children = []}]},Node {state = State [1,0,1,1], children = [Node {state = State [0,0,0,1], children = []}]}]}]}]}]}]}";
    assertEquals(n_expected,HaskellOutputParser.getChildren(n));
  }

  @Test
  public void splitNodesByComma_test01(){
    String input;
    String expected;
    input = "Node {stuffhere},Node {stuffhere}";
    expected = "[Node {stuffhere}, Node {stuffhere}]";
      
    //LinkedList<Integer> test = new LinkedList<Integer>();
    //test.add(2); test.add(3);
    //assertEquals("",test.toString());

    //assertEquals(0,HaskellOutputParser.splitNodesByComma(input).size());
    assertEquals(expected,HaskellOutputParser.splitNodesByComma(input).toString());
  }

  @Test
  public void childrenNodes_test01(){
    
  }
}