package game.entities.areas;
import game.SoundHandler;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.GameObject;
import game.entities.players.AIPlayer;
import game.entities.players.Player;

import java.awt.*;
import java.util.*;

import static game.Level.getGameObjects;
import static game.Level.getPlayers;

import static game.Utility.getRandomElementInRangeSeeded;
import static game.Utility.getRandomIntInRangeSeeded;
import static game.Utility.getRandomFloatInRangeSeeded;

public class MindlessAI extends AnimArea implements GravityObject, CollidingObject, SolidCollider {

	private float velX = 0;
	private float velY = 0;
	private float terminalVelY = 15;
	private int i = 0;
	private int speed = 4;
	private int sum = 0;
	private int[] bruh = new int[]{-1,1};
	private int yuh=1;
	private int iMax;
	private boolean bouncing=false;
	private int bouncingSpeed = 0;
	private int bouncingTimer=0;
	private boolean bounceImmunity = false;
	private int bi = 0;
	private int idleIndex=1;
	private int idleSum = 0;
	private float originalX;
	private int minRange,maxRange;

	public MindlessAI(float x, float y, int width, int height,int minRange, int maxRange, String url) {
		super(x, y, width, height,url);
		this.minRange=minRange;
		this.maxRange=maxRange;
		originalX=x;
	}

	/**
	 * This function is responsible for the movement of the mindless ai.
	 * It also triggers any necessary effects when the player collides with it.
	 */
	public void tick() {
		//Gather all collisions
		CollidingObject.getCollisions(this);

		for (Player p : getPlayers())
		{
			if (this.getInteraction(p))
				if (!(p.isBounceImmune())) {
					p.bounce(this.getSpeed());
					
					if (!(p instanceof AIPlayer))
					SoundHandler.playRandomChicken();
					}
		}


		for (GameObject o : getGameObjects())
		{
			if (o !=  this)
				if (o instanceof MindlessAI)
				{
					if (((MindlessAI) o).getInteraction(this))
					{
						if (!this.isBounceImmune())
							this.bouncing(((MindlessAI) o).getSpeed());
					}
				}

			if (o instanceof EventDamageZone)
			{
				for (Area xd: ((EventDamageZone) o).getEventArea() )
					if (xd.getInteraction(this)) {
						((EventDamageZone) o).setTriggered(true);
					}
				if (((EventDamageZone) o).getActive())
					if (((EventDamageZone) o).getInteraction(this))
						this.bouncing(this.getSpeed());

			}

			if (o instanceof Player)
			{
				if (this.getInteraction(o))
					if (!(((Player)o).isBounceImmune()))
						((Player)o).bounce(this.getSpeed());
			}

		}


		if (bi<15) {
			bi++;
		}else {
			bounceImmunity=false;
		}

		idleSum++;
		this.x = this.x + getRandomFloatInRangeSeeded(0.1f,1.0f) * idleIndex;

		if (idleSum>=getRandomIntInRangeSeeded(10,40))
		{
			idleIndex=idleIndex *(-1);
			idleSum=0;
		}

		if (this.x < originalX - minRange)
			this.x = this.x + 5;

		if (this.x > originalX + maxRange)
			this.x = this.x - 5;

		if (i<iMax)
			i++;
		else
		{
				this.x = this.x + speed;
				sum = sum + speed;



			if (sum*yuh>=getRandomIntInRangeSeeded(20,200))
			{
				sum=0;
				i=0;
				iMax=getRandomIntInRangeSeeded(30,100);
				speed= getRandomIntInRangeSeeded(3,7);
				yuh = getRandomElementInRangeSeeded(bruh);
				speed = speed * yuh;
			}
		}

		//If you're not on ground, you should fall
		if(!isOnGround()) {
			fall(this);
		}

		//Move player if it will not cause a collision
		if(!SolidCollider.willCauseSolidCollision(this, this.velX+1, true)) {
			this.x += velX;
		}
		if(!SolidCollider.willCauseSolidCollision(this, this.velY+1, false)) {
			this.y += this.velY;
		} else {
			// Stop player falling through the floor
			CollidingObject o = SolidCollider.nextCollision(this,  this.velY, false);
			if(o != null) {
				Rectangle s = o.getBounds();

				if(this.velY > 0 && !isOnWall()) {
					this.y = s.y - this.height;
					this.velY = 0;
				} else if(this.velY < 0 && !isOnWall()) {
					this.velY = 0;
				} else {	// When velY == 0 and velX == 0 the sticking to the wall bug occurs.
					// Rebounds the player off the wall to avoid sticking.
					if (SolidCollider.willCauseSolidCollision(this, 5, true)) {
						this.velX = -2.0f;
					} else if (SolidCollider.willCauseSolidCollision(this, -5, true)) {
						this.velX = 2.0f;
					}
				}
			}
		}


		if (bouncing)
		{
			this.x = this.x + (bouncingSpeed);
			if (bouncingSpeed<0)
				this.y = this.y - bouncingSpeed*(-1);
			else
				this.y = this.y - bouncingSpeed;

			bouncingTimer++;
			if(bouncingTimer>=10) {
				bouncing = false;
				bouncingTimer=0;
			}
		}



	}

	public void bouncing(int speed){
		this.bouncing = true;
		bouncingSpeed= speed *(-1);
		bounceImmunity=true;
		bi=0;
	}


	private boolean isOnGround() {
		return SolidCollider.willCauseSolidCollision(this, 5, false);
	}

	private boolean isOnWall() {
		if ((SolidCollider.willCauseSolidCollision(this, this.velX, true) || SolidCollider.willCauseSolidCollision(this, -this.velX, true))
				&& !isOnGround()){
			return true;
		} else {
			return false;
		}
	}

	public void handleCollisions(LinkedList<CollidingObject> collisions) {
	}

	public void setVelY(float velY) {
		this.velY = velY;
	}

	public float getVelY() {
		return this.velY;
	}

	public float getTerminalVel() {
		return this.terminalVelY;
	}


	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, width, height);
	}


	public int getSpeed() {
		return speed;
	}

	public int getYuh() {
		return yuh;
	}
	public boolean isBounceImmune() {
		return bounceImmunity;
	}
}
