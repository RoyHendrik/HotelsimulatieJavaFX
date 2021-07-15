package HotelSimulatie.Constructor;

import HotelSimulatie.Models.Lobby;
import HotelSimulatie.Models.Room;
import HotelSimulatie.Models.Unit;
import HotelSimulatie.SimLogic.Constants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.LinkedList;

public class VisualBuilder {
    private ArrayList<Room> allRooms;

    public ArrayList<Room> getAllRooms() {
        return allRooms;
    }

    /**
     * @return BorderPane with recurring settings
     */
    public BorderPane base() {
        BorderPane base = new BorderPane();
        base.setStyle("-fx-background-color: lightblue;");
        return base;
    }

    public GridPane simGridPane() {
        GridPane space = new GridPane();
        space.setGridLinesVisible(true);
        space.setAlignment(Pos.BOTTOM_CENTER);
        space.setPadding(new Insets(10));
        space.setStyle("-fx-background-image:url(/HotelSimulatie/Images/bg.png);" +
                "-fx-background-color: #5a9091; " +
                "-fx-grid-lines-visible: true;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1 1 1 1;" +
                "-fx-spacing: 1;" +
                "-fx-background-repeat: repeat-x");
        return space;
    }

    public GridPane visualBuilder(GridPane space, LinkedList<Unit> allUnits) {

        HotelBuilder builder = new HotelBuilder(allUnits);
        Unit[][] building = codeGridToVisualGrid(builder.getCodeGrid());

        this.allRooms = builder.getAllRooms();

        //Grid sizing
        ColumnConstraints columnConstraint = new ColumnConstraints();
        RowConstraints rowConstraint = new RowConstraints();
        columnConstraint.setMinWidth(Constants.getUNITPIXELWIDTH());
        rowConstraint.setMinHeight(Constants.getUNITPIXELHEIGHT());
        for (int i = 0; i < builder.getMaxSpanHeight(); i++) {
            space.getRowConstraints().add(rowConstraint);
        }
        for (int j = 0; j < builder.getMaxSpanWidth(); j++) {
            space.getColumnConstraints().add(columnConstraint);
        }

        //Visual draw
        try {
            for (Unit[] units : building) {
                for (Unit unit : units) {
                    if (unit.getVisualYCorrectionMultiFloors() == -1) {
                        space.add(unit.getBgImage(), unit.getVisualXPos(), unit.getVisualYPos(), unit.getWidth(), unit.getHeight());
                    } else {
                        space.add(unit.getBgImage(), unit.getVisualXPos(), unit.getVisualYCorrectionMultiFloors(), unit.getWidth(), unit.getHeight());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return space;
    }

    public Unit[][] codeGridToVisualGrid(Unit[][] building) {
        for (Unit[] units : building) {
            for (Unit unit : units) {
                unit.setVisualYPos(building.length - unit.getVisualYPos() - 1);
            }
        }
        return building;
    }
}
