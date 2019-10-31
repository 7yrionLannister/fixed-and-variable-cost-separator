package ui;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("vs.fxml"));
		Scene s = new Scene(root);
		primaryStage.setScene(s);
		primaryStage.setTitle("Presupuetar");
		primaryStage.getIcons().add(new Image(new File("icon.svg.png").toURI().toString()));
		primaryStage.show();
	}
}
