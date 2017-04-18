package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameRunner implements KeyListener
{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;


	public GameRunner() throws Exception
	{

		model = new Maze(MAZE_DIMENSION);
    	view = new GameView(model);

    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);

    	placePlayer();

    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);

    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development) By Thomas McNamara and Alanna Curran");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);


	}


	// where the player is placed when the game is initially executed
	private void placePlayer()
	{
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, '5'); //A Spartan warrior is at index 5

    	updateView();
	}

	// draw the characters new position on the window
	private void updateView()
	{
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow, currentCol + 1))
        		currentCol++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0)
        {
        	if (isValidMove(currentRow, currentCol - 1))
        		currentCol--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0)
        {
        	if (isValidMove(currentRow - 1, currentCol))
        		currentRow--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow + 1, currentCol))
        		currentRow++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_Z)
        {
        	view.toggleZoom();
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
        	System.exit(0);
        }
        else
        {
        	return;
        }

        updateView();
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore



	private boolean isValidMove(int row, int col)
	{
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' ')
		{
			model.set(currentRow, currentCol, '\u0020'); // places a space in your last position
			model.set(row, col, '5');
			return true;
		}
		else
		{
			{

				if (JOptionPane.showConfirmDialog(null, "Do you want to Interact with this?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
	        	    // yes option
					// if the block is a Sword
					if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0031')
					{
					    JOptionPane.showMessageDialog(null, "For some reason you're stronger now. Good for you!");
						model.set(row, col, '\u0020'); // // removes block
					}

					// if the block is a Question Mark
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0032')
					{
					    JOptionPane.showMessageDialog(null, "This is a tip of sorts");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}

					// Bomb Block
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0033')
					{
						JOptionPane.showMessageDialog(null, "You are da bomb... No wait you were hit by it. You take { X } damage");
						model.set(row, col, '\u0020'); // \u0020 is a blank

					}

					 // H-Bomb Block
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0034')
					{
						// Deletes all blocks in a nearby radius
						JOptionPane.showMessageDialog(null, "You've set off tzar bomba, thank goodness you were behind your computer screen.");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Black Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0036')
					{
						JOptionPane.showMessageDialog(null, "Black");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Blue Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0037')
					{
						JOptionPane.showMessageDialog(null, "Blue");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Brown Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0038')
					{
						JOptionPane.showMessageDialog(null, "Brown");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Green Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0039')
					{
						JOptionPane.showMessageDialog(null, "Green");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Grey Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003A')
					{
						JOptionPane.showMessageDialog(null, "Grey");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Orange Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003B')
					{
						JOptionPane.showMessageDialog(null, "Orange");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Red Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003C')
					{
						JOptionPane.showMessageDialog(null, "Red");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}
					// Yellow Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003D')
					{
						JOptionPane.showMessageDialog(null, "Yellow");
						model.set(row, col, '\u0020'); // \u0020 is a blank
					}

					else
					{
						// removes block in front of the character
						model.set(row, col, '\u0020'); // \u0020 is a blank
					    JOptionPane.showMessageDialog(null, "Item Destroyed");
					}
	        	}
	        	else
	        	{
	        	    // no option
	        	}
			}
			return false; //Can't move
		}
	}

	private Sprite[] getSprites() throws Exception
	{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images...
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", "resources/hedge.png");
		sprites[1] = new Sprite("Sword", "resources/sword.png");
		sprites[2] = new Sprite("Help", "resources/help.png");
		sprites[3] = new Sprite("Bomb", "resources/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", "resources/h_bomb.png");
		sprites[5] = new Sprite("Spartan Warrior", "resources/spartan_1.png", "resources/spartan_2.png");
		sprites[6] = new Sprite("Black Spider", "resources/black_spider_1.png", "resources/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", "resources/blue_spider_1.png", "resources/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", "resources/brown_spider_1.png", "resources/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", "resources/green_spider_1.png", "resources/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", "resources/grey_spider_1.png", "resources/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", "resources/orange_spider_1.png", "resources/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", "resources/red_spider_1.png", "resources/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", "resources/yellow_spider_1.png", "resources/yellow_spider_2.png");
		return sprites;
	}

	public static void main(String[] args) throws Exception
	{
		new GameRunner();


	}
}