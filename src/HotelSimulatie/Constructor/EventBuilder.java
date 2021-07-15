package HotelSimulatie.Constructor;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Interfaces.IBuilder;
import HotelSimulatie.Models.Event;
import HotelSimulatie.SimLogic.Constants;

import java.util.ArrayList;

/**
 * EventBuilder
 * the builder class that makes events and put them into an array.
 */
public class EventBuilder implements IBuilder<Event> {
    private final ArrayList<Event> events = new ArrayList<Event>();

    /**
     * Constructor
     *
     * @param filepath
     * @param reader   Gets the filepath and the reader object.
     *                 starts the reader function in the FileReader.
     */
    public EventBuilder(String filepath, FileReader reader) {
        reader.readEvents(filepath, this);
    }

    /**
     * addNew
     *
     * @param newEvent for adding new Objects to the Array
     */
    @Override
    public void addNew(Event newEvent) {
        this.events.add(newEvent);
    }

    /**
     * GetArray
     *
     * @return returns the class Array
     */
    @Override
    public ArrayList<Event> getArray() {
        return this.events;
    }

    /**
     * ConstructNew
     *
     * @param event
     * @param time
     * @param guestID
     * @param checkInStars
     * @param duration     the function that creates the events and sends them To the addNew function to put them into the array.
     *                     it checks the type of event, the none-legit type will be automatically set to Default and gets filtered out.
     */
    @Override
    public void constructNew(String event, int time, int guestID, int checkInStars, int duration) {
        EVENTTYPES eventTypes = checkType(event);
        Event newEvent;

        switch (eventTypes) {
            case CHECK_IN:
                if (checkInStars > Constants.getMaxRoomStars()) {
                    checkInStars = 5;
                }
                newEvent = new Event(eventTypes, time, guestID, checkInStars);
                addNew(newEvent);
                break;
            case CHECK_OUT:
            case GO_TO_CINEMA:
            case GO_TO_DINER:
            case CLEANING_EMERGENCY:
                newEvent = new Event(eventTypes, time, guestID);
                addNew(newEvent);
                break;
            case GO_TO_FITNESS:
                if (duration == 0) {
                    duration = Constants.getFitnessNoTimeFallback();
                }
                newEvent = new Event(eventTypes, time, guestID, duration);
                addNew(newEvent);
                break;
            case START_CINEMA:
                newEvent = new Event(eventTypes, time, duration);
                addNew(newEvent);
                break;
            case EVACUATE:
                newEvent = new Event(eventTypes, time);
                addNew(newEvent);
                break;
            case DEFAULT:
                break;
        }
    }

    /**
     * checkType
     *
     * @param type
     * @return checks the type of event, if it is legit it returns the type
     * if it isn't it returns DEFAULT.
     */
    private EVENTTYPES checkType(String type) {
        EVENTTYPES event;
        try {
            event = EVENTTYPES.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            event = EVENTTYPES.DEFAULT;
        }
        return event;
    }
}
