package player;

import java.awt.List;
import java.util.LinkedList;

import com.sun.glass.events.KeyEvent;

import attacks.AttackInt;
import attacks.Attacks;
import attacks.SemenSpit;
import level.Level;
import tools.Collision;
import tools.GameDetails;
import tools.RigidBodyInt;

public class RigidBody implements RigidBodyInt{
	
	private int x,y,w,h, bound1, bound2, bound3, bound4;
	private double multi;
	
	//The Player And Level
	Player player;
	Level level;
	
	//Attacks
	Attacks attacks;
	private int attacking;
	private final int SEMENSPIT,SEMENSPIT_X, SEMENSPIT_Y;
	private SemenSpit semenSpit;

	
	//Player Afloat Related Variables
	private double gravity;
	private final int MAX_GRAVITY;
	private final double GRAVITY_VELOCITY;
	private final double FLY_GRAVITY;
	private final int JUMP_GRAVITY;
	private boolean gliding;
	private boolean jump;
	
	//Collision
	LinkedList<Collision> colliders	; 
	LinkedList<Collision> enem;
	Collision body;
	
	//Movement
	private final int RIGHT,LEFT;
	private final int SPEED;
	private double velocity;
	private int direction;
	private int jumpCount;
	private int stopMovementTimer;
	
	//Get Hit
	private final int HIT_ANIMATION_TIME;
	private final int HIT_ANIMATION_SPEED;
	private final int HIT_ANIMATION_GRAVITY;
	private final int HIT_ENEMY_DAMAGE;
	private int damageBlinkTimer;
	
	
	
	
	
	//Health
	private double currHealth;
	
	public RigidBody(Player player, Level level) {
		//Setting The Position And Dimensions
		multi = GameDetails.SIZE_MULTIPLY;
		this. player = player;
		this.level = level;
		this.x = player.getX();
		this.y = player.getY();
		this.w = player.getPlayerWidth()-20;
		this.h= (int)(player.getPlayerHeight() + (5*multi));
		
		
		
		
		//Setting The Body Collision
		body = new Collision(x,y,w,h);
		
		//Setting Speed And Direction
		SPEED = (int)(6*multi*GameDetails.fpsRatio);
		RIGHT = 1;
		LEFT = -1;
		velocity = 0;
		direction = 1;
		
		//Setting The Afloat Details
		gravity = (int)(0*multi*GameDetails.fpsRatio);
		JUMP_GRAVITY = (int)(-8.5*multi*GameDetails.fpsRatio);
		MAX_GRAVITY = (int)(10*multi*GameDetails.fpsRatio);
		GRAVITY_VELOCITY = (0.3*multi*GameDetails.fpsRatio);
		FLY_GRAVITY = 1.5*multi;
		gliding = false;
		jump = true;
		jumpCount = 0;
		
		
		//Attacks
		attacks = new Attacks(level);
		attacking = 0;
		SEMENSPIT = 1;
		SEMENSPIT_X = (int)(25*GameDetails.SIZE_MULTIPLY);
		SEMENSPIT_Y = (int)(8*GameDetails.SIZE_MULTIPLY);
		semenSpit = new SemenSpit();
		
		//Hits
		HIT_ANIMATION_GRAVITY = (int)(-2*GameDetails.SIZE_MULTIPLY);
		HIT_ANIMATION_SPEED = (int)(5*GameDetails.SIZE_MULTIPLY);
		HIT_ANIMATION_TIME = 50;
		HIT_ENEMY_DAMAGE = 25;
		damageBlinkTimer = 0;
		currHealth = player.getMaxHealth();
		
		getBounds();
		
	}
	
	
	public void update() {
		

		if(!damageBlinking())
			enemyCollision();
		
//X Movements		
		
		xMovements();
		
		//Check Tile Collision For X
		checkTileCollision(0);
		
			
//Y Movements

		yMovements();
		gravityWorks();
		
		
		//Check Tile Collision for Y;
		checkTileCollision(1);
		checkBounds();
	
		
//Attacks
		//Perform Attack
		attack();
	
//Hit Collision
		
		if(stopMovementTimer > 0) {
			stopMovementTimer--;
			if(velocity > 0)
				velocity -= (0.3*GameDetails.SIZE_MULTIPLY);
			else if( velocity < 0)
				velocity += (0.3*GameDetails.SIZE_MULTIPLY);
		}
		
		//Setting sprite position
		setPos(x,y);
	}

	
	
	
	
	
	
