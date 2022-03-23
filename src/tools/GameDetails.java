package tools;

public class GameDetails {

	public static int screenW = 1280;
	public static int screenH = 720;
	public static double FPS = 80;
	public static double fpsRatio = 78/FPS;
	public static int playerW = (int) ((screenW/16)/1.45);
	public static int playerH = (int) (playerW*1.4);
	public static int TILE_SIZE = playerH/2-2;
	public static int worldX = 0;
	public static int worldY = 0;
	public static double SIZE_MULTIPLY = screenW/948;
	
}
