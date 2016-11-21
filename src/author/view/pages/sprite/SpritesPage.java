package author.view.pages.sprite;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class SpritesPage {
	
	private Pane myPane;
	private SpriteScroller myPlayerScroller;
	private SpriteScroller myEnemyScroller;
	private SpriteScroller myTerrainScroller;
	private SpriteScroller myItemScroller;
	
	public SpritesPage(){
		myPane = new HBox();
		myPlayerScroller = new SpriteScroller("Player");
		myEnemyScroller = new SpriteScroller("Enemy");
		myTerrainScroller = new SpriteScroller("Terrain");
		myItemScroller = new SpriteScroller("Item");
		
		myPane.getChildren().addAll(
				myPlayerScroller.getNode(),
				myEnemyScroller.getNode(),
				myTerrainScroller.getNode(),
				myItemScroller.getNode()
				);
		
	}
	
	public Region getRegion(){
		return myPane;
	}
	
	@Override
	public String toString(){
		return "Sprite Editor";
	}
}
