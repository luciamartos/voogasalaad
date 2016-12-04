package game_engine.actions;

import game_data.Sprite;
import game_engine.GameResources;

public class SpeedBoost implements Action {

	private Sprite myPlayerSprite;
	private double timeInEffect;
	private double speedBoost;
	private double prevSpeed;

	public SpeedBoost(Sprite playerSprite, double speedBoost, double timeInEffect) {
		myPlayerSprite = playerSprite;
		this.timeInEffect = timeInEffect;
		this.speedBoost = speedBoost;
		prevSpeed = 0;
	}
	
	public double getPrevSpeed(){
		return prevSpeed;
	}

	@Override
	public void act() {
		// Possible to change the physics for one specific sprite?
		// If so:
		// myPlayerSprite.setSpritePhysics(SpritePhysics speedBoostPhysics);
		// or
		// myPlayerSprite.getSpritePhysics().setHorizontalGravity(.5);
		// myPlayerSprite.getSpritePhysics().setVerticalGravity(.5);
		
		prevSpeed = myPlayerSprite.getMyXVelocity();
		myPlayerSprite.setMyXVelocity(myPlayerSprite.getMyXVelocity() + speedBoost);
	}

}
