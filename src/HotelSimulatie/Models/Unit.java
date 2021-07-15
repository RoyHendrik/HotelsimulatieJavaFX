package HotelSimulatie.Models;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Unit {

    protected int passingTime = 1;
    protected ImageView bgImage;
    private final ArrayList<Unit> connections = new ArrayList<>();
    private int width;
    private int height;
    private int visualXPos;
    private int visualYPos;
    private int visualYCorrectionMultiFloors = -1;
    private int gCost = 0;
    private int hCost = 0;
    private Unit previousConnection = null;

    public Unit(int width, int height, int visualYPos, int visualXPos) {
        this.width = width;
        this.height = height;
        this.visualYPos = visualYPos;
        this.visualXPos = visualXPos;
    }

    public int getFCost() {
        return gCost + hCost;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int g) {
        this.gCost = g;
    }

    public void setHCost(int h) {
        this.hCost = h;
    }

    public Unit getPreviousConnection() {
        return previousConnection;
    }

    public void setPreviousConnection(Unit previousConnection) {
        this.previousConnection = previousConnection;
    }

    public int getPassingTime() {
        return passingTime;
    }

    public ImageView getBgImage() {
        return bgImage;
    }

    public ArrayList<Unit> getConnections() {
        return connections;
    }

    public void addConnections(Unit connection) {
        this.connections.add(connection);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getVisualXPos() {
        return visualXPos;
    }

    public void setVisualXPos(int visualXPos) {
        this.visualXPos = visualXPos;
    }

    public int getVisualYCorrectionMultiFloors() {
        return visualYCorrectionMultiFloors;
    }

    public int getVisualYPos() {
        return visualYPos;
    }

    public void setVisualYPos(int visualYPos) {
        this.visualYPos = visualYPos;
        if (this.height > 1) {
            this.visualYCorrectionMultiFloors = visualYPos - this.height + 1;
        }
    }
}
