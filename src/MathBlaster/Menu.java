package MathBlaster;

/**
 *
 * @author Joe
 */
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class Menu {
	@FXML
    private Button playBtn;
	private Controller ctrl;
	private Stage menuStage;
	@FXML
	private ToggleButton fsModeBtn;

	@FXML
	private void play()
{
	ctrl = new Controller();
	ctrl.setFastMode(fsModeBtn.isSelected());

	menuStage.hide();
}
	public void setMenuStage(Stage stage)
	{
		menuStage = stage;
	}
}
