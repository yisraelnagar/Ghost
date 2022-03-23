package tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteMovement{

	int row,rowX,coll,collX,width,height,pointX,pointY, timer, timerX;
	private final int finalSprite;
	BufferedImage img;
	
	public SpriteMovement(BufferedImage img,int coll,int row, int pointX, int pointY,int width,int height,int timer){
		
		finalSprite = -1;
		this.row = row;
		this.coll = coll;
		this.width = width;
		this.height = height;
		this.pointX = ((pointX)*width) - width;
		this.pointY = ((pointY)*height) - height;
		this.timer = timer;
		timerX = 0;
		rowX=0;
		collX = 0;

			this.img = img;	
		
	}
	
public SpriteMovement(BufferedImage img,int coll,int row, int pointX, int pointY,int width,int height,int timer, int finalSprite){
		
		this.finalSprite = finalSprite-1;
		this.row = row;
		this.coll = coll;
		this.width = width;
		this.height = height;
		this.pointX = ((pointX)*width) - width;
		this.pointY = ((pointY)*height) - height;
		this.timer = timer;
		timerX = 0;
		rowX=0;
		collX = 0;
		this.img = img;
			
		
	}
	
	
	public void draw(Graphics2D g, int x, int y, int w, int h) {
		
		if(collX != finalSprite) {
		timerX++;
		if(timerX >= timer) {
			timerX = 0;
			collX++;
			if(collX >= coll) {
				collX = 0;
				rowX++;
				if(rowX >= row)
					rowX = 0;
			}	
		}
		}
		
		
		g.drawImage(img, x, y, x+w, y+h,
		pointX+(width*collX),//X of image
		pointY+(height*rowX),// Y of image
		pointX+(width*collX)+width,//Width of image
		pointY+(height*rowX)+height,//Height of image
		null);	
	}

	public int getcurrRow() {return rowX;}
	public int getcurrColl() {return collX;}
	public void setRow(int row) {rowX = row; timerX = 0;}
	public void setColl(int coll) {collX = coll; timerX = 0;}
	public boolean lastSprite() {
		
		if(collX == finalSprite)
			return true;
		return false;}
	
}
