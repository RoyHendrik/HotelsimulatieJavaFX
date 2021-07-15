package HotelSimulatie.Models;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.IAmOccupiable;
import HotelSimulatie.SimLogic.PathFinding;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Queue;

import static HotelSimulatie.Models.Lobby.lobby;

public class Human {
    private final GridPane space;
    private int visualYPos;
    private int visualXPos;
    private boolean isCheckedIn = false;
    private Event currentEvent = null;
    private EVENTTYPES currentEventType;
    private Event nextEvent;
    protected boolean evacuate = false;
    protected boolean evacuating = false;
    protected boolean isMoving = false;
    protected int hteToWait = 0;
    protected Unit locationUnit;
    protected Queue<Unit> pathToGo;
    protected PathFinding pathFinding;
    protected ImageView bgImage;

    public Human(GridPane space) {
        this.space = space;
        this.locationUnit = lobby;
        this.visualYPos = lobby.getVisualYPos();
        this.visualXPos = lobby.getVisualXPos();
        this.pathFinding = new PathFinding();
    }

    public boolean getHasCheckedIn() {
        return this.isCheckedIn;
    }

    public void hasCheckedIn() {
        this.isCheckedIn = true;
    }

    public void hasCheckedOut() {
        this.isCheckedIn = false;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
        setNextEvent(null);
        if (currentEvent == null) {
            this.setCurrentEventType(null);
            return;
        }
        this.setCurrentEventType(currentEvent.getType());
    }

    public Unit getLocationUnit() {
        return locationUnit;
    }

    public ImageView getBgImage() {
        return bgImage;
    }

    public int getVisualYPos() {
        return visualYPos;
    }

    public void setVisualYPos(int visualYPos) {
        this.visualYPos = visualYPos;
    }

    public int getVisualXPos() {
        return visualXPos;
    }

    public void setVisualXPos(int visualXPos) {
        this.visualXPos = visualXPos;
    }

    // First Initialize
    public void humanDrawer() {
        if (!lobby.getOccupants().contains(this)) {
            lobby.addOccupant(this);
            space.add(this.getBgImage(), lobby.getVisualXPos(), lobby.getVisualYPos());
        }

        if (this.locationUnit == null) {
            this.locationUnit = lobby;
        }
    }

    // Exiting the simulation
    public void humanWithdrawer() {
        space.getChildren().remove(this.getBgImage());
        this.locationUnit = null;
        lobby.removeOccupant(this);
    }

    // Only image remover
    public void visualRemover() {
        space.getChildren().remove(this.getBgImage());
    }

    // Only image adder
    public void visualAdder() {
        space.add(this.bgImage, this.locationUnit.getVisualXPos(), this.locationUnit.getVisualYPos());
    }

    public int getHteToWait() {
        return hteToWait;
    }

    public void setDestination(Unit unit) {
        pathToGo = pathFinding.pathToUnit(this, unit);

        isMoving = true;
        if (getHasCheckedIn()) {
            ((IAmOccupiable) this.locationUnit).unitCheckOut(this);
        }
        move();
    }

    public void setDestination(Queue<Unit> path) {
        pathToGo = path;
        isMoving = true;
        move();
    }

    public void move() {
        if (hteToWait > 0) {
            hteToWait--;
            return;
        }

        if (evacuating && this instanceof Cleaner && this.locationUnit instanceof Lobby) {
            ((Lobby) this.locationUnit).unitCheckOut(this);
            isMoving = false;
            return;
        }

        if (pathToGo.size() < 1) {

            this.isMoving = false;
            if (this.locationUnit instanceof IAmOccupiable) {
                ((IAmOccupiable) this.locationUnit).unitCheckin(this);
            }
            return;
        }

        Unit nextUnit = pathToGo.poll();

        if (getHasCheckedIn() && this.locationUnit instanceof IAmOccupiable && ((IAmOccupiable) this.locationUnit).getOccupants().contains(this)) {
            ((IAmOccupiable) this.locationUnit).unitCheckOut(this);
        }

        if (nextUnit == null) {
            isMoving = false;
            return;
        }

        hteToWait = nextUnit.getPassingTime() - 1;
        this.locationUnit = nextUnit;
        this.redraw();
    }

    public void redraw() {
        space.getChildren().remove(this.getBgImage());
        setVisualXPos(this.locationUnit.getVisualXPos());
        setVisualYPos(this.locationUnit.getVisualYPos());
        space.add(this.bgImage, this.locationUnit.getVisualXPos(), this.locationUnit.getVisualYPos());
    }

    public void evacuateNow(Event event) {
        setCurrentEvent(event);

        if (this.locationUnit != null) {
            this.evacuating = true;
            this.evacuate = true;

            if (this.getHasCheckedIn() && !(this.locationUnit instanceof Lobby)) {

                ((IAmOccupiable) this.locationUnit).unitCheckOut(this);
                setDestination(pathFinding.pathToUnit(this, lobby));

            } else if (!(this.getLocationUnit() instanceof Lobby)) {

                setDestination(pathFinding.pathToUnit(this, lobby));

            } else if (this.locationUnit instanceof Lobby) {

                lobby.unitCheckOut(this);
            }
        }
    }

    public EVENTTYPES getCurrentEventType() {
        return currentEventType;
    }

    public void setCurrentEventType(EVENTTYPES currentEventType) {
        this.currentEventType = currentEventType;
    }

    public Event getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(Event event) {
        this.nextEvent = event;
    }
}
