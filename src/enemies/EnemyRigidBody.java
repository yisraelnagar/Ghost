package enemies;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Random;

import attacks.AttackInt;
import tools.Collision;
import tools.GameDetails;
import tools.RigidBodyInt;

public class EnemyRigidBody implements RigidBodyInt{

	
	//Position
	private int x,y,w,h;
	private int boundX1,boundX2;
	
	//Movement
	private int worldX, worldY;
	private final int tilesRight, tilesLeft;
	
	//Air Movement
	private double gravity;
	private final double MAX_GRAVITY;
	private final double GRAVITY_VELOCITY;
	
	//Bodys
	Collision body;
	LinkedList<Collision> colliders;
	LinkedList<AttackInt> attacks;
	
	//Enemy
	EnemyInt enemy;
	
	
	//Health
	private final int DAMAGE;
	private final int MAX_HEALTH;
	private int currHealth;
	
	public EnemyRigidBody(EnemyInt enemy,int x, int y, int tilesRight, int tilesLeft) {
	
		//Enemy
		this.enemy = enemy;
		
		//Position
		this.x = x;
		this.y = y;
		this.w = this.enemy.getW();
		this.h = this.enemy.getH();
		
		//Body Collision
		body = new Collision(x, y, w, h);
		attacks = new LinkedList<AttackInt>();
		
		//Air Dimensions
		gravity = 0;
		MAX_GRAVITY = (int)(8*GameDetails.SIZE_MULTIPLY*GameDetails.fpsRatio);
		GRAVITY_VELOCITY = (0.4*GameDetails.SIZE_MULTIPLY*GameDetails.fpsRatio);
		
		//Bounds
		this.tilesLeft = tilesLeft;
		this.tilesRight = tilesRight;
		
		//Health
		DAMAGE = enemy.getDamage();
		MAX_HEALTH = enemy.getMaxHealth();
		
		
	}
	
	public void init() {
	
		//Setting Health
		currHealth = MAX_HEALTH;
	
		//Setting WorldDetails
		boundX2 = x + tilesRight*GameDetails.TILE_SIZE+ (GameDetails.TILE_SIZE - w);
		boundX1 = x - tilesLeft*GameDetails.TILE_SIZE ;
		worldX = GameDetails.worldX;
		worldY = GameDetails.worldY;
		checkBounds();
		enemy.setBoundsX(boundX1, boundX2);
		
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkTileCollision(int var) {
		// TODO Auto-generated method stub
		//Checking For X Collision With Tiles After Movement
		
		if(var == 0) {
			
			for(int i = 0; i < colliders.size(); i++) {
				if(colliders.get(i).getX() >= (-GameDetails.TILE_SIZE*2) && colliders.get(i).getX() <= GameDetails.screenW + (GameDetails.TILE_SIZE)) { 
					if(body.collidingY(colliders.get(i)) != 0) {
						int dif = body.collidingX(colliders.get(i));
						x += dif;
						body.setX(x);
					}
			}
			}
			
		}
		//Checking For Y Collision With Tiles After Movement
		else {
			for(int i = 0; i < colliders.size(); i++) 
				if(colliders.get(i).getX() >= (-GameDetails.TILE_SIZE*2) && colliders.get(i).getX() <= GameDetails.screenW + GameDetails.TILE_SIZE)
					if(body.collidingX(colliders.get(i))!= 0) {
						int dif = body.collidingY(colliders.get(i));
						if(dif != 0) {
						y+=dif;
						gravity = 0;
						}
						body.setY(y);
						
					}
		}
	}

	//Increasing The Gravity When Falling
	@Override
	public void gravityWorks() {
		// TODO Auto-generated method stub

		//Checking If Gravity Isn't At Max
		if(gravity < MAX_GRAVITY) 
			//Increasing Gravity
			gravity += GRAVITY_VELOCITY;
		
	}

	@Override
	public void xMovements() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void yMovements() {
		// TODO Auto-generated method stub
		y+= gravity;
		body.setY(y);
	}

	@Override
	public void checkBounds() {
		// TODO Auto-generated method stub
		
		if(worldX != GameDetails.worldX) {
			x += worldX-GameDetails.worldX;
			enemy.worldX(worldX-GameDetails.worldX);
			boundX1 += worldX-GameDetails.worldX;
			boundX2+= worldX-GameDetails.worldX;
			enemy.setBoundsX(boundX1, boundX2);
		}
		
		worldX = GameDetails.worldX;

		body.setX(x);
		
		if(worldY != GameDetails.worldY) {
			y += worldY-GameDetails.worldY;
		}
		
		worldY = GameDetails.worldY;

		body.setY(y);
		setPos(x,y);
	}

	@Override
	public void getBounds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void move(int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tileCollisionCheck(LinkedList<Collision> coll) {
		// TODO Auto-generated method stub
		this.colliders = coll;
	
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub
		
	}

	
	
	@Override
	public void attackCollision() {
		// TODO Auto-generated method stub
		if(currHealth > 0)
		for(int i = 0; i < attacks.size(); i++) {
			
			Collision atkCol = new Collision(attacks.get(i).getX(),attacks.get(i).getY(),attacks.get(i).getW(),attacks.get(i).getH());
			if(body.colliding(atkCol)) {
				if(!attacks.get(i).getHit()) {
					takeDamage(attacks.get(i).getDamage(), i);
					hitCollision(atkCol.getX());
				}
			}
			
		}
		
	}

	@Override
	public boolean fellOffScreen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public int getW() {
		// TODO Auto-generated method stub
		return w;
	}

	@Override
	public int getH() {
		// TODO Auto-generated method stub
		return h;
	}

	@Override
	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		this.enemy.setPos(x,y);
	}

	public void setX(int x) {this.x = x;body.setX(x);}
	
	public void setY(int y) {this.y = y;body.setY(y);}
	
	public boolean onScreen() {
		
		if(x + w >= 0 && x <= GameDetails.screenW) {
		
			return true;
			
		}
		
		return false;
		
	}

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return currHealth;
	}

	@Override
	public void setHealth(int health) {
		// TODO Auto-generated method stub
		this.currHealth = health;
	}
	
	public void setAttacks(LinkedList<AttackInt> attacks) {
		
		this.attacks = attacks;

	}

	@Override
	public boolean dead() {
		// TODO Auto-generated method stub
		
		if(this.enemy.isDead())
			return true;
		
		return false;
	}
	
	public void takeDamage(int d,int i) {
		

		this.currHealth -= d;
		this.enemy.damageTaken(d);
		if(i != -1)
		this.attacks.get(i).Hit();
		
	}
	
	public Collision getBodyCollider() {
		if(currHealth > 0)
			return body;
		else
			return null;
	}
	
	public EnemyInt getEnemyInt() {return enemy;}



	public void hitCollision(int x) {
		
		enemy.damageMovement(x);
		
	}
	
	public void setGravity(double g) {this.gravity = g;}
	
}

