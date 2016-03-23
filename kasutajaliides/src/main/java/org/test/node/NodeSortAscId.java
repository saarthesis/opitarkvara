package org.test.node;

import java.util.Comparator;;

public class NodeSortAscId implements Comparator<Node>{

	@Override
	public int compare(Node o1, Node o2) {
		if(o1.getId() > o2.getId()) return 1;
		else if(o1.getId() < o2.getId()) return -1;
		else return 0;
	}

}
