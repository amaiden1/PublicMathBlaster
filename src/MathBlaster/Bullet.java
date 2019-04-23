package MathBlaster;

import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet {

    private Image image = new Image("/img/bblast.png");
    private ImageView iv;
    private Rectangle pixel;

    private double x;
	private double y;

	public Bullet(double x, double y) {

        iv = new ImageView(image);
        iv.setFitHeight(50);
        iv.setFitWidth(15);
		pixel = new Rectangle(1,1);
		pixel.setFill(Paint.valueOf("LIME"));
		pixel.relocate(x + (iv.getFitWidth()/2), y);

		this.x = x + 50;
		this.y = y;
	}

	public void decY(double y) {
		this.y -= y;
		relocate();
	}

	public double getX() {
		return pixel.getX();
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return pixel.getY();
	}

	public void setY(double y) {
		this.y = y;
	}

	private void relocate() {
		iv.relocate(x, y);
		pixel.relocate(x + (iv.getFitWidth()/2), y);
	}

	public boolean willDespawn() {
		return y <= 0;
	}

	public Node getIV() {
		return iv;
	}

	public Node getPixel() {
		return pixel;
	}

}
