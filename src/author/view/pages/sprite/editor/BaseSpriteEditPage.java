package author.view.pages.sprite.editor;

import java.io.File;

import author.view.util.ToolBarBuilder;
import author.view.util.authoring_buttons.ButtonFactory;
import game_data.Location;
import game_data.Sprite;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Abstract class for Sprite viewing and editing
 * 
 * 
 * @author George Bernard
 */
public abstract class BaseSpriteEditPage {
	private Pane myPane;
	private ToolBarBuilder myToolBarBuilder;
	
	private Sprite mySprite;
	private BaseSpriteEditBox mySpriteEditBox;
	
	public BaseSpriteEditPage(){
		myPane = new VBox();
		myToolBarBuilder = new ToolBarBuilder();
		mySpriteEditBox = new BaseSpriteEditBox();
		myPane.getChildren().addAll(myToolBarBuilder.getToolBar(), mySpriteEditBox.getPane());
		Button buildButton = new ButtonFactory().createButton("save", e -> buildSprite()).getButton();
		myToolBarBuilder.addBurst(new Label(getSpriteType()));
		myToolBarBuilder.addBurst(buildButton);

	}

	public BaseSpriteEditPage(Sprite aSprite){
		this();
		mySprite = aSprite;
		mySpriteEditBox.setLocation(aSprite.getMyLocation());
		mySpriteEditBox.setImageFile(new File(aSprite.getMyImagePath()));
		mySpriteEditBox.setName(aSprite.getName());
		mySpriteEditBox.setSize(aSprite.getMyWidth(), aSprite.getMyHeight());
	}
	
	public abstract Sprite buildSprite();
	
	public abstract String getSpriteType();

	public Pane getPane(){
		return myPane;
	}
	
	protected final String getSpriteName(){
		return mySpriteEditBox.getName();
	}
	
	protected final void setSpriteName(String aSpriteName){
		mySpriteEditBox.setName(aSpriteName);
	}
	
	protected final int getWidth(){
		return mySpriteEditBox.getWidth();
	}
	
	protected final int getHeight(){
		return mySpriteEditBox.getWidth();
	}
	
	protected final boolean hasSprite(){
		return mySprite == null;
	}

	protected final File getImageFile(){
		return mySpriteEditBox.getImageFile();
	}
	
	protected final Location getLocation(){
		return mySpriteEditBox.getLocation();
	}
	
	protected ToolBarBuilder getToolBarBuilder(){
		return myToolBarBuilder;
	}

	protected Sprite getSprite(){
		return mySprite;
	}

}
