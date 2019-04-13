package MathBlaster;

import com.sun.javafx.sg.prism.web.NGWebView;
import com.sun.jnlp.FileSaveServiceImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.management.StandardEmitterMBean;
import javax.naming.Name;
import javax.print.event.PrintJobAttributeEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class DeathBox2 {

	@FXML private Label hsHighScoreLabel;
	@FXML private Button quitBtn;
	@FXML private Button mainMenuBtn;
	@FXML private Label scoreLabel;
	@FXML private TextField hsNameField;
	@FXML private Label hsErrorLabel;
	@FXML private AnchorPane thisPane;
	private Stage gameStage;
	private Stage thisStage;
	private boolean newGame;
	private int score = -1;
	private String randName;
	private String nameResult;
	private boolean isHS;

	public void setGameStage(Stage gameStage) {
		this.gameStage = gameStage;
	}

	public void setThisStage(Stage thisStage) {
		this.thisStage = thisStage;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void postInit() {
		if(gameStage == null || thisStage == null || score == -1) {
			throw new IllegalStateException("postInit() called without setting the required variables");
		}
		scoreLabel.setText("Score: " + score);
		hsErrorLabel.setText("");
		isHS = false;
		thisStage.setOnCloseRequest(event -> Platform.exit());
		thisStage.initStyle(StageStyle.UNDECORATED);
		randName = randomName();

		isHS = isHighScore(score);
		// do we show the high score stuff?
		if(!isHS) {
			thisPane.getChildren().removeAll(hsNameField, hsErrorLabel, hsHighScoreLabel);
		} else {
			hsNameField.setPromptText(randName);
		}
	}

	@FXML
	private void mainMenuBtnClicked() {

		if(isHS) {
			// do high score stuff
			String hsText = hsNameField.getText();
			if (hsText.isEmpty()) {
				nameResult = randName;
				saveHighScore();
				resetGame();
			} else if (hsText.contains(";")) {
				hsErrorLabel.setText("Hold up - your name can't contain a semicolon (;)");
			} else {
				nameResult = hsText;
				saveHighScore();
				resetGame();
			}
		} else {
			resetGame();
		}

	}

	@FXML
	private void quitBtnClicked() {
		// do high score stuff
		String hsText = hsNameField.getText();
		if(hsText.isEmpty()) {
			nameResult = randName;
			saveHighScore();
			Platform.exit();
		}
		else if(hsText.contains(";")) {
			hsErrorLabel.setText("Hold up - your name can't contain a semicolon (;)");
		}
		else {
			nameResult = hsText;
			saveHighScore();
			// now we go commit die
			Platform.exit();
		}
	}

	private void resetGame() {
		newGame = true;
		// now close this and start a new game
		gameStage.close();
		Controller controller = new Controller(false);
		thisStage.close();
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		try {
			controller.resetGame(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isHighScore(int currentScore) {
		try {
			Scanner fileScan = new Scanner(new File("highscores.txt"));
			int lineCount = 0;
			int lowestScore = 0;
			while(fileScan.hasNextLine()) {
				// name;score
				Scanner lineScan = new Scanner(fileScan.nextLine()).useDelimiter(";");
				lineScan.next();
				int score = lineScan.nextInt();
				if(score < lowestScore) lowestScore = score;
				lineCount++;
			}
			// if there's less than 20 lines, or
			return lineCount < 20 || lowestScore < currentScore;
		} catch (Exception e) {
			new Alert(Alert.AlertType.ERROR, "Error reading the high scores file. It might be corrupt. The exception was: " + e.getMessage()).show();
			return false;
		}
	}

	private void saveHighScore() {
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter("highscores.txt", true));
			buff.write(nameResult + ";" + score + "\n");
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Error reading the high scores file. It might be corrupt. The exception was: " + e.getMessage()).show();
		}
	}

	private String randomName() {
		String[] adjs = {"Frisky", "Tangy", "Bushy", "Floaty", "Hairy", "Twisty"};
		String[] animals = {"Flamingo", "Gecko", "Kleptosaurus", "Giraffe", "Aardvark", "Piranha"};
		Random rand = new Random();
		return adjs[rand.nextInt(adjs.length)] + " " + animals[rand.nextInt(animals.length)];
	}

	public boolean isNewGame(){
		return newGame;
	}

}
