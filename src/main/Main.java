package main;

import java.awt.Rectangle;

import javax.swing.JFrame;

import tools.GameDetails;

public class Main {

	public static void main(String [] args) {
		
		JFrame frame = new JFrame("Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setVisible(true);
		frame.add(new GamePanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		
	}
	
}
