package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ResetWaiting {

    private final JFXPanel panel = new JFXPanel();
    private GridPane space;

    private Room room;
    private Visitor visitor;


    @Before
    public void setUp() throws Exception {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();
        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.room = new Room(1, 1, 1, 1, 5);
        HumanBuilder.setGridPane(space);
        Lobby.lobby.addConnections(room);
        this.room.addConnections(Lobby.lobby);
        this.visitor = new Visitor(1, space, 5);
        this.visitor.setMyRoom(room);
        visitor.isMoving = false;
        visitor.setCurrentEvent(new Event(EVENTTYPES.GO_TO_DINER, 1, 1));
        visitor.iWantToWaitNextTime();
        visitor.stillWaiting();
        visitor.stillWaiting();
        visitor.iveWaited();
    }

    @Test
    public void waitingTimeReset() {
        assertFalse(visitor.doYouWantToWaitForDiner());
    }


}