package ie.gmit.sw.ai;

// All files from ie.gmit.sw.ai.maze/traversers is from the Artificial Intelligence Moodle page.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	private GameView view;
	private Maze model;
	private int currentRow;
	private int currentCol;
	private int maxHealth = 20;
	private int maxStrength = 5;
	private int maxSpiderHealth = 10;
	private int playerHealth = 20;
	private int playerStrength = 5;
	private int spiderHealth = 10;
	private int spiderStrength = 3;
	private boolean hasSword = false;
	private double healthPotion = 0.0;
	private EnemyMovement ms;

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

    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}

	private void placePlayer()
	{
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, new Node(currentRow, currentCol, 5)); //A Spartan warrior is at index 5
    	updateView();
	}

	private void updateView()
	{
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0)
        {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0)
        {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow + 1, currentCol)) currentRow++;
        }
        else if (e.getKeyCode() == KeyEvent.VK_Z)
        {
        	view.toggleZoom();
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
        	System.exit(0);
        }
        else if(e.getKeyCode() == KeyEvent.VK_S)
        {
        	//- Display Player Stats in a popup.
        	JOptionPane.showMessageDialog(null, "Player Stats: \n\tHealth: " + playerHealth + "/" + maxHealth + "\n\tStrength: " + playerStrength + "/" + maxStrength + "\n\tPotions: " + healthPotion);
        }
        else if(e.getKeyCode() == KeyEvent.VK_H)
        {
        	//- Use health Potion if there is a whole one available.
        	if (healthPotion > 0.99 && playerHealth < maxHealth)
        	{
        		JOptionPane.showMessageDialog(null, "You used a potion and recovered your health. \nCongrats on being slightly intelligent and not underestimating the spiders strength!");
        		playerHealth = maxHealth;
        		healthPotion -= 1.00;
        	}
        	else if (healthPotion < 1.00)
        	{
        		JOptionPane.showMessageDialog(null, "You have no potions. Go kill more spiders!");
        	}
        	else if (playerHealth == maxHealth)
        	{
        		JOptionPane.showMessageDialog(null, "You have full health! Why do you want to waste a potion?! Come back when you are hurt!");
        	}
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
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == -1)
		{
			model.set(currentRow, currentCol, new Node(currentRow, currentCol, -1));
			model.set(row, col, new Node(row, col, 5));
			return true;
		}else
		{
			{
				if (JOptionPane.showConfirmDialog(null, "Do you want to Interact with this?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
	        	    // yes option
					// if the block is a Sword
					if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 1)
					{
					    JOptionPane.showMessageDialog(null, "For some reason you're stronger now. Good for you!");
					    hasSword = true;
					    //- iHasSword();
					    //- maxHealth += 2;
						model.set(row, col, new Node(row, col, 0)); // 0 is a hedge
					}

					// if the block is a Question Mark
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 2)
					{
					    JOptionPane.showMessageDialog(null, "This is a tip of sorts");
						model.set(row, col, new Node(row, col, 0)); // 0 is a hedge
					}

					// Bomb Block
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 3)
					{
						JOptionPane.showMessageDialog(null, "You da bomb!!! \n...No wait, you were hit by it. \nSucks to be you. \nYou take 4 damage");
						playerHealth -= 4;
						amIAlive(playerHealth);
						model.set(row, col, new Node(row, col, 0)); // 0 is a hedge

					}

					 // H-Bomb Block
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 4)
					{
						// Deletes all blocks in a nearby radius
						JOptionPane.showMessageDialog(null, "Oh no! The spiders have mutated with the radiation! Watch out!");
						maxSpiderHealth += 5;
						spiderStrength += 2;
						model.set(row, col, new Node(row, col, 0)); // 0 is a hedge
					}
					// Black Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 6)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;

							//ms.isDead = true;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Black spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}

					}
					// Blue Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 7)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Blue spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Brown Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 8)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Brown spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Green Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 9)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Green spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Grey Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 10)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Grey spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Orange Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 11)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Orange spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Red Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 12)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Red spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Yellow Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 13)
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, new Node(row, col, -1)); // -1 is a blank
							//- Delete node, don't make it invisible, Or stop the spider moving.

							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Yellow spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}
					// Hedge
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col).getId() == 0)
					{
						//JOptionPane.showMessageDialog(null, "This is just a plain old hedge, why would you try to interact with a hedge?");
					}
					else
					{
						// removes block in front of the character
						//model.set(row, col, '\u0020'); // \u0020 is a blank
						//JOptionPane.showMessageDialog(null, "Nothing to see here. \nMove along. \nNo lollygaging.");
					}

					iHasSword();
	        	}
	        	else
	        	{
	        	    // no option
	        	}
			}

			return false; //Can't move
		}
	}

	private void iHasSword()
	{
		//- When the player moves, after interacting with a block, this method is called to check if the player found a Sword.
		//- If they did the boolean variable hasSword was set to true, then this if statement is run.
		if (hasSword)
		{
			//- 2 Strength is added to the player and hasSword is set to false, this lets the player find multiple swords.
			maxStrength += 2;
			maxHealth += 5;
			playerHealth += 5;
			playerStrength = maxStrength;
			hasSword = false;
		}//- End of if
	}//- End of iHasSword

	private boolean amIAlive(int health)
	{
		if (health > 0)
		{
			return true;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "You died. Please try not to be spider food next time.");
			System.exit(0);
			return false;
		}//- End of if/else
	}//- End of amIAlive()


	private Sprite[] getSprites() throws Exception{
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

	public int getSpiderHealth()
	{
		return this.spiderHealth;
	}

	public static void main(String[] args) throws Exception{
		new GameRunner();
		int spiderHealth = 10;

		/*FIS fis = FIS.load("fcl/Maze.fcl", true); //Load and parse the FCL
		FunctionBlock fb = fis.getFunctionBlock("fuzzyMaze");
		JFuzzyChart.get().chart(fb);
		//fis.chart(); //Display the linguistic variables and terms
		fis.setVariable("playerStrength", 7); //Apply a value to all variables
		fis.setVariable("spiderStrength", 5);
		fis.setVariable("playerHealth", 24);
		fis.setVariable("spiderHealth", 15);
		fis.evaluate(); //Execute the fuzzy inference engine

		Variable maze = fb.getVariable("killability");
		//JFuzzyChart.get.chart(tip, tip.getDefuzzifier(), true);
		JFuzzyChart.get().chart(maze.getDefuzzifier(), "Crisp Output", true);

		System.out.println(maze.defuzzify());
		//System.out.println(fis.getVariable("killability").getValue()); //Output end result
*/
	}//- End of main()
}//- End of GameRunner
