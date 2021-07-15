package HotelSimulatie.Models;

import HotelSimulatie.Interfaces.IAmOccupiable;
import HotelSimulatie.SimLogic.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Fitness extends Unit implements IAmOccupiable {
    private final ArrayList<Human> occupants = new ArrayList<>();

    public Fitness(int width, int height, int visualYPos, int visualXPos) {
        super(width, height, visualYPos, visualXPos);
        this.bgImage = new ImageView(new Image("HotelSimulatie/Images/gym.png", width * Constants.getUNITPIXELWIDTH(), height * Constants.getUNITPIXELHEIGHT(), false, true));
    }

    @Override
    public void addOccupant(Human human) {
        occupants.add(human);
    }

    @Override
    public ArrayList<Human> getOccupants() {
        return occupants;
    }

    public void removeOccupant(Human human) {
        occupants.remove(human);
    }

    @Override
    public void unitCheckin(Human human) {
        if (human instanceof Visitor) {
            ((Visitor) human).isAtGym();
        }
        addOccupant(human);
        human.visualRemover();
        human.hasCheckedIn();
        human.hteToWait = human.getCurrentEvent().getDuration();
    }

    @Override
    public void unitCheckOut(Human human) {
        if (human instanceof Visitor) {
            ((Visitor) human).notAtGym();
        }
        if (occupants.contains(human)) {
            human.visualAdder();
            removeOccupant(human);
        }
        human.hasCheckedOut();
    }
}
