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

import static MathBlaster.Constants.*;

public class PauseMenu {

	@FXML private Button continueBtn;
	@FXML private Button mainMenuBtn;
	@FXML private Button quitBtnClicked;
	@FXML private ImageView moveStageBtn;
	@FXML private ImageView minBtn;

	private Stage thisStage;
	private Stage gameStage;
	private IntegerProperty valueListener;
	private double pDragX, pDragY;
	private double gDragX, gDragY;

	public void setThisStage(Stage thisStage) {
		this.thisStage = thisStage;
	}

	public void setGameStage(Stage gameStage) {
		this.gameStage = gameStage;
	}

	public void setupWindowProperties() {

		// add listener for on minimize/restore
		gameStage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue) {
				// if minimized, hide pause menu
				thisStage.hide();
			}
			else {
				// if not minimized, show pause menu
				thisStage.show();
				thisStage.toFront();
			}
		});

		// add listener for move
		pDragX = 0;
		pDragY = 0;
		gDragX = 0;
		gDragY = 0;

		moveStageBtn.setCursor(Cursor.OPEN_HAND);
		moveStageBtn.setOnMousePressed(event -> {
			if(event.getButton() != MouseButton.MIDDLE) {
				pDragX = event.getSceneX();
				pDragY = event.getSceneY();
				gDragX = event.getSceneX() + 150;
				gDragY = event.getSceneY() + 115;
			}
		});
		moveStageBtn.setOnMouseDragged(event -> {
			if(event.getButton() != MouseButton.MIDDLE) {
				gameStage.getScene().getWindow().setX(event.getScreenX() - gDragX);
				gameStage.getScene().getWindow().setY(event.getScreenY() - gDragY);
				thisStage.getScene().getWindow().setX(event.getScreenX() - pDragX);
				thisStage.getScene().getWindow().setY(event.getScreenY() - pDragY);
			}
		});

	}

	public void setValueListener(IntegerProperty listener) {
		this.valueListener = listener;
	}

	@FXML
	private void continueBtnClicked() {
		valueListener.setValue(PAUSE_RESPONSE_CONTINUE);
		thisStage.close();
	}

	@FXML
	private void mainMenuBtnClicked() {
		valueListener.setValue(PAUSE_RESPONSE_MAIN_MENU);
		thisStage.close();
	}

	@FXML
	private void quitBtnClicked() {
		valueListener.setValue(PAUSE_RESPONSE_QUIT);
		thisStage.close();
	}

	@FXML
	private void minBtnClicked() {
		gameStage.setIconified(true);
	}

}
