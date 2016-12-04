package game_engine;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import game_data.Controllable;
import game_data.Level;
import game_data.Location;
import game_data.SpeedBooster;
import game_data.Sprite;
import game_data.characteristics.Characteristic;
import game_data.sprites.Enemy;
import game_data.sprites.Player;
import game_data.sprites.WinningObject;
import game_data.states.Health;
import game_data.states.LevelWon;
import game_data.states.Physics;
import game_data.states.State;
import game_engine.actions.Action;
import game_engine.actions.Launch;
import game_engine.actions.MoveLeft;
import game_engine.actions.MoveRight;
import game_engine.actions.MoveUpFly;
import game_engine.actions.MoveUpJump;
import game_engine.actions.SpeedBoost;
import game_engine.actions.StopLeftMovement;
import game_engine.actions.StopRightMovement;
import game_engine.actions.StopUpMovement;
import javafx.geometry.Side;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

/**
 * TODO make sure that player doesnt run into walls or thigns NOTE: doing the
 * runKeyCalls and then updating sprite posistions separately might lead to an
 * issue with glitches CHECK: CALCULATING HEARDING FROM THE VERTICAL (NOON)
 * CHECK CORRECT CALC FOR VEL QUESTION who is going to keep track of the time of
 * the game? how are we going to provoke a win? through interface? TODO give
 * myLevel all the properties I want. CLARIFY: does the x and y loc represent
 * the middle or the left top corner or what? There are some things that I dont
 * know if I should be getting from level class of the engine itself Losses
 * should actually probably be integrated within characteristics so we dont
 * check for collision repeatedly. Do we have to deal with if the sprite hits a
 * block at multiple sides?
 * 
 * @author LuciaMartos, Austin Gartside
 *
 */

public class UpdateStates {

	private Level myLevel;
	private List<Sprite> mySpriteList;
	private double timeElapsed;
	private KeyCode myKey;
	private Map<KeyCode, Action> myKeyPressedMap;
	private Set<KeyCode> myKeysPressed;
	private Set<KeyCode> myKeysReleased;
	private Map<Sprite, ImageView> mySpriteImages;
	private Map<KeyCode, Action> myKeyReleasedMap;
	private Map<Action, Double> myCurrentPowerUps;

	private List<Sprite> myControllableSpriteList;

	public UpdateStates(Level aLevel, double timeElapsed, Set<KeyCode> myKeysPressed, Set<KeyCode> myKeysReleased,
			Map<Sprite, ImageView> mySpriteImages) {
		this.myLevel = aLevel;
		this.myCurrentPowerUps = myLevel.getMainPlayer().getMyPowerUps();
		this.mySpriteList = myLevel.getMySpriteList();
		this.timeElapsed = timeElapsed;
		this.myKeysPressed = myKeysPressed;
		this.myKeysReleased = myKeysReleased;
		this.mySpriteImages = mySpriteImages;

		
		// System.out.println("number of sprites" +
		// myLevel.getMySpriteList().size());
		// for (State myState : aLevel.getMainPlayer().getStates()) {
		// if (myState instanceof Health) {
		// System.out.println("Health of sprite" + ((Health)
		// myState).getMyHealth());
		// }
		// }
		// how do I make an ImageView
		// hardcode
		// ImageView view = new ImageView(mySpriteList.get(1).getMyImagePath());
		// this.mySpriteImages.put(mySpriteList.get(1), view);
		// end hardcode

		this.myKeyPressedMap = new HashMap<KeyCode, Action>();
		this.myKeyReleasedMap = new HashMap<KeyCode, Action>();
		myControllableSpriteList = new ArrayList<Sprite>();
		this.myControllableSpriteList = myLevel.getMyControllableSpriteList();

		generateDefaultKeyPressedMap();
		// generateDefaultKeyReleasedMap();
		// runKeyCalls();
		// runKeyReleased();
		executeControls();
		executeCharacteristics();
		cleanGame();
		updateSpritePositions();
//		System.out.println("size of map " +myLevel.getMyPowerUps().size());
		activatePowerUps();
		checkPowerUps();
		
		System.out.println("size of sprites " +myLevel.getMySpriteList().size());

		// checkForWin();
		// checkForLoss();
	}

