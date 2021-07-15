package HotelSimulatie.SimLogic;

import HotelSimulatie.Enums.EVENTTYPES;
import HotelSimulatie.Models.*;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Pathfinding
 * the class that takes care of finding a path inside the hotel.
 */
public class PathFinding {

    private LinkedList<Unit> openNodes;
    private LinkedList<Unit> closedNodes;
    private LinkedList<Unit> path;

    public PathFinding() {
    }

    /**
     * pathToUnit
     * An method that needs a human type object and a Unit as target for direct A* searching.
     * returns a list with the path
     *
     * @param human
     * @param end
     * @return
     */
    public LinkedList<Unit> pathToUnit(Human human, Unit end) {
        openNodes = new LinkedList<>();
        closedNodes = new LinkedList<>();
        path = new LinkedList<>();

        //add the first node to the Open list
        Unit start = human.getLocationUnit();
        if (start != null) {
            addNodeToOpen(start);
        }

        while (!openNodes.isEmpty()) {

            if (openNodes.size() > 0) {

                Unit current = openNodes.get(getLowestIndex());

                // stop if current node is the target node
                if (current == end) {
                    makePath(current, human.getLocationUnit());
                    break;
                }

                //remove current node from open nodes list
                removeNodeFromOpenList(current);

                //set current node is the closed nodes list
                addNodeToClosed(current);

                //get the neighbours of the current node in a arraylist
                ArrayList neighbours = current.getConnections();

                //loop through all neighbours of current node
                for (int i = 0; i < current.getConnections().size(); i++) {

                    //current neighbor
                    Unit neighbor = (Unit) neighbours.get(i);
                    if (!Constants.isLiftActive() && (neighbor instanceof Lift)
                            || (neighbor instanceof Lift) && (human instanceof Cleaner)) {
                        continue;
                    }

                    //if current neighbor is not in closedList
                    if (!closedNodes.contains(neighbor)) {
                        //calculate G cost (distance from starting node)
                        int tempG = current.getGCost() + (current.getPassingTime() * 10);

                        if (openNodes.contains(neighbor)) {
                            if (tempG < neighbor.getGCost()) {
                                neighbor.setGCost(tempG);
                            }
                        } else {
                            neighbor.setGCost(tempG);
                            openNodes.push(neighbor);
                        }

                        //calculate H cost (distance fom end node)
                        neighbor.setHCost(heuristic(neighbor, end));
                        //get F cost (which is G cost + H cost)
                        neighbor.getFCost();
                        //set neighbour as parent to current node
                        neighbor.setPreviousConnection(current);
                    }

                }

            } else {
                break;
            }
        }

        return path;
    }

