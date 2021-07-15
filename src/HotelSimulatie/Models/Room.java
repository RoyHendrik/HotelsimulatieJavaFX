package HotelSimulatie.Models;

import HotelSimulatie.Enums.STATUS;
import HotelSimulatie.Interfaces.IAmOccupiable;
import HotelSimulatie.Interfaces.ICleanable;
import HotelSimulatie.Interfaces.ICleaningService;
import HotelSimulatie.SimLogic.Constants;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Room extends Unit implements ICleanable, IAmOccupiable {
    private final ArrayList<Human> occupants = new ArrayList<>();
    private final int stars;
    private STATUS status;
    private ICleaningService cleaningNotifyUnit;
    private boolean isOccupied = false;

    public Room(int width, int height, int visualYPos, int visualXPos, int stars) {
        super(width, height, visualYPos, visualXPos);
        this.stars = stars;
        this.status = STATUS.CLEAN;
        bgImage = new ImageView(new Image("HotelSimulatie/Images/room" + this.stars + ".png", width * Constants.getUNITPIXELWIDTH(), height * Constants.getUNITPIXELHEIGHT(), false, true));
    }

    public int getStars() {
        return stars;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
        if (status == STATUS.DIRTY) {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.4);

            bgImage.setEffect(colorAdjust);
        } else if (status == STATUS.CLEAN) {
            ColorAdjust colorAdjust = new ColorAdjust();
            if (occupants.size() == 0) {
                colorAdjust.setBrightness(0);
            } else {
                colorAdjust.setBrightness(0.4);
            }

            bgImage.setEffect(colorAdjust);
        }
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void setCleaningNotifyUnit(ICleaningService cleaningNotifyUnit) {
        if (this.cleaningNotifyUnit == null) {
            this.cleaningNotifyUnit = cleaningNotifyUnit;
        }
    }

    @Override
    public void addOccupant(Human human) {
        occupants.add(human);
    }

    @Override
    public ArrayList<Human> getOccupants() {
        return this.occupants;
    }

    @Override
    public void removeOccupant(Human human) {
        if (occupants.contains(human)) {
            occupants.remove(human);
            if (occupants.size() == 0) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(0);
                bgImage.setEffect(colorAdjust);
            }

            unitCheckOut(human);
        }
    }


    @Override
    public void unitCheckin(Human human) {

        addOccupant(human);
        human.visualRemover();
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.3);

        bgImage.setEffect(colorAdjust);
        if (human instanceof Cleaner) {
            ((Cleaner) human).cleaningRoom();
            ((Cleaner) human).setCurrentTask(this);
        }
        human.hasCheckedIn();
    }

    @Override
    public void unitCheckOut(Human human) {
        if (human instanceof Cleaner) {
            this.status = STATUS.CLEAN;
            ((Cleaner) human).removeCurrentTask();
        }
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0);

        bgImage.setEffect(colorAdjust);
        human.hasCheckedOut();
    }


}
