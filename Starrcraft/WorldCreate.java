import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WorldCreate extends JPanel {
	private PlayerPhysics player;
    private static final int BLOCK_SIZE = 48;
    private byte[][][] blocks;
    private long seed;
    private Timer loop;
    //info panels
    private static final int GAP = Main.GAP;
    private static int WIDTH = Main.WIDTH, HEIGHT = Main.HEIGHT;
    private static final int SCALE = 150;
    private static final Font UI_FONT = new Font("Serif", Font.PLAIN, 26);
    private static final String CORD_FORMAT = "%.1f";
    //initialize a blocks
    public WorldCreate(long seed) {
    	this.seed = seed;
        blocks = new byte[256][16][256];
        player = new PlayerPhysics();
        generateWorld();
        gameLoop();
        //bg
        setBackground(Color.CYAN);
        setFocusable(true);
        //keys
        addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyPressed(KeyEvent e) {
        		int key = e.getKeyCode();
        		switch (key) {
        			case KeyEvent.VK_ESCAPE: System.exit(0); break;
        			default: player.press(key, true);
        		}
        	}
        	@Override
        	public void keyReleased(KeyEvent e) {
        		int key = e.getKeyCode();
        		player.press(key, false);
        	}
        });
    }
    //blocks ID
    private static final byte AIR = 0;
    private static final byte GRASS = 1, DIRT = 2;
    private static final byte STONE = 3;
    private static final byte WATER = 4;
    private static final byte BEDROCK = 5;
    //block colors
    private static final Color AIR_COL = new Color(0, 0, 0, 0);
    private static final Color GRASS_COL = new Color(7, 240, 82);
    private static final Color WATER_COL = new Color(44, 152, 230);
    private static final Color DIRT_COL = new Color(54, 35, 27);
    private static final Color STONE_COL = new Color(90, 94, 97);
    private static final Color BEDROCK_COL = new Color(40, 40, 40);
    //colors array
    private static final Color[] BLOCK_COLS = {
    	AIR_COL,
    	GRASS_COL,
    	DIRT_COL,
    	STONE_COL,
    	WATER_COL,
    	BEDROCK_COL
    };
    //paintComponent
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D g2d = (Graphics2D) g;
    	//painting methods
    	drawWorld(g2d);
    	drawPlayer(g2d);
    	initTop(g2d);
    	initBottom(g2d);
    }
    //generate method
    private void generateWorld() {
    	for (int x = 0; x < 256; x++) {
    		for (int z = 0; z < 256; z++) {
    			blocks[x][0][z] = BEDROCK;
    			//stone
    			for (int y = 1; y <= 4; y++) { blocks[x][y][z] = STONE; }
    			//dirt
    			for (int y = 5; y <= 7; y++) { blocks[x][y][z] = DIRT; }
    			//grass
    			blocks[x][8][z] = GRASS;
    			//air
    			for (int y = 9; y < 16; y++) { blocks[x][y][z] = AIR; }
    		}
    	}
    }
    //gameloop
    private void gameLoop() {
    	loop = new Timer(16, e -> {
    		player.update();
    		repaint();
    	});
    	loop.start();
    }
    //world painting
    private void drawWorld(Graphics2D g2d) {
    	double plrX = player.getX(), plrZ = player.getZ(); 
    	int plrY = (int)player.getY();
    			
    	int screenWid = getWidth(), screenHei = getHeight();
    	double blocksWide = (double)screenWid / BLOCK_SIZE + 2;
    	double blocksHigh = (double)screenHei / BLOCK_SIZE + 2;
    	double startX = plrX - blocksWide / 3;
    	double startZ = plrZ - blocksHigh / 3;
    	
    	for (int x = (int)startX; x < startX + blocksWide; x++) {
    		for (int z = (int)startZ; z < startZ + blocksHigh; z++) {
    			if (x < 0 || x >= 256 || z < 0 || z >= 256) continue;
    			
    			byte block = blocks[x][plrY][z];
    			if (block == AIR) continue;
    			
    			int screenX = (int)((x - startX) * BLOCK_SIZE);
    			int screenY = (int)((z - startZ) * BLOCK_SIZE);
    			 
    			g2d.setColor(BLOCK_COLS[block]);
    			g2d.fillRect(screenX, screenY, BLOCK_SIZE, BLOCK_SIZE);
    			g2d.setColor(Color.BLACK);
    			g2d.drawRect(screenX, screenY, BLOCK_SIZE, BLOCK_SIZE);
    		}
    	}
    }
    //draw player
    private void drawPlayer(Graphics2D g2d) {
    	double plrX = player.getX(), plrZ = player.getZ(); 
    	
    	int screenWid = getWidth(), screenHei = getHeight();
    	double blocksWide = (double)screenWid / BLOCK_SIZE + 2;
    	double blocksHigh = (double)screenHei / BLOCK_SIZE + 2;
    	double startX = plrX - blocksWide / 3;
    	double startZ = plrZ - blocksHigh / 3;
    	
    	int screenX = (int)((plrX - startX) * BLOCK_SIZE);
		int screenY = (int)((plrZ - startZ) * BLOCK_SIZE);
		
		int size = (int)(BLOCK_SIZE * player.SIZE);
		int offset = (BLOCK_SIZE - size) / 2;
		
		g2d.setColor(Color.BLACK);
		g2d.fillRect(screenX + offset, screenY + offset, size, size);
		g2d.setColor(Color.WHITE);
		g2d.drawRect(screenX + offset, screenY + offset, size, size);
    }
    //top info
    private void initTop(Graphics2D g2d) {
    	g2d.setColor(Color.BLACK);
    	g2d.fillOval(GAP, GAP, SCALE, SCALE);
    	
    	int textX = (WIDTH - SCALE) - (SCALE / 3);
    	
    	//info about player
    	g2d.setColor(Color.WHITE);
    	g2d.setFont(UI_FONT);
    	g2d.drawString("HP: " + player.getHp() + "/20", textX, GAP * 3);
    	g2d.drawString("Hunger: " + player.getHunger() + "/20", textX, GAP * 5);
    }
    //bottom info
    private void initBottom(Graphics2D g2d) {
    	String text = String.format("X: " + CORD_FORMAT + " Y: " + CORD_FORMAT + " Z: " + CORD_FORMAT,
    		player.getX(), player.getY(), player.getZ()
    	);
    	int textY = (HEIGHT - GAP * 5) + 5;
    	//player x, y, z
    	g2d.setColor(Color.WHITE);
    	g2d.setFont(UI_FONT);
    	g2d.drawString(text, GAP, textY);
    }
}
