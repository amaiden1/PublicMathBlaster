package MathBlaster;

import javafx.scene.image.ImageView;

public class Shooter {

	private ImageView iv;
	private double x;
	private double y;

	public Shooter(double x, double y)
	{
		this.x = x;
		this.y = y;
		iv = new ImageView("/img/spaceship.jpg");
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
	}

	public void decX(double x) {
		this.x -= x;
		relocate();
	}

	private void relocate() {
		iv.relocate(x, y);
	}

	public ImageView getIV() {
		return iv;
	}

	public Bullet shoot() {
		return new Bullet(x, y);
	}



}
