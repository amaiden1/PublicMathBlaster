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

public class Controller extends Main {

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
	private Player player = new Player(3, 0);

	private final int SHOOTER_DELTA = 5;
	private final int BULLET_DELTA = 3;

	public Controller() {
		pane = new Pane();
		pane.setPrefSize(600,600);
		scene = new Scene(pane);
		shooty = new Shooter(400, 500);
		bulletsOnScreen = new ArrayList<>();
		getRect = new ArrayList<>();
	
		//Blocks
		for (int i = 0; i <= 4; i++) 
		{
		getRect.add(new Button(Integer.toString(new Random().nextInt(11))));
		getRect.get(i).setPrefSize(120, 120);
		getRect.get(i).relocate(i * 120,0);
		getRect.get(i).setDisable(true);
			
		}

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
                            pane.getChildren().remove(butt);

                    /*
						try {
                            bulletsOnScreen.remove(b);
                        }catch (Exception e) {

                        }
                    */
                            bulletsOnScreen.remove(b);
                            //b = null;
                            minusLife();
                            pane.getChildren().remove(b.getIV());
                            System.out.println("Boom!");
                        }
                    }
                    if (b.willDespawn()) {
                        // despawn bullet
                        pane.getChildren().remove(b.getIV());
                    }
                }

            } catch (ConcurrentModificationException e) {
				
            }



		}));
		update.setCycleCount(Timeline.INDEFINITE);
		update.play();

		// add all things to pane
		pane.getChildren().addAll(shooty.getIV(),getRect.get(0),
				getRect.get(1),getRect.get(2),getRect.get(3),getRect.get(4));
		
	}
	
	/**
	 * initializes new level
	 * @param level 
	 */
	public void newLevel(int level){
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
		if (player.getLives() == 0){
			update.stop();
			DeathBox deathbox = new DeathBox();
			deathbox.display("Game Over!", "Want to play again?", update);
			if(deathbox.setNewGame()){
			    update.play();
			   update.playFromStart();
			   player.setLives(3);
            }

		}
	}

	//public void scoreIncrease()

}
