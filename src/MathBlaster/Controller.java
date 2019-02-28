package MathBlaster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

public class Controller {

	//I am testing this to test committing to git
    // this is another comment
	private Scene scene;
	private Pane pane;
	private boolean leftPressed;
	private boolean rightPressed;
	private Shooter shooty;
	private ArrayList<Bullet> bulletsOnScreen;

	private final int SHOOTER_DELTA = 5;
	private final int BULLET_DELTA = 3;

	public Controller() {
		pane = new Pane();
		pane.setPrefSize(600,600);
		scene = new Scene(pane);
		shooty = new Shooter(400, 500);
		bulletsOnScreen = new ArrayList<>();

		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.LEFT) {
			    leftPressed = true;
			}
			if(event.getCode() == KeyCode.RIGHT) {
			    rightPressed = true;
			}
			if(event.getCode() == KeyCode.SPACE) {
				// shoot
				Bullet bullet = shooty.shoot();
				bulletsOnScreen.add(bullet);
				pane.getChildren().add(bullet.getIV());
				System.out.println("SHOOTING");
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

		// main update thread, fires at 10 ms
		Timeline update = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			// update shooter
		    updateShooter();

		    // update bullets
			for(Bullet b : bulletsOnScreen) {
				b.decY(BULLET_DELTA);
				if(b.willDespawn()) {
					// despawn bullet
					pane.getChildren().remove(b.getIV());
				}
			}
		}));
		update.setCycleCount(Timeline.INDEFINITE);
		update.play();

		// add all things to pane
		pane.getChildren().add(shooty.getIV());

	}

	public void updateShooter() {

		if(rightPressed) {
		    // move left
            shooty.incX(SHOOTER_DELTA);
		}
		if(leftPressed) {
		    // move left
			shooty.decX(SHOOTER_DELTA);
		}

	}

	public Scene getScene() {
	    return scene;
	}

}
