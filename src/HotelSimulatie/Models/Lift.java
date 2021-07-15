package HotelSimulatie.Models;

import HotelSimulatie.SimLogic.Constants;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Lift extends Unit {

    public Lift(int visualYPos, int visualXPos) {
        super(1, 1, visualYPos, visualXPos);
        bgImage = new ImageView(new Image("HotelSimulatie/Images/elevator.png", Constants.getUNITPIXELWIDTH(), Constants.getUNITPIXELHEIGHT(), false, true));
        if (!Constants.isLiftActive()) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.7);
            bgImage.setEffect(colorAdjust);
        }
    }
}