									/*Collision*/
	
	
	//Tile Collision + attackCollisin
	public void checkTileCollision(int var) {
		
		colliders = this.level.getTileCollision();
		
		//Checking For X Collision With Tiles After Movement
		if(var == 0) {
			for(int i = 0; i < colliders.size(); i++) {
				//Checking For Player Tile Collision
				if(colliders.get(i).getX() >= bound1 - GameDetails.TILE_SIZE && colliders.get(i).getX() <= bound2 + GameDetails.TILE_SIZE) { 
					if(body.collidingY(colliders.get(i)) != 0) {
						x += body.collidingX(colliders.get(i));
						body.setX(x);						
					}
				}
				//Checking For Attack Collision
				attacks.Colliding(colliders.get(i));
			}
			
		}
		//Checking For Y Collision With Tiles After Movement
		else {
			for(int i = 0; i < colliders.size(); i++) {
				if(colliders.get(i).getX() > bound1 - GameDetails.TILE_SIZE && colliders.get(i).getX() < bound2 + GameDetails.TILE_SIZE) 
					if(body.collidingX(colliders.get(i))!= 0) {
						int dif = body.collidingY(colliders.get(i));
						if(dif != 0) {
						y+=dif;
						body.setY(y);
						gravity = 0;
						//If(Fell And Not Hit From Top Then Stop Jump)
						if(dif < 0) {
						jump = false;
						gliding = false;
						jumpCount = 0;
						stopMovementTimer = 0;
						}
						
						}
					}
				
			}
		}
	}
	
	
	

	//Setting The Colliders
		public void tileCollisionCheck(LinkedList<Collision> coll) {
			
			this.colliders = coll;
			
		}
		
		public void enemyCollisionCheck(LinkedList<Collision> coll) {
			
			this.enem = coll;
			
		}

	
	//Checking Bounds
	public void checkBounds() {
		
		//Colliding With Bound2
		if(x > bound2){
				this.level.setWorldX(x - bound2);	
				x = bound2;
		}
		//Colliding With Bound1
		else if(x < bound1) {
				this.level.setWorldX(x - bound1);	
				x = bound1;
		}
		body.setX(x);
		//Colliding With Bound4
				if(y > bound4){
					if(GameDetails.worldY < level.getMaxWorldY()) {
					this.level.setWorldY(y - bound4);	
					y = bound4;
					}
				}
		//Colliding With Bound3	
				else if(y < bound3) {
					this.level.setWorldY(y - bound3);	
					y = bound3;
				}
				
				body.setY(y);
	}
	
	
	//Enemy Collision
	public void enemyCollision() {
		
		for(int i = 0; i < enem.size(); i++) {
			if(enem.get(i) != null ) {
			if(enem.get(i).onScreen()) {
				int x = enem.get(i).collidingX(body);
				if(x != 0 && enem.get(i).collidingY(body) != 0)
					{
						hitAnimation(x);
						takeDamage(HIT_ENEMY_DAMAGE,0);
					}
					
			}
					
		}
		}
	}
	

	//Setting The Level Bounds
	public void getBounds() {
		
		this.bound1 = level.getBound1();
		this.bound2 = level.getBound2();
		this.bound3 = level.getBound3();
		this.bound4 = level.getBound4();
	}
	
	
		

										/*Movement*/
	
	
	//Move
		public void move(int direction) {
			
			if(direction == RIGHT) { 
				velocity = SPEED;	
			}
			else if (direction == LEFT) {
				velocity = -SPEED;
			}
			else if(direction == RIGHT+1) {
				if(velocity > 0)
				velocity = 0;
			}
			else if(direction == LEFT-1) {
				if(velocity < 0)
					velocity = 0;
				
			}
			if(direction > 0)
				this.direction = 1;
			else
				this.direction = -1;
			
		
		}
		
	
	//X Movement
	public void xMovements() {

		//Moving The Character By Velocity
			x+=velocity;
			body.setX(x);
			
	}
	
	//Y Movements
	public void yMovements() {
	
		//Adding Gravity
		y+=gravity;
		body.setY(y);	
		
	}
	
	
	//Updating Gravity
		public void gravityWorks() {
			
			if(gravity < MAX_GRAVITY) {		
				if(gliding && gravity >= 0) {
					gravity = FLY_GRAVITY;
				}
				else
					gravity += GRAVITY_VELOCITY;
				
			}
			
		}
		
	
	//Jump
	public void jump() {
		if(!jump) {
		gravity = JUMP_GRAVITY;
		jump = true;
		}
		}
		
	
	//Check For Falling OFF SCREEN
	public boolean fellOffScreen() {
		
		if(y >= GameDetails.screenH) {
			gravity = 0;
			gliding = false;
			jump = true;
			jumpCount = 0;
			
			return true;
		}
		return false;
		
	}
	
