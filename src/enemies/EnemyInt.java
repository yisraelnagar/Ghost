package enemies;

import java.awt.Graphics2D;

public interface EnemyInt {

	public void setPos(int x, int y);

	public void update();
	public void draw(Graphics2D g);
	public int getW();
	public int getH();
	public void setHealth();
	public int getDamage();
	public int getMaxHealth();
	public void damageTaken(int d);
	public void changeSprite(int sprite);
	public boolean isDead();
	public void setDirection(int dir);
	public void movementBehaviour();
	public void damageMovement(int x);
	public void setBody(EnemyRigidBody body);
	public void setBoundsX(int boundX1, int boundX2);
	public void attackPlayer();
	public void worldX(int x);
	
}
