package game.entities.areas;
import game.entities.players.AIPlayer;
import game.SoundHandler;
import game.entities.players.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static game.Level.getPlayers;

public class Chest extends AnimArea{

	public Chest(float x, float y, int width, int height, String url) {
		super(x, y, width, height,url);
	}

	/**
	 * Called every frame, this checks for players within it's interactable area and gives any players within its interactable area a random item
	 */
	public void tick() {

		for (Player p: getPlayers())
		{
			if (this.isVisibile())
			{
				if (this.getInteraction(p))
				{
					if (p.firstFreeSpace()!=-1)
					{
						p.getInventory().get(p.firstFreeSpace()).setUrl(randomItem());
						this.setVisibile(false);
						p.setInventoryChanged(true);
						
						SoundHandler.playSound("chest", 0.4f);

						if (p instanceof AIPlayer) {
							((AIPlayer) p).startInventoryTimer();
						}
					}
				}
			}
		}
	}


	/**
	 * @return A random item
	 */
	public String randomItem(){
		ArrayList<String> itemPool =new ArrayList<String>(Arrays.asList("./img/shoes.png","./img/jump.png","./img/banana.png","./img/fart1.png"));
		int rnd1;
		rnd1 = new Random().nextInt(itemPool.size());

		return itemPool.get(rnd1);
	}

}
