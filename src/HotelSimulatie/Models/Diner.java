package HotelSimulatie.Models;

import HotelSimulatie.Interfaces.IAmOccupiable;
import HotelSimulatie.SimLogic.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Diner extends Unit implements IAmOccupiable {
    private final ArrayList<Human> occupants = new ArrayList<>();
    private int maxOccupants;
    public Diner(int width, int height, int visualYPos, int visualXPos, int maxOccupants) {
        super(width, height, visualYPos, visualXPos);
        if (maxOccupants == 0) {
            maxOccupants = Constants.getMaxDinerOccupants();
        }

        this.maxOccupants = maxOccupants;
        this.bgImage = new ImageView(new Image("HotelSimulatie/Images/diner.png", width * Constants.getUNITPIXELWIDTH(), height * Constants.getUNITPIXELHEIGHT(), false, true));

    }

    @Override
    public void addOccupant(Human human) {
        this.occupants.add(human);
    }

    @Override
    public ArrayList<Human> getOccupants() {
        return this.occupants;
    }

    public void removeOccupant(Human human) {
        this.occupants.remove(human);
    }

    @Override
    public void unitCheckin(Human human) {
        if (getOccupants().size() < this.maxOccupants) {
            ((Visitor) human).iveWaited();
            human.hteToWait = Constants.getDinerTime();
            ((Visitor) human).setDining(true);
            human.visualRemover();
            human.hasCheckedIn();
            addOccupant(human);

        } else if (human instanceof Visitor && ((Visitor) human).doYouWantToWaitForDiner()) {

            ((Visitor) human).stillWaiting();

        } else if (human instanceof Visitor) {
            ((Visitor) human).iWantToWaitNextTime();
            human.setDestination(human.pathFinding.pathToEvent(human, human.getCurrentEventType()));
        }
    }

    @Override
    public void unitCheckOut(Human human) {
        if (occupants.contains(human)) {
            human.hasCheckedOut();
            human.visualAdder();
            removeOccupant(human);
        }
    }

}
