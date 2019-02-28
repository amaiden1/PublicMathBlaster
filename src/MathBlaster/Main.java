/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MathBlaster;

import javafx.application.Application;
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

		Controller ctrl = new Controller();
		primaryStage.setScene(ctrl.getScene());
		primaryStage.show();

	}

},
