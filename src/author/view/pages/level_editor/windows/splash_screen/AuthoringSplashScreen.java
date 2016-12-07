package author.view.pages.level_editor.windows.splash_screen;

import java.io.File;

import author.controller.AuthorControllerFactory;
import author.controller.IAuthorControllerExternal;
import author.view.util.authoring_buttons.ButtonFactory;
import author.view.util.file_helpers.FileLoader;
import author.view.util.file_helpers.FileLoader.FileType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthoringSplashScreen {
	
	private Stage stage;
	private VBox menu;
	private final String TITLE = "VOOGA Authoring";
	private final String STYLESHEET = "data/GUI/author-style.css";
	private IAuthorControllerExternal authorControllerExternal;
	private TextField gameNameTextField = new TextField("[insert new game name]");


	public AuthoringSplashScreen(){
		initializeWindow();
	}

	private void initializeWindow(){
		this.stage = new Stage();
		this.stage.setTitle(TITLE);
		this.stage.setScene(initScene());
		this.stage.getScene().getStylesheets().add(getStyleSheet());
		this.stage.setResizable(false);
		this.stage.showAndWait();
	}
	
	private Scene initScene(){
		initPane();
		initToolBarBuilder();
		Scene s = new Scene(menu, 500, 500);
		return s;
	}
	
	private String getStyleSheet(){
		File css = new File(STYLESHEET);
		return css.toURI().toString();
	}
	
	private void initToolBarBuilder(){
		this.menu.getChildren().add(
				createHBox(new Label("Name: "), gameNameTextField));
		
		this.menu.getChildren().add(
				createHBox( new ButtonFactory().createButton("New Game", e -> createNewGame()).getButton("splash-button"),
							new ButtonFactory().createButton("Load Game", e -> loadSavedGame()).getButton("splash-button") ));
		
	}
	
	private void initPane(){
		this.menu = new VBox();
		menu.setAlignment(Pos.BOTTOM_CENTER);
		menu.setSpacing(10);
		menu.setPadding(new Insets(0, 20, 100, 20)); 

	}
	
	private HBox createHBox(Node...nodes){
		HBox hBox = new HBox();
		hBox.getChildren().addAll(nodes);
		hBox.setAlignment(Pos.BOTTOM_CENTER);
		hBox.setSpacing(10);
		hBox.setPadding(new Insets(0, 20, 10, 20)); 

		return hBox;
	}
	
	private VBox createVBox(Node...nodes){
		VBox vBox = new VBox();
		vBox.getChildren().addAll(nodes);
		return vBox;
	}
	
	private void createNewGame(){
		authorControllerExternal = new AuthorControllerFactory().create();
		authorControllerExternal.createNewGame(gameNameTextField.getText());
		openGame();
	}
	
	private void loadSavedGame(){
		authorControllerExternal = new AuthorControllerFactory().create(new FileLoader(FileType.XML).loadImage());
		openGame();
	}
	
	private void openGame(){
		this.stage.close();
		Stage aStage = new Stage();
		aStage.setTitle("VOOGASalad");
		Scene scene = authorControllerExternal.getScene();
		aStage.setMaximized(true);
		aStage.setResizable(true);
		aStage.setScene(scene);
		aStage.show();
	}
}

