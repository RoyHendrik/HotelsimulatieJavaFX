package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class VisitorGoesToRoomAfterDining {

    ArrayList<Event> events = new ArrayList<>();
    private final JFXPanel panel = new JFXPanel();
    private GridPane space;
    private Room room;
    private Visitor visitor;
    private Diner diner;
    private Corridor corridor;

    @Before
    public void setUp() {
        this.space = new GridPane();
        Lobby.lobby.setLobbySpecs(1, 1, 1, 1);
        this.room = new Room(1, 1, 1, 2, 5);
        this.corridor = new Corridor(1, 3, false);
        this.diner = new Diner(1, 1, 1, 4, 4);

        HumanBuilder.setGridPane(space);

        Lobby.lobby.addConnections(room);
        this.room.addConnections(Lobby.lobby);
        this.room.addConnections(corridor);
        this.corridor.addConnections(room);
        this.corridor.addConnections(diner);
        this.diner.addConnections(corridor);

        this.visitor = new Visitor(1, space, 5);

        this.visitor.setMyRoom(room);
        this.visitor.setDestination(Lobby.lobby);

        Event event = new Event(EVENTTYPES.GO_TO_DINER, 1, 1, 7);
        events.add(event);

        this.visitor.update(events);

        events.remove(event);


    }

    @Test
    public void GoesToRoomAfterDining() {

        for (int i = 0; i < Constants.getDinerTime() + 5; i++) {
            this.visitor.update(events);
        }

        assertEquals(room, this.visitor.locationUnit);
    }
}