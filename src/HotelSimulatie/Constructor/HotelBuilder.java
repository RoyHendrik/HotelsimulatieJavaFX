package HotelSimulatie.Constructor;

import HotelSimulatie.Interfaces.ICleanable;
import HotelSimulatie.Models.*;

import java.util.ArrayList;
import java.util.LinkedList;

public class HotelBuilder {
    private final LinkedList<Unit> allObjects;
    private Unit[][] codeGrid;
    private final ArrayList<Room> allRooms = new ArrayList<>();
    private int maxSpanWidth;
    private int maxSpanHeight;

    public HotelBuilder(LinkedList<Unit> allObjects) {
        this.allObjects = allObjects;
        initializer();
    }

    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    public int getMaxSpanWidth() {
        return maxSpanWidth;
    }

    public Unit[][] getCodeGrid() {
        return codeGrid;
    }

    public int getMaxSpanHeight() {
        return maxSpanHeight;
    }

    private void initializer() {
        sizeCalc();
        roomInitializer();
        lobbyLiftStairs();
        cleaningServiceAttachment();
        arrayBuilder();
        visibleCorridorFiller();
        setConnections();
    }

    private void sizeCalc() {
        for (Unit unit : allObjects) {
            maxSpanHeight = Math.max(maxSpanHeight, unit.getVisualYPos() + unit.getHeight());
            maxSpanWidth = Math.max(maxSpanWidth, unit.getVisualXPos() + unit.getWidth());
        }

    }

    private void roomInitializer() {
        for (Unit unit : allObjects) {
            if (unit instanceof Room) {
                allRooms.add((Room) unit);
            }
        }
    }

    private void lobbyLiftStairs() {
        maxSpanHeight++;
        maxSpanWidth += 2;
        for (Unit unit : allObjects) {
            unit.setVisualXPos(unit.getVisualXPos() + 1);
            unit.setVisualYPos(unit.getVisualYPos() + 1);
        }

        for (int i = 0; i < maxSpanHeight; i++) {
            allObjects.add(new Lift(i, 0));
            allObjects.add(new Stairs(i, maxSpanWidth - 1));
        }
        Lobby.lobby.setLobbySpecs(maxSpanWidth - 2, 1, 0, 1);
        allObjects.add(Lobby.lobby);
    }

    private void arrayBuilder() {
        codeGrid = new Unit[maxSpanHeight][maxSpanWidth];
        for (Unit unit : allObjects) {
            codeGrid[unit.getVisualYPos()][unit.getVisualXPos()] = unit;

            if (unit.getHeight() > 1) {
                for (int i = 1; i < unit.getHeight(); i++) {
                    codeGrid[unit.getVisualYPos() + i][unit.getVisualXPos()] = new Corridor(unit.getVisualYPos() + i, unit.getVisualXPos(), false);
                    if (unit.getWidth() > 1) {
                        for (int j = 1; j < unit.getWidth(); j++) {
                            codeGrid[unit.getVisualYPos() + i][unit.getVisualXPos() + j] = new Corridor(unit.getVisualYPos() + i, unit.getVisualXPos() + j, false);
                        }
                    }
                }
            }
            if (unit.getWidth() > 1) {
                for (int i = 1; i < unit.getWidth(); i++) {
                    codeGrid[unit.getVisualYPos()][unit.getVisualXPos() + i] = new Corridor(unit.getVisualYPos(), unit.getVisualXPos() + i, false);

                }
            }

        }
    }

    private void visibleCorridorFiller() {
        for (int i = 0; i < codeGrid.length; i++) {
            for (int j = 0; j < codeGrid[i].length; j++) {
                if (codeGrid[i][j] == null) {
                    codeGrid[i][j] = new Corridor(i, j, true);
                }
            }
        }
    }

    private void setConnections() {
        for (int y = 0; y < codeGrid.length; y++) {
            for (int x = 0; x < codeGrid[y].length; x++) {

                Unit unit = codeGrid[y][x];
                if (unit == null) {
                    continue;
                }

                if (unit instanceof Stairs || unit instanceof Lift) {
                    try {
                        unit.addConnections(codeGrid[y - 1][x]);
                    } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {
                    }
                    try {
                        unit.addConnections(codeGrid[y + 1][x]);
                    } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {
                    }
                }
                try {
                    unit.addConnections(codeGrid[y][x - 1]);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {
                }
                try {
                    unit.addConnections(codeGrid[y][x + 1]);
                } catch (NullPointerException | ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }

    private void cleaningServiceAttachment() {
        for (Unit unit : allObjects) {
            if (unit instanceof ICleanable) {
                ((ICleanable) unit).setCleaningNotifyUnit(Lobby.lobby);
            }
        }
    }
}
