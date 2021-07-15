package HotelSimulatie.Interfaces;

import HotelSimulatie.Models.Event;

import java.util.ArrayList;

public interface IEventReceiver {
    void update(ArrayList<Event> events);
}
