package HotelSimulatie.Constructor;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Models.Event;
import HotelSimulatie.SimLogic.Constants;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EventBuilderSafetyCheck {

    String eventFilePath = "LIBS/testFile_Event.json";
    FileReader fileReader = FileReader.getFileReader();
    ArrayList<Event> testEvents;
    Event testCheckInEvent = new Event(EVENTTYPES.CHECK_IN, 1, 1, 1);

    @Test
    public void testIfEventsAreOfTheSameType() {
        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertEquals(testCheckInEvent.getType(), testEvents.get(0).getType());
    }

    @Test
    public void testIfEventsHaveTheSameTime() {
        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertEquals(testCheckInEvent.getTime(), testEvents.get(0).getTime());
    }

    @Test
    public void testIfEventsHaveTheSameGuestId() {
        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertEquals(testCheckInEvent.getGuestId(), testEvents.get(0).getGuestId());
    }

    @Test
    public void testIfEventsHaveTheSameStars() {
        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertEquals(testCheckInEvent.getStars(), testEvents.get(0).getStars());
    }


    @Test
    public void testIfGodzillaIsRemovedFromArray() {
        int OriginalJsonArraySize = 5;

        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertNotEquals(testEvents.size(), OriginalJsonArraySize);
    }

    @Test
    public void testIfMaximumStarsOfCheckInEventGetsSetToFive()
    {
        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertEquals(Constants.getMaxRoomStars(), testEvents.get(1).getStars());
    }

    @Test
    public void checkIfFitnessDurationFallbackWorks()
    {
        EventBuilder eventBuilder = new EventBuilder(eventFilePath, fileReader);
        testEvents = eventBuilder.getArray();

        assertEquals(Constants.getFitnessNoTimeFallback(),testEvents.get(6).getDuration());

    }

}