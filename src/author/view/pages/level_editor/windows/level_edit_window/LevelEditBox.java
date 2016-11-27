/**
 * 
 */
package author.view.pages.level_editor.windows.level_edit_window;


import java.io.File;
import author.view.util.FileLoader;
import author.view.util.FileLoader.FileType;
import author.view.util.authoring_buttons.ButtonFactory;
import game_data.Level;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Cleveland Thompson V (ct168)
 *
 */
class LevelEditBox {

	//private static final String levelEditResources ="author.view.resources.LevelEditResources.properties";
	private String backgroundPath;
	private TextField nameField = new TextField();
	private TextField widthField = createNumberField();
	private TextField heightField = createNumberField();
	private Level level;
	
	private VBox container;
	LevelEditBox() {
		this.container = initializeVBox();
		this.level = new Level(null, 0, 0, null);
	}
	
	LevelEditBox(Level aLevel){
		this();
		this.level = aLevel;
		this.backgroundPath = aLevel.getBackgroundImageFilePath();
		this.nameField.setText(aLevel.getName());
		this.widthField.setText(new Integer(aLevel.getWidth()).toString());
		this.heightField.setText(new Integer(aLevel.getHeight()).toString());
	}
	
	public VBox getEditBox(){
		return this.container;
	}
	
	public Level getLevel(){
		this.level.setBackgroundImageFilePath(this.backgroundPath);
		this.level.setName(nameField.getText());
		this.level.setWidth(Integer.parseInt(widthField.getText()));
		this.level.setHeight(Integer.parseInt(heightField.getText()));
		return this.level;
	}
	
	
	private VBox initializeVBox(){
		VBox editBox = new VBox();
		
		editBox.getChildren().add(createHBox(new Label("Name: "), nameField));
		editBox.getChildren().add(createHBox(new Label("Width:"), widthField));
		editBox.getChildren().add(createHBox(new Label("Height:"), heightField));
		editBox.getChildren().add(new ButtonFactory().createButton("Choose Background", e -> updateBackgroundString()).getButton());
		
		return editBox;
	}
	
	private void updateBackgroundString(){
		File file = new FileLoader(
				FileType.GIF, 
				FileType.JPEG, 
				FileType.PNG,
				FileType.JPG ).loadImage();
		
		this.backgroundPath = file.toURI().toString();
	}
	
	private HBox createHBox(Node...nodes){
		HBox hBox = new HBox();
		hBox.getChildren().addAll(nodes);
		return hBox;
	}
	
	private TextField createNumberField(){
		TextField myNumberField = new TextField();
		myNumberField.textProperty().addListener(makeOnlyNumberProperty(myNumberField));
		return myNumberField;
	}
	
	private ChangeListener<? super String> makeOnlyNumberProperty(TextField aTextField){
		return (obs, ov, nv) -> { 
			if(!nv.matches("\\d+(\\.\\d+)")){
				aTextField.setText(nv.replaceAll("[^\\d+(\\.\\d+)]", ""));
			}
		}; 
	}
	

}
