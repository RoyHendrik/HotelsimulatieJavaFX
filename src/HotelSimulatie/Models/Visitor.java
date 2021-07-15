package HotelSimulatie.Models;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.IAmOccupiable;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import HotelSimulatie.SimLogic.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static HotelSimulatie.Models.Lobby.lobby;

public class Visitor extends Human implements IEventReceiver {
    private final int id;
    private final int stars;
    private Room myRoom;
    private boolean dining = false;
    private boolean atGym = false;
    private boolean wantToWaitForDiner = false;
    private int iveBeenWaiting = 0;
    private boolean dead = false;

    public Visitor(int id, GridPane space, int stars) {
        super(space);
        this.id = id;
        this.stars = stars;
        setBgImage();
        humanDrawer();
    }

    public boolean isDead() {
        return dead;
    }

    public void stillWaiting() {
        iveBeenWaiting++;
        if (iveBeenWaiting > Constants.getDieAfterWaiting()) {
            dieHard4point0();
        }
    }

    public void resetWaiting() {
        iveBeenWaiting = 0;
    }

    public void dieHard4point0() {
        EventHandler.getHandler().unSubscribe(this);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.8);
        bgImage.setEffect(colorAdjust);
        dead = true;
    }

    public boolean doYouWantToWaitForDiner() {
        return wantToWaitForDiner;
    }

    public void iWantToWaitNextTime() {
        wantToWaitForDiner = true;
    }

    public void iveWaited() {
        resetWaiting();
        wantToWaitForDiner = false;
    }

    public void isAtGym() {
        this.atGym = true;
    }

    public void notAtGym() {
        this.atGym = false;
    }

    public boolean getAtGym() {
        return this.atGym;
    }

    public void setDining(boolean dining) {
        this.dining = dining;
    }

    public Room getMyRoom() {
        return myRoom;
    }

    public void setMyRoom(Room myRoom) {
        this.myRoom = myRoom;
        goToRoom();
    }

    private void setBgImage() {
        int randomImageNumber = (int) Math.round(Math.random() * (20 - 1) + 1);
        this.bgImage = new ImageView(new Image("HotelSimulatie/Images/humans/human" + randomImageNumber + ".png"));
    }

    public void goToRoom() {
        if (getNextEvent() != null) {
            setCurrentEvent(getNextEvent());
            eventTrigger(getCurrentEventType());
            setNextEvent(null);
            return;
        }

        setCurrentEvent(new Event(EVENTTYPES.DEFAULT, 999));
        setDestination(this.myRoom);
    }

    public void removeMyRoom() {
        this.myRoom.setOccupied(false);
        this.myRoom = null;
    }

    public void justLeave() {
        humanWithdrawer();
        EventHandler.getHandler().unSubscribe(this);
    }

    public int getId() {
        return id;
    }

    public int getStars() {
        return stars;
    }

    public void fitness() {
        if (hteToWait > 0) {
            hteToWait--;
        } else if (!getAtGym()) {
            setDestination(this.pathFinding.pathToEvent(this, this.getCurrentEventType()));
        } else {
            if (this.locationUnit instanceof IAmOccupiable) {
                ((IAmOccupiable) (this.locationUnit)).unitCheckOut(this);
            }

            goToRoom();
        }
    }

    public void diner() {
        if (doYouWantToWaitForDiner()) {
            ((Diner) this.locationUnit).unitCheckin(this);
            return;
        }
        if (hteToWait > 0) {
            hteToWait--;
        } else {
            setDestination(this.pathFinding.pathToEvent(this, this.getCurrentEventType()));
        }
    }

    public void cinema() {
        if (!(this.locationUnit instanceof Cinema)) {
            setDestination(this.pathFinding.pathToEvent(this, this.getCurrentEventType()));
        }
    }

    public void checkOut() {
        if (!(this.locationUnit instanceof Lobby)) {
            setDestination(this.pathFinding.pathToUnit(this, lobby));
        } else {
            ((Lobby) this.locationUnit).visitorCheckout(this);
        } // only for Evacuating when the visitor is at the Lobby
    }

    // First trigger
    public void eventTrigger(EVENTTYPES eventtype) {
        if (eventtype == null) {
            goToRoom();
            return;
        }

        switch (eventtype) {
            case GO_TO_DINER:
                diner();
                break;
            case GO_TO_CINEMA:
                cinema();
                break;
            case GO_TO_FITNESS:
                fitness();
                break;
            case CHECK_OUT:
                checkOut();
                break;
        }

    }

    @Override
    public void update(ArrayList<Event> events) {
        if (events.stream().anyMatch(e -> e.getType() == EVENTTYPES.EVACUATE)) {
            List<Event> eventList = events.stream().filter(event -> event.getType() == EVENTTYPES.EVACUATE).collect(Collectors.toList());

            if (getCurrentEvent().getType() == EVENTTYPES.CHECK_OUT) {
                return;
            }
            hteToWait = 0;
            evacuateNow(eventList.get(0));
        } else if (events.stream().anyMatch(e -> e.getGuestId() == this.id)) {
            List<Event> humanEvent = events.stream().filter(event -> event.getGuestId() == getId()).collect(Collectors.toList());

            if (humanEvent.get(0).getType().equals(EVENTTYPES.CLEANING_EMERGENCY)) {

                lobby.needsCleaning(myRoom, true);

            } else if (evacuating) {

                if (humanEvent.get(0).getType().equals((EVENTTYPES.CHECK_OUT))) {
                    evacuating = false;
                    setCurrentEvent(humanEvent.get(0));
                    eventTrigger(getCurrentEventType());
                }

            } else if (evacuate) {

                lobby.unitCheckin(this);
                setCurrentEvent(humanEvent.get(0));
                eventTrigger(getCurrentEventType());

            } else if (((getCurrentEventType().equals(EVENTTYPES.GO_TO_DINER) || getCurrentEventType().equals(EVENTTYPES.GO_TO_CINEMA)) || (hteToWait > 0)) && !isMoving) {

                setNextEvent(humanEvent.get(0));

            } else {

                setCurrentEvent(humanEvent.get(0));
                eventTrigger(getCurrentEventType());

            }

            if (hteToWait > 0) {
                hteToWait--;
            }

        } else if (evacuating || (isMoving && getCurrentEventType() != null)) {
            if (hteToWait > 0) {
                hteToWait--;
                return;
            }

            move();
        } else if (dining) {
            hteToWait--;

            if (hteToWait == 0) {
                goToRoom();
                dining = false;
            }
        } else {
            eventTrigger(getCurrentEventType());
        }
    }
}
