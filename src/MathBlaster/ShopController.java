package MathBlaster;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;

public class ShopController {

	@FXML private Button backBtn;
	@FXML private Button c1;
	@FXML private Button c2;
	@FXML private Button c3;
	@FXML private Button c4;
	@FXML private Button c5;
	@FXML private Button c6;
	@FXML private Button c7;
	@FXML private Button c8;
	@FXML private Button c9;
	@FXML private Button c10;
	@FXML private Label hsLabel;
	@FXML private Label c1Lbl;
	@FXML private Label c2Lbl;
	@FXML private Label c3Lbl;
	@FXML private Label c4Lbl;
	@FXML private Label c5Lbl;
	@FXML private Label c6Lbl;
	@FXML private Label c7Lbl;
	@FXML private Label c8Lbl;
	@FXML private Label c9Lbl;
	@FXML private Label c10Lbl;

	private Stage thisStage;
	private Stage menuStage;
	private String bulletImg;
	private String shipImg;
	private int highScore;
	private IntegerProperty shipValueListener;
	private IntegerProperty bulletValueListener;

	public void postInit(Stage thisStage, Stage menuStage, int highScore) {
		this.thisStage = thisStage;
		this.menuStage = menuStage;
		this.highScore = highScore;
		hsLabel.setText("" + highScore);

		setUnlocks();
	}

	public void setShipValueListener(IntegerProperty listener) {
		shipValueListener = listener;
		// select the correct value
		ArrayList<Button> shipBtns = new ArrayList<>(Arrays.asList(c1, c2, c3, c4, c5));
		shipBtns.get(shipValueListener.getValue() - 1).setStyle("-fx-border-color: #45c9f2");
		shipBtns.get(shipValueListener.getValue() - 1).setTextFill(Paint.valueOf("0x45c9f2"));
	}

	public void setBulletValueListener(IntegerProperty listener) {
		bulletValueListener = listener;
		ArrayList<Button> bulletBtns = new ArrayList<>(Arrays.asList(c6, c7, c8, c9, c10));
		bulletBtns.get(bulletValueListener.getValue() - 1).setStyle("-fx-border-color: #45c9f2");
		bulletBtns.get(bulletValueListener.getValue() - 1).setTextFill(Paint.valueOf("0x45c9f2"));
	}

	@FXML
	private void backBtnClicked() {
		// show main menu
		System.out.println("back clicked");
		thisStage.hide();
		menuStage.show();
	}

	@FXML
	private void c1Clicked() {
		// Default Ship
		deactivateAllShips();
		c1.setStyle("-fx-border-color: #45c9f2");
		c1.setTextFill(Paint.valueOf("0x45c9f2"));
		shipValueListener.setValue(1);
	}

	@FXML
	private void c2Clicked() {
		deactivateAllShips();
		c2.setStyle("-fx-border-color: #45c9f2");
		c2.setTextFill(Paint.valueOf("0x45c9f2"));
		shipValueListener.setValue(2);
	}

	@FXML
	private void c3Clicked() {
		deactivateAllShips();
		c3.setStyle("-fx-border-color: #45c9f2");
		c3.setTextFill(Paint.valueOf("0x45c9f2"));
		shipValueListener.setValue(3);
	}

	@FXML
	private void c4Clicked() {
		deactivateAllShips();
		c4.setStyle("-fx-border-color: #45c9f2");
		c4.setTextFill(Paint.valueOf("0x45c9f2"));
		shipValueListener.setValue(4);
	}

	@FXML
	private void c5Clicked() {
		deactivateAllShips();
		c5.setStyle("-fx-border-color: #45c9f2");
		c5.setTextFill(Paint.valueOf("0x45c9f2"));
		shipValueListener.setValue(5);
	}

	@FXML
	private void c6Clicked() {
		// Default Bullet
		deactivateAllBullets();
		c6.setStyle("-fx-border-color: #45c9f2");
		c6.setTextFill(Paint.valueOf("0x45c9f2"));
		bulletValueListener.setValue(1);
	}

	@FXML
	private void c7Clicked() {
		deactivateAllBullets();
		c7.setStyle("-fx-border-color: #45c9f2");
		c7.setTextFill(Paint.valueOf("0x45c9f2"));
		bulletValueListener.setValue(2);
	}

	@FXML
	private void c8Clicked() {
		deactivateAllBullets();
		c8.setStyle("-fx-border-color: #45c9f2");
		c8.setTextFill(Paint.valueOf("0x45c9f2"));
		bulletValueListener.setValue(3);
	}

	@FXML
	private void c9Clicked() {
		deactivateAllBullets();
		c9.setStyle("-fx-border-color: #45c9f2");
		c9.setTextFill(Paint.valueOf("0x45c9f2"));
		bulletValueListener.setValue(4);
	}

	@FXML
	private void c10Clicked() {
		deactivateAllBullets();
		c10.setStyle("-fx-border-color: #45c9f2");
		c10.setTextFill(Paint.valueOf("0x45c9f2"));
		bulletValueListener.setValue(5);
	}

	@FXML
	private void mouseHover() {

	}

	private void deactivateAllShips() {
		// #45c9f2
		ArrayList<Button> btns = new ArrayList<>(Arrays.asList(c1, c2, c3, c4, c5));
		for (Button btn : btns) {
			btn.setStyle("-fx-border-color: #f74040");
			btn.setTextFill(Paint.valueOf("0xf74040"));
		}
	}

	private void deactivateAllBullets() {
		ArrayList<Button> btns = new ArrayList<>(Arrays.asList(c6, c7, c8, c9, c10));
		for (Button btn : btns) {
			btn.setStyle("-fx-border-color: #f74040");
			btn.setTextFill(Paint.valueOf("0xf74040"));
		}
	}

	private void setUnlocks() {
		ArrayList<Button> unlockBtns = new ArrayList<>(Arrays.asList(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10));
		ArrayList<Label> labels = new ArrayList<>(Arrays.asList(c1Lbl, c2Lbl, c3Lbl, c4Lbl, c5Lbl, c6Lbl, c7Lbl, c8Lbl, c9Lbl, c10Lbl));
		int[] shopItemPrices = {0, 10000, 20000, 50000, 100000, 0, 10000, 20000, 50000, 100000};
		for (int i = 0 ; i < 10 ; i++) {
			if(highScore >= shopItemPrices[i]) {
				// this is unlockable
				unlockBtns.get(i).setText("EQUIP");
				labels.get(i).setText("Unlocked");
			} else {
				// this is not unlockable
				unlockBtns.get(i).setDisable(true);
				unlockBtns.get(i).setCursor(Cursor.NONE);
				labels.get(i).setText("Unlocks at " + shopItemPrices[i]);
			}
		}
	}

}
