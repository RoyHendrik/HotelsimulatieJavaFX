package HotelSimulatie.Models;

import HotelSimulatie.SimLogic.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Stairs extends Unit {
    public Stairs(int visualYPos, int visualXPos) {
        super(1, 1, visualYPos, visualXPos);
        bgImage = new ImageView(new Image("HotelSimulatie/Images/stair.png"));
        this.passingTime = Constants.getStairsPassingTime();
    }

}
