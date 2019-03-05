package MathBlaster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ConcurrentModificationException;
import java.util.Random;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Controller {

	//I am testing this to test committing to git
    // this is another comment
	private Scene scene;
	private Pane pane;
	private Timeline update;
	private boolean leftPressed;
	private boolean rightPressed;
	private Shooter shooty;
	private ArrayList<Bullet> bulletsOnScreen;
	private ArrayList<Button> getRect;
	private int currentLevel;
	private int answer;
	private Button answerBox;
	private final int ANSWER_LIMIT = 5;
	private final int NUM_BUTTONS = 5;
	private Label levelLabel;
	private Label livesLabel;
	private Label equationLabel;
	private Player player = new Player(3, 0);

	private final int SHOOTER_DELTA = 5;
	private final int BULLET_DELTA = 3;

	public Controller() {
		pane = new Pane();
		pane.setPrefSize(600,600);
		scene = new Scene(pane);
		shooty = new Shooter(400, 500);
		bulletsOnScreen = new ArrayList<>();
		
		resetButtons();

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
		newLevel(1);
		// main update thread, fires at 10 ms
		update = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			// update shooter
		    updateShooter();
			
		    // update bullets
            try {
                for (Bullet b : bulletsOnScreen) {
                    b.decY(BULLET_DELTA);
                    for (Button butt : getRect) {
                        if (b.getIV().getBoundsInParent().intersects(butt.getBoundsInParent())) {
							if(butt == answerBox){
								System.out.println("good work!");
								newLevel(currentLevel+1);
							}
							else{
								System.out.println("Wrong button, pal");
								minusLife();
							}
                            pane.getChildren().remove(butt);
                            bulletsOnScreen.remove(b);
                            
                            pane.getChildren().remove(b.getIV());
                            System.out.println("Boom!");
                        }
                    }
                    if (b.willDespawn()) {
                        // despawn bullet
                        pane.getChildren().remove(b.getIV());
						bulletsOnScreen.remove(b);
                    }
                }
            } catch (ConcurrentModificationException e) {
            }

//why so many spaces??? 

		}));
		update.setCycleCount(Timeline.INDEFINITE);
		update.play();
	}
	
	private void resetButtons(){
		getRect = new ArrayList<>();
		for(int i = 0; i < NUM_BUTTONS; i++){
			getRect.add(new Button());
			getRect.get(i).setPrefSize(120, 120);
			getRect.get(i).relocate(i * 120,30);
			getRect.get(i).setDisable(true);
			pane.getChildren().add(getRect.get(i));
		}
	}
	//test?
	private void updateLabels(){
		levelLabel = new Label("level: " + currentLevel);
		levelLabel.relocate(0,0);
		equationLabel = new Label("Answer: " + answer);
		equationLabel.relocate(50, 0);
		livesLabel = new Label("Lives: " + player.getLives());
		livesLabel.relocate(200, 0);
		pane.getChildren().addAll(levelLabel, equationLabel, livesLabel);
	}
	
	public void initialize(){
		pane.getChildren().clear();
		//must reset buttons
		resetButtons();
		//must relocate player
		pane.getChildren().add(shooty.getIV());
		//must update labels
		updateLabels();
	}
	
	/**
	 * initializes new level
	 * @param level the numerical value of the new level
	 */
	public void newLevel(int level){
		initialize();
		currentLevel = level;
		Random rand = new Random();
		answer = rand.nextInt(ANSWER_LIMIT);
		int answerIndex = rand.nextInt(getRect.size()-1);
		answerBox = getRect.get(answerIndex);
		ArrayList<Integer> answers = new ArrayList<>();
		for(int i = 0; i < getRect.size(); i++){
			answers.add(answer);
			if(i == answerIndex){
				getRect.get(i).setText("" + answer);
			}
			else{
				int wrongAnswer;
				do{
					wrongAnswer = rand.nextInt(ANSWER_LIMIT);
					System.out.println("generating: " + wrongAnswer);
				}while(wrongAnswer == answer || answers.contains((Integer)wrongAnswer));
				answers.set(i, wrongAnswer);
				getRect.get(i).setText("" + wrongAnswer);
			}
		}
		System.out.println("current level: " + currentLevel + "\nAnswer: " + answer + "\nBox with answer: " + getRect.indexOf(answerBox));
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

	public void minusLife(){
		player.setLives(player.getLives() - 1);
		System.out.print(player.getLives());
		checkDeath();
	}

	public void checkDeath(){
		if (player.getLives() <= 0){
			update.stop();
			DeathBox deathbox = new DeathBox();
			deathbox.display("Game Over!", "Want to play again?", update);
			if(deathbox.setNewGame()){
			   player.setLives(3);
            }
		}
	}

	//public void scoreIncrease()
	public void resetGame(){
		System.out.println("Well yes button works");
	}
}