    /**
     * pathToEvent
     * An method that needs an human type object and a Event type.
     * the appropriate room will be found that matches with the event type.
     * returns a list with the path
     *
     * @param human
     * @param eventTypes
     * @return
     */
    public LinkedList<Unit> pathToEvent(Human human, EVENTTYPES eventTypes) {
        openNodes = new LinkedList<>();
        closedNodes = new LinkedList<>();
        path = new LinkedList<>();

        //add the first node to the Open list
        Unit humanUnit = human.getLocationUnit();
        if (humanUnit != null) {
            addNodeToOpen(humanUnit);
        }

        while (!openNodes.isEmpty()) {

            if (openNodes.size() > 0) {

                Unit current = openNodes.get(getLowestIndex());

                // stop if current node is equal to target type node
                boolean foundPath = false;
                switch (eventTypes) {
                    case GO_TO_CINEMA:
                        if (current instanceof Cinema) {
                            makePath(current, human.getLocationUnit());
                            foundPath = true;
                        }
                        break;
                    case GO_TO_DINER:
                        if (current instanceof Diner && humanUnit != current) {
                            makePath(current, human.getLocationUnit());
                            foundPath = true;
                        }
                        break;
                    case GO_TO_FITNESS:
                        if (current instanceof Fitness) {
                            makePath(current, human.getLocationUnit());
                            foundPath = true;
                        }
                        break;
                    case EVACUATE:
                        if (current instanceof Lobby) {
                            makePath(current, human.getLocationUnit());
                            foundPath = true;
                        }
                }

                if (foundPath) {
                    break;
                }

                //remove current node from open list
                removeNodeFromOpenList(current);

                //set current node is the closed nodes list
                addNodeToClosed(current);

                //get the neighbours of the current node in a arraylist
                ArrayList neighbours = current.getConnections();

                //loop through all neighbours of current node
                for (int i = 0; i < current.getConnections().size(); i++) {

                    //current neighbor
                    Unit neighbor = (Unit) neighbours.get(i);
                    if (!Constants.isLiftActive() && (neighbor instanceof Lift)
                            || (neighbor instanceof Lift) && (human instanceof Cleaner)) {
                        continue;
                    }

                    //if current neighbor is not in closedList
                    if (!closedNodes.contains(neighbor)) {
                        //calculate G cost (distance from starting node)
                        int tempG = current.getGCost() + (current.getPassingTime() * 10);
                        if (openNodes.contains(neighbor)) {
                            if (tempG < neighbor.getGCost()) {
                                neighbor.setGCost(tempG);
                            }
                        } else {
                            neighbor.setGCost(tempG);
                            openNodes.push(neighbor);
                        }

                        //get F cost (which is G cost + H cost)
                        neighbor.getFCost();
                        //set neighbour as parent to current node
                        neighbor.setPreviousConnection(current);
                    }

                }

            } else {
                break;
            }
        }

        return path;
    }

    /**
     * makePath
     * a private method that puts all the units that are need in the path in an Linkedlist.
     *
     * @param current
     */
    private void makePath(Unit current, Unit currentLocation) {
        // find the path
        //set all previous nodes in the path list
        Unit temp = current;
        addToPath(temp);

        while (temp != currentLocation && temp.getPreviousConnection() != null) {
            addToPath(temp.getPreviousConnection());
            temp = temp.getPreviousConnection();
        }
        path.removeFirst();

        resetCosts();

    }

    /**
     * heuristic
     * the method to calculate the distance between the current node and the target node in an grid.
     *
     * @param a
     * @param b
     * @return
     */
    private int heuristic(Unit a, Unit b) {

        //Manhattand distance (*10)
        int D = b.getPassingTime() * 10;

        int dx = Math.abs(a.getVisualXPos() - b.getVisualXPos());
        int dy = Math.abs(a.getVisualYPos() - b.getVisualYPos());

        return D * (dx + dy);

    }

    /**
     * getLowestIndex
     * returns the lowest F-cost in the openNodes array.
     *
     * @return
     */
    private int getLowestIndex() {
        int lowestIndex = 0;

        for (int i = 0; i < openNodes.size(); i++) {

            //loop
            if (openNodes.get(i).getFCost() < openNodes.get(lowestIndex).getFCost()) {
                lowestIndex = i;
            }
        }
        return lowestIndex;
    }

    /**
     * addNodeToOpen
     * method to add an node to the OpenNodes Array.
     *
     * @param node
     */
    private void addNodeToOpen(Unit node) {
        openNodes.push(node);
    }

    /**
     * removeNodeFromOpenList
     * removes the parameter node from the OpenNodes array.
     *
     * @param node
     */
    private void removeNodeFromOpenList(Unit node) {
        for (int i = openNodes.size() - 1; i >= 0; i--) {
            if (openNodes.get(i) == node) {
                openNodes.remove(openNodes.get(i));
            }
        }
    }

    /**
     * addNodeToClosed
     * adds parameter node tp the closedNodes array.
     *
     * @param node
     */
    private void addNodeToClosed(Unit node) {
        closedNodes.push(node);
    }

    /**
     * addToPath
     * adds the parameter node the path array.
     *
     * @param node
     */
    private void addToPath(Unit node) {
        path.push(node);
    }


    private void resetCosts() {
        for (Unit u : openNodes) {
            u.setHCost(0);
            u.setGCost(0);
        }
        for (Unit u : closedNodes) {
            u.setHCost(0);
            u.setGCost(0);
        }
    }
}
