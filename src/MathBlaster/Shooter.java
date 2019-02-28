package MathBlaster;

import javafx.scene.image.ImageView;

public class Shooter {

	private ImageView iv;
	private double x;
	private double y;

	public Shooter(int x, int y)
	{
		this.x = x;
		this.y = y;
		iv = new ImageView("/img/glock.jpg");
		iv.setFitWidth(50);
		iv.setFitHeight(50);
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
		System.out.println("x increased");
		System.out.println("iv layout x:" + iv.getLayoutX());
		System.out.println("iv x:" + iv.getX());
		relocate();
	}

	public void decX(double x) {
		this.x -= x;
		System.out.println("x decresed");
		System.out.println("iv layout x:" + iv.getLayoutX());
		System.out.println("iv x:" + iv.getX());
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
