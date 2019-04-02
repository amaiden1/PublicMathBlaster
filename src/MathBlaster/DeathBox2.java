package MathBlaster;

import com.sun.jnlp.FileSaveServiceImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.management.StandardEmitterMBean;
import javax.naming.Name;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class DeathBox2 {

	@FXML private Label hsHighScoreLabel;
	@FXML private Button quitBtn;
	@FXML private Button mainMenuBtn;
	@FXML private Label scoreLabel;
	@FXML private TextField hsNameField;
	@FXML private Label hsErrorLabel;
	private Stage gameStage;
	private Stage thisStage;
	private boolean newGame;
	private int score = -1;
	private String nameResult;

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
		thisStage.setOnCloseRequest(event -> Platform.exit());
		thisStage.initStyle(StageStyle.UNDECORATED);
	}

	@FXML
	private void mainMenuBtnClicked() {

		// do high score stuff
		String hsText = hsNameField.getText();
		if(hsText.isEmpty()) {
			hsErrorLabel.setText("Hold up - please enter a name");
		}
		else if(hsText.contains(";")) {
			hsErrorLabel.setText("Hold up - your name can't contain a semicolon (;)");
		}
		else {
			nameResult = hsText;
			saveHighScore();
			newGame = true;
			// now close this and start a new game
			gameStage.close();
			Controller controller = new Controller(false);
			thisStage.close();
			Stage primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			try {
				controller.resetGame(primaryStage);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@FXML
	private void quitBtnClicked() {
		// do high score stuff
		String hsText = hsNameField.getText();
		if(hsText.isEmpty()) {
			hsErrorLabel.setText("Hold up - please enter a name");
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

	private void saveHighScore() {
		try {
			BufferedWriter buff = new BufferedWriter(new FileWriter("highscores.txt", true));
			buff.write(nameResult + ";" + score + "\n");
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Error reading the high scores file. It might be corrupt. The exception was: " + e.getMessage()).showAndWait();
		}
	}

	public boolean isNewGame(){
		return newGame;
	}

}
