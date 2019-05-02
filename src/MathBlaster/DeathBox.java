package MathBlaster;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.animation.Timeline;

import java.io.IOException;

public class DeathBox{
    //Variables gotten from pop up box
    private int answer;
    private boolean newGame = false;
	private Stage stage;
	
	public DeathBox(Stage stayyyyge)
	{
		stage = stayyyyge;
	}
	
    public int display(String title, String message, Timeline game) {
		Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yes = new Button("Yes");

        VBox layout = new VBox(10);
        layout.getStyleClass().addAll("mathblaster");


        yes.setOnAction(event -> {
            stage.close();
			Controller controller = new Controller(false);
            window.close();
            Stage primaryStage = new Stage();
            try {
                controller.resetGame(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        layout.getChildren().addAll(label, yes);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

        return answer;
    }

    public boolean setNewGame(){
        boolean check = false;
        if (newGame){
            check = true;
        }
        return check;
    }
}
