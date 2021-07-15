package HotelSimulatie.SimLogic;

import HotelSimulatie.Models.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UsingLiftBasedOnConstants {

    ArrayList<Event> events = new ArrayList<>();
    private final JFXPanel panel = new JFXPanel();
    private GridPane space;

    private Room room;
    private Corridor corridorY0;
    private Corridor corridorY1;
    private Stairs stairsY0;
    private Stairs stairsY1;
    private Lift liftY0;
    private Lift liftY1;
    private Visitor visitor;

    @Before
    public void setUp() {
        this.space = new GridPane();

        this.liftY0 = new Lift(0, 0);
        this.liftY1 = new Lift(1, 0);
        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.room = new Room(1, 1, 1, 1, 5);
        this.corridorY0 = new Corridor(1, 2, false);
        this.corridorY1 = new Corridor(0, 2, false);
        this.stairsY0 = new Stairs(0, 3);
        this.stairsY1 = new Stairs(1, 3);

        this.liftY0.addConnections(liftY1);
        this.liftY0.addConnections(Lobby.lobby);

        Lobby.lobby.addConnections(liftY0);
        Lobby.lobby.addConnections(corridorY0);

        this.corridorY0.addConnections(Lobby.lobby);
        this.corridorY0.addConnections(stairsY0);

        this.stairsY0.addConnections(corridorY0);
        this.stairsY0.addConnections(stairsY1);

        this.stairsY1.addConnections(stairsY0);
        this.stairsY1.addConnections(corridorY1);

        this.corridorY1.addConnections(stairsY1);
        this.corridorY1.addConnections(room);

        this.room.addConnections(corridorY1);
        this.room.addConnections(liftY1);

        this.liftY1.addConnections(room);
        this.liftY1.addConnections(liftY0);

        this.visitor = new Visitor(1, space, 5);

    }

    @Test
    public void notUsingLiftWhenSwitchedOff() {

        Constants.setLiftActive(false);
        this.visitor.setMyRoom(room);

        for (int i = 0; i < 8; i++) {
            this.visitor.update(events);
            if (i == 4) {
                assertNotEquals(room, this.visitor.getLocationUnit());
            }
        }
        assertEquals(room, this.visitor.getLocationUnit());

    }

    @Test
    public void notUsingLiftWhenSwitchedOn() {

        Constants.setLiftActive(true);
        this.visitor.setMyRoom(room);

        for (int i = 0; i < 4; i++) {
            this.visitor.update(events);
        }

        assertEquals(room, this.visitor.getLocationUnit());

    }
}
