package MathBlaster;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.media.AudioClip;
import java.io.*;
import java.util.*;

public class Menu {

	@FXML private Slider fastModeSlider;
	@FXML private Button clearHighScoresBtn;
	@FXML private Button playD1Btn;
	@FXML private Button playD2Btn;
	@FXML private Button playD3Btn;
	@FXML private Button playD4Btn;
	@FXML private Button playD5Btn;
	@FXML private Button quitBtn;
	@FXML private VBox hsLeftBox;
	@FXML private VBox hsRightBox;
	@FXML private Label clearHSConfirm;


	private AudioClip hoverSound = new AudioClip(this.getClass().getResource("/sounds/buttonhover.wav").toString());
	private Controller ctrl;
	private Stage menuStage;
	private int difficulty;
	private boolean clearBtnArmed;

	public void postInit() {
		// do initialization things
		fastModeSlider.getStyleClass().add("off-slider");
		fastModeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {/* fastModeStateLabel.setStyle(newValue.doubleValue() == 0 ? "-fx-text-fill:red;" : "-fx-text-fill:green;"));*/
			if(newValue.doubleValue() == 1) {
				// fastmode is ON
				fastModeSlider.getStyleClass().remove("off-slider");
				fastModeSlider.getStyleClass().add("on-slider");
			} else {
				// fastmode is OFF
				fastModeSlider.getStyleClass().remove("on-slider");
				fastModeSlider.getStyleClass().add("off-slider");
			}
		});
		// attempt to fix slider issues
		// not fully working yet
		/*
		fastModeSlider.setOnMouseClicked(event -> {
			if(fastModeSlider.getValue() == 1) {
				fastModeSlider.setValue(0);
			} else {
				fastModeSlider.setValue(1);
			}
		});
		*/
		clearBtnArmed = false;
		clearHSConfirm.setVisible(false);
		readHighScores();
	}

	private void readHighScores() {
		try {
			File hsFile;
			Scanner fileScan;
			try {
				// the file is there, let's use it!
				hsFile = new File("highscores.txt");
				fileScan = new Scanner(hsFile);
			} catch (FileNotFoundException e) {
				// create the file since it apparently does not exist
				BufferedWriter buff = new BufferedWriter(new FileWriter("highscores.txt"));
				buff.close();
				hsFile = new File("highscores.txt");
				fileScan = new Scanner(hsFile);
			}

			// Using barland best practices!
			Map<String, Integer> scores = new HashMap<>();
			while(fileScan.hasNextLine()) {
				System.out.println("parsing line");
				Scanner line = new Scanner(fileScan.nextLine()).useDelimiter(";");
				String name = line.next();
				int score = Integer.parseInt(line.next());
				scores.put(name, score);
			}

			scores.forEach((key, value) -> {
				Label name = new Label(key);
				name.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
				hsLeftBox.getChildren().add(name);
				Label score = new Label("" + value);
				score.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
				hsRightBox.getChildren().add(score);
			});

		} catch (Exception e) {
			// theoretically this should never happen
			e.printStackTrace();
			new Alert(Alert.AlertType.ERROR, "Error reading the high scores file. It might be corrupt. The exception was: " + e.getMessage()).showAndWait();
		}
	}

	private Label makeHighScoreLabel(String text) {
		Label result = new Label(text);
		result.setStyle("-fx-text-fill: white;");
		return result;
	}

	@FXML
	private void clearHighScoresBtnClicked() {

		if(!clearBtnArmed) {
			clearHSConfirm.setVisible(true);
			clearHighScoresBtn.setStyle("-fx-text-fill: #f74040;");
			clearHighScoresBtn.setStyle("-fx-border-color: #f74040;");
			clearBtnArmed = true;
		}
		else {
			clearHSConfirm.setVisible(false);
			hsLeftBox.getChildren().clear();
			hsRightBox.getChildren().clear();
			try {
				BufferedWriter buff = new BufferedWriter(new FileWriter("highscores.txt"));
				buff.flush();
				buff.close();
			} catch (IOException e) {
				// do nothing, since the file doesn't exist anyway
			}
			clearHighScoresBtn.setStyle("-fx-text-fill: #3a5ff0;");
			clearHighScoresBtn.setStyle("-fx-border-color: #3a5ff0;");
			clearBtnArmed = false;
		}

		/*
		Alert clearAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to clear the high scores?");
		Optional<ButtonType> result = clearAlert.showAndWait();
		result.ifPresent(buttonType -> {
			if(buttonType == ButtonType.OK) {
				hsLeftBox.getChildren().clear();
				hsRightBox.getChildren().clear();
				try {
					BufferedWriter buff = new BufferedWriter(new FileWriter("highscores.txt"));
					buff.flush();
					buff.close();
				} catch (IOException e) {
					// do nothing, since the file doesn't exist anyway
				}
			}
		});
		*/
	}

	@FXML
	private void playD1BtnClicked() {
		difficulty = 1;
		newGame();
	}

	@FXML
	private void playD2BtnClicked() {
		difficulty = 2;
		newGame();
	}

	@FXML
	private void playD3BtnClicked() {
		difficulty = 3;
		newGame();
	}

	@FXML
	private void playD4BtnClicked() {
		difficulty = 4;
		newGame();
	}

	@FXML
	private void playD5BtnClicked() {
		difficulty = 5;
		newGame();
	}

	@FXML
	private void quitBtnClicked() {
		Platform.exit();
	}

	private void newGame() {
		ctrl = new Controller();
		ctrl.setFastMode(fastModeSlider.getValue() == 1);
		//ctrl.setDifficulty(difficulty); <-- to be implemented
		menuStage.hide();
	}

	public void setMenuStage(Stage stage) {
		menuStage = stage;
	}

}
