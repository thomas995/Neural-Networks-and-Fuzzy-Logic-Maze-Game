package ie.gmit.sw.ai;

import java.awt.Color;

/*
 * Adapted from class file on Moodle
 */

public class Node {

    private int id = -1;
	public enum Direction {North, South, East, West};
	private Node parent;
	private Color color = Color.BLACK;
	private Direction[] paths = null;
	public boolean visited =  false;
	public boolean goal;
	private int row = -1;
	private int col = -1;
	private int distance;
	
	public Node(int row, int col, int id) {
		this.row = row;
		this.col = col;
		this.id = id;
	}//- End of Node

    public int getId() {
        return id;
    }//- End of getId

    public void setId(int id) {
        this.id = id;
    }//- End of setId

	public int getRow() {
		return row;
	}//- End of getRow

    public void setRow(int row) {
		this.row = row;
	}//- End of setRow
    
	public int getCol() {
		return col;
	}//- End of getCol

	public void setCol(int col) {
		this.col = col;
	}//- End of setCol

	public Node getParent() {
		return parent;
	}//- End of getParent

	public void setParent(Node parent) {
		this.parent = parent;
	}//- End of setParent

	public Color getColor() {
		return color;
	}//- End of getColor

	public void setColor(Color color) {
		this.color = color;
	}//- End of setColor
	
	public boolean hasDirection(Direction direction){	
		for (int i = 0; i < paths.length; i++) {
			if (paths[i] == direction) {
				return true;
			}//- End of if
		}//- End of for
		return false;
	}//- End of hasDirection
	
	public Node[] children(Node[][] maze){		
		java.util.List<Node> children = new java.util.ArrayList<Node>();
				
		if (row > 0 && maze[row - 1][col].hasDirection(Direction.South)) {
			children.add(maze[row - 1][col]); //Add North
		}//- End of if
		if (row < maze.length - 1 && maze[row + 1][col].hasDirection(Direction.North)) {
			children.add(maze[row + 1][col]); //Add South
		}//- End of if
		if (col > 0 && maze[row][col - 1].hasDirection(Direction.East)) {
			children.add(maze[row][col - 1]); //Add West
		}//- End of if
		if (col < maze[row].length - 1 && maze[row][col + 1].hasDirection(Direction.West)) {
			children.add(maze[row][col + 1]); //Add East
		}//- End of if
		
		return (Node[]) children.toArray(new Node[children.size()]);
	}//- End of children()

	public Node[] adjacentNodes(Node[][] maze){
		java.util.List<Node> adjacents = new java.util.ArrayList<Node>();

		if (row > 0) {
			if(maze[row - 1][col].getId() != 0) {//- Only add if not a hedge
				adjacents.add(maze[row - 1][col]); //-Add North
			}//- End of inner if
		}//- End of outer if
		if (row < maze.length - 1) {
			if(maze[row + 1][col].getId() != 0) {//- Only add if not a hedge
				adjacents.add(maze[row + 1][col]); //-Add South
			}//- End of inner if
		}//- End of outer if
		if (col > 0) {
			if(maze[row][col - 1].getId() != 0) {//- Only add if not a hedge
				adjacents.add(maze[row][col - 1]); //-Add West
			}//- End of inner if
		}//- End of outer if
		if (col < maze[row].length - 1) {
			if(maze[row][col + 1].getId() != 0) {//- Only add if not a hedge
				adjacents.add(maze[row][col + 1]); //-Add East
			}//- End of inner if
		}//- End of outer if
		
		return (Node[]) adjacents.toArray(new Node[adjacents.size()]);
	}//- End of adjacentNodes
	
	public Direction[] getPaths() {
		return paths;
	}//- End of getPaths

	public void addPath(Direction direction) {
		int index = 0;
		if (paths == null){
			paths = new Direction[index + 1];		
		}else{
			index = paths.length;
			Direction[] temp = new Direction[index + 1];
			for (int i = 0; i < paths.length; i++) {
				temp[i] = paths[i];
			}//- End of for
			paths = temp;
		}//- End of if/else
		
		paths[index] = direction;
	}//- End of addPath

	public boolean isVisited() {
		return visited;
	}//- End of isVisited

	public void setVisited(boolean visited) {
		this.color = Color.BLUE;
		this.visited = visited;
	}//- End of setVisited

	public boolean isGoalNode() {
		return goal;
	}//- End of isGoalNode

	public void setGoalNode(boolean goal) {
		this.goal = goal;
	}//- End of setGoalNode
	
	public int getHeuristic(Node goal){
		double x1 = this.col;
		double y1 = this.row;
		double x2 = goal.getCol();
		double y2 = goal.getRow();
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}//- End of getHeuristic
	
	public int getPathCost() {
		return distance;
	}//- End of getPathCost

	public void setPathCost(int distance) {
		this.distance = distance;
	}//- End of setPathCost

	public String toString() {
		return "[" + row + "/" + col + "]";
	}//- End of toString
}//- End of Node