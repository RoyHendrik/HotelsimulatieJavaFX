package HotelSimulatie.Models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Corridor extends Unit {

    public Corridor(int visualYPos, int visualXPos, boolean isVisible) {
        super(1, 1, visualYPos, visualXPos);
        String imgVisiblePath = "HotelSimulatie/Images/corridor.png";
        String imgNotVisiblePath = "HotelSimulatie/Images/corridorNotVisible.png";
        this.bgImage = new ImageView(new Image(isVisible ? imgVisiblePath : imgNotVisiblePath));
    }
}
