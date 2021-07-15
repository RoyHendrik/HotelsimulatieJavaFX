package HotelSimulatie.SimLogic;

import HotelSimulatie.Constructor.HumanBuilder;
import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.Interfaces.IHTEUpdate;
import HotelSimulatie.Models.Event;
import HotelSimulatie.Models.Human;
import HotelSimulatie.Models.Visitor;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class EventHandler implements IHTEUpdate {
    private static final EventHandler handler = new EventHandler();
    private int lastMovie = 0;
    private final ArrayList<IEventReceiver> subscribers = new ArrayList<>();
    private final ArrayList<IEventReceiver> unsubscribers = new ArrayList<>();
    private Text hteTime;
    private ArrayList<Event> hteEvents;
    private final Queue<Event> events = new LinkedList<>();
    private SimulationTimer timer = new SimulationTimer(1);
    private EventHandler() {
    }

    public static EventHandler getHandler() {
        return handler;
    }

    public int getLastMovie() {
        return lastMovie;
    }

    public void addSubscriber(IEventReceiver iEventReceiver) {
        subscribers.add(iEventReceiver);
    }

    private void removeSubscriber(IEventReceiver iEventReceiver) {
        subscribers.remove(iEventReceiver);
    }

    public void unSubscribe(IEventReceiver iEventReceiver) {
        unsubscribers.add(iEventReceiver);
    }

    public void setHandler(ArrayList<Event> events, double hteSpeed, Text hteTime) {
        events.sort(Comparator.comparingInt(event1 -> event1.getTime()));
        this.events.addAll(events);
        this.events.forEach(event -> {
            if (event.getType().equals(EVENTTYPES.START_CINEMA) && (event.getTime() + event.getDuration()) > lastMovie) {
                lastMovie = event.getTime() + event.getDuration();
            }
        });
        this.timer = new SimulationTimer((int) hteSpeed);
        timer.subscribe(this);
        this.hteTime = hteTime;
    }

    public void startTimer() {
        this.timer.startTimer();
    }

    public void pauseTimer() {
        this.timer.pauseTimer();
    }

    public void resumeTimer() {
        this.timer.resumeTimer();
    }

    @Override
    public void update() {
        hteTime.setText("Time: " + this.timer.getLoopNumber());
        setHteEvents();
        setCheckinHte();

        for (IEventReceiver humanSubscriber : subscribers) {
            humanSubscriber.update(hteEvents);
        }
        subscriberSync();
    }

    private void subscriberSync() {
        if (!unsubscribers.isEmpty()) {
            for (int i = 0; i < unsubscribers.size(); i++) {
                removeSubscriber(unsubscribers.get(i));
            }
        }
    }

    public int getHteTime() {
        return this.timer.getLoopNumber();
    }

    private void setCheckinHte() {
        if (hteEvents.isEmpty()) {
            return;
        }

        for (Event event : hteEvents) {
            if (event.getType().equals(EVENTTYPES.CHECK_IN)) {
                Human newHuman = HumanBuilder.visitorToBeBorn(event.getGuestId(), event.getStars(), event);
                subscribers.add((Visitor) newHuman);
            }
        }
    }

    private void setHteEvents() {
        if (events.isEmpty()) {
            hteEvents = new ArrayList<>();
            return;
        }

        boolean gettingEvents = true;
        hteEvents = new ArrayList<>();

        while (gettingEvents) {
            if (!events.isEmpty() && events.peek().getTime() == timer.getLoopNumber()) {
                hteEvents.add(events.remove());
            } else {
                gettingEvents = false;
            }
        }
    }
}
