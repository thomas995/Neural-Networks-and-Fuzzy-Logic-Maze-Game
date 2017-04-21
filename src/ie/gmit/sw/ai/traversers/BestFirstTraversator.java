package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.maze.*;
import java.util.*;
public class BestFirstTraversator implements Traversator{
	private Nade goal;
	
	public BestFirstTraversator(Nade goal){
		this.goal = goal;
	}
	
	public void traverse(Nade[][] maze, Nade node) {
		LinkedList<Nade> queue = new LinkedList<Nade>();
		queue.addFirst(node);
		
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	
		while(!queue.isEmpty()){
			node = queue.poll();
			node.setVisited(true);	
			visitCount++;
			
			if (node.isGoalNode()){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
				break;
			}
			
			try { //Simulate processing each expanded node
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Nade[] children = node.children(maze);
			for (int i = 0; i < children.length; i++) {
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]);
				}
			}
			
			//Sort the whole queue. Effectively a priority queue, first in, best out
			Collections.sort(queue,(Nade current, Nade next) -> current.getHeuristic(goal) - next.getHeuristic(goal));		
		}
	}
}
