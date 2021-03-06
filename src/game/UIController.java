package game;

import game.display.Window;
import game.entities.GameObject;
import game.entities.players.Player;
import game.graphics.LevelState;
import game.network.packets.Packet04StartGame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static game.Level.*;
import static game.ProgressBarController.getAllBlips;
import static game.ProgressBarController.getProgressBarElements;

public class UIController extends GameObject {

	private ArrayList<String> countdownUrls = new ArrayList<String>(Arrays.asList("5.png", "4.png","3.png","2.png","1.png","finish2.png","asdgasdg.png"));
	private int index=0;
	private int timer=0;
	private boolean startCountdown=false;
	private boolean endGameCelebration=false;
	private UIElement announcer,announcerMessage,spectating, exit;
	private double time1=0,time2;
	private static BufferedImage loading, starting, winner, close, gameover;

	public UIController(float x, float y, int width, int height) {
		super(x, y, 4, width, height);
		
		try {
			if (UIController.loading == null) {
				UIController.loading = ImageIO.read(new File("./img/loading.png"));
			}
			if (UIController.starting == null) {
				UIController.starting = ImageIO.read(new File("./img/starting.png"));
			}
			if (UIController.winner == null) {
				UIController.winner = ImageIO.read(new File("./img/winner.png"));
			}
			if (UIController.close == null) {
				UIController.close = ImageIO.read(new File("./img/close.png"));
			}
			if (UIController.gameover == null) {
				UIController.gameover = ImageIO.read(new File("./img/gameover.png"));
			}
		} catch (IOException e) {
		}

		//Initialize center announcer UI Element
		announcer = new UIElement(x, y, 0, 0, "./img/5.png");
		announcer.setVisible(false);
		announcer.centerHorizontally();
		announcer.centerVertically();

		announcerMessage = new UIElement(x, y-200, 0, 0, "./img/waiting.png");
		announcerMessage.centerHorizontally();
		announcerMessage.centerVertically();
		announcerMessage.setVisible(false);
		announcerMessage.setY(announcerMessage.getY()-165);

		spectating = new UIElement(x, y-200, 0, 0, "./img/spectating.png");
		spectating.centerHorizontally();
		spectating.centerVertically();
		spectating.setVisible(false);
		spectating.setY(spectating.getY()-165);

		exit = new UIElement(this.x + 550 + 0 , this.y + 420, 0 , 0, "./img/exit.png");
		exit.setVisible(false);

		getGameObjects().add(announcer);
		getGameObjects().add(announcerMessage);
		getGameObjects().add(spectating);
		getGameObjects().add(exit);
	}

	/**
	 * Called every frame, this is responsible for loading and updating the relevant UI elements for the current level state
	 */
	public void tick() {
		if (getLevelState()==LevelState.InProgress) {
			announcerMessage.setVisible(false);

			if (((Player)Camera.getTarget()).isGhostMode()) {
				if (!spectating.isVisible())
				spectating.setVisible(true);

				if (!exit.isVisible())
					exit.setVisible(true);
			}

		} else {

			if (announcer.isVisible()) {
				announcer.centerHorizontally();
				announcer.centerVertically();
			}

			//START OF THE GAME COUNTDOWN SLIDESHOW
			if (getLevelState() == LevelState.Starting) {
				startCountdown = true;
			}

			if (getLevelState() == LevelState.Finished)
				endGameCelebration = true;

			if (getLevelState() == LevelState.Waiting) {

				if (!announcer.isVisible()) {
					announcer.setVisible(true);
					announcerMessage.setVisible(true);
				}

				announcer.setImg(UIController.loading);
			}
		}


		if (startCountdown) {

			if (!announcer.isVisible()) {
				announcer.setVisible(true);
				announcerMessage.setVisible(true);
				automaticallySetTime1();
			}

			announcerMessage.setImg(UIController.starting);

			if (getElapsedTime() < 1000)
				timer++;

			else {
				if(index< countdownUrls.size()) {
					try {
						announcer.setImg(ImageIO.read(new File("./img/".concat(countdownUrls.get(index)))));
					}
					catch (IOException exc) {
					}
				}
				index++;
				if (index == countdownUrls.size()-1) {
					if(Game.socketServer != null) {
						Packet04StartGame packet = new Packet04StartGame();
		                packet.writeData(Game.socketClient);

						announcerMessage.setVisible(false);

					} else {
						announcerMessage.setVisible(false);
						Level.setLevelState(LevelState.InProgress);

					}
				}

				if (index == countdownUrls.size()) {
					startCountdown = false;
					announcer.setVisible(false);
				}

				automaticallySetTime1();
			}
		}

		if (endGameCelebration)
		{

			for (Blip b: getAllBlips())
				b.setVisible(false);
			for (UIElement uiElement: getProgressBarElements())
				uiElement.setVisible(false);


			if (!announcer.isVisible()) {
				announcer.setVisible(true);
				announcerMessage.setVisible(true);
			}

			if (getWinner()!=null)
				announcer.setImg(UIController.winner);
			else
				announcer.setImg(UIController.gameover);

			announcerMessage.setImg(UIController.close);
			
			announcer.setY(y-8);
			announcer.setX(x+((Window.WIDTH/6)/2) - (announcer.getImg().getWidth()/2));

			announcerMessage.setY(y+50);
			announcerMessage.setX(x+((Window.WIDTH/6)/2) - (announcerMessage.getImg().getWidth()/2));
		}


		}
	

	public void render(Graphics g, float xOffset, float yOffset) {

	}


	public void centerHorizontally()
	{
		this.x=(640/2)-(this.width/2);
	}

	public void centerVertically()
	{
		this.y=(480/2)-(this.height/2);
	}

	/**
	 * Sets the value of time1 to the current time
	 */
	public void automaticallySetTime1()
	{
		time1 = System.currentTimeMillis();
	}

	/**
	 * @return The time passed since time1 was set
	 */
	public double getElapsedTime()
	{
		time2 = System.currentTimeMillis();

		return time2-time1;
	}

}
