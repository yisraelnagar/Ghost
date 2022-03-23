package level;

import java.awt.Graphics2D;
import java.util.LinkedList;

import attacks.AttackInt;
import enemies.Enemies;
import player.Player;
import player.RigidBody;
import tools.Collision;
import tools.GameDetails;

public class Level {

	
	//Level Details
	private final int LEVEL_NUM;
	private final int MAX_WORLD_Y;
	private int levelBound1,levelBound2,levelBound3,levelBound4;
	private int playertartX, playertartY;

	//The player
	Player player;
	RigidBody body;
	LinkedList<AttackInt> attacks;
	
	//Enemies
	Enemies enemies;
	private LinkedList<Collision> enemCollision;
	
	
	//The Map
	Map  map;
	

	
	/*Constructor*/
	public Level(int levelNum) {
		
		levelBound1 = (int) (522*GameDetails.SIZE_MULTIPLY);
		levelBound2 = (int) (666*GameDetails.SIZE_MULTIPLY);
		levelBound3 = (int) (300*GameDetails.SIZE_MULTIPLY);
		levelBound4 = (int) (400*GameDetails.SIZE_MULTIPLY);
		
		//Defining  The Level
		this.LEVEL_NUM = levelNum;
		playertartX = (int) (425*GameDetails.SIZE_MULTIPLY);
		playertartY = (int)(100*GameDetails.SIZE_MULTIPLY);
		MAX_WORLD_Y = 0;
		//Creating map and monsters
			//Enemies
			enemies = null;
			enemCollision = new LinkedList<Collision>();
			map = new Map(levelNum,this);
			createEnemy(map.getEnemies());
		
		
		//Creating The player
		createplayer();
		attacks = new LinkedList<AttackInt>();
		
		
		
		
	}
	/*Enemy Creation Function*/
	public void createEnemy(Enemies enem) {
		enemies = new Enemies(enem, this);
	}
	
	
	/*Player Creation Function*/
	public void createplayer() {
		//Defining an x position to multiply and place each player after each other
		
		
		//Creating The Number Of player
			
			player = new Player(playertartX, playertartY);
			body = new RigidBody(player, this);
		
		
	}
	
	
	
	public void init() {
		

		 {
				player.init();
		}
		enemies.init();
		
	}
	
	
	public void update() {

		
		/*Looping The Update Methods*/
		
		//Updating Enemies
		enemies.update();
		
			
			//Checking If Player Exists
				//Player Update
				player.update();
				//Player Rigid Body Collision
				body.enemyCollisionCheck(enemCollision);
				//Player Rigid Body Update
				body.update();
				//Checking If Player Fell Off Screen
				if(body.fellOffScreen()) {
					body.setBodyPos(playertartX,playertartY);
					setWorldX(-GameDetails.worldX);
					setWorldY(-GameDetails.worldY);
				}
			}
		
		
	
	
	
	public void draw(Graphics2D g) {
		
		
		map.drawBackground(g);
		
		//Drawing The player
		 	
				player.draw(g);
		enemies.draw(g);
		
		//Drawing The Map
				map.draw(g);

		}
	
	
	public void keyP(int k) {
		
		 {
		player.keyP(k);
		body.keyP(k);
		}
		
	}
	public void keyR(int k) {
		

		 {
		player.keyR(k);
		body.keyR(k);
	}
	}
	public void keyT(int k) {
		

		
		player.keyT(k);
		
	}
	
	public int getBound1 () {return levelBound1;}
	public int getBound2 () {return levelBound2;}
	public int getBound3 () {return levelBound3;}
	public int getBound4 () {return levelBound4;}
	
	public void addAttack(AttackInt attack) {
		attacks.add(attack);	
	}
	public void removeAttack(AttackInt attack) {
		attacks.remove(attack);
	}
	
	public void removeEnemy(int index) {
		enemCollision.remove(index);
	}
	
	public void setWorldX(int x) {
		
		GameDetails.worldX += x;
		map.setWorldX();
		enemies.setWorldX(map.getColliders());
	
	}		
	
	
	public void setWorldY(int y) {
	
		GameDetails.worldY += y;
		map.setWorldY();
		enemies.setWorldY(map.getColliders());
	
	}
	
	public RigidBody getPlayerBody(){return body;}
	
	public LinkedList<Collision> getTileCollision(){return map.colliders;}
	
	public int getMaxWorldY() {return MAX_WORLD_Y;}
	
	public void addEnemyCollider(Collision coll){enemCollision.add(coll);}
	public void setEnemyCollider(int i, Collision coll){enemCollision.set(i, coll);}
	
	
	public LinkedList<Collision> getTiles(){
		
		return map.getColliders();
		
	}
	
	
	public LinkedList<AttackInt> getPlayerAttacks(){return attacks;}
	}
