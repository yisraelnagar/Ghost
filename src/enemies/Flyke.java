package enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import level.Level;
import tools.GameDetails;
import tools.Health;
import tools.SpriteMovement;

public class Flyke implements EnemyInt{

	//Sprites
	SpriteMovement[] flyke;
	EnemyRigidBody body;
	BufferedImage img;
	private final int FLY_LEFT,FLY_RIGHT,TAKE_DAMAGE_RIGHT,TAKE_DAMAGE_LEFT, DIE_RIGHT, DIE_LEFT;
	private int currSprite;
	private int velocity;
	private int attackVelocity;
	private double currHealth;
	private final double MAX_HEALTH;
	private final int DAMAGE;
	private Health healthBar;
	private int direction;
	private int attackDestination;
	private int boundX1,boundX2;	
	
	//Dimensions
	private int x,y,w,h;
	
	//Health
	private boolean dead;
	
	//Classes
	Level level;
	
	//Booleans
	private boolean noticedPlayer;
	
	
	public Flyke(Level level) {
		
		this.level = level;
		
		//Sprite's
		FLY_LEFT = 0;
		FLY_RIGHT = 1;
		TAKE_DAMAGE_RIGHT = 2;
		TAKE_DAMAGE_LEFT = 3;
		DIE_RIGHT = 4;
		DIE_LEFT=5;
		
		try {
			img= ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\Monsters\\flyke.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		flyke = new SpriteMovement[8];
		flyke[FLY_LEFT] = new SpriteMovement(img,4,1,1,1,12,18,(int)(12/GameDetails.fpsRatio));
		flyke[FLY_RIGHT] = new SpriteMovement(img,4,1,1,2,12,18,(int)(12/GameDetails.fpsRatio));
		flyke[TAKE_DAMAGE_RIGHT] = new SpriteMovement(img,4,1,1,3,12,18,(int)(8/GameDetails.fpsRatio));
		flyke[TAKE_DAMAGE_LEFT] = new SpriteMovement(img,4,1,1,4,12,18,(int)(8/GameDetails.fpsRatio));
		flyke[DIE_RIGHT] = new SpriteMovement(img,8,1,1,5,12,18,(int)(8/GameDetails.fpsRatio),8);	
		flyke[DIE_LEFT] = new SpriteMovement(img,8,1,1,6,12,18,(int)(8/GameDetails.fpsRatio),8);	
		
		
		currSprite = 0;
		
		//Dimensions
		this.w = (int)(GameDetails.TILE_SIZE) + 10;
		this.h = (int)(this.w*1.5);
		
		DAMAGE = 10;
		MAX_HEALTH = 100;
		currHealth = MAX_HEALTH;
		healthBar = new Health(MAX_HEALTH, currHealth);
		dead = false;
	
	
		//Movement
		velocity = (int)(1.5*GameDetails.SIZE_MULTIPLY*GameDetails.fpsRatio);
		attackVelocity = (int)(3.5*GameDetails.SIZE_MULTIPLY*GameDetails.fpsRatio);
		direction = new Random().nextInt(2);
		if(direction == 0)
			direction = -1;
		
	}
	
	public void setBoundsX(int boundX1, int boundX2) {
		
		this.boundX1 = boundX1;
		this.boundX2 = boundX2;
		
	}

	public void setBody(EnemyRigidBody body) {
		this.body = body;
	}
	
	@Override
	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

		healthBar.update();
		setHealth();
		if(currHealth > 0)
		setDirection(direction);
		
		//Update RigidBody
		
//X Movements
		movementBehaviour();
		body.xMovements();
		if(body.onScreen()) {
		//Check Tile Collision For X
		body.checkTileCollision(0);
		
//Y Movements
		
		body.yMovements();
		body.setY(y);
		body.gravityWorks();
		
		
//Check Collision;
		body.checkTileCollision(1);
	
		body.attackCollision();

		}
		setPos(x,y);
		
	
		}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
			
