package HotelSimulatie.Models;

import HotelSimulatie.SimLogic.Constants;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class VisitorDiesWaitingLong {
    Visitor visitor;
    private final JFXPanel panel = new JFXPanel();

    @Before
    public void setUp() throws Exception {
        GridPane space = new GridPane();
        Lobby.lobby.setLobbySpecs(1, 1, 0, 1);
        visitor = new Visitor(1, space, 1);
        visitor.locationUnit = Lobby.lobby;

    }

    @Test
    public void isDeadAfterWaiting1MoreThenMax() {
        for (int i = 0; i < Constants.getDieAfterWaiting() + 1; i++) {
            visitor.stillWaiting();
        }
        assertTrue(visitor.isDead());
    }

}