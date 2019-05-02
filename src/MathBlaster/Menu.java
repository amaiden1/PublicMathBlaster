package MathBlaster;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.*;


public class Menu {

	@FXML
	private Slider fastModeSlider;
	@FXML private Button clearHighScoresBtn;
	@FXML private Button playD1Btn;
	@FXML private Button playD2Btn;
	@FXML private Button playD3Btn;
	@FXML private Button playD4Btn;
	@FXML private Button playD5Btn;
	@FXML private Button quitBtn;
	@FXML private Button shopBtn;
	@FXML private Button creditsBtn;
	@FXML private VBox hsLeftBox;
	@FXML private VBox hsRightBox;
	@FXML private Label clearHSConfirm;
	@FXML private ImageView minBtn;
	@FXML private ImageView closeBtn;
	@FXML private ImageView showTitleBtn;
	@FXML private ImageView moveStageBtn;


	private AudioClip hoverSound = new AudioClip(this.getClass().getResource("/sounds/buttonhover.wav").toString());
	private Controller ctrl;
	private Stage menuStage;
	private int difficulty;
	private int highScore;
	private boolean clearBtnArmed;
	private boolean titleHidden;
	private double dragX, dragY;
	private IntegerProperty chosenShipCostume;
	private IntegerProperty chosenBulletCostume;

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
		titleHidden = true;
		clearHSConfirm.setVisible(false);
		readHighScores();
		dragX = 0;
		dragY = 0;


		moveStageBtn.setCursor(Cursor.OPEN_HAND);
		moveStageBtn.setOnMousePressed(event -> {
			if(event.getButton() != MouseButton.MIDDLE) {
				dragX = event.getSceneX();
				dragY = event.getSceneY();
			}
		});
		moveStageBtn.setOnMouseDragged(event -> {
			if(event.getButton() != MouseButton.MIDDLE) {
				menuStage.getScene().getWindow().setX(event.getScreenX() - dragX);
				menuStage.getScene().getWindow().setY(event.getScreenY() - dragY);
			}
		});

		shopBtn.getStyleClass().add("shop-btn");
		shopBtn.setStyle("-fx-border-color: linear-gradient(from 0% 0% to 100% 100%, #3589eb, #bd36d8);\n");

		chosenBulletCostume = new SimpleIntegerProperty(1);
		chosenShipCostume = new SimpleIntegerProperty(1);

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
			List<Map.Entry<String, Integer>> scoreList = new ArrayList<>();

			// add all the scores to the list
			// normally this would be a HashMap but you can't sort those very easily!
			while(fileScan.hasNextLine()) {
				System.out.println("parsing line");
				Scanner line = new Scanner(fileScan.nextLine()).useDelimiter(";");
				String name = line.next();
				int score = Integer.parseInt(line.next());
				scoreList.add(new AbstractMap.SimpleEntry<>(name, score));
			}

			// sort the list using voodoo magic (actually, just lambdas)
			scoreList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

			// set highest score for shop
			if(!scoreList.isEmpty()) {
				highScore = scoreList.get(0).getValue();
			} else {
				highScore = 0;
			}

			// add the scores to the screen
			for(Map.Entry<String, Integer> entry : scoreList) {
				Label name = new Label(entry.getKey());
				name.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
				hsLeftBox.getChildren().add(name);
				Label score = new Label("" + entry.getValue());
				score.setStyle("-fx-text-fill: white; -fx-font-size: 18;");
				hsRightBox.getChildren().add(score);
			}

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
	private void mouseHover() {
		// ADD SOUND CODE HERE!!
		//System.out.println("hovered over a button!");

		//Node object = (Node)event.getSource();
		//object.setStyle("-fx-text-fill: white");

		hoverSound.play();
	}

	@FXML
	private void quitBtnClicked() {
		Platform.exit();
	}

	@FXML
	private void shopBtnClicked() {

		try {
			ShopController shop = new ShopController();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Shop.fxml"));
			loader.setController(shop);
			Pane root = loader.load();
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			shop.postInit(primaryStage, menuStage, highScore);
			shop.setShipValueListener(chosenShipCostume);
			shop.setBulletValueListener(chosenBulletCostume);
			primaryStage.setTitle("Mathblaster");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			menuStage.hide();
			primaryStage.show();
		} catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Fatal error: Could not shop. The game will now exit." +
				"Please submit a bug report to our developers. (Details: An IOException occurred when trying to instantiate the" +
				" Shop stage from @FXML Menu.shopBtnClicked().)").showAndWait();
			Platform.exit();
		}

	}

	private void newGame() {
		ctrl = new Controller(difficulty, fastModeSlider.getValue() == 1, chosenShipCostume.get(), chosenBulletCostume.get());
		//ctrl.setFastMode(fastModeSlider.getValue() == 1);
		//ctrl.setDifficulty(difficulty); <-- to be implemented
		menuStage.hide();
	}

	public void setMenuStage(Stage stage) {
		menuStage = stage;
	}

	@FXML
	private void closeBtnClicked(MouseEvent event) {
		Platform.exit();
	}

	@FXML
	private void minBtnClicked(MouseEvent event) {
		menuStage.setIconified(true);
	}

	@FXML
	private void creditsBtnClicked(ActionEvent event) {
		try {
			Credits credits = new Credits();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Credits.fxml"));
			loader.setController(credits);
			Pane root = loader.load();
			Stage primaryStage = new Stage();
			Scene scene = new Scene(root);
			credits.postInit(primaryStage, menuStage);
			primaryStage.setTitle("Mathblaster");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			menuStage.hide();
			primaryStage.show();
		} catch (IOException e) {
			new Alert(Alert.AlertType.ERROR, "Fatal error: Could not shop. The game will now exit." +
				"Please submit a bug report to our developers. (Details: An IOException occurred when trying to instantiate the" +
				" Shop stage from @FXML Menu.shopBtnClicked().)").showAndWait();
			Platform.exit();
		}
	}

}