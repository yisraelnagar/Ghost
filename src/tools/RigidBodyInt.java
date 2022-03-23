package tools;

import java.util.LinkedList;

public interface RigidBodyInt {

	public void update();
	void checkTileCollision(int var); 
	void gravityWorks();
	void xMovements();
	void yMovements();
	void checkBounds();
	void setPos(int x, int y);
	public void getBounds();
	void move(int direction);
	void tileCollisionCheck(LinkedList<Collision> coll);
	void attack();
	void attackCollision();
	boolean fellOffScreen();
	public int getX();
	public int getY();
	public int getW();
	public int getH();
	public boolean onScreen();
	public int getHealth();
	public void setHealth(int health);
	public boolean dead();
	public void takeDamage(int damage, int i);
	
}
