package gameplayer.front_end.popup;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class PlayerOptionsPopUp {

	private List<HBox> myOptions;
	
	public PlayerOptionsPopUp(){
		myOptions = new ArrayList<HBox>();
	}
	
	public List<HBox> addOptions(){
		createFontOption();
		createKeyInputOption();
		return myOptions;
	}

	private void createKeyInputOption() {
		HBox keyOption = new HBox(20);
		keyOption.getChildren().add(new Label("Key Input: "));
		final ToggleGroup keyboardGroup = new ToggleGroup();
		RadioButton defaultKey = new RadioButton("Default");
		defaultKey.setToggleGroup(keyboardGroup);
		defaultKey.setSelected(true);
		RadioButton leftKeys = new RadioButton("Left (WASD)");
		leftKeys.setToggleGroup(keyboardGroup);
		leftKeys.setSelected(false);
		keyboardGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				
			}
		});
		keyOption.getChildren().addAll(defaultKey, leftKeys);
		myOptions.add(keyOption);
	}

	private void createFontOption() {
		HBox fontOption = new HBox(20);
		fontOption.getChildren().add(new Label("HUD Font Color: "));
		final ToggleGroup fontGroup = new ToggleGroup();
		RadioButton red = new RadioButton("Red");
		red.setToggleGroup(fontGroup);
		red.setSelected(true);
		red.setUserData("Red");
		RadioButton green = new RadioButton("Green");
		green.setToggleGroup(fontGroup);
		green.setUserData("Green");
		RadioButton blue = new RadioButton("Blue");
		blue.setToggleGroup(fontGroup);
		blue.setUserData("Blue");
		fontGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(fontGroup.getSelectedToggle() != null){
					
				}
			}
		});
		fontOption.getChildren().addAll(red, green, blue);
		myOptions.add(fontOption);
	}
}