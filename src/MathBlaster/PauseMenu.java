package MathBlaster;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import static MathBlaster.Constants.*;

public class PauseMenu {

	@FXML private Button continueBtn;
	@FXML private Button mainMenuBtn;
	@FXML private Button quitBtnClicked;

	private Stage thisStage;
	private IntegerProperty valueListener;

	public void setThisStage(Stage thisStage) {
		this.thisStage = thisStage;
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

}
