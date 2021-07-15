package HotelSimulatie.Constructor;

import HotelSimulatie.Models.*;
import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class HotelBuilderCorrectCheck {
    JFXPanel jfxPanel = new JFXPanel();
    private LinkedList<Unit> list;
    private HotelBuilder hotelBuilder;
    @Before
    public void setUp() throws Exception {

        list = new LinkedList<>();
        for (int i = 0; i < 16; i++) {

            list.add(new Room(2,1,i, 0,3));
            list.add(new Room(2,1,i, 2,4));

            if (i==1){
                list.add(new Room(2,1,i, 8, 5));
            }

            list.add(new Fitness(2,1,i, 4));

            if (i == 8 || i==3){

                list.add(new Cinema(2,2,i, 6));

            } else if (i == 9 || i==4){

            } else {
                list.add(new Diner(2, 1, i, 6, 2));
            }
        }

        hotelBuilder = new HotelBuilder(list);
    }

    @Test
    public void checkCorrectMaxSpanWidth() {
        assertEquals(12,hotelBuilder.getMaxSpanWidth());
    }

    @Test
    public void checkCorrectMaxSpanHeight() {
        assertEquals(17,hotelBuilder.getMaxSpanHeight());
    }

    @Test
    public void checkCorrectPossitionCinema() {
        Unit[][] grid = hotelBuilder.getCodeGrid();

        assertEquals(Cinema.class,grid[9][7].getClass());
        assertEquals(Cinema.class,grid[4][7].getClass());

    }

    @Test
    public void checkCorrectRoomsCount(){
        assertEquals(33,hotelBuilder.getAllRooms().size());
    }

}