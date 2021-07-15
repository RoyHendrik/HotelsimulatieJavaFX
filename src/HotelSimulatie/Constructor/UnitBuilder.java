package HotelSimulatie.Constructor;

import HotelSimulatie.Enums.UNITTYPES;
import HotelSimulatie.Interfaces.IBuilder;
import HotelSimulatie.Models.*;
import HotelSimulatie.SimLogic.Constants;

import java.util.ArrayList;

/**
 * UnitBuilder
 * The Unit builder class that constructs units and put them into an array.
 */
public class UnitBuilder implements IBuilder<Unit> {
    private final ArrayList<Unit> units = new ArrayList<>();

    /**
     * Constructor
     *
     * @param filepath
     * @param reader   get the filepath & the FileReader.
     *                 starts the reader function in the FileReader.
     */
    public UnitBuilder(String filepath, FileReader reader) {
        reader.layoutReader(filepath, this);
    }

    /**
     * addNew
     *
     * @param newUnit for adding new Objects to the Array
     */
    @Override
    public void addNew(Unit newUnit) {
        this.units.add(newUnit);
    }


    /**
     * GetArray
     *
     * @return returns the class Array
     */
    @Override
    public ArrayList<Unit> getArray() {
        return this.units;
    }

    /**
     * ConstructNew
     *
     * @param type
     * @param width
     * @param height
     * @param visualYPos
     * @param visualXpos the unit creating function that makes an new Unit and sends them to addNew to put them into an array.
     *                   it checks which type of unit it is creates a new unit and sends it away.
     */
    @Override
    public void constructNew(String type, int width, int height, int visualYPos, int visualXpos) {
        UNITTYPES unitTypes = checkType(type);

        switch (unitTypes) {
            case FITNESS:
                Fitness newFitness = new Fitness(width, height, visualYPos, visualXpos);
                addNew(newFitness);
                break;
            case MOVIE:
                Cinema newCinema = new Cinema(width, height, visualYPos, visualXpos);
                addNew(newCinema);
                break;
            case LOBBY:
                Lobby.lobby.setLobbySpecs(width, height, visualYPos, visualXpos);
                addNew(Lobby.lobby);
                break;
            case LIFT:
                Lift newLift = new Lift(visualYPos, visualXpos);
                addNew(newLift);
                break;
            case STAIRS:
                Stairs newStairs = new Stairs(visualYPos, visualXpos);
                addNew(newStairs);
                break;
            case DEFAULT:
                break;
        }
    }

    /**
     * ConstructNew
     *
     * @param type
     * @param width
     * @param height
     * @param visualYPos
     * @param visualXPos
     * @param data       the unit creating function that makes an new Unit and sends them to addNew to put them into an array.
     *                   this ConstructNew has an extra parameter argument "data" for the units that have additional data,
     *                   all the other units get made in the other unit.
     */
    public void constructNew(String type, int width, int height, int visualYPos, int visualXPos, int data) {
        UNITTYPES unitTypes = checkType(type);

        switch (unitTypes) {
            case ROOM:
                if (data > Constants.getMaxRoomStars()) {
                    data = 5;
                }
                Room newRoom = new Room(width, height, visualYPos, visualXPos, data);
                addNew(newRoom);
                break;
            case DINER:
                Diner newDiner = new Diner(width, height, visualYPos, visualXPos, data);
                addNew(newDiner);
                break;
            case DEFAULT:
                break;
        }
    }

    /**
     * CheckType
     *
     * @param type
     * @return a return method that checks if the given unit type a legit type is.
     * if it is it returns the type if it isn't it returns default;
     */
    private UNITTYPES checkType(String type) {
        UNITTYPES unit;
        try {
            unit = UNITTYPES.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            unit = UNITTYPES.DEFAULT;
        }
        return unit;
    }
}
