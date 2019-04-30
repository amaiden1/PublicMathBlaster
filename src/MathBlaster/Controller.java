package MathBlaster;

import com.sun.org.apache.xerces.internal.impl.dv.xs.BooleanDV;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelAttribute;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.beans.binding.Bindings;
import java.io.IOException;
import java.security.Key;
import java.util.ConcurrentModificationException;
import java.util.Random;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private Meteor meteor;
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
	private Player player;
	private boolean dead;
	private boolean devMode;

	private AudioClip shoot;
	private AudioClip move;
	private AudioClip endGame;
	private AudioClip bulletHit;

	public Controller(int _difficulty, boolean _fastMode, int shipNum) {
		score = 0;
		streak = 0;
		difficulty = _difficulty;
		fastMode = _fastMode;
		dead = false;

		player = new Player(3, 0);

		// Override this to set it to true
		// Otherwise, press F9 in game to activate it
		devMode = false;

        Media m = new Media(getClass().getResource("/media/mblastBg (2).mp4").toExternalForm());
        Media m2 = new Media(getClass().getResource("/media/final_5cbe6ab076e9430014769b98_434486.mp4").toExternalForm());

		final MediaPlayer bgVid = new MediaPlayer(m);
		final MediaPlayer bgVid2 = new MediaPlayer(m2);
        MediaView bgView = new MediaView(bgVid);
        MediaView bgView2 = new MediaView(bgVid2);

        bgView.setMediaPlayer(bgVid);
        bgView2.setMediaPlayer(bgVid2);

        //bgVid.setRate(20);
        bgVid.setCycleCount(MediaPlayer.INDEFINITE);
        bgVid.play();

        bgVid2.setRate(20);
        bgVid2.setCycleCount(MediaPlayer.INDEFINITE);
        bgVid2.play();


        pane = new Pane();
        pane.setPrefSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        pane.setMaxSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        pane.setMinSize(SCREEN_WIDTH, SCREEN_HEIGHT);


        bgView.setPreserveRatio(true);
        bgView.setFitHeight(pane.getHeight());
        bgView.setFitWidth(pane.getWidth());

        DoubleProperty mvw = bgView.fitWidthProperty();
        DoubleProperty mvh = bgView.fitHeightProperty();
        mvw.bind(Bindings.selectDouble(bgView.sceneProperty(), "width"));
        mvh.bind(Bindings.selectDouble(bgView.sceneProperty(), "height"));

        StackPane group = new StackPane(bgView);
        group.getChildren().add(pane);

        //Group group2 = new Group(bgView2);

        //bgView2.setLayoutY(300);

		scene = new Scene(group);
		stage = new Stage();
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
		stage.setResizable(false);
		scene.getStylesheets().addAll("mathblaster.css");

		shoot = new AudioClip(this.getClass().getResource("/sounds/Blaster.wav").toString());
		move = new AudioClip(this.getClass().getResource("/sounds/Movement.wav").toString());
		endGame = new AudioClip(this.getClass().getResource("/sounds/Starship_destroyed.wav").toString());
		bulletHit = new AudioClip(this.getClass().getResource("/sounds/Explosion.wav").toString());

		shooty = new Shooter(pane.getPrefWidth()/2.0, pane.getPrefHeight()-80.0, shipNum);

		shoot.setVolume(shoot.getVolume() - .8);
		bulletsOnScreen = new ArrayList<>();
		resetButtons();

		// these two were refactored into methods down below, in an effort to make the constructor more concise
		// this makes use of an alternate lambda syntax, that automatically calls the method with the 'event' param
		scene.setOnKeyPressed(this::handleKeyPressEvents);
		scene.setOnKeyReleased(this::handleKeyReleaseEvents);

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



		//Meteor meteor[] = new Meteor[5];

		//meteor[0] = new Meteor();

		//group.getChildren().add(meteor.getIV());
		//meteor.getIV().setLayoutY(butt.getLayoutY() + 0.03 * (this.fastMode ? this.currentLevel : 1)+.25);



		Timeline meteorTimeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {



		}));
		meteorTimeline.setCycleCount(Animation.INDEFINITE);
		meteorTimeline.play();

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
			// this is intentional
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
			pauseMenu.setGameStage(stage);
			pauseMenu.setupWindowProperties();
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
					try {
						exitToMainMenu();
					} catch (IOException e) {
						new Alert(Alert.AlertType.ERROR, "Fatal error: Could not load main menu. The game will now exit." +
							"Please submit a bug report to our developers. (Details: An IOException occurred when trying to instantiate the" +
							" MainMenu stage from Controller.pause().)").showAndWait();
						Platform.exit();
					}
				}
				else if(newValue.intValue() == PAUSE_RESPONSE_QUIT /* 2 */) {
					Platform.exit();
				}
			});

		}
		catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Fatal error: Could not load pause menu. The game will now exit." +
				"Please submit a bug report to our developers. (Details: An IOException occurred when trying to instantiate the" +
				" PauseMenu stage.)").showAndWait();
		}
	}

	private void handleKeyPressEvents(KeyEvent event) {
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
		if(event.getCode() == KeyCode.CLOSE_BRACKET) {
			// turns on dev mode
			showToast("Dev Mode Enabled");
			devMode = true;
			player.setLives(Integer.MAX_VALUE);
		}
		if(event.getCode() == KeyCode.F && devMode) {
			// turns on fast mode if in dev mode
			showToast("Fast Mode On");
			fastMode = true;
		}
		if(event.getCode() == KeyCode.BACK_QUOTE && devMode) {
			newLevel(currentLevel+1);
			showToast("Skipped Level");
		}
	}

	private void handleKeyReleaseEvents(KeyEvent event) {
		if(event.getCode() == KeyCode.LEFT) {
			leftPressed = false;
		}
		if(event.getCode() == KeyCode.RIGHT) {
			rightPressed = false;
		}
		if(event.getCode() == KeyCode.ESCAPE) {
			pause();
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
			buttList.get(i).setFont(new Font(27));
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
	private void newLevel(int level){
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
				}while(wrongAnswer == answer || answers.contains(wrongAnswer));
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

	private void minusLife(){
		player.setLives(player.getLives() - 1);
		System.out.print(player.getLives());
		livesLabel.setText("Lives: " + player.getLives());
		checkDeath();
	}

	private void checkDeath() {
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
		if(!dead) {
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
				deathBox2.setScore((int) score);
				// end post init things
				deathBoxStage.setTitle("Game Over");
				deathBoxStage.setScene(new Scene(root));
				deathBox2.postInit();
				deathBoxStage.show();
				dead = true;

				if (deathBox2.isNewGame()) {
					initialize();
					player.setLives(3);
					update.play();
				}
			} catch (IOException e) {
				System.out.println("Fatal Error: cannot load death box FXML. The game will crash.");
			}
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

	public void exitToMainMenu() throws IOException {

		Menu mainMenu = new Menu();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
		loader.setController(mainMenu);
		Pane root = loader.load();
		mainMenu.postInit();
		Stage primaryStage = new Stage();
		mainMenu.setMenuStage(primaryStage);
		primaryStage.setTitle("Mathblaster");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setScene(new Scene(root));
		stage.hide();
		primaryStage.show();

	}

	private void showToast(String text) {
		Label toastLbl = new Label(text);
		toastLbl.setTextFill(Paint.valueOf("white"));
		toastLbl.setStyle("-fx-font-size: 16");
		toastLbl.relocate(10, 570);
		Timeline toastTimeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
			pane.getChildren().remove(toastLbl);
		}));
		pane.getChildren().add(toastLbl);
		toastTimeline.setCycleCount(1);
		toastTimeline.play();
	}

	public void setFastMode(boolean fastMode) {
		this.fastMode = fastMode;
	}

	private Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}

















