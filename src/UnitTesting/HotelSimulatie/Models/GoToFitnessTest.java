package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GoToFitnessTest {
    private final JFXPanel panel = new JFXPanel();
    private GridPane space;
    private Corridor corridor;
    private Room room;
    private Fitness fitness;
    private Visitor visitor;

    @Before
    public void setUp() throws Exception {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();

        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.corridor = new Corridor(1, 2, false);
        this.room = new Room(1, 1, 1, 1, 5);
        this.fitness = new Fitness(1, 1, 1, 3);

        HumanBuilder.setGridPane(this.space);

        Stairs strair1 = new Stairs(0, 4);
        Stairs strair2 = new Stairs(1, 4);

        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(this.room);
        Lobby.lobby.setAllRooms(rooms);

        Lobby.lobby.addConnections(strair1);

        strair1.addConnections(strair2);
        strair2.addConnections(strair1);

        strair2.addConnections(this.fitness);
        this.fitness.addConnections(strair2);

        this.fitness.addConnections(this.corridor);
        this.corridor.addConnections(this.fitness);

        this.corridor.addConnections(this.room);
        this.room.addConnections(this.corridor);

        this.visitor = (Visitor) HumanBuilder.visitorToBeBorn(1, 5, new Event(EVENTTYPES.CHECK_IN, 1));
    }

    @Test
    public void hasHumanGotARoomAfterBeingBorn() {
        assertNotNull(this.visitor.getMyRoom());
    }

    @Test
    public void humanGoesToFitness() {
        ArrayList<Event> events = new ArrayList<>();
        this.visitor.update(events);
        for (int i = 0; i < 7; i++) {
            this.visitor.update(events);
        }

        events.add(new Event(EVENTTYPES.GO_TO_FITNESS, 4, 1, 5));
        this.visitor.update(events);
        events.remove(0);

        for (int i = 0; i < 3; i++) {
            this.visitor.update(events);
        }

        assertTrue(this.visitor.locationUnit instanceof Fitness);

        Fitness fitness = (Fitness) this.visitor.locationUnit;
        assertEquals(1, fitness.getOccupants().size());

        for (int i = 0; i < 10; i++) {
            this.visitor.update(events);
        }

        assertEquals(0, fitness.getOccupants().size());
        assertTrue(this.visitor.locationUnit instanceof Room);
    }
}