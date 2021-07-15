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

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HumanEvacute {

    private final JFXPanel panel = new JFXPanel();
    private GridPane space;
    private Room room;
    private Visitor visitor;
    private Cleaner cleaner;
    private final ArrayList<IEventReceiver> humans = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        Constants.setNumberofcleaners(1);
        this.space = new GridPane();

        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        this.room = new Room(1, 1, 1, 1, 5);

        HumanBuilder.setGridPane(this.space);

        Lobby.lobby.addConnections(this.room);
        this.room.addConnections(Lobby.lobby);

        this.visitor = new Visitor(1, this.space, 5);
        visitor.setMyRoom(this.room);
        Lobby.lobby.unitCheckin(this.visitor);
        this.visitor.locationUnit = Lobby.lobby;
        humans.add(this.visitor);

        this.cleaner = new Cleaner(this.space);
        Lobby.lobby.unitCheckin(this.cleaner);
        this.cleaner.locationUnit = Lobby.lobby;
        humans.add(this.cleaner);

        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(EVENTTYPES.EVACUATE, 1));

        humans.forEach(human -> {
            human.update(events);
        });
    }

    @Test
    public void isEvacuating() {
        humans.forEach(human -> {
            if (human instanceof Visitor) {
                assertTrue(((Visitor) human).evacuate);
            } else if (human instanceof Cleaner) {
                assertTrue(((Cleaner) human).evacuate);
            }
        });
    }
}