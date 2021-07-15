package HotelSimulatie.Models;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.STATUS;
import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CleanerTaskLoadAfterVisitorCheckout {

    private final JFXPanel panel = new JFXPanel();
    private GridPane space;
    private Room room;
    private Visitor visitor;

    @Before
    public void setUp() {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();
        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.room = new Room(1, 1, 1, 1, 5);
        HumanBuilder.setGridPane(space);

        Lobby.lobby.addConnections(room);
        this.room.addConnections(Lobby.lobby);

        this.visitor = new Visitor(1, space, 5);

        this.visitor.setMyRoom(room);
        this.visitor.setDestination(Lobby.lobby);

        this.visitor.checkOut();
    }


    @Test
    public void roomSetDirtyAfterVisitorCheckout() {
        assertEquals(STATUS.DIRTY, room.getStatus());
    }

    @Test
    public void cleanerTaskLoadIncreasesAfterVisitorCheckout() {
        int taskLoad = Lobby.lobby.getCleaners().get(0).getTaskLoad();
        assertEquals(1, taskLoad);
    }

}