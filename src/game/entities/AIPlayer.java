package game.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import game.Game;
import game.attributes.AnimatedObject;
import game.attributes.CollidingObject;
import game.attributes.GravityObject;
import game.attributes.SolidCollider;
import game.entities.areas.RespawnPoint;
import game.entities.platforms.MovingPlatform;
import game.graphics.Animation;
import game.graphics.AnimationStates;
import game.graphics.Assets;
import game.input.KeyInput;
import game.network.packets.Packet02Move;

import javax.imageio.ImageIO;

public class AIPlayer extends Player{

	static BufferedImage sprite;

	private float terminalVelY = 15;

	private static final float DECELERATION = 0.4f; 	 	// Rate at which velX decreases when A/D key released (for sliding)
	private static final float JUMP_GRAVITY = -7.5f; 	// VelY changes to this number upon jump
	private static final float RUN_SPEED = 3.6f; 		// Default run speed

    public String direction = "N"; // or private?
    public String jump = "N";
    public float wait = 600;
	public Player humanPlayer;
	public float dist_from_player;
	public RespawnPoint penultimateRespawnPoint;

	public LinkedList<RespawnPoint> visitedRespawnPoints = new LinkedList<>();


	private int max_distance_between_players = 550;

	private static AnimationStates defaultAnimationState = AnimationStates.IDLE;
	private static AnimationStates currentAnimationState = defaultAnimationState;
	private static HashMap<AnimationStates, Animation> animations = new HashMap<AnimationStates, Animation>();
	private Assets s = new Assets();
	private String username;


	public AIPlayer(float x, float y, int width,int height, Player humanPlayer) {
		super(x, y, null, width, height);

		
		this.username = UUID.randomUUID().toString();;
		this.humanPlayer = humanPlayer;
	}

	public void tick() {


		super.tick();

		dist_from_player = this.x - this.humanPlayer.x;
		// teleports AI Player to the penultimate RespawnPoint that the player has reached 
		if (dist_from_player < -max_distance_between_players) {

			if (humanPlayer.getRespawnPoints().size() > 2){

				// first get the penultimate one
				penultimateRespawnPoint = humanPlayer.getRespawnPoints().get(humanPlayer.getRespawnPoints().size()-2);

				// so that it only teleports once, and doesn't keep getting sent back
				if (!this.visitedRespawnPoints.contains(penultimateRespawnPoint)) {

					// then set AIPlayer x to the value of that RP
					this.x = penultimateRespawnPoint.x;
					this.y = penultimateRespawnPoint.y-40;

				}

			}

		}




		if (dist_from_player < max_distance_between_players) {

			
			if (this.wait > 0) {

				this.wait--;

				if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
					if (this.velX >= -0.1f && this.velX <= 0.1f) {
						this.velX = 0;
						currentAnimationState = AnimationStates.IDLE;
					} else if (this.velX > 0.1f) {
						this.velX -= DECELERATION;
					} else {
						this.velX += DECELERATION;
					}
				} else {
					this.velX = 0;
					currentAnimationState = AnimationStates.IDLE;
				}

			}
			else {
				if(this.direction.equals("R")) {

				/* Beware: Java floating point representation makes it difficult to have perfect numbers
				( e.g. 3.6f - 0.2f = 3.3999999 instead of 3.4 ) so this code allows some leeway for values. */

						// Simulates acceleration when running right
						if (this.velX >= RUN_SPEED){
							this.velX = RUN_SPEED;
						} else {
							this.velX += RUN_SPEED/6;
						}
						currentAnimationState = AnimationStates.RIGHT;

				} else if(this.direction.equals("L")) { 

						// Simulates acceleration when running left
						if (this.velX <= -RUN_SPEED){
							this.velX = -RUN_SPEED;
						} else {
							this.velX -= RUN_SPEED/6;
						}
						currentAnimationState = AnimationStates.LEFT;

				} else { 
					// For deceleration effect
					if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
						if (this.velX >= -0.1f && this.velX <= 0.1f) {
							this.velX = 0;
							currentAnimationState = AnimationStates.IDLE;
						} else if (this.velX > 0.1f) {
							this.velX -= DECELERATION;
						} else {
							this.velX += DECELERATION;
						}
					} else {
						this.velX = 0;
						currentAnimationState = AnimationStates.IDLE;
					}
				}


				if(jump.equals("Y")) {
					if(isOnGround() && !hasCeilingAbove() && !isOnWall()) {
						this.velY = JUMP_GRAVITY;
					}
					this.jump = "N";
				}
			}

		}


		/*
		
		if(Game.isMultiplayer) {
			if((int)this.x != this.prevPos.x || (int)this.y != this.prevPos.y) {
				Packet02Move packet = new Packet02Move(this.getUsername(), this.x, this.y);
				packet.writeData(Game.socketClient);
			}
		}
	 */
	}


	//AI COMMANDS

    public void setDirection(String r) {
		this.direction = r;
	}

	public String getDirection() {
		return this.direction;
	}

    public void setJump(String j) {
		this.jump = j;
	}

    public void setWait(float w) {
		this.wait = w;
	}



}
