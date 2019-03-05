/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathBlaster;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Nolan
 */
public class Main extends Application {

 	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Menu mainMenu = new Menu();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));
		loader.setController(mainMenu);
		Pane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Mathblaster");
        primaryStage.setScene(scene);
		mainMenu.setMenuStage(primaryStage);
        primaryStage.show();
	}
}
