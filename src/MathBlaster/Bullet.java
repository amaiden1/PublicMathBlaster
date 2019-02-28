package MathBlaster;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Bullet {

	private Circle bull;
	private double x;
	private double y;

	public Bullet(double x, double y) {
		bull = new Circle(10);
		bull.setFill(Paint.valueOf("RED"));
		this.x = x;
		this.y = y;
	}

	public void decY(double y) {
		this.y -= y;
		relocate();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	private void relocate() {
		bull.relocate(x, y);
	}

	public boolean willDespawn() {
		return y <= 0;
	}

	public Node getIV() {
		return bull;
	}

}