	private void activatePowerUps() {
		for (Action powerUp : myCurrentPowerUps.keySet()) {
			if (powerUp instanceof SpeedBooster) {
//				System.out.println("Activate Power up " + myCurrentPowerUps.get(powerUp));
				myKeyPressedMap.put(KeyCode.RIGHT,
						new MoveRight(myLevel.getMainPlayer(), GameResources.MOVE_RIGHT_SPEED.getDoubleResource()
								+ GameResources.SPEED_BOOST.getDoubleResource()));
				myKeyPressedMap.put(KeyCode.LEFT,
						new MoveLeft(myLevel.getMainPlayer(), GameResources.MOVE_LEFT_SPEED.getDoubleResource()
								+ GameResources.SPEED_BOOST.getDoubleResource()));
				myKeyPressedMap.put(KeyCode.UP, new MoveUpJump(myLevel.getMainPlayer(),
						GameResources.JUMP_SPEED.getDoubleResource() + GameResources.SPEED_BOOST.getDoubleResource()));
			}

		}
	}

	private void checkPowerUps() {
		for (Action powerUp : myCurrentPowerUps.keySet()) {
			System.out.println("POWER UP SPEED" + myCurrentPowerUps.get(powerUp));
			myCurrentPowerUps.put(powerUp, myCurrentPowerUps.get(powerUp) - 1);
			if (myCurrentPowerUps.get(powerUp) <= 0) {
				if (powerUp instanceof SpeedBooster) {
					generateDefaultKeyPressedMap();
					//myLevel.getMainPlayer().resetTerminalVelocities();
				}
				myCurrentPowerUps.remove(powerUp);
				powerUpHasBeenRemoved(powerUp);
			}
		}
	}

	private void powerUpHasBeenRemoved(Action powerUp) {
		if (powerUp instanceof SpeedBoost) {
			// SpeedBoost powerDown = new SpeedBoost(null, ((SpeedBoost)
			// powerUp).getPrevSpeed(), timeElapsed);
		}
	}

	private void cleanGame() {
		ArrayList<Sprite> removeSprites = new ArrayList<Sprite>();
		for (Sprite mySprite : mySpriteList) {
			for (State state : mySprite.getStates()) {
				if (state instanceof Health) {
					if (!((Health) state).isAlive()) {
						// System.out.println("DEAD SPRITE");
						removeSprites.add(mySprite);
					}
				}
			}
		}
		for (Sprite mySprite : removeSprites) {
			myLevel.removeSprite(mySprite);
			mySpriteImages.remove(mySprite);
		}
		updateSpritePositions();
		checkForWin();
		checkForLoss();
		// System.out.println("want this to be after launching");
	}

	private void executeControls() {
		for (Sprite mySprite : myControllableSpriteList) {
			// System.out.println("sprite list length " + mySpriteList.size());
			// System.out.println("sprite image list length " +
			// mySpriteImages.size());
			// ListOfCollidingSprites collidingSprites = new
			// ListOfCollidingSprites(mySprite, mySpriteList, mySpriteImages,
			// timeElapsed);
			ListOfCollidingSprites collidingSprites = new ListOfCollidingSprites(mySprite, mySpriteList,
					mySpriteImages);
			Map<Sprite, Side> myCollisionMap = collidingSprites.getCollisionSpriteMap();
			Controllable control = mySprite.getControllable();
			// System.out.println(myCollisionMap.size());
			if (control.isControllable()) {
				control.sendCurrentKeys(myKeysPressed, myKeysReleased);
				control.execute(myCollisionMap);
			}
		}
	}

	private void checkForLoss() {
		for (State s : myLevel.getMainPlayer().getStates()) {
			if (s instanceof Health) {
				if (!(((Health) s).isAlive())
						|| myLevel.getMainPlayer().getMyLocation().getYLocation() > myLevel.getHeight()) {
					myLevel.setLevelLost();
				}
			}
		}
	}

