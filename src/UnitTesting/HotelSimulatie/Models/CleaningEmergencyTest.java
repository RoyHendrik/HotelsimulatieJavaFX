package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.ICanClean;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CleaningEmergencyTest {

    private final JFXPanel panel = new JFXPanel();
    private GridPane space;
    private Room roomX1;
    private Room roomX2;
    private Fitness fitness;

    @Before
    public void setUp() throws Exception {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();

        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.roomX2 = new Room(1, 2, 1, 2, 4);
        this.roomX1 = new Room(1, 1, 1, 1, 5);
        this.fitness = new Fitness(1, 1, 1, 3);

        HumanBuilder.setGridPane(this.space);

        Stairs strair1 = new Stairs(0, 4);
        Stairs strair2 = new Stairs(1, 4);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(this.roomX1);
        rooms.add(this.roomX2);
        Lobby.lobby.setAllRooms(rooms);

        Lobby.lobby.addConnections(strair1);

        strair1.addConnections(strair2);
        strair2.addConnections(strair1);

        strair2.addConnections(this.fitness);
        this.fitness.addConnections(strair2);

        this.fitness.addConnections(this.roomX2);
        this.roomX2.addConnections(this.fitness);

        this.roomX2.addConnections(this.roomX1);
        this.roomX1.addConnections(this.roomX2);

        HumanBuilder.visitorToBeBorn(1, 5, new Event(EVENTTYPES.CHECK_IN, 1));

        ArrayList<ICanClean> cleaners = Lobby.lobby.getCleaners();
        cleaners.get(0).addTask(this.roomX2, false);

        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(EVENTTYPES.CLEANING_EMERGENCY, 2, 1));
        Lobby.lobby.update(events);
    }

    @Test
    public void checkIfRoomIsAddedToTheTopOfCleanerTask() {
        ArrayList<ICanClean> cleaners = Lobby.lobby.getCleaners();
        Room roomToClean = ((Room) ((Cleaner) cleaners.get(0)).getTasks().peek());

        assertEquals(1, roomToClean.getVisualYPos());
        assertEquals(1, roomToClean.getVisualXPos());
        assertEquals(2, ((Cleaner) cleaners.get(0)).getTasks().size());
    }
}