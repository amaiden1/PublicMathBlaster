package MathBlaster;

import javafx.scene.Node;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Rotate;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet {

    private Image image = new Image("/img/bblast.png");
    private ImageView iv;

    private double x;
	private double y;

	public Bullet(double x, double y) {

        iv = new ImageView(image);
        iv.setFitHeight(90);
        iv.setFitWidth(24);

		this.x = x + 50;
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
		iv.relocate(x, y);
	}

	public boolean willDespawn() {
		return y <= 0;
	}

	public Node getIV() {
		return iv;
	}

}
