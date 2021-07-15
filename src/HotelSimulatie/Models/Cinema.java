package HotelSimulatie.Models;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.IAmOccupiable;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.SimLogic.Constants;
import HotelSimulatie.SimLogic.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cinema extends Unit implements IAmOccupiable, IEventReceiver {
    private final ArrayList<Human> occupants = new ArrayList<>();
    private int movieDuration = 0;
    private boolean movieRunning = false;

    public Cinema(int width, int height, int visualYPos, int visualXPos) {
        super(width, height, visualYPos, visualXPos);
        this.bgImage = new ImageView(new Image("HotelSimulatie/Images/cinema.png", width * Constants.getUNITPIXELWIDTH(), height * Constants.getUNITPIXELHEIGHT(), false, true));
    }

    private void reduceMovieTime() {

        this.movieDuration--;
        if (movieDuration == 0) {
            movieRunning = false;

            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(0);
            bgImage.setEffect(colorAdjust);

            int occpantSize = occupants.size();
            for (int i = 0; i < occpantSize; i++) {
                unitCheckOut(occupants.get(0));
            }
        }
    }

    @Override
    public void addOccupant(Human human) {
        occupants.add(human);
    }

    @Override
    public ArrayList<Human> getOccupants() {
        return occupants;
    }

    public void removeOccupant(Human human) {
        occupants.remove(human);
    }

    @Override
    public void unitCheckin(Human human) {
        addOccupant(human);
        human.visualRemover();
        human.hasCheckedIn();
        if (EventHandler.getHandler().getLastMovie() < EventHandler.getHandler().getHteTime()) {
            unitCheckOut(human);
        }
    }

    @Override
    public void unitCheckOut(Human human) {
        if (occupants.contains(human)) {
            human.hasCheckedOut();
            removeOccupant(human);
            human.visualAdder();
            Visitor visitor = (Visitor) human;
            visitor.goToRoom();
        }
    }

    @Override
    public void update(ArrayList<Event> events) {
        if (events.stream().anyMatch(e -> e.getType() == EVENTTYPES.START_CINEMA)) {
            List<Event> allEvents = events.stream().filter(e -> e.getType() == EVENTTYPES.START_CINEMA).collect(Collectors.toList());
            movieDuration = allEvents.get(0).getDuration();
            movieRunning = true;
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.5);
            bgImage.setEffect(colorAdjust);
        } else {
            if (movieRunning) {
                reduceMovieTime();
            }
        }
    }
}
