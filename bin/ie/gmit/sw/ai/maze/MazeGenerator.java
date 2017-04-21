package ie.gmit.sw.ai.maze;

public interface MazeGenerator {
	public enum GeneratorAlgorithm {BinaryTree, HuntAndKill, RandomDepthFirst, RecursiveBacktracker, RecursiveDivision, RandomizedPrim, RandomizedKruskal};
	
	public abstract void setGoalNode();
	public abstract Nade getGoalNode();
	public abstract Nade[][] getMaze();
	public abstract void generateMaze();
}