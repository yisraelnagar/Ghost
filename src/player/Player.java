package player;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import com.sun.glass.events.KeyEvent;

import attacks.AttackInt;
import tools.GameDetails;
import tools.Health;
import tools.SpriteMovement;

public class Player {
	
	//Player Sprite
	SpriteMovement[] jeremy;
	BufferedImage img;
	
	//Attacks
	LinkedList<AttackInt> attacks;
	
	//Player Movement States
	int playerState;
	public final int IDLE_RIGHT,IDLE_LEFT,WALK_RIGHT,WALK_LEFT, SEMEN_SPRITE;
	private final int SEMEN_SPIT_TYPE;
	private final int SEMEN_SPIT_TIME;
	private int semenSpit;
	
	//Player Position
	private int posX,posY;
	
	//Player Dimensions
	private final int playerWidth, playerHeight;
	
	//Health
	Health healthBar;
	private final int MAX_HEALTH;
	private int currHealth;
	
	public Player(int x, int y) {
		
		//Player Health
		MAX_HEALTH = 100;
		currHealth = MAX_HEALTH;
		healthBar = new Health(MAX_HEALTH, currHealth);
		
		//Initialize Player Movements
		playerState = 0;
		IDLE_RIGHT = 0;
		WALK_RIGHT = 1;
		WALK_LEFT = 2;
		IDLE_LEFT = 3;
		SEMEN_SPRITE = 4;
		

		//Initialize Player Position
		this.posX = x;
		this.posY = y;
		
		//Initialize player Dimensions
		this.playerWidth = GameDetails.playerW;
		this.playerHeight = GameDetails.playerH;
		
		//Initialize Player Sprite
		try {
			img = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\Characters\\Ghost.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jeremy = new SpriteMovement[8];
		for(int i = 0; i < jeremy.length; i++) {
			jeremy[i] = new SpriteMovement(img, 4, 1, 1, i+1, 12, 18, (int)(12/GameDetails.fpsRatio));
		}
	
		//Initialize Attack Sprite
		attacks = new LinkedList<AttackInt>();
		semenSpit = 0;
		SEMEN_SPIT_TIME = 20;
		SEMEN_SPIT_TYPE = 0;
		
	}
	
	public void init() {}
	
	
	public void update() {
		
		healthBar.update();
		
	}
	
	
	public void draw(Graphics2D g) {
		
		stopAnimations();
		
		if(semenSpit > 0) 	
			jeremy[playerState + SEMEN_SPRITE].draw(g, posX,posY,playerWidth,playerHeight);
		else
			jeremy[playerState].draw(g, posX,posY,playerWidth,playerHeight);
		
		for(int i = 0; i < attacks.size(); i++) {
			
			attacks.get(i).update();
			attacks.get(i).draw(g);
			if(attacks.get(i).delete())
				attacks.remove(i);
			
		}
		
		healthBar.draw(g, posX - (int)(7*GameDetails.SIZE_MULTIPLY), posY, playerWidth);
	}
	
	private void stopAnimations() {
		
		if(semenSpit > 0)
		semenSpit--;
		
	}
	
	
	public void keyP(int k) {
		
		switch(k) {
		
		case KeyEvent.VK_RIGHT: playerState = WALK_RIGHT;
		break;
		case KeyEvent.VK_LEFT: playerState = WALK_LEFT;
		break;
		}
		
	}
	public void keyR(int k) {
		
switch(k) {
		
		case KeyEvent.VK_RIGHT: if(playerState == WALK_RIGHT)playerState = IDLE_RIGHT;
		break;
		case KeyEvent.VK_LEFT: if(playerState == WALK_LEFT)playerState = IDLE_LEFT;
		break;
		
		
		}
		
		
	}
	public void keyT(int k) {}

	
	public int getX() {return posX;}
	public int getY() {return posY;}
	public void setX(int x) {this.posX = x;}
	public void setY(int y) {this.posY = y;}

	public int getPlayerWidth() {
		return playerWidth;
	}

	public int getPlayerHeight() {
		return playerHeight;
	}

	
	public void addAttack(AttackInt attack) {
		
		attacks.add(attack);
		if(attacks.getLast().getType() == SEMEN_SPIT_TYPE)
			semenSpit  = SEMEN_SPIT_TIME;
		
	}
	public int getMaxHealth() {return MAX_HEALTH;}
	
	public void takeDamage(int d) {
		
		healthBar.takeDamage(d);
		currHealth -= d;
		
	}
	
}
