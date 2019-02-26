package MathBlaster;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {

    private Stage window;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;

        Pane root = new Pane();

        //Label hi = new Label("Goodbye, world!");
        Button test = new Button("Click Me");
        //hi.setFont(Font.font(30));
        //hi.relocate(10,10);
        root.getChildren().add(test);
        test.relocate(350, 200);

        scene = new Scene(root, 800, 600);

        //STYLE USED for all boxes, buttons, font, etc

        scene.getStylesheets().add("mathblaster.css");

        window.setTitle("Hello World");
        window.setScene(scene);
        window.setResizable(false);
        window.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
