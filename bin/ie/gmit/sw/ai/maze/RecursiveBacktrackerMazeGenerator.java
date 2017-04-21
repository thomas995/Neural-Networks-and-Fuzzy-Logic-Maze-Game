package ie.gmit.sw.ai.maze;

import ie.gmit.sw.ai.maze.Nade.*;
import java.util.*;
public class RecursiveBacktrackerMazeGenerator extends AbstractMazeGenerator {
	public RecursiveBacktrackerMazeGenerator(int rows, int cols) {
		super(rows, cols);
	}

	//Generates Mazes with about as high a "river" factor as possible, with fewer but longer dead ends, and usually a very long and meandering solution. 
	public void generateMaze(){
		Nade[][] maze = super.getMaze();
		
		Random generator = new Random();
		int randRow = generator.nextInt(maze.length);
		int randCol = generator.nextInt(maze[0].length);
		Nade node = maze[randRow][randCol];
		carve(node);
	}
	
	private void carve(Nade node){
		node.setVisited(true);
		Nade[] adjacents = node.adjacentNodes(super.getMaze());
		super.shuffle(adjacents);
		
		for (int i = 0; i < adjacents.length; i++) {
			if (!adjacents[i].isVisited()){
				Direction dir = getDirection(node, adjacents[i]);
				node.addPath(dir);
				adjacents[i].addPath(opposite(dir));
				carve(adjacents[i]);
			}
		}
	}
}
