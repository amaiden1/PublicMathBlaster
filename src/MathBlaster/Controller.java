package MathBlaster;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Controller {

    //I am testing this to test committing to git
	private Scene scene;
	private Pane pane;
	private boolean leftPressed;
	private boolean rightPressed;
	private Shooter shooty;

	private final int DELTA = 5;

	public Controller() {
		pane = new Pane();
		scene = new Scene(pane);
		shooty = new Shooter(400, 400);

		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.LEFT) {
				leftPressed = true;
			}
			if(event.getCode() == KeyCode.RIGHT) {
				rightPressed = true;
			}
			System.out.println("a key pressed");
		});
		scene.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.LEFT) {
				leftPressed = false;
			}
			if(event.getCode() == KeyCode.RIGHT) {
				rightPressed = false;
			}
			System.out.println("a key released");
		});

		// timeline to update player
		Timeline playerUpdate = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			updatePlayer();
		}));
		playerUpdate.setCycleCount(Timeline.INDEFINITE);
		playerUpdate.play();

		// add all things to pane
		pane.getChildren().add(shooty.getIV());

	}

	public void updatePlayer() {

		if(rightPressed) {
			// move left
			shooty.incX(DELTA);
		}
		if(leftPressed) {
			// move left
			shooty.decX(DELTA);
		}

	}

	public Scene getScene() {
		return scene;
	}

}
