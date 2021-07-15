package HotelSimulatie.SimLogic;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Models.Diner;
import HotelSimulatie.Models.Human;
import HotelSimulatie.Models.Lobby;
import HotelSimulatie.Models.Unit;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class PathFindingTest {
    PathFinding pathFinding;
    private final JFXPanel panel = new JFXPanel();
    private GridPane space;

    private Diner diner;
    private Unit unit;
    private Human human;

    @Before
    public void setupPathfinderTest() {
        this.space = new GridPane();
        this.pathFinding = new PathFinding();
        Lobby.lobby.setLobbySpecs(1, 1, 0, 0);
        this.unit = new Unit(1, 1, 0, 1);
        this.diner = new Diner(1, 1, 0, 2, 1);
        this.human = new Human(space);
        Lobby.lobby.addConnections(unit);
        this.unit.addConnections(Lobby.lobby);
        this.unit.addConnections(diner);
        this.diner.addConnections(unit);

    }

    @Test
    void testPathfinderHumanToUnit() {
        setupPathfinderTest();

        LinkedList<Unit> run = this.pathFinding.pathToUnit(human, unit);
        LinkedList<Unit> expResult = new LinkedList<>();
        expResult.push(unit);

        Assert.assertEquals(expResult, run);

    }

    @Test
    void testPathfinderHumanToEvent() {
        setupPathfinderTest();
        LinkedList<Unit> run = this.pathFinding.pathToEvent(human, EVENTTYPES.GO_TO_DINER);
        LinkedList<Unit> expResult = new LinkedList<>();
        expResult.push(diner);
        expResult.push(unit);

        Assert.assertEquals(expResult.getClass(), run.getClass());

    }
}