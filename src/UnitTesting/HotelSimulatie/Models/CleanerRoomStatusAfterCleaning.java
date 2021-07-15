package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.STATUS;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class CleanerRoomStatusAfterCleaning {

    private final JFXPanel panel = new JFXPanel();
    private GridPane space;

    private Room room;
    private Visitor visitor;
    private IEventReceiver cleaner;

    @Before
    public void setUp() {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();
        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.room = new Room(1, 1, 1, 1, 5);
        HumanBuilder.setGridPane(space);
        HumanBuilder.employeeGenerator();

        Lobby.lobby.addConnections(room);
        this.room.addConnections(Lobby.lobby);

        this.visitor = new Visitor(1, space, 5);

        this.visitor.setMyRoom(room);
        this.visitor.setDestination(Lobby.lobby);

        this.visitor.checkOut();

        this.cleaner = (IEventReceiver) Lobby.lobby.getCleaners().get(0);

    }

    @Test
    public void RoomStatusAfterCleaning() {

        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < Constants.getRoomCleaningTime() + 2; i++) {
            this.cleaner.update(events);
        }
        assertEquals(STATUS.CLEAN, room.getStatus());

    }

}
