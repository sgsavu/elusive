package game.graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Assets {
	

	///Keep in mind that all animations lists should be same length if not it will lead to out of bound problems
	public static ArrayList <BufferedImage> player_idle;
	public static ArrayList <BufferedImage> player_right;
	public static ArrayList <BufferedImage> player_left;
	public static ArrayList <BufferedImage> player_other;
	public static ArrayList <BufferedImage> player_swag;
	public static ArrayList <BufferedImage> player_jump_right;
	public static ArrayList <BufferedImage> player_jump_left;



	public static ArrayList<Asset> assets = new ArrayList<>();
	String[] splitted;
	private static int yOffset = 6;
	private static int xOffset = 13;
	private static int yDistance = 37;
	private static int xDistance = 50;
	private static int PLAYER_WIDTH=22;
	private static int PLAYER_HEIGHT=30;

	public Assets(){

		addAnimationFromSpritesheet(new SpriteSheet(Image.loadImage("./img/adventurer1.png")),"player1");
		addAnimationFromSpritesheet(new SpriteSheet(Image.loadImage("./img/adventurer2.png")),"player2");
		addAnimationFromSpritesheet(new SpriteSheet(Image.loadImage("./img/adventurer3.png")),"player3");
		addAnimationFromSpritesheet(new SpriteSheet(Image.loadImage("./img/adventurer4.png")),"player4");
		parseAssets();
	}

	/**
	 * This extracts all the images for all the animations from a spritesheet
	 * @param sheet The spritesheet object for the asset
	 * @param name The name of the asset
	 */
	public void addAnimationFromSpritesheet(SpriteSheet sheet, String name)
	{
		player_idle = new ArrayList<BufferedImage>();

		player_idle.add(sheet.crop(xOffset, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance*2, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance*3, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_idle.add(sheet.crop(xOffset+xDistance, yOffset, PLAYER_WIDTH, PLAYER_HEIGHT));

		player_other = flipHorizontally(player_idle);

		player_right = new ArrayList<BufferedImage>();

		player_right.add(sheet.crop(xOffset+xDistance, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance, PLAYER_WIDTH	, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_right.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance, PLAYER_WIDTH, PLAYER_HEIGHT));

		player_left = new ArrayList<BufferedImage>();

		player_left.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_left.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*2, PLAYER_WIDTH, PLAYER_HEIGHT));
		
		player_jump_right = new ArrayList<BufferedImage>();
		
		player_jump_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_jump_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_jump_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_jump_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_jump_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_jump_right.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH, PLAYER_HEIGHT));

		player_swag = new ArrayList<BufferedImage>();

		player_swag.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*4, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*4, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*4, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*3, PLAYER_WIDTH+5, PLAYER_HEIGHT));

		player_swag.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*13, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*13, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*13, PLAYER_WIDTH, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*13, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*13, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance*13, PLAYER_WIDTH+10, PLAYER_HEIGHT));

		player_swag.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));

		player_swag.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance*14, PLAYER_WIDTH+10, PLAYER_HEIGHT));

		player_swag.add(sheet.crop(xOffset+xDistance*0, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*1, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*2, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*3, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*4, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*5, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));
		player_swag.add(sheet.crop(xOffset+xDistance*6, yOffset+yDistance*15, PLAYER_WIDTH+10, PLAYER_HEIGHT));


		Asset as = new Asset(name,new HashMap<AnimationStates, Animation>());
		as.getAnimations().put(AnimationStates.IDLE, new Animation(10,player_idle));
		as.getAnimations().put(AnimationStates.RIGHT, new Animation(10,player_right));
		as.getAnimations().put(AnimationStates.LEFT, new Animation(10,player_left));
		as.getAnimations().put(AnimationStates.OTHER, new Animation(10,player_other));
		as.getAnimations().put(AnimationStates.SWAG, new Animation(5,player_swag));

		assets.add(as);
	}

	/**
	 * Gets the first idle frame for a given asset
	 * @param code The name of the asset
	 * @return The first idle frame for the asset
	 */
	public static BufferedImage getImageForReference(String code)
	{
		for (Asset a : assets)
			if (a.getName().equals(code))
				return a.getAnimations().get(AnimationStates.IDLE).getFrame(0);
		return null;
	}

	/**
	 * Parses ./src/game/assets and extracts the animations for each asset
	 */
	public void parseAssets(){


		try
		{
			File myObj = new File("./src/game/assets.txt");
			Scanner myReader = new Scanner(myObj);
			Asset asset = null;

			while (myReader.hasNextLine()) {

				String data = myReader.nextLine();
				splitted = data.split("\\s+");

				if (splitted.length==1) {
					if (asset!=null)
						assets.add(asset);
					asset = new Asset(splitted[0], new HashMap<AnimationStates, Animation>());
				}
				else
				{
					switch (splitted[0]){
						case "IDLE":
							asset.getAnimations().put(AnimationStates.IDLE, new Animation(Integer.parseInt(splitted[1]),createURLS(2)));
							break;
						case "LEFT":
							asset.getAnimations().put(AnimationStates.LEFT, new Animation(Integer.parseInt(splitted[1]),createURLS(2)));
							break;
						case "RIGHT":
							asset.getAnimations().put(AnimationStates.RIGHT, new Animation(Integer.parseInt(splitted[1]),createURLS(2)));
							break;
						case "JUMP":
							asset.getAnimations().put(AnimationStates.JUMP, new Animation(Integer.parseInt(splitted[1]),createURLS(2)));
							break;
						case "OTHER":
							asset.getAnimations().put(AnimationStates.OTHER, new Animation(Integer.parseInt(splitted[1]),createURLS(2)));
							break;
					}

				}
			}

			assets.add(asset);

			myReader.close();


		} catch (FileNotFoundException e) {
		System.out.println("An error occurred.");
		e.printStackTrace();
		}

	}

	/**
	 * Gets all the animations for a given asset
	 * @param name The name of the asset
	 * @return All of the animations for all of the states of the given asset
	 */
	public static HashMap<AnimationStates,Animation> getAnimations(String name){

		for (Asset a : assets)
		{
			if (a.getName().equals(name))
				return a.getAnimations();
		}

		return null;
	}

	/**
	 * Generates the urls for asset images extracted from the assets file
	 * @param index An integer representing for which parsed asset line the image url is being created
	 * @return A list of the image urls for the given line
	 */
	public String[] createURLS(int index)
	{
		//prepares a string array with the urls
		List<String> list = new ArrayList<String>();
		for (int l = index+1 ; l < index+1 + Integer.parseInt(splitted[index]); l++)
			list.add("./img/".concat(splitted[l]));

		String[] arr = list.toArray(new String[0]);

		return arr;
	}

	/**
	 * Horizontally flips every given image
	 * @param imagesToFlip A list of images
	 * @return A list of images that have been horizontally flipped
	 */
	public ArrayList<BufferedImage> flipHorizontally(ArrayList<BufferedImage> imagesToFlip)
	{

		ArrayList<BufferedImage> flippedImages = new ArrayList<BufferedImage>();
		for (BufferedImage b : imagesToFlip)
		{
			BufferedImage bb = b;
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-bb.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bb = op.filter(bb, null);
			flippedImages.add(bb);
		}

		return flippedImages;
	}


}
