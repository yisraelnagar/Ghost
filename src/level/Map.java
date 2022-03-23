package level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import enemies.Enemies;
import enemies.EnemyInt;
import enemies.EnemyRigidBody;
import enemies.Flyke;
import tools.Collision;
import tools.GameDetails;

public class Map {

	//The Map Tiles
	Tile[][] tiles;
	
	//The Level
	private final int NUM_LEVEL;
	private final int W,H,tileSize;
	private Level level;
	private final int TILE_SIZE;
	private final int colls, rows;
	LinkedList<Collision> colliders;
	Enemies enemies;
	int numTiles;
	int  tss = 8; //tileSpriteSize
	private final int FLYKE_NUMBER;
	
	//The Map Images
	BufferedImage colorMap, tileSprite, backGround;
	
	public Map(int LevelNum, Level level) {
		this.level = level;
		NUM_LEVEL = LevelNum;
		TILE_SIZE = GameDetails.TILE_SIZE;
		
		//Getting Game Details
				W= GameDetails.screenW;
				H= GameDetails.screenH;
				tileSize = GameDetails.TILE_SIZE;
				
		
		colliders = new LinkedList<Collision>();
		
		//Setting number of colls
		try {
			colorMap = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\ColorMaps\\level" +NUM_LEVEL+".png"));
			tileSprite = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\ColorMaps\\GroundTiles.png"));
			backGround = ImageIO.read(new File(System.getProperty("user.dir") + "\\Images\\BackGrounds\\level" + LevelNum + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		colls = colorMap.getWidth();
		rows = colorMap.getHeight();

		enemies = new Enemies(level);
		createTiles(LevelNum);
		numTiles = 0;
		FLYKE_NUMBER = 0;
	}
	
	
	
	
	private void createTiles(int num) {
		//Getting Game Details
		int W= GameDetails.screenW;
		int H= GameDetails.screenH;
		int tileSize = GameDetails.TILE_SIZE;
		tiles = new Tile[rows][colls];
		
		for(int row = 0; row < rows; row++) {
			for(int coll = 0; coll < colls; coll++) {
				
				//Use To Find More Color Tiles
				
				//if(colorMap.getRGB(coll, row) != 0)
					//System.out.println(colorMap.getRGB(coll, row));
					
				//Asuming Theres A tile to add collision to
				boolean tileExists = true;
				
				//Creating Tiles
				switch (colorMap.getRGB(coll, row)) {
				
					case -9229824: tiles[row][coll] = new Tile(new Random().nextInt(3)); createTile(coll,row);
					break;
					case -16091136: tiles[row][coll] = new Tile(new Random().nextInt(3)+3);createTile(coll,row);
					break;
					case -5804467: tiles[row][coll] = new Tile(new Random().nextInt(3));
					break;
					case -2877152: createEnemyBounds(new Flyke(this.level),colorMap, row+1, coll);tileExists = false;
					break;
					case 0:tiles[row][coll] = null; tileExists = false;
					break;
						
				}
				
				
							
			
			}
		
		}
		
		
		
	}
	
	public void createTile(int coll, int row) {
		
		int x = coll*tileSize - GameDetails.worldX;
		int y = H - ((rows - row)*tileSize);
		int w = tileSize;
		int h = tileSize;
		
		//Creating Colliders
		colliders.add(new Collision(x,y,w,h));
		numTiles++;
		
		
	}
	
	public void drawBackground(Graphics2D g) {g.drawImage(backGround, 0, 0, GameDetails.screenW, GameDetails.screenH,null);
	}
	
	public void draw(Graphics2D g) {
		
		//Counter For Collision Creation
		int count = 0;
		
		int worldX = GameDetails.worldX;
		int worldY = GameDetails.worldY;
		
		//Loop Of Drawing Tiles
		for(int row = 0; row < rows; row++) {
			for(int coll = 0; coll < colls; coll++) {	
				if(tiles[row][coll] != null) {
					
					//Setting The Dimensions Of The Tile
					int type = tiles[row][coll].type;
					int x = coll*tileSize - worldX;
					int y = H - ((rows - row)*tileSize)- worldY;
					int w = x + tileSize;
					int h = y + tileSize;
					int dx = (type%6)*tss + 1;
					int dy = (type/6)*tss + 1;
					int dw = dx + tss - 2;
					int dh = dy +tss - 2;
					
				
					//Drawing The Tile
					g.drawImage(tileSprite, x, y,w,h,dx,dy,dw,dh,null);
					count++;
				}
				
			}
		}
		
	}
	
	private void createEnemyBounds(EnemyInt enem, BufferedImage map, int row, int coll) {
		
		//getting enemy height in tiles
		int tileH = enem.getH()/
		GameDetails.TILE_SIZE + 
		((enem.getH()%GameDetails.TILE_SIZE)/(enem.getH()%GameDetails.TILE_SIZE));
		
		
		
		if(map.getRGB(coll,row) != 0) {
	
		boolean right = false;
		boolean left = false;
		int moneRight = 0;
		int moneLeft = 0;
	
			while(!right || !left) {
				//Mone Of While loop
				
				//Checking Tile To The Right
				if(!right) {
					
					moneRight++;
					if(coll+moneRight< map.getWidth() && map.getRGB(coll+moneRight,row) != 0&& map.getRGB(coll+moneRight,row) != -2877152) {
				
						for(int i = 0; i <  tileH; i++) {
							if(map.getRGB(coll+moneRight,((row)-(i+1))) != 0) {
								if(map.getRGB( coll+moneRight,((row)-(i+1))) != -2877152) {
									right = true;	
									moneRight--;
								}
							}
						}
					}
				
					else {
						
						right = true;	
						moneRight--;
				
					}
				}
				
				if(!left) {
					moneLeft++;
					if(coll-moneLeft>=0 && map.getRGB(coll-moneLeft,row) != 0 && map.getRGB(coll-moneLeft,row) != -2877152) {
					
						
						for(int i = 0; i <  tileH; i++) {
							if(map.getRGB( coll-moneLeft,((row)-(i+1))) != 0) {
								if(map.getRGB( coll-moneLeft,((row)-(i+1))) != -2877152) {
								left = true;	
								moneLeft--;
								}
							}
						}
					}
					
					else {

						left = true;	
						moneLeft--;
					}
				}
						
				
				}
			

			int x = coll*GameDetails.TILE_SIZE - GameDetails.worldX;
			int y = GameDetails.screenH - ((rows - (row - tileH))*GameDetails.TILE_SIZE) + (GameDetails.TILE_SIZE*tileH)-enem.getH();
			enemies.addEnemy(new EnemyRigidBody(enem, x,y, moneRight,moneLeft));
		}
		else {
			
		}
		
			}
	
	public void setWorldX() {
		
		for(int i = 0; i < colliders.size(); i++) 
			this.colliders.get(i).setWorldX();
		
		
	}
	public void setWorldY() {
		
		for(int i = 0; i < colliders.size(); i++) 
			this.colliders.get(i).setWorldY();
		
	}
	
	public int getRows() {return rows;}
	
	public int getColls() {return colls;}

	public Tile[][] getTiles(){return tiles;}
	
	public Enemies getEnemies(){return enemies;}
	
	public LinkedList<Collision> getColliders(){return colliders;}
}
