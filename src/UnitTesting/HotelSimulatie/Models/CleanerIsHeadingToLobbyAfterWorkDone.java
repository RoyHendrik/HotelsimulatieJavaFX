package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CleanerIsHeadingToLobbyAfterWorkDone {


    ArrayList<Event> events = new ArrayList<>();
    private final JFXPanel panel = new JFXPanel();
    private GridPane space;
    private Lobby lobby = Lobby.lobby;
    private Room room;
    private Visitor visitor;
    private IEventReceiver cleaner;

    @Before
    public void setUp() {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();
        lobby.setLobbySpecs(1, 1, 0, 1);
        this.room = new Room(1, 1, 1, 1, 5);
        HumanBuilder.setGridPane(space);
        HumanBuilder.employeeGenerator();

        lobby.addConnections(room);
        this.room.addConnections(lobby);

        this.visitor = new Visitor(1, space, 5);

        this.visitor.setMyRoom(room);
        this.visitor.setDestination(lobby);

        this.visitor.checkOut();

        this.cleaner = (IEventReceiver) lobby.getCleaners().get(0);

        for (int i = 0; i < Constants.getRoomCleaningTime() + 3; i++) {
            this.cleaner.update(events);
        }
    }

    @Test
    public void CleanerIsHeadingToLobby() {
        assertEquals(lobby, ((Human) this.cleaner).locationUnit);
    }

    @Test
    public void CleanerIsRandomMovingAfterWorkDone() {

        for (int i = 2; i < 20; i++) {

            this.cleaner.update(events);
            this.cleaner.update(events);

            if (i % 2 == 0) {
                assertEquals(room, ((Human) this.cleaner).locationUnit);
            } else {
                assertEquals(Lobby.lobby, ((Human) this.cleaner).locationUnit);
            }
        }
    }
}