package HotelSimulatie.Constructor;

import HotelSimulatie.Models.Unit;
import javafx.embed.swing.JFXPanel;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UnitBuildingCheck {

    String unitFilePath = "LIBS/testFile_Unit.json";
    FileReader fileReader = FileReader.getFileReader();
    ArrayList<Unit> testUnits;
    private final JFXPanel panel = new JFXPanel();

    @Test
    public void testIfAUnitsHaveBeenMade() {
        UnitBuilder unitBuilder = new UnitBuilder(unitFilePath, fileReader);
        testUnits = unitBuilder.getArray();

        int numberOfJsonObjects = 5;

        assertEquals(numberOfJsonObjects, testUnits.size());

    }

    @Test
    public void testIfUnitFilterWorks() {
        UnitBuilder unitBuilder = new UnitBuilder(unitFilePath, fileReader);
        testUnits = unitBuilder.getArray();

        int numberOfJsonObjects = 6;

        assertNotEquals(numberOfJsonObjects, testUnits.size());
    }

}