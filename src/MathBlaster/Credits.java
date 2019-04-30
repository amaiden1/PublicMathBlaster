package MathBlaster;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Credits {

	@FXML private Button creditsBackBtn;
	private Stage thisStage;
	private Stage menuStage;

	public void postInit(Stage thisStage, Stage menuStage) {
		this.thisStage = thisStage;
		this.menuStage = menuStage;
	}

	@FXML
	private void creditsBackBtnClicked() {
		thisStage.hide();
		menuStage.show();
	}

}
