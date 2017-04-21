package ie.gmit.sw.ai;

import ie.gmit.sw.ai.Node;

public interface Traversator
{

	public void setMaze(Node[][] maze);

	public void setCurrentNode(Node currentNode); public Node getCurrentNode();

	public void setFinished(boolean finished);
	


}
