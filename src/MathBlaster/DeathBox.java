package MathBlaster;

/**
 * @author Emmanuel Edamwen
 */

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.animation.Timeline;

public class DeathBox extends Controller{
    //Variables gotten from pop up box
    private int answer;
    private boolean newGame = false;

    public int display(String title, String message, Timeline game) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        Button yes = new Button("Yes");
        //Button no = new Button("No");

        VBox layout = new VBox(10);
        layout.getStyleClass().addAll("mathblaster");


        yes.setOnAction(e -> {
            Controller getMethod = new Controller();
            getMethod.resetGame();
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
        if (newGame == true){
            check = true;
        }
        return check;
    }
}
