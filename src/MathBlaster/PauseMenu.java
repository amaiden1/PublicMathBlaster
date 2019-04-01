package MathBlaster;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PauseMenu {

	@FXML private Button continueBtn;
	@FXML private Button mainMenuBtn;
	@FXML private Button quitBtnClicked;

	private Stage thisStage;
	private BooleanProperty quitValue;
	private BooleanProperty continueValue;

	public void setThisStage(Stage thisStage) {
		this.thisStage = thisStage;
	}

	public void setQuitValueListener(BooleanProperty quitValue) {
		this.quitValue = quitValue;
	}

	public void setContinueValueListener(BooleanProperty continueValue) {
		this.continueValue = continueValue;
	}

	@FXML
	private void continueBtnClicked() {
		continueValue.setValue(true);
		thisStage.close();
	}

	@FXML
	private void mainMenuBtnClicked() {
		// not yet implemented!
	}

	@FXML
	private void quitBtnClicked() {
		quitValue.setValue(true);
		thisStage.close();
	}

}
