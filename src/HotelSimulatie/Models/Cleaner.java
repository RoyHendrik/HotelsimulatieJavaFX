package HotelSimulatie.Models;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Enums.STATUS;
import HotelSimulatie.Interfaces.ICanClean;
import HotelSimulatie.Interfaces.ICleanable;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static HotelSimulatie.Models.Lobby.lobby;

public class Cleaner extends Human implements ICanClean, IEventReceiver {

    private static final Random random = new Random();
    private LinkedList<ICleanable> tasks = new LinkedList<>();
    private boolean working = false;
    private ICleanable currentCleanable;

    public Cleaner(GridPane space) {
        super(space);
        setBgImage();
        humanDrawer();
    }

    private void setBgImage() {
        this.bgImage = new ImageView(new Image("HotelSimulatie/Images/humans/cleaner.png"));
    }

    public int getTaskLoad() {
        return tasks.size();
    }

    public boolean containsTaskAlready(ICleanable iCleanable) {
        return tasks.contains(iCleanable);
    }

    public void removeCurrentTask() {
        tasks.remove(currentCleanable);
        currentCleanable = null;
    }

    public void setCurrentTask(ICleanable iCleanable) {
        currentCleanable = iCleanable;
    }

    public void cleaningRoom() {
        if (!working && this.locationUnit instanceof ICleanable && ((ICleanable) this.locationUnit).getStatus() == STATUS.DIRTY) {
            hteToWait = Constants.getRoomCleaningTime();
            working = true;
        } else if (working && hteToWait == 0) {
            if (!tasks.isEmpty()) {
                working = false;
                setDestination((Unit) tasks.getFirst());
            } else {
                setDestination(lobby);
            }
            working = false;
        } else if (working) {
            hteToWait--;
        }
    }

    public void addTask(ICleanable cleanable, boolean priority) {
//         re-entering the lobby after evacuate
        if (priority) {
            tasks.addFirst(cleanable);
        } else {
            tasks.addLast(cleanable);
        }
        cleanable.setStatus(STATUS.DIRTY);

        if (evacuating) {
            return;
        }

        if (evacuate) {
            hteToWait = 0;
            lobby.unitCheckin(this);
        }

        if (!working) {
            setDestination((Unit) tasks.getFirst());
        }
    }

    public LinkedList<ICleanable> getTasks() {
        return tasks;
    }

    private void randomMovement() {
        if (this.locationUnit == null) {
            return;
        }
        if (this.locationUnit.getVisualYPos() != lobby.getVisualYPos()) {
            setDestination(lobby);
        } else {
            int randomnumber = random.nextInt(this.locationUnit.getConnections().size());
            Unit randomNeighbour = this.locationUnit.getConnections().get(randomnumber);

            if (!(randomNeighbour instanceof Stairs) && !(randomNeighbour instanceof Lift)) {
                setDestination(randomNeighbour);
            }
        }
    }

    @Override
    public void update(ArrayList<Event> events) {
        if (events.stream().anyMatch(e -> e.getType() == EVENTTYPES.EVACUATE)) {
            List<Event> eventList = events.stream().filter(event -> event.getType() == EVENTTYPES.EVACUATE).collect(Collectors.toList());
            working = false;
            if (this.locationUnit instanceof Room) {
                ((Room) this.locationUnit).unitCheckOut(this);
            }

            hteToWait = 0;
            evacuateNow(eventList.get(0));
        } else if (evacuating) {
            move();
        } else if (evacuate) {
            if (hteToWait > 0) {
                hteToWait--;
                return;
            }

            lobby.unitCheckin(this);
            if (tasks.size() > 0) {
                setDestination((Unit) tasks.getFirst());
            }
        } else if (working) {
            cleaningRoom();
        } else if (isMoving) {
            move();
        } else if (!tasks.isEmpty()) {
            setDestination((Unit) tasks.getFirst());
        } else {
            randomMovement();
        }
    }
}
