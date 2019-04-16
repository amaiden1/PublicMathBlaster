package MathBlaster;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import static MathBlaster.Constants.*;

public class Shooter {

	private ImageView iv;
	private double x;
	private double y;

	public Shooter(double x, double y)
	{
		this.x = x;
		this.y = y;
		iv = new ImageView("/img/spaceship.png");
		iv.setFitWidth(80);
		iv.setFitHeight(80);
		iv.relocate(x, y);
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void incX(double x) {
		this.x += x;
		relocate();
		wrap();
	}

	public void decX(double x) {
		this.x -= x;
		relocate();
		wrap();
	}

	private void relocate() {
		iv.relocate(x, y);
	}

	public ImageView getIV() {
		return iv;
	}

	public void setIv(Image img){
		this.iv.setImage(img);
	}

	public Bullet shoot() {
		return new Bullet(x, y);
	}

	private void wrap() {
		if(x > SCREEN_WIDTH) {
			// wrap right to left
			setX(0 - iv.getFitWidth());
			relocate();
		}
		if(x + iv.getFitWidth() < 0) {
			// wrap left to right;
			setX(SCREEN_WIDTH);
			relocate();
		}
	}

}
