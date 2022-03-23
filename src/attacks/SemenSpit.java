package attacks;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import tools.GameDetails;
import tools.SpriteMovement;

public class SemenSpit implements AttackInt{


	//Damage
	private final int DAMAGE;
	
	//Image
	private SpriteMovement[] sm;
	private BufferedImage img;
	private final int SPRITE_EXPLODE_RIGHT;
	private final int SPRITE_EXPLODE_LEFT;
	private final int SPRITE_LEFT;
	private final int SPRITE_LEFT_BUILD;
	private final int SPRITE_RIGHT_BUILD;
	private final int SPRITE_RIGHT;
	private int currSprite;
	private int worldX, worldY;
	private int spriteTime;
	
	//Health
	private int timer;
	private int deathTime;
	private boolean dead;
	private boolean Hit;
	
	//Position
	private int x, y,w,h, dir;
	
	//Movement
	private int SPEED;
	
	
	public SemenSpit() {
		
		DAMAGE = 0;
		
		//Sprite
		sm = new SpriteMovement[6];
		SPRITE_RIGHT = 0;
		SPRITE_RIGHT_BUILD = 3;
		SPRITE_LEFT_BUILD = 4;
		SPRITE_LEFT = 1;
		SPRITE_EXPLODE_RIGHT = 2;
		SPRITE_EXPLODE_LEFT = 5;
		spriteTime = (int)(7/GameDetails.fpsRatio);
		try {
			img= ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\Attacks\\SemenSpit.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sm[SPRITE_RIGHT_BUILD] = new SpriteMovement(img,4,1,1,1,20,12,spriteTime,4);
		sm[SPRITE_RIGHT] = new SpriteMovement(img,4,1,1,2,20,12,spriteTime);
		sm[SPRITE_LEFT] = new SpriteMovement(img,4,1,1,4,20,12,spriteTime);
		sm[SPRITE_EXPLODE_RIGHT] = new SpriteMovement(img,4,1,1,5,20,12,(int)(4/GameDetails.fpsRatio),4);
		sm[SPRITE_EXPLODE_LEFT] = new SpriteMovement(img,4,1,1,6,20,12,(int)(4/GameDetails.fpsRatio),4);
		sm[SPRITE_LEFT_BUILD] = new SpriteMovement(img,4,1,1,3,20,12,spriteTime,4);
	
	}
	
	public SemenSpit(SemenSpit s) {
		
		//Dimensions
		this.w = (int) (40*GameDetails.SIZE_MULTIPLY);
		this.h = (int) (22*GameDetails.SIZE_MULTIPLY);
		
		//Time Dimensions
		timer = 0;
		deathTime = (int)(300/GameDetails.fpsRatio);
		
		//Movement
		SPEED = (int)(5*GameDetails.SIZE_MULTIPLY*GameDetails.fpsRatio);
	
		//Damage
		DAMAGE = 40;
		Hit = false;
		dead = false;
		
		//Sprite
		sm = s.sm;
		SPRITE_RIGHT = 0;
		SPRITE_RIGHT_BUILD = 3;
		SPRITE_LEFT_BUILD = 4;
		SPRITE_LEFT = 1;
		SPRITE_EXPLODE_RIGHT = 2;
		SPRITE_EXPLODE_LEFT = 5;
		
		currSprite = 0;
		
		worldX = GameDetails.worldX;
		worldY = GameDetails.worldY;
		sm[SPRITE_RIGHT].setColl(0);
		sm[SPRITE_RIGHT].setRow(0);
	}
	
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return DAMAGE;
	}
	
	public void update() {
		
		//Updating Position And Direction
		if(!Hit) {
			if(dir > 0) {
				currSprite = SPRITE_RIGHT_BUILD;
				x += SPEED;
				if(sm[SPRITE_RIGHT_BUILD].lastSprite()) 
					currSprite = SPRITE_RIGHT;
				
			}
			else {
				currSprite = SPRITE_LEFT_BUILD;
				x-= SPEED;
				if(sm[SPRITE_LEFT_BUILD].lastSprite()) 
					currSprite = SPRITE_LEFT;
				
			}
			
		}
		
		//Exploding On Hit
		else {
			if(dir > 0)
				currSprite = SPRITE_EXPLODE_RIGHT;
			else
				currSprite = SPRITE_EXPLODE_LEFT;
			if(sm[currSprite].lastSprite())
				dead = true;
			
		}
		
		
		//Checking Timer
		if(timer >= deathTime) 
			dead = true;
		
		timer++;
		

		//Setting WorldX
		if(worldX != GameDetails.worldX) {
			
			x += worldX-GameDetails.worldX;
			worldX = GameDetails.worldX;
			
		}
		//Setting WorldX
		if(worldY != GameDetails.worldY) {
					
			y += worldY-GameDetails.worldY;
			worldY = GameDetails.worldY;
					
		}
		
	}
	
	
	//Death Clock
	public boolean delete() {
		
		if(dead) {
	
			return true;
	}
		return false;
		
	}
	
	
	
	//Draw The Image
	public void draw(Graphics2D g) {
		
			
			sm[currSprite].draw(g, x, y, w, h);
		
	}
	
	//Hit Method
	public void Hit() {
		if(Hit == false) {
		this.Hit = true;
		timer = 0;
		sm[SPRITE_EXPLODE_RIGHT].setColl(0);
		sm[SPRITE_EXPLODE_RIGHT].setRow(0);
		}
	}

	
	//Getters Setters
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	@Override
	public int getDirection() {
		// TODO Auto-generated method stub
		return dir;
	}
	
	public boolean getHit() {return Hit;}
	
	//Setting The Position And Direction
	@Override
	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}

	public void resetSprite() {
		
		for(int i = 0; i < sm.length; i++) {
		sm[i].setColl(0);
		sm[i].setRow(0);
		}
	}
	
	
	public void setDirection(int dir) {this.dir = dir;}
	
	public int getType() {return 0;}
	
}
