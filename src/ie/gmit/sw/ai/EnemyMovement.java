package ie.gmit.sw.ai;

import java.util.*;
import java.util.concurrent.*;

public class EnemyMovement extends Node

{
	// Variables
    private Node[][] maze = null;
    private Random random = new Random();
    private Object lock = null;
    private ExecutorService executor = Executors.newFixedThreadPool(50);
    private Node lastNode = null;
    private volatile int moveNumber = 0;
    private long spiderWait = 1500; //- waits 1.5 seconds
    public boolean isDead = false;

    public EnemyMovement(int row, int col, int id, Node[][] maze, Object lock)
    {
        super(row, col, id);

        //this.lock = lock;
        this.maze = maze;

        //Move spider
        executor.submit(() -> {
            while (true) {
                try {
                	// pauses the spiders before their next movement
                    Thread.sleep(spiderWait);

                    move();
                } catch (Exception ex)
                {

                }//- End of try/catch
            } //- End of while
        });
    }//- End of MoveSpider()

    //Spider movement in a thread
    private void move(){

      //  synchronized (lock) {
            Node[] adjacentNodes = null;
            List<Node> movementTo = new ArrayList<>();

            adjacentNodes = adjacentNodes(maze);

            for (int i = 0; i < adjacentNodes.length; i++)
            {
				Node n = adjacentNodes[i];

				if (n.getId() == -1)
				{
				 	movementTo.add(n);
				}//- End of if
			}//- End of for

            if (movementTo.size() > 0)
            {
              /*  int newX, newY, oldX, oldY;

                oldX = getRow();
                oldY = getCol();

                int index = rand.nextInt(movementTo.size());

                newX = movementTo.get(index).getRow();
                newY = movementTo.get(index).getCol();

                setRow(newX);//Sets new row
                setCol(newY);//Sets new column

                movementTo.get(index).setRow(oldX);
                movementTo.get(index).setCol(oldY);


                lastNode = movementTo.get(index);
                maze[newX][newY] = (MoveSpider)this;
                maze[oldX][oldY] = movementTo.get(index); */
                if(isDead != true)
                {
                	 int newX, newY, oldX, oldY;

                     oldX = getRow();
                     oldY = getCol();

                     int index = random.nextInt(movementTo.size());

                     newX = movementTo.get(index).getRow();
                     newY = movementTo.get(index).getCol();

                     setRow(newX);//Sets new row
                     setCol(newY);//Sets new column

                     movementTo.get(index).setRow(oldX);
                     movementTo.get(index).setCol(oldY);


                     lastNode = movementTo.get(index);
                     maze[newX][newY] = (EnemyMovement)this;
                     maze[oldX][oldY] = movementTo.get(index);
                }
                else if(isDead = true)
                {
                	int newX, newY, oldX, oldY;

                    oldX = getRow();
                    oldY = getCol();

                    int index = movementTo.size();

                    newX = movementTo.get(index).getRow();
                    newY = movementTo.get(index).getCol();

                    setRow(newX);//Sets new row
                    setCol(newY);//Sets new column

                    movementTo.get(index).setRow(oldX);
                    movementTo.get(index).setCol(oldY);


                    lastNode = movementTo.get(index);
                    maze[newX][newY] = (EnemyMovement)this;
                    maze[oldX][oldY] = movementTo.get(index);

            }//- End of if
        }//- End of synchronized
    }//- End of move
}//- End of MoveSpider
