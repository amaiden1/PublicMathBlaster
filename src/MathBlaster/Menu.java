package MathBlaster;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class Menu {

	@FXML private Slider fastModeSlider;
	@FXML private Button clearHighScoresBtn;
	@FXML private Button playD1Btn;
	@FXML private Button playD2Btn;
	@FXML private Button playD3Btn;
	@FXML private Button playD4Btn;
	@FXML private Button playD5Btn;

	private Controller ctrl;
	private Stage menuStage;
	private int difficulty;

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
		/*
		fastModeSlider.setOnMouseClicked(event -> {
			if(fastModeSlider.getValue() == 1) {
				fastModeSlider.setValue(0);
			} else {
				fastModeSlider.setValue(1);
			}
		});
		*/
	}

	@FXML
	private void clearHighScoresBtnClicked() {
		// clear the leaderboard
		// to be implemented
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