	//
	private void checkForWin() {
		for (State s : myLevel.getMainPlayer().getStates()) {
			if (s instanceof LevelWon) {
				if (((LevelWon) s).isHasWon()) {
					myLevel.setLevelWon();
				}
			}
		}
	}

	// private void checkForLoss() {
	// for(State s: myLevel.getMainPlayer().getStates()){
	// if(s instanceof Health){
	// myLevel.setLevelLost(!((Health)s).isAlive());
	// }
	// }
	// }
	//
	// private void checkForWin() {
	// for(State s: myLevel.getMainPlayer().getStates()){
	// if(s instanceof LevelWon){
	// myLevel.setLevelWon(((LevelWon)s).isHasWon());
	// }
	// }
	//
	// }

	// keys will only control the main player rn
	private void generateDefaultKeyPressedMap() {
		// <<<<<<< HEAD
		// //
		// System.out.println(GameResources.MOVE_RIGHT_SPEED.getDoubleResource());
		// // System.out.println(myLevel.getMainPlayer()==null);
		// myKeyPressedMap.put(KeyCode.RIGHT, new
		// MoveRight(myLevel.getMainPlayer(),
		// GameResources.MOVE_RIGHT_SPEED.getDoubleResource(), mySpriteList,
		// mySpriteImages));
		// myKeyPressedMap.put(KeyCode.LEFT,
		// new MoveLeft(myLevel.getMainPlayer(),
		// GameResources.MOVE_LEFT_SPEED.getDoubleResource()));
		// myKeyPressedMap.put(KeyCode.UP, new
		// MoveUpJump(myLevel.getMainPlayer(),
		// GameResources.JUMP_SPEED.getDoubleResource(), mySpriteList,
		// mySpriteImages, timeElapsed));
		// =======
		// System.out.println(GameResources.MOVE_RIGHT_SPEED.getDoubleResource());
		// System.out.println(myLevel.getMainPlayer()==null);
		myKeyPressedMap.put(KeyCode.RIGHT,
				new MoveRight(myLevel.getMainPlayer(), GameResources.MOVE_RIGHT_SPEED.getDoubleResource()));
		myKeyPressedMap.put(KeyCode.LEFT,
				new MoveLeft(myLevel.getMainPlayer(), GameResources.MOVE_LEFT_SPEED.getDoubleResource()));
		myKeyPressedMap.put(KeyCode.UP,
				new MoveUpJump(myLevel.getMainPlayer(), GameResources.JUMP_SPEED.getDoubleResource()));
		// myKeyPressedMap.put(KeyCode.SPACE, new
		// Launch(myLevel.getMainPlayer(), 10, 0));
	}
	// private void generateDefaultKeyReleasedMap(){
	// myKeyReleasedMap.put(KeyCode.RIGHT, new
	// StopRightMovement(myLevel.getMainPlayer(),
	// GameResources.MOVE_RIGHT_SPEED.getDoubleResource()));
	// myKeyReleasedMap.put(KeyCode.LEFT, new
	// StopLeftMovement(myLevel.getMainPlayer(),
	// GameResources.MOVE_LEFT_SPEED.getDoubleResource()));
	// //myKeyReleasedMap.put(KeyCode.UP, new
	// StopUpMovement(myLevel.getMainPlayer(),
	// GameResources.JUMP_SPEED.getDoubleResource()));
	// >>>>>>> katrina
	// }

	private void generateDefaultKeyReleasedMap() {
		myKeyReleasedMap.put(KeyCode.RIGHT,
				new StopRightMovement(myLevel.getMainPlayer(), GameResources.MOVE_RIGHT_SPEED.getDoubleResource()));
		myKeyReleasedMap.put(KeyCode.LEFT,
				new StopLeftMovement(myLevel.getMainPlayer(), GameResources.MOVE_LEFT_SPEED.getDoubleResource()));
		// myKeyReleasedMap.put(KeyCode.UP, new
		// StopUpMovement(myLevel.getMainPlayer(),
		// GameResources.JUMP_SPEED.getDoubleResource()));
	}

	private void runKeyCalls() {
		for (KeyCode myKey : myKeysPressed) {
			if (myKeyPressedMap.containsKey(myKey)) {
				myKeyPressedMap.get(myKey).act();
			}
		}
	}

