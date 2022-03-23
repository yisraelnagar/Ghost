package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import tools.GameDetails;

public class GamePanel extends JPanel implements Runnable, KeyListener{

	Thread thread;
	
	Dimension screenSize;
	Image image;

	GameStateManager gsm;
	boolean exiting;
	
	public GamePanel() {
		

		super();
		exiting = false;
		screenSize = new Dimension(GameDetails.screenW,GameDetails.screenH);
		thread = new Thread(this);
		
		this.setBackground(Color.BLACK);
		this.setPreferredSize(screenSize);
		this.gsm = new GameStateManager();
	}
	
	public void addNotify() {
		
		super.addNotify();
		thread.start();
		
	}
	
	public void init() {
		
		addKeyListener(this);
		this.setFocusable(true);
		this.requestFocus();
		gsm.init();
		
	}
	
	
	public void run() {
		init();
		long start = System.currentTimeMillis();
		long dif = System.nanoTime();
		int fps = 0;
		long frameSize = (long)(1000000000/GameDetails.FPS);
		while(true) {
		
			if(System.nanoTime() - dif >= frameSize) {
				dif = System.nanoTime();
				update();
				repaint();
				fps++;
			}
			
			if(start+1000 <= System.currentTimeMillis()) {
				
				System.out.println(fps);
				fps = 0;
				start = System.currentTimeMillis();
				
			}
		
			
		}
			
		}
		
	
	
	
	public void update() {
		
		if(!exiting)
		gsm.update();
	
	
	
	
	}
	public void draw(Graphics2D g) {
	
		gsm.draw(g);
		
		if(exiting) {
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Are You Sure?", GameDetails.screenW/2 - (int)(130*GameDetails.SIZE_MULTIPLY), GameDetails.screenH/2);
		}
		g.finalize();
		
	}
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		draw((Graphics2D) g);

	}

	@Override
	public void keyPressed(KeyEvent k) {
		// TODO Auto-generated method stub
		if(!exiting)
			gsm.keyP(k.getKeyCode());
		if(k.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(!exiting)
				exiting = true;
			else
				exiting = false;
		}
		else if(k.getKeyCode() == KeyEvent.VK_ENTER)
			if(exiting)
				System.exit(0);
	}

	@Override
	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		gsm.keyR(k.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent k) {
		// TODO Auto-generated method stub
		gsm.keyT(k.getKeyCode());
	}
	
}
