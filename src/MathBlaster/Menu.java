package MathBlaster;

/**
 *
 * @author Joe
 */
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Menu {
	@FXML
    private Button playBtn;
	private Controller ctrl;
	private Stage stg;
	private Stage menuStage;

	@FXML
	private void play()
{
	ctrl = new Controller();
	stg = new Stage();
	stg.setScene(ctrl.getScene());
	stg.show();
	menuStage.hide();
}
	public void setMenuStage(Stage stage)
	{
		menuStage = stage;
	}
}
