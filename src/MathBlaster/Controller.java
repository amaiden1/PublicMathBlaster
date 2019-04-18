package MathBlaster;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ConcurrentModificationException;
import java.util.Random;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;

import static MathBlaster.Constants.*;

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
	//private Meteor meteor;
	private ArrayList<Bullet> bulletsOnScreen;
	private ArrayList<Button> buttList;
	private int currentLevel;
	private int answer;
	private int minusButtSpeed = 1;
	private int difficulty;
	private int streak;
	private double score;
	private Button answerBox;
	private EquationGenerator equationGenerator;
	private Label scoreLabel;
	private Label livesLabel;
	private Label equationLabel;
	private Player player = new Player((DEV_MODE)?Integer.MAX_VALUE:3, 0);

	private AudioClip shoot;
	private AudioClip move;
	private AudioClip endGame;
	private AudioClip bulletHit;

	public Controller(int _difficulty, boolean _fastMode) {
		score = 0;
		streak = 0;
		difficulty = _difficulty;
		fastMode = _fastMode;

        Media m = new Media(Paths.get("/Users/mac/Documents/Google Sync Mac/School/ITEC 370/mathblaster/src/img/background2.mp4").toUri().toString());
        final MediaPlayer bgVid = new MediaPlayer(m);
        MediaView bgView = new MediaView(bgVid);
        bgView.setMediaPlayer(bgVid);
        bgVid.setRate(20);
        bgVid.setCycleCount(MediaPlayer.INDEFINITE);
        bgVid.play();

        pane = new Pane();
        pane.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		scene = new Scene(pane);
		stage = new Stage();
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
		scene.getStylesheets().addAll("mathblaster.css");

		shoot = new AudioClip(this.getClass().getResource("/sounds/Blaster.wav").toString());
		move = new AudioClip(this.getClass().getResource("/sounds/Movement.wav").toString());
		endGame = new AudioClip(this.getClass().getResource("/sounds/Starship_destroyed.wav").toString());
		bulletHit = new AudioClip(this.getClass().getResource("/sounds/Explosion.wav").toString());




		shooty = new Shooter(pane.getPrefWidth()/2.0, pane.getPrefHeight()-80.0);

		shoot.setVolume(shoot.getVolume() - .8);
		bulletsOnScreen = new ArrayList<>();
		resetButtons();

		scene.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.LEFT) {
			    leftPressed = true;
				move.play();
			}
			if(event.getCode() == KeyCode.RIGHT) {
			    rightPressed = true;
				move.play();
			}
			if(event.getCode() == KeyCode.SPACE) {
				// shoot
				shoot.play();
				Bullet bullet = shooty.shoot();
				bulletsOnScreen.add(bullet);
				pane.getChildren().add(bullet.getIV());

			}
			if(event.getCode() == KeyCode.BACK_QUOTE && DEV_MODE)
			{
				newLevel(currentLevel+1);
			}				
		});

		scene.setOnKeyReleased(event -> {
			if(event.getCode() == KeyCode.LEFT) {
			    leftPressed = false;
			}
			if(event.getCode() == KeyCode.RIGHT) {
			    rightPressed = false;
			}
			if(event.getCode() == KeyCode.ESCAPE) {
				pause();
			}
		});

		equationGenerator = new EquationGenerator(difficulty);
		newLevel(1);
		// main update thread, fires at 10 ms
		update = new Timeline(new KeyFrame(Duration.millis(10), event -> {
			// update shooter
		    updateShooter();
		    updateBullets();
		}));
		update.setCycleCount(Timeline.INDEFINITE);
		update.play();

		//moves the buttons down
		// original values: 3 seconds, 10 pixels

		buttonTimeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
			for (Button butt: buttList) {

				butt.setLayoutY(butt.getLayoutY() + 0.03 * (this.fastMode ? this.currentLevel : 1)+.25);

				if (butt.getLayoutY() == 500){
				    player.setLives(0);
                }
				checkDeath();
			}
		}));

		buttonTimeline.setCycleCount(Timeline.INDEFINITE);
		buttonTimeline.play();
	}

	public Controller(boolean b)
	{
		// does nothing, but it's supposed to ;)
	}

	private void updateBullets() {
		try {
			for (Bullet b : bulletsOnScreen) {
				b.decY(BULLET_DELTA);
				for (Button butt : buttList) {

					if (b.getPixel().getBoundsInParent().intersects(butt.getBoundsInParent())) {
						if(butt == answerBox) {
							double distance = shooty.getY() - b.getY();
							System.out.println("DISTANCE: " + distance);
							updateScore(distance);
							bulletHit.play();
							newLevel(currentLevel+1);
						}
						else {
							streak = 0;
							minusLife();
							bulletHit.play();
						}
						pane.getChildren().remove(butt);
						bulletsOnScreen.remove(b);

						pane.getChildren().remove(b.getIV());
					}
				}
				if (b.willDespawn()) {
					// despawn bullet
					pane.getChildren().remove(b.getIV());
					bulletsOnScreen.remove(b);
				}
			}
		}
		catch (ConcurrentModificationException e) {
			// do nothing
		}
	}

	private void pause() {
		update.pause();
		buttonTimeline.pause();
		isPaused = true;

		// create pause menu
		try {

			// set up OV for continue, main menu, and exit triggers
			// this is updated whenever the player clicks a button in the menu
			IntegerProperty actionValue = new SimpleIntegerProperty(-1);

			// create and show the menu
			Stage pauseStage = new Stage();
			PauseMenu pauseMenu = new PauseMenu();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseMenu.fxml"));
			loader.setController(pauseMenu);
			Pane root = loader.load();
			// do post init things here
			pauseMenu.setThisStage(pauseStage);
			pauseMenu.setValueListener(actionValue);
			// end post init things
			pauseStage.setTitle("Game Over");
			pauseStage.setScene(new Scene(root));
			pauseStage.initStyle(StageStyle.UNDECORATED);
			pauseStage.show();

			// these OVs trigger whenever the user clicks conitnue or exit respectively
			actionValue.addListener((observable, oldValue, newValue) -> {
				if(newValue.intValue() == PAUSE_RESPONSE_CONTINUE /* 0 */) {
					update.play();
					buttonTimeline.play();
					isPaused = false;
				}
				else if(newValue.intValue() == PAUSE_RESPONSE_MAIN_MENU /* 1 */) {
					// yet to be implemented
					System.out.println("This feature isn't implemented yet");
				}
				else if(newValue.intValue() == PAUSE_RESPONSE_QUIT /* 2 */) {
					Platform.exit();
				}
			});

		}
		catch (IOException e) {
			System.out.println("Fatal Error: cannot load pause menu FXML. The game will crash.");
		}
	}
	
	private void updateScore(double distance){
		//difficulty = baseScore
		//distance multiplier = distance/370(full length between player and the answer boxes) + 1
		//fastmode multiplier: 1.99 + level/100
		//streak multiplier: streak/20 + 1
		//fastMode equation: difficulty * distanceMultiplier * fastModeMultiplier * streakMultiplier
		int baseScore = 100 * difficulty;
		double distanceMultiplier = 1 + (distance / 370.0);
		double fastModeMultiplier = (fastMode)?1.99 + (currentLevel/100.0):1;
		double streakMultiplier = 1 + (streak/20.0);
		System.out.println("Total multiplier: " + (distanceMultiplier * fastModeMultiplier * streakMultiplier));
		score += baseScore * distanceMultiplier * fastModeMultiplier * streakMultiplier;
		streak++;
		
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
		scoreLabel = new Label("Score: " + (int)score);
		scoreLabel.relocate(0,0);
		scoreLabel.setTextFill(Color.WHITE);
		scoreLabel.setFont(new Font(25));
		equationLabel = new Label(equationGenerator.getEquation());
		equationLabel.relocate(200, 0);
		equationLabel.setTextFill(Color.WHITE);
		equationLabel.setFont(new Font(25));
		livesLabel = new Label("Lives: " + player.getLives());
		livesLabel.relocate(500, 0);
		livesLabel.setTextFill(Color.WHITE);
		livesLabel.setFont(new Font(25));
		pane.getChildren().addAll(scoreLabel, equationLabel, livesLabel);
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
		equationGenerator.newEquation();
		currentLevel = level;
		Random rand = new Random();
		answer = equationGenerator.getAnswer();
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
				}while(wrongAnswer == answer || answers.contains((Integer)wrongAnswer));
				answers.set(i, wrongAnswer);
				buttList.get(i).setText("" + wrongAnswer);
			}
		}
	}

	public void updateShooter() {

		if(rightPressed) {
		    // move right
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

	public void checkDeath() {
		// one way to die: you run out of lives
		if (player.getLives() == 0) {
            shooty.setIv(new Image("/img/shipdeath.png"));
			die();
		}
		// another way to die: the blocks hit you
		if(buttList.get(0).getLayoutY() >= FATAL_BUTTON_DIST) {
			endGame.play();
			die();
		}
	}

	private void die() {
		update.stop();
		buttonTimeline.stop();
		try {
            shooty.setIv(new Image("/img/shipdeath.png"));
			Stage deathBoxStage = new Stage();
			DeathBox2 deathBox2 = new DeathBox2();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DeathBox2.fxml"));
			loader.setController(deathBox2);
			Pane root = loader.load();
			// do post init things here
			deathBox2.setGameStage(getStage());
			deathBox2.setThisStage(deathBoxStage);
			deathBox2.setScore((int)score);
			// end post init things
			deathBoxStage.setTitle("Game Over");
			deathBoxStage.setScene(new Scene(root));
			deathBox2.postInit();
			deathBoxStage.show();

			if (deathBox2.isNewGame()) {
				initialize();
				player.setLives(3);
				update.play();
			}
		}
		catch (IOException e) {
			System.out.println("Fatal Error: cannot load death box FXML. The game will crash.");
		}
	}

	//public void scoreIncrease()
	public void resetGame(Stage primaryStage) throws IOException {

		Menu mainMenu = new Menu();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
		loader.setController(mainMenu);
		Pane root = loader.load();
		mainMenu.postInit();
		mainMenu.setMenuStage(primaryStage);
		primaryStage.setTitle("Mathblaster");
		primaryStage.setScene(new Scene(root));
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

