			if(currSprite == TAKE_DAMAGE_RIGHT) 
				if(flyke[TAKE_DAMAGE_RIGHT].getcurrColl() ==2)
					changeSprite(FLY_RIGHT);
			if(currSprite == TAKE_DAMAGE_LEFT) 
				if(flyke[TAKE_DAMAGE_LEFT].getcurrColl() ==2)
					changeSprite(FLY_LEFT);
			
			flyke[currSprite].draw(g, x, y, w, h);
			
			if(!dead) {
			healthBar.draw(g, x, y);
			}
			
	}
	
	public int getW() {return w;}
	
	public int getH() {return h;}

	@Override
	public void setHealth() {
		// TODO Auto-generated method stub
		//Set Enemy Health to rigid body health
		this.currHealth = this.healthBar.getHealth();
		//Checking If Died
		if(currHealth <= 0) {
			
			isDead();
			
		}
	}
		
	//taking damage
	public void damageTaken(int d) {
		
		this.healthBar.takeDamage(d);
		
		//Setting Taking damage sprite right
		if(currSprite == FLY_RIGHT)
			changeSprite(TAKE_DAMAGE_RIGHT);
		//Setting Taking damage sprite Left
		else
			changeSprite(TAKE_DAMAGE_LEFT);
	}
	
	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return DAMAGE;
	}

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		return (int) MAX_HEALTH;
	}

	//Change Sprite
	@Override
	public void changeSprite(int sprite) {
		// TODO Auto-generated method stub
		this.currSprite = sprite;
		flyke[currSprite].setColl(0);
		flyke[currSprite].setRow(0);
		
	}

	
	public boolean isDead() {
		if(currHealth <= 0) {	
			//If Not declared dead - declare and set sprite to dying
			if(!dead) {
				if(currSprite == TAKE_DAMAGE_RIGHT || currSprite == FLY_RIGHT)	
					changeSprite(DIE_RIGHT);
				else
					changeSprite(DIE_LEFT);
				dead = true;
			}
			if(flyke[currSprite].lastSprite()) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public void setDirection(int direction) {if( direction == 1) currSprite = 1; else currSprite = 0;}

	@Override
	public void movementBehaviour() {
		// TODO Auto-generated method stub

		
		int pX = this.level.getPlayerBody().getX();
		int pY = this.level.getPlayerBody().getY();
		int pW = this.level.getPlayerBody().getW();
		int pH = this.level.getPlayerBody().getH();
		
		if(body.getY() + body.getH() == pY + pH) {
			if(pX + pW >= boundX1 && pX <= boundX2) {
				System.out.println((pX - body.getX())/Math.abs(pX - body.getX()) + ", " + direction);
				if((pX - body.getX())/Math.abs(pX - body.getX()) == direction) {

					if(attackDestination == body.getX())
						attackDestination = pX;
						
					noticedPlayer = true;
					attackPlayer();
				}
			}
		}
		
		if(noticedPlayer) {
			
			if(direction == -1)
				if(attackDestination >= body.getX())
					noticedPlayer = false;
			if(direction == 1)
				if(attackDestination <= body.getX())
					noticedPlayer = false;
			
		}
		
		if(!noticedPlayer) {
		//When Roaming And Not seeing Player
		//If Alive
		if(currHealth > 0) {
			//If Not One Square
			if(boundX2 >= boundX1 + GameDetails.TILE_SIZE){
				//If Moving Right
				if(direction > 0) {
			
					if(x + velocity < boundX2)
						x+=velocity;
					else
						direction*=-1;
			
				}
				//If Moving Left
				else {
					
					if(x - velocity > boundX1)
						x-=velocity;
					else
						direction*=-1;
				}
			
				//Setting Changes To Body
				body.setX(x);	
				attackDestination = body.getX();
			}
		}
		}
	}
	
	
	public void attackPlayer() {
		
		
		if(x > attackDestination) {
			x -= attackVelocity;
		}
		else {
			x += attackVelocity;
		
		}
			body.setX(x);
	}
	
	@Override
	public void damageMovement(int x) {
		// TODO Auto-generated method stub
		body.setGravity(-3*GameDetails.fpsRatio);
	}

	public void worldX(int x) {
		
		attackDestination += x;
		
	}
	
}
