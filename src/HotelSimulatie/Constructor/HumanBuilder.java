package HotelSimulatie.Constructor;

import HotelSimulatie.Interfaces.ICanClean;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.Models.*;
import HotelSimulatie.SimLogic.Constants;
import HotelSimulatie.SimLogic.EventHandler;
import javafx.scene.layout.GridPane;


public class HumanBuilder {
    private static GridPane space;

    private HumanBuilder() {}

    public static void setGridPane(GridPane gridPane) {
        HumanBuilder.space = gridPane;
    }

    public static Human visitorToBeBorn(int id, int stars, Event event) {
        Human newHuman = new Visitor(id, space, stars);
        newHuman.setCurrentEvent(event);
        Lobby.lobby.unitCheckin(newHuman);
        return newHuman;
    }

    public static void employeeGenerator() {
        EventHandler eventHandler = EventHandler.getHandler();
        for (int i = 0; i < Constants.getNumberofcleaners(); i++) {
            ICanClean cleaner = new Cleaner(space);

            Lobby.lobby.addCleaner(cleaner);
            eventHandler.addSubscriber((IEventReceiver) cleaner);
        }
    }


}
