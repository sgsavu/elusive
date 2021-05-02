package game.entities.players;

import java.util.LinkedList;
import java.util.UUID;

import game.Effect;
import game.attributes.SolidCollider;
import game.entities.areas.RespawnPoint;
import game.entities.areas.Waypoint;
import game.graphics.AnimationStates;

import static game.Level.getPlayers;

public class AIPlayer extends Player {

    public String direction = "N"; // or private?
    public String jump = "N";
    public float wait = 0;
	public Player humanPlayer;
	public float dist_from_player;
	public RespawnPoint penultimateRespawnPoint;
	private int max_distance_ahead = 1500;
	private int max_distance_behind = -400;
	private String username;
	private Waypoint currentWaypoint;
	private int inventoryTimer = 0;
	private boolean inventoryTimerOn = false;
	private int id;
	private int interactionTimer = 0;
	private int interactionWait = 0;


	public AIPlayer(float x, float y, int width,int height, Player humanPlayer,String url) {
		super(x, y, null, width, height,url);

		
		this.username = UUID.randomUUID().toString();;
		this.humanPlayer = humanPlayer;
		this.id = Character.getNumericValue(url.charAt(6));
	}

	public void tick() {


		super.tick();

		this.dist_from_player = this.x - this.humanPlayer.getX();

		if (this.isGhostMode() == false) {

			if (this.humanPlayer.isGhostMode() == false) {

				// sends AI Player to the penultimate RespawnPoint that the player has reached 
				if (this.dist_from_player < max_distance_behind && humanPlayer.getRespawnPoints().size() > 2) {

					this.invincible = true;

					// Get the penultimate RespawnPoint that humanPlayer visited
					penultimateRespawnPoint = humanPlayer.getRespawnPoints().get(humanPlayer.getRespawnPoints().size()-2);

					if (this.humanPlayer.getX() - penultimateRespawnPoint.getX() > 350) {
						this.invincibleMove = true;
					}
				}

				if (this.invincibleMove == true) {

					// to make smooth transition on minimap
					if (this.x < penultimateRespawnPoint.getX()) {
						this.x += 3;
					} else { 
						this.invincible = false; 
						this.invincibleMove = false; 
					}

					// so that it only teleports once, and doesn't keep getting sent back
					if (!this.getRespawnPoints().contains(penultimateRespawnPoint)) {
						this.y = penultimateRespawnPoint.getY()-20;
					} 	
				}
			}	

			for (Player p: getPlayers())
				{
					if (p instanceof AIPlayer)
						if (this.getInteraction(p)){

							this.interactionTimer++;

							if (this.interactionTimer >= 30) {

								if (this.id < p.id) {
									this.interactionWait = 20;
								} else {
									p.interactionWait = 20;
								}

								this.interactionTimer -= 30;
								p.interactionTimer -= 30; 

							}
						}
					}

			if (this.canMove == true && this.invincibleMove == false) {

				if (this.interactionWait > 0) {
					this.interactionWait--;
				} 
				else {

					if (this.wait > 0) {

						this.wait--;

						if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
							if (this.velX >= -0.1f && this.velX <= 0.1f) {
								this.velX = 0;
								currentAnimState = AnimationStates.IDLE;
							} else if (this.velX > 0.1f) {
								this.velX -= DECELERATION;
							} else {
								this.velX += DECELERATION;
							}
						} else {
							this.velX = 0;
							currentAnimState = AnimationStates.IDLE;
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
								currentAnimState = AnimationStates.RIGHT;

							} else if(this.direction.equals("L")) { 

									// Simulates acceleration when running left
									if (this.velX <= -RUN_SPEED){
										this.velX = -RUN_SPEED;
									} else {
										this.velX -= RUN_SPEED/6;
									}
								currentAnimState = AnimationStates.LEFT;

							} else { 
								// For deceleration effect
								if (!SolidCollider.willCauseSolidCollision(this, this.velX, true)){
									if (this.velX >= -0.1f && this.velX <= 0.1f) {
										this.velX = 0;
										currentAnimState = AnimationStates.IDLE;
									} else if (this.velX > 0.1f) {
										this.velX -= DECELERATION;
									} else {
										this.velX += DECELERATION;
									}
								} else {
									this.velX = 0;
									currentAnimState = AnimationStates.IDLE;
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

					if (this.inventoryTimerOn == true) {

						this.inventoryTimer++;

						if (this.inventoryTimer >= 80) {
							
							INVENTORY.get(0).getEffect();
							if (!(INVENTORY.get(0).getUrl().equals("./img/jump.png")) && !(INVENTORY.get(0).getUrl().equals("./img/banana.png")))
								CURRENT_EFFECTS.add(new Effect(INVENTORY.get(0).getUrl(), 500));
							INVENTORY.get(0).setUrl("./img/empty.png");
							this.setInventoryChanged(true);

							this.inventoryTimer = 0;
							this.inventoryTimerOn = false;
						}
					}
			}
			
		} else {	// If AIPlayer isGhostMode

			if (this.y < this.humanPlayer.getY()+600) {

				if (this.velY <= -6f) {
					this.velY = -6f;
				} else {
					this.velY -= 0.5;
				}

				this.velX = 0;

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

	public void setCurrentWaypoint(Waypoint currentWaypoint) {
		this.currentWaypoint = currentWaypoint;
	}

	public Waypoint getCurrentWaypoint() {
		return currentWaypoint;
	}

	public void startInventoryTimer(){
		this.inventoryTimerOn = true;
	}

	public boolean getInteraction(Player player){
		return ((int)this.x 		 < 	(int)player.getX()+player.getWidth() && 
				(int)player.getX() < 	this.x+this.width && 
				(int)this.y-20 	 < 	(int)player.getY()+player.getHeight() && 
				(int)player.getY() < 	(int)this.y+this.height);
	}

}
