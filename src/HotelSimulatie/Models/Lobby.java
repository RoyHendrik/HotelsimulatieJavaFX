package HotelSimulatie.Models;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Enums.STATUS;
import HotelSimulatie.Interfaces.*;
import HotelSimulatie.SimLogic.Constants;
import HotelSimulatie.SimLogic.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class Lobby extends Unit implements ICleaningService, IAmOccupiable, IEventReceiver {
    public static Lobby lobby = new Lobby(1,1,0,1);
    private boolean specsAreSet = false;
    private final ArrayList<Human> occupants = new ArrayList<>();
    private final ArrayList<ICanClean> cleaners = new ArrayList<>();
    private ArrayList<Room> allRooms = new ArrayList<>();
    private final HashMap<Integer, Human> allVisitors = new HashMap<>();

    private Lobby(int width, int height, int visualYPos, int visualXPos) {
        super(width, height, visualYPos, visualXPos);

        }
    public void setLobbySpecs (int width, int height, int visualYPos, int visualXPos){
        if (!specsAreSet) {
            lobby.setWidth(width);
            lobby.setHeight(height);
            lobby.setVisualYPos(visualYPos);
            lobby.setVisualXPos(visualXPos);
            bgImage = new ImageView(new Image("HotelSimulatie/Images/lobby.png", width * Constants.getUNITPIXELWIDTH(), height * Constants.getUNITPIXELHEIGHT(), false, true));

            specsAreSet = true;
        }
    }


    public ArrayList<ICanClean> getCleaners() {
        return cleaners;
    }

    public HashMap<Integer, Human> getAllVisitors() {
        return allVisitors;
    }

    public void setAllRooms(ArrayList<Room> allRooms) {
        this.allRooms = allRooms;
    }

    public void addCleaner(ICanClean cleaner) {
        this.cleaners.add(cleaner);
    }

    @Override
    public void needsCleaning(ICleanable unitToBeCleaned, boolean priority) {
        if (cleaners.size() == 0) {
            return;
        }

        ICanClean cleanerWithLessWorkLoad = null;

        for (ICanClean cleaner : cleaners) {
            if (cleaner.containsTaskAlready(unitToBeCleaned)) {
                return;
            }

            if (cleanerWithLessWorkLoad == null) {
                cleanerWithLessWorkLoad = cleaner;

            } else if (cleaner.getTaskLoad() < cleanerWithLessWorkLoad.getTaskLoad()) {
                cleanerWithLessWorkLoad = cleaner;
            }
        }
        cleanerWithLessWorkLoad.addTask(unitToBeCleaned, priority);
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
        if (human.evacuate && human.evacuating) {
            human.humanWithdrawer();
            human.evacuating = false;
        } else if (human.evacuate) {
            human.humanDrawer();
            human.evacuate = false;
        } else if (human instanceof Visitor && ((Visitor) human).getMyRoom() == null) {
            visitorCheckIn((Visitor) human);
        } else if (human instanceof Visitor && ((Visitor) human).getMyRoom() != null) {
            visitorCheckout((Visitor) human);
        }
    }

    @Override
    public void unitCheckOut(Human human) {
        // Checkout for Evacuate
        if (human.evacuate) {
            human.hteToWait = 15;
            human.evacuating = false;
            human.humanWithdrawer();
            return;
        }
        removeOccupant(human);
    }

    public void visitorCheckIn(Visitor visitor) {
        Room room = getSuitableRoom(visitor.getStars());
        if (room == null) {
            visitor.justLeave();
        } else {
            this.allVisitors.put(visitor.getId(), visitor);
            room.setOccupied(true);
            visitor.setMyRoom(room);
        }
    }

    public void visitorCheckout(Visitor visitor) {
        EventHandler.getHandler().unSubscribe(visitor);
        this.allVisitors.remove(visitor.getId());
        visitor.getMyRoom().setStatus(STATUS.DIRTY);
        needsCleaning(visitor.getMyRoom(), false);
        visitor.removeMyRoom();
        visitor.humanWithdrawer();
    }

    private Room getSuitableRoom(int stars) {
        while (stars <= Constants.getMaxRoomStars()) {
            for (Room room : allRooms) {
                if (!room.isOccupied() && room.getStars() == stars && room.getStatus() == STATUS.CLEAN) {
                    return room;
                }
            }
            stars++;
        }
        return null;
    }

    private void cleaningEmergency(int visitorID) {
        Visitor visitor;
        try {
            visitor = (Visitor) allVisitors.get(visitorID);
        } catch (Exception e) {
            return;
        }

        needsCleaning(visitor.getMyRoom(), true);
    }

    @Override
    public void update(ArrayList<Event> events) {
        events.stream().filter(e -> e.getType() == EVENTTYPES.CLEANING_EMERGENCY).forEach(event -> {
            cleaningEmergency(event.getGuestId());
        });
    }
}
