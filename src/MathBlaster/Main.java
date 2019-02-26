package MathBlaster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		Pane root = new Pane();

		Label hi = new Label("Goodbye, world!");
		hi.setFont(Font.font(30));
		hi.relocate(10,10);
		root.getChildren().add(hi);

		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 300, 275));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
