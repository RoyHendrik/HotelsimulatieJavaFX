package HotelSimulatie.Interfaces;

import HotelSimulatie.Models.Human;

import java.util.ArrayList;

public interface IAmOccupiable {

    ArrayList<Human> getOccupants();

    void addOccupant(Human human);

    void removeOccupant(Human human);

    void unitCheckin(Human human);

    void unitCheckOut(Human human);
}
