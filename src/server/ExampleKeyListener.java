package server;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ExampleKeyListener extends GameObject implements KeyListener, GraphicalObject, CollidingObject{
	
	private int velX, velY;
	private static BufferedImage sprite;
	private static String url = "./img/Anglerfish.png";
	
	
	public ExampleKeyListener(float x, float y, int z) {
		super(x, y, z, 64, 32);
		this.velX = 0;
		this.velY = 0;
		sprite = this.loadImage(ExampleKeyListener.url);
		CollidingObject.addCollider(this);
		Listeners.addKeyListener(this);
		Game.handler.addObject(this);
	}

	public void tick() {
		move();
		CollidingObject.getCollisions(this);
		
	}
	
	private void move() {
		if(!SolidCollider.willCauseSolidCollision(this, this.velX, true)) {
			this.x += (this.velX);
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY, false)) {
			this.y += (this.velY);
		}
	}

	public void render(Graphics g) {
		this.drawSprite(g, sprite, (int)this.x, (int)this.y);
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_D) {
			this.velX = 3;
		}else if(key == KeyEvent.VK_A) {
			this.velX = -3;
		}
		
		if(key == KeyEvent.VK_S) {
			this.velY = 3;
		}else if(key == KeyEvent.VK_W) {
			this.velY = -3;
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
			this.velX = 0;
		}
		
		if(key == KeyEvent.VK_S || key == KeyEvent.VK_W) {
			this.velY = 0;
		}
	}

	@Override
	public void handleCollisions(LinkedList<CollidingObject> collisions) {
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)this.x, (int)this.y, this.width, this.height);
	}

}
