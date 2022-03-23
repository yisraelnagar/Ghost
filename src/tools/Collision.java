package tools;

import java.awt.Rectangle;

public class Collision {

	private int x,y,w,h,worldX, worldY;

	private final int RIGHT,LEFT;
	
	public Collision(int x, int y, int w,int h) {
		RIGHT = 1;
		LEFT = -1;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		worldX = GameDetails.worldX;
		worldY = GameDetails.worldY;
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
		worldX = GameDetails.worldX;
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
	
	public void setWorldX() {
		
		if(GameDetails.worldX != worldX) {
			
			x += worldX - GameDetails.worldX;
			worldX = GameDetails.worldX;
			
		}
		
	}
	
public void setWorldY() {
		
		if(GameDetails.worldY != worldY) {
			
			y += worldY - GameDetails.worldY;
			worldY = GameDetails.worldY;
			
		}
		
	}
	
	public int collidingX(Collision coll) {
	
		int x2 = coll.getX();
		int w2 = coll.getW();
		//If The Objects R Colliding
		if(x + w > x2 && x2 + w2 > x) {
			
			//If The First Object Is Touching From Its Right or Bottom Side
			if(x < x2)
				return -((x+w)-x2);
			//If The First Object Is Touching From Its Left or Top Side
			else
				return (x2 + w2) - x;
		}
		
		return 0;
		
	}
	
	public int collidingY(Collision coll) {
		
		int y2 = coll.getY();
		int h2 = coll.getH();
		//If The Objects R Colliding
		if(y + h > y2 && y2 + h2 > y) {
			
			//If The First Object Is Touching From Its Right or Bottom Side
			if(y < y2)
				return -((y+h) - y2);
			//If The First Object Is Touching From Its Left or Top Side
			else
				return (y2+h2)-y;
		}
		
		return 0;
		
		
	}
	

	public boolean colliding(Collision coll) {
		
		
		Rectangle rect1 = new Rectangle(x,y,w,h);
		Rectangle rect2 = new Rectangle(coll.x,coll.y,coll.w,coll.h);
		
		if(rect1.intersects(rect2))
			return true;
		
		return false;
		
	}
	
	public boolean onScreen() {
		
		if(x + w >= 0 && x <= GameDetails.screenW)
			return true;
		
		return false;
		
	}
}
