package MathBlaster;

import javafx.scene.image.ImageView;

public class Meteor {

    private ImageView iv;

    public Meteor()
    {
        iv = new ImageView("/img/meteor.gif");
        iv.setFitWidth(120);
        iv.setFitHeight(120);
    }

    public ImageView getIV() {
        return iv;
    }
}
