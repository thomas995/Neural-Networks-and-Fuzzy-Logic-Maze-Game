package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ie.gmit.sw.ai.nn.activator.Activator;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class GameRunner implements KeyListener
{
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

	
	
/*	private double[][] data = { //playerHealth, playerStrength, spiderHealth, spiderStrength
			{ 20, 5, 10, 3 }, { 2, 0, 0, 1 }, { 2, 0, 1, 1 }, { 2, 0, 1, 2 }, { 2, 1, 0, 2 },
			{ 2, 1, 0, 1 }, { 1, 0, 0, 0 }, { 1, 0, 0, 1 }, { 1, 0, 1, 1 }, { 1, 0, 1, 2 }, 
			{ 1, 1, 0, 2 }, { 1, 1, 0, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 1 }, 
			{ 0, 0, 1, 2 }, { 0, 1, 0, 2 }, { 0, 1, 0, 1 } };

	private double[][] expected = { //Panic, Attack, Hide, Run
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0, 0.0 }, 
			{ 0.0, 0.0, 0.0, 1.0 }, { 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, 
			{ 1.0, 0.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, 
			{ 0.0, 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0, 0.0 }, 
			{ 0.0, 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0, 1.0 } };*/
	
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


	}//- End of GameRunner()


	// where the player is placed when the game is initially executed
	private void placePlayer()
	{
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	model.set(currentRow, currentCol, '5'); //A Spartan warrior is at index 5

    	updateView();
	}//- End of placePlayer()

	// draw the characters new position on the window
	private void updateView()
	{
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}//- End of updateView()

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow, currentCol + 1))
        	{
        		currentCol++; //- Move Spartian Right
        	}
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0)
        {
        	if (isValidMove(currentRow, currentCol - 1))
        	{
        		currentCol--; //- Move Spartian Left
        	}
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0)
        {
        	if (isValidMove(currentRow - 1, currentCol))
        	{
        		currentRow--; //- Move Spartian Forward/Up
        	}
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1)
        {
        	if (isValidMove(currentRow + 1, currentCol))
        	{
        		currentRow++; //- Move Spartian Backward/Down
        	}
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
        }//- End of in/else

        updateView();
    }//- End of keyPressed()
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
				// Bomb Block
				if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0033')
				{
					JOptionPane.showMessageDialog(null, "You are da bomb... No wait you were hit by it. You take 4 damage");
					playerHealth -= 4;
					amIAlive(playerHealth);
					model.set(row, col, '0'); // \u0020 is a blank

				}

				 // H-Bomb Block
				else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0034')
				{
					// make spiders stronger
					JOptionPane.showMessageDialog(null, "Oh no! The spiders have mutated with the radiation! Watch out!");
					maxSpiderHealth += 5;
					spiderStrength += 2;
					model.set(row, col, '0'); // \u0020 is a blank
				}

				else if (JOptionPane.showConfirmDialog(null, "Do you want to Interact with this?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
	        	    // yes option
					// if the block is a Sword
					if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0031')
					{
					    JOptionPane.showMessageDialog(null, "For some reason you're stronger now. Good for you!");
					    hasSword = true;
					    //- iHasSword();
					    //- maxHealth += 2;
						model.set(row, col, '0'); // // removes block
					}

					// if the block is a Question Mark
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0032')
					{
					    JOptionPane.showMessageDialog(null, "This is a tip of sorts");
						model.set(row, col, '0'); // \u0020 is a blank
					}



					// Black Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0036')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Black spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}

					}
					// Blue Spider
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0037')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
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
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0038')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
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
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0039')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
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
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003A')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
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
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003B')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
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
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003C')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
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
					else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003D')
					{
						playerHealth -= spiderStrength;
						spiderHealth -= playerStrength;
						amIAlive(playerHealth);
						if (spiderHealth <= 0)
						{
							//- Spider is dead
							model.set(row, col, '\u0020'); // \u0020 is a blank
							spiderHealth = maxSpiderHealth;
							healthPotion += 0.5;
							JOptionPane.showMessageDialog(null, "Congrats! You squished that arachnid and found half a health potion!");
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Yellow spider took " + playerStrength + " damage. \nYou took " + spiderStrength + " damage. \nSpider health is " + spiderHealth + "/" + maxSpiderHealth);
						}
					}

					else
					{
						// removes block in front of the character
						//model.set(row, col, '\u0020'); // \u0020 is a blank
					    JOptionPane.showMessageDialog(null, "This is just a plain old hedge, why would you try to interact with a hedge?");
					}

					iHasSword();
	        	}
	        	else
	        	{
	        	    // no option
	        	}
			}
			return false; //Can't move


		}//- End of outer if/else
	}//- End of isValidMove()

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
	}//- End of getSprites()

/*	private void makeSpidersMove() throws Exception
	{
		//- Add code from Lab6-BackPropNN GameRunner class.
		double[] params = {playerHealth, playerStrength, spiderHealth, spiderStrength};
		
		NeuralNetwork nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 4, 3, 4);
        Trainator trainer = new BackpropagationTrainer(nn); 
        trainer.train(data, expected, 0.01, 1000000);
        
        double[] result = nn.process(params);
        //System.out.println("==>" + (Utils.getMaxIndex(result) + 1));
        
        int output = (Utils.getMaxIndex(result) +1);
        switch(output){
	        case 1:
	        	//- Make spiders move left
	        	//panic();
	        	break;
	        case 2:
	        	//- Make spiders move right
	        	//attack();
	        	break;
	        case 3:
	        	//- Make spiders move up
	        	//hide();
	        	break;
	        default:
	        	//- Make spiders move down
	        	//runAway();
        }//- End of Switch
        
	}//- End of makeSpidersMove() */
	
	public static void main(String[] args) throws Exception
	{
		new GameRunner();

		FIS fis = FIS.load("fcl/Maze.fcl", true); //Load and parse the FCL 
		FunctionBlock fb = fis.getFunctionBlock("fuzzyMaze");
		JFuzzyChart.get().chart(fb);
		//fis.chart(); //Display the linguistic variables and terms 
		fis.setVariable("playerStrength", 7); //Apply a value to variables 
		fis.setVariable("spiderStrength", 5);
		fis.setVariable("playerHealth", 24); 
		fis.setVariable("spiderHealth", 15);
		fis.evaluate(); //Execute the fuzzy inference engine 

		Variable maze = fb.getVariable("killability");
		//JFuzzyChart.get.chart(tip, tip.getDefuzzifier(), true);
		JFuzzyChart.get().chart(maze.getDefuzzifier(), "Crisp Output", true);
		
		System.out.println(maze.defuzzify());
		//System.out.println(fis.getVariable("risk").getValue()); //Output end result
		
	}//- End of main()
}//- End of GameRunner