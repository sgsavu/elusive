import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class ExampleKeyListener extends JumpingCharacter {
	
	private static BufferedImage sprite;
	private static String url = "./img/player1.png";

	public ExampleKeyListener(float x, float y, int z) {
		super(x, y, z);
        sprite = this.loadImage(ExampleKeyListener.url);
	}

    @Override
	public void render(Graphics g) {
		this.drawSprite(g, sprite, (int)this.x, (int)this.y);
	}

    // This example key listener uses WASD to move.
    // Controls can be adjusted.
    @Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_D) {
			this.velX = DEFAULT_X_VELOCITY;
		} else if (key == KeyEvent.VK_A) {
			this.velX = -DEFAULT_X_VELOCITY;
		} else if (key == KeyEvent.VK_S) {
			this.velY = 10;
		} else if (key == KeyEvent.VK_W) {
			if (cd==false)
			{
				this.velY = JUMP_GRAVITY;
				cd = true;
			}
		}
	}

    @Override
	public void keyReleased(KeyEvent e) { 	/* TODO fix this so that when both A and D are held 
											and one is released the character doesn't stop */
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_D || key == KeyEvent.VK_A) {
			this.velX = 0;
		}		
	}
}
