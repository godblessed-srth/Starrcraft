import java.awt.event.*;

public class PlayerPhysics {
	private int hp = 20;
	private int hunger = 20;
	private double x = 128, y = 8, z = 128;
	private double groundY = 8;
	private boolean forward, backward, left, right, jump;
	private boolean moveX, moveZ;
	private boolean isGround = false;
	private byte[][][] blocks;
	//constants
	private static final int MAX_HP = 20;
	private static final int MAX_HUNGER = 20;
	private static final double SPEED = 0.05;
	private static final double JUMP = 0.4;
    private static final int WORLD = 256;
    private static final int WORLD_HEIGHT = 16;
    public static final double SIZE = 0.75;
    private static final double GRAVITY = 0.05;
    private double vy = 0.0; 
	//HP
	public int getHp() { return hp; }
	public void setHp(int hp) { this.hp = Math.max(0, Math.min(MAX_HP, hp)); }
	//hunger
	public int getHunger() { return hunger; }
	public void setHunger(int hunger) { this.hunger = Math.max(0, Math.min(MAX_HUNGER, hunger)); }
	//x
	public double getX() { return x; }
	public void setX(double x) { this.x = Math.max(0, Math.min(WORLD - 1, x)); }
	//y
	public double getY() { return y; }
	public void setY(double y) { this.y = Math.max(0, Math.min(WORLD_HEIGHT - 1, y)); }
	//z
	public double getZ() { return z; }
	public void setZ(double z) { this.z = Math.max(0, Math.min(WORLD - 1, z)); }
	//moving
	public void press(int key, boolean pressed) {
		switch (key) {
			case KeyEvent.VK_W: forward = pressed; break;
			case KeyEvent.VK_A: left = pressed; break;
			case KeyEvent.VK_S: backward = pressed; break;
			case KeyEvent.VK_D: right = pressed; break;
			case KeyEvent.VK_SPACE: jump = pressed; break;
		}
	}
	//update method
	public void update() {
		moveX = left || right;
		moveZ = forward || backward;
		
		if (moveX && !moveZ) {
			if (left) x -= SPEED;
			if (right) x += SPEED;
		} else if (moveZ && !moveX) {
			if (forward) z -= SPEED;
			if (backward) z += SPEED;
		}
		
		if (jump) y += JUMP; jump = false;
		
		x = Math.max(0, Math.min(WORLD - 1, x));
		z = Math.max(0, Math.min(WORLD - 1, z));
		y = Math.max(0, Math.min(WORLD_HEIGHT - 1, y));
	}
}