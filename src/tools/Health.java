package tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Health {

	private final double MAX_HEALTH;
	private double currHealth;
	private int damage;
	BufferedImage healthBar;
	private final int healthDownSpeed;
	private final double w,h;
	private final int yDiff;
	
	public Health(double MAX_HEALTH, double currHealth) {
		
		yDiff = (int)(10*GameDetails.SIZE_MULTIPLY);
		w = (int)(50*GameDetails.SIZE_MULTIPLY);
		h = (int)(5*GameDetails.SIZE_MULTIPLY);
		
		healthDownSpeed = 3;
		
		this.MAX_HEALTH = MAX_HEALTH;
		this.currHealth = currHealth;
		this.damage = 0;
		
		try {
			healthBar = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\Misc\\HealthBar.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void takeDamage(int d) {
		
		damage += d;
		
	}

	public void update() {
		
		
			if(damage>0) {
				damage-=(int)(healthDownSpeed*GameDetails.fpsRatio);
				currHealth-=(int)(healthDownSpeed*GameDetails.fpsRatio);
			}
			
			if(currHealth < 0) {
				currHealth = 0;
				damage = 0;
			}
			if(damage < 0)
				damage = 0;
			
		
	}
	
	public double getHealth() {return currHealth;}
	public void draw(Graphics2D g, int x, int y) {
			
			float num1 = (float) ((currHealth/MAX_HEALTH) + ((1-(currHealth/MAX_HEALTH))/2));
			float num2 = (1-(float)(currHealth/MAX_HEALTH))/4;
			
			g.setColor(new Color(num1, 0, num2, 0.8f));
			g.fillRect(x, y - yDiff,(int)(this.w*(currHealth/MAX_HEALTH)),(int) this.h);
			g.setColor(Color.black);
			g.drawRect(x, y - yDiff, (int)this.w, (int)this.h);
			
			if(damage > 0) {
				g.setColor(Color.YELLOW);
				g.fillRect(x + (int)(this.w*(currHealth/MAX_HEALTH)), y - yDiff, 1, (int)this.h);
			}
	}
	public void draw(Graphics2D g, int x, int y, int w) {
		
		float num1 = (float) ((currHealth/MAX_HEALTH) + ((1-(currHealth/MAX_HEALTH))/2));
		float num2 = (1-(float)(currHealth/MAX_HEALTH))/4;
	
		g.setColor(new Color(num1, 0, num2, 0.8f));
		g.fillRect(x, y - yDiff,(int)(w*(currHealth/MAX_HEALTH)),(int) this.h);
		g.setColor(Color.black);
		g.drawRect(x, y - yDiff, (int)w, (int)this.h);
		if(damage > 0) {
			g.setColor(Color.YELLOW);
			g.fillRect(x + (int)(this.w*(currHealth/MAX_HEALTH)), y - yDiff, 1, (int)this.h);
		}

	}
		
	
	
}