	//Set Body After Fall Off Screen
	public void setBodyPos(int x, int y) {
		
		this.x = x;
		this. y = y;
		
	}
	
		
	//Setting Player Position
		public void setPos(int x, int y) {
			player.setX(x);
			player.setY(y);	
		}	

	
										/*Attack*/

	public void attack() {
		
		/*Checking for attacking*/
		if(attacking !=0 && attacking < 90) {
			
			AttackInt attack = null;
			
			//Creating SemenSpit Attack	
			if (attacking == SEMENSPIT) {
				attack = new SemenSpit(semenSpit);
				
				if(direction > 0)
					attack.setPos(x ,y + SEMENSPIT_Y);
				else
					attack.setPos(x- SEMENSPIT_X,y + SEMENSPIT_Y);
				attacks.addAttack(attack);
			}
			
			//Setting Up Attack Details
			attack.setDirection(direction);
			attack.resetSprite();
			
			//Adding Attack Image To PLayer Class
			player.addAttack(attack);
			
			//Adding Attack To Level To Be sent to enemy bodys
			this.level.addAttack(attack);
			
			//resetting attacking option for player
			attacking = 0;
			
			//Adding The Attack
			attacks.addAttack(attack);
		}
		
		/*Attacks Update Meethods*/
		attacks.update();
			
	}
	
	
	public LinkedList<AttackInt> getAttacks(){
		
		return attacks.getList();
		
	}
	
	
	/*
	 * 
	 * KeyBoard
	 * 
	 * 
	 */
	
public void keyP(int k) {
		
	//Checking If Movement Is Enabled
		if(stopMovementTimer == 0) {
		switch (k) {
		
		case KeyEvent.VK_Z: if(attacking != 91) {attacking = 91;}
		break;
		case KeyEvent.VK_RIGHT: move(RIGHT);
		break;
		case KeyEvent.VK_LEFT: move(LEFT); 
		break;	
		case KeyEvent.VK_UP: jump();
		break;
		case KeyEvent.VK_CONTROL: if(jump && !gliding) {gliding = true;}
		break;
		case KeyEvent.VK_SPACE: 
			switch (attacking) {
			case 91:attacking = SEMENSPIT;
			}
		break;
		}
		}
	}
	public void keyR(int k) {
		if(stopMovementTimer == 0) {
		switch (k) {
		
		case KeyEvent.VK_RIGHT: if(direction > 0)move(RIGHT+1);
		break;
		case KeyEvent.VK_LEFT: if(direction < 0)move(LEFT-1);
		break;
		case KeyEvent.VK_UP: if(jumpCount < 1 && !gliding) {jump = false;jumpCount++;}
		break;
		case KeyEvent.VK_CONTROL: gliding = false;  
		break;
		case KeyEvent.VK_Z: attacking = 0;
		break;
		}
		}
		
	}
	
	
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getW() {return w;}
	public int getH() {return h;}


	@Override
	public boolean onScreen() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	/*
	 * 
	 * Damage And Health SHIT
	 * 
	 */

	@Override
	public int getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setHealth(int health) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean dead() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void takeDamage(int damage, int i) {
		// TODO Auto-generated method stub
		if(currHealth - damage >0) {
			currHealth -= damage;
		}
		else {
			damage = (int) currHealth;
			currHealth = 0;
		}
			this.player.takeDamage(damage);
	}

	private void disableMovement(int timer) {
		
		this.stopMovementTimer = timer;
		
	}
	
	public void hitAnimation(int x) {
		
		disableMovement(HIT_ANIMATION_TIME);
		damageBlinkTimer = HIT_ANIMATION_TIME*2;
		velocity = HIT_ANIMATION_SPEED*(-x/Math.abs(x));
		gravity = HIT_ANIMATION_GRAVITY;
		
	}
	
	

	
	private boolean damageBlinking() {

		if(damageBlinkTimer > 0) {
			damageBlinkTimer--;
			return true;
		}
		
		return false;
			
	}


	@Override
	public void attackCollision() {
		// TODO Auto-generated method stub
		
	}
	
	
}
