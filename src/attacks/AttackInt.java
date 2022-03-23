package attacks;

import java.awt.Graphics2D;

public interface AttackInt {

	public void setPos(int x, int y);
	public int getDamage();
	public void update();
	public boolean delete();
	public void Hit();
	public boolean getHit();
	public void draw(Graphics2D g);
	public int getX();
	public int getY();
	public int getW();
	public int getH();
	public int getDirection();
	public void setDirection(int dir);
	public void resetSprite();
	public int getType();
	
}
