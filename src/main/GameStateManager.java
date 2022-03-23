package main;

import java.awt.Graphics2D;

import level.Level;

public class GameStateManager {
	
	Level level;
	
	public GameStateManager() {
		
		level = new Level(1);
		
	}
	
	
	public void init() {	level.init();	}
	public void update() {level.update();}
	public void draw(Graphics2D g) {level.draw(g);}
	public void keyP(int k) {level.keyP(k);}
	public void keyR(int k) {level.keyR(k);}
	public void keyT(int k) {level.keyT(k);}

}
