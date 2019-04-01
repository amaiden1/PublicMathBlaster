package MathBlaster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Random;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Controller {

	//plz work
    // this is another comment
	private Scene scene;
	private Stage stage;
	private Pane pane;
	private Timeline update;
	private Timeline buttonTimeline;
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean isPaused;
	private boolean fastMode;
	private Shooter shooty;
	private ArrayList<Bullet> bulletsOnScreen;
	private ArrayList<Button> buttList;
	private int currentLevel;
	private int answer;
	private int minusButtSpeed = 1;
	private Button answerBox;
	private final boolean DEV_MODE = false;
	private final int ANSWER_LIMIT = 5;
	private final int NUM_BUTTONS = 5;
	private Label levelLabel;
	private Label livesLabel;
	private Label equationLabel;
	private Player player = new Player((DEV_MODE)?Integer.MAX_VALUE:3, 0);
	
//	AudioClip shoot = new AudioClip(this.getClass().getResource("/sounds/Blaster.wav").toString());
	//AudioClip move = new AudioClip(this.getClass().getResource("/sounds/move.wav").toString());
	

	private final int SHOOTER_DELTA = 5;
	private final int BULLET_DELTA = 3;

	public Controller() {
		pane = new Pane();
		pane.setPrefSize(600,600);
		scene = new Scene(pane);
		stage = new Stage();
		stage.setScene(scene);
		stage.show();
		scene.getStylesheets().addAll("mathblaster.css");


		pane.setStyle("-fx-background-image: url(\"/img/galaxy.jpg\"); -fx-background-repeat: stretch; -fx-background-size: 600 600; -fx-text-fill: white; -fx-background-position: center center;");
		shooty = new Shooter(pane.getPrefWidth()/2.0, pane.getPrefHeight()-80.0);
		bulletsOnScreen = new ArrayList<>();
		
		resetButtons();
		

		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.LEFT) {
			    leftPressed = true;
				//move.play();
			}
			if(event.getCode() == KeyCode.RIGHT) {
			    rightPressed = true;
				//move.play();
			}
			if(event.getCode() == KeyCode.SPACE) {
				// shoot
				//shoot.play();
				Bullet bullet = shooty.shoot();
				bulletsOnScreen.add(bullet);
				pane.getChildren().add(bullet.getIV());
			}
				System.out.println("SHOOTING");
				
			System.out.println("a key pressed");
		});

		scene.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.LEFT) {
			    leftPressed = false;
			}
			if(event.getCode() == KeyCode.RIGHT) {
			    rightPressed = false;
			}
			if(event.getCode() == KeyCode.ESCAPE) {
				//pause
				if(!isPaused) {
					update.pause();
					isPaused = true;

					// create pause menu
					try {

						// set up OVs for continue and exit triggers
						BooleanProperty continueValue = new SimpleBooleanProperty(false);
						BooleanProperty quitValue = new SimpleBooleanProperty(false);

						// create and show the menu
						Stage pauseStage = new Stage();
						PauseMenu pauseMenu = new PauseMenu();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseMenu.fxml"));
						loader.setController(pauseMenu);
						Pane root = loader.load();
						// do post init things here
						pauseMenu.setThisStage(pauseStage);
						pauseMenu.setContinueValueListener(continueValue);
						pauseMenu.setQuitValueListener(quitValue);
						// end post init things
						pauseStage.setTitle("Game Over");
						pauseStage.setScene(new Scene(root));
						pauseStage.initStyle(StageStyle.UNDECORATED);
						pauseStage.show();

						// these OVs trigger whenever the user clicks conitnue or exit respectively
						continueValue.addListener(observable -> update.play());
						quitValue.addListener(observable -> Platform.exit());

						//new Alert(Alert.AlertType.NONE,"You are currently paused", new ButtonType("Continue playing")).showAndWait();
					}
					catch (IOException e) {

					}
				}
				else {
					update.play();
					isPaused = false;
				}
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
                    for (Button butt : buttList) {
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
            	// do nothing, sweet sweet nothing
            }

		}));
		update.setCycleCount(Timeline.INDEFINITE);
		update.play();


		//moves the buttons down
		// original values: 3 seconds, 10 pixels
		buttonTimeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {
			for (Button butt: buttList){
				butt.setLayoutY(butt.getLayoutY() + 0.3 * (this.fastMode ? this.currentLevel : 1));

				//Collision hasn't been dealt with yet

			}
		}));

		buttonTimeline.setCycleCount(Timeline.INDEFINITE);
		buttonTimeline.play();

	}
	public Controller(boolean b)
	{
		// does nothing, but it's supposed to ;)
	}
	
	private void resetButtons()
	{
		buttList = new ArrayList<>();
		for(int i = 0; i < NUM_BUTTONS; i++)
		{
			buttList.add(new Button());
			buttList.get(i).setPrefSize(120, 120);
			buttList.get(i).relocate(i * 120,30);
			buttList.get(i).setDisable(true);
			buttList.get(i).setFont(new Font(30));
			buttList.get(i).setTextFill(Paint.valueOf("0xbf3de2"));
			pane.getChildren().add(buttList.get(i));
		}
	}
	//test?
	private void updateLabels(){
		levelLabel = new Label("Level: " + currentLevel);
		levelLabel.relocate(0,0);
		levelLabel.setTextFill(Color.WHITE);
		levelLabel.setFont(new Font(25));
		equationLabel = new Label("Answer: " + answer);
		equationLabel.relocate(200, 0);
		equationLabel.setTextFill(Color.WHITE);
		equationLabel.setFont(new Font(25));
		livesLabel = new Label("Lives: " + player.getLives());
		livesLabel.relocate(500, 0);
		livesLabel.setTextFill(Color.WHITE);
		livesLabel.setFont(new Font(25));
		pane.getChildren().addAll(levelLabel, equationLabel, livesLabel);
	}
	
	public void initialize(){
		bulletsOnScreen.clear();
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
		currentLevel = level;
		Random rand = new Random();
		answer = rand.nextInt(ANSWER_LIMIT);
		initialize();
		int answerIndex = rand.nextInt(buttList.size()-1);
		answerBox = buttList.get(answerIndex);
		ArrayList<Integer> answers = new ArrayList<>();
		for(int i = 0; i < buttList.size(); i++){
			answers.add(answer);
			if(i == answerIndex){
				buttList.get(i).setText("" + answer);
			}
			else{
				int wrongAnswer;
				do{
					wrongAnswer = rand.nextInt(ANSWER_LIMIT);
					System.out.println("generating: " + wrongAnswer);
				}while(wrongAnswer == answer || answers.contains((Integer)wrongAnswer));
				answers.set(i, wrongAnswer);
				buttList.get(i).setText("" + wrongAnswer);
			}
		}
		System.out.println("current level: " + currentLevel + "\nAnswer: " + answer + "\nBox with answer: " + buttList.indexOf(answerBox));
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
		livesLabel.setText("Lives: " + player.getLives());
		checkDeath();
	}

	public void checkDeath(){
		if (player.getLives() == 0){
			update.stop();

			try {
				Stage deathBoxStage = new Stage();
				DeathBox2 deathBox2 = new DeathBox2();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("DeathBox2.fxml"));
				loader.setController(deathBox2);
				Pane root = loader.load();
				// do post init things here
				deathBox2.setGameStage(getStage());
				deathBox2.setThisStage(deathBoxStage);
				deathBox2.setScore(player.getScore());
				deathBox2.postInit();
				// end post init things
				deathBoxStage.setTitle("Game Over");
				deathBoxStage.setScene(new Scene(root));
				deathBoxStage.show();

				if (deathBox2.isNewGame()) {
					initialize();
					player.setLives(3);
					update.play();
				}
			} catch (IOException e) {
				System.out.println("Fatal Error, cannot find the death box files. The game will crash.");
			}

			/*
			DeathBox deathbox = new DeathBox(this.getStage());
			deathbox.display("Game Over!", "Want to play again?", update);
			if(deathbox.setNewGame()){
				initialize();
				player.setLives(3);
				update.play();
            }
            */

		}
	}

	//public void scoreIncrease()
	public void resetGame(Stage primaryStage) throws IOException {

		Menu mainMenu = new Menu();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Menu.fxml"));
		loader.setController(mainMenu);
		Pane root = loader.load();
		mainMenu.postInit();
		primaryStage.setTitle("Mathblaster");
		primaryStage.setScene(new Scene(root));
		mainMenu.setMenuStage(primaryStage);
		primaryStage.show();

	}

	public void setFastMode(boolean fastMode) {
		this.fastMode = fastMode;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