	private void runKeyReleased() {
		for (KeyCode myKey : myKeysReleased) {
			if (myKeyReleasedMap.containsKey(myKey)) {
				myKeyReleasedMap.get(myKey).act();
			}
		}
	}

	private void executeCharacteristics() {
		for (Sprite mySprite : mySpriteList) {
			// System.out.println("sprite list length " + mySpriteList.size());
			// System.out.println("sprite image list length " +
			// mySpriteImages.size());
			// ListOfCollidingSprites collidingSprites = new
			// ListOfCollidingSprites(mySprite, mySpriteList, mySpriteImages,
			// timeElapsed);
			ListOfCollidingSprites collidingSprites = new ListOfCollidingSprites(mySprite, mySpriteList,
					mySpriteImages);
			Map<Sprite, Side> myCollisionMap = collidingSprites.getCollisionSpriteMap();
			Set<Characteristic> characteristics = mySprite.getCharacteristics();
			// <<<<<<< HEAD
			// // System.out.println(myCollisionMap.size());
			// for (Characteristic myCharacteristic : characteristics) {
			// myCharacteristic.execute(myCollisionMap);
			// System.out.println(myCollisionMap.size());
			/*
			 * Controllable control = mySprite.getControllable();
			 * //System.out.println(myCollisionMap.size());
			 * if(control.isControllable()){
			 * control.sendCurrentKeys(myKeysPressed, myKeysReleased);
			 * control.execute(myCollisionMap); }
			 */
			for (Characteristic myCharacteristic : characteristics) {
				myCharacteristic.execute(myCollisionMap);

			}
		}
	}

	// not the best design in the world but works for the time being

	/**
	 * Checking for win should just happen naturally while checking collisions
	 * (i.e. once something collides with winning object)
	 */
	/*
	 * private void checkForWin() { Set<String> type =
	 * enginePlayerController.getMyLevel().getWinType();
	 * if(type.contains("time")&& enginePlayerController.getMyLevel().getTime()
	 * > enginePlayerController.getMyLevel().getTimeToWin()){
	 * System.out.print("YOU WIN"); }
	 * 
	 * if(type.contains("score") &&
	 * enginePlayerController.getMyLevel().getMainPlayerSprite().getPoints() >
	 * enginePlaterController.getMyLevel().getPointsToWin()){
	 * System.out.println("YOU WIN"); }
	 * 
	 * if(type.contains("object") &&
	 * enginePlayerController.getMyLevel().getWinningSprite().getBoundsInLocal()
	 * .interects(enginePlayerController.getMyLevel().getMainPlayerSprite())){
	 * System.out.println("YOU WIN"); } }
	 */

	/*
	 * private void checkForWin(Sprite aSprite){ if(aSprite instanceof
	 * WinningObject){ System.out.println("Do win action???"); //Do somethin? }
	 * }
	 */

	/**
	 * Checking for loss also should happen naturally (or just have to check if
	 * the players health characteristic has health<0)
	 */
	/*
	 * private void checkForLoss() { Set<String> type =
	 * enginePlayerController.getMyLevel().getLossType(); Sprite mainPlayer =
	 * enginePlayerController.getMainPlayer(); if(type.contains("object")){
	 * List<Sprite> deathProvokingObj =
	 * enginePlayerController.getMyObjectSpriteList(); for(Sprite myObj :
	 * deathProvokingObj){
	 * if(enginePlayerController.getImageView(myObj).getBoundsInLocal().
	 * intersects(enginePlayerController.getImageView(mainPlayer).
	 * getBoundsInLocal())){ System.out.println("DEATH"); } } } }
	 */

	private void updateSpritePositions() {
		for (Sprite sprite : mySpriteList) {
			UpdateLocation updateLocation = new UpdateLocation(sprite, timeElapsed);
			updateLocation.updateSpriteParameters();
			if (sprite instanceof Enemy) {
				// System.out.println("x is " +
				// sprite.getMyLocation().getXLocation());
				// System.out.println("y is " +
				// sprite.getMyLocation().getYLocation());
			}
		}
	}
}