package HotelSimulatie.Constructor;

import HotelSimulatie.Models.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.layout.GridPane;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import static org.junit.Assert.*;

public class VisualBuilderVisualYXCheck {
    private JFXPanel jfxPanel = new JFXPanel();
    private Cinema cinema1;
    private Cinema cinema2;

    @Before
    public void setUp(){

        GridPane space = new GridPane();
        VisualBuilder visualBuilder = new VisualBuilder();
        space = visualBuilder.visualBuilder(space,allUnitBuilder());
    }

    @Test
    public void visualYXCheck(){
        assertEquals(12,cinema1.getVisualYPos());
        assertEquals(7,cinema1.getVisualXPos());
        assertEquals(7,cinema2.getVisualYPos());
        assertEquals(7,cinema2.getVisualXPos());
    }

    private LinkedList<Unit> allUnitBuilder(){
        LinkedList<Unit> list = new LinkedList<>();
        for (int i = 0; i < 16; i++) {

            list.add(new Room(2,1,i, 0,3));
            list.add(new Room(2,1,i, 2,4));

            if (i==1){
                list.add(new Room(2,1,i, 8, 5));
            }

            list.add(new Fitness(2,1,i, 4));

            if (i==3){
                cinema1 = new Cinema(2,2,i, 6);
                list.add(cinema1);

            } else if (i == 8){

                cinema2 = new Cinema(2,2,i, 6);
                list.add(cinema2);

            } else if (i == 9 || i==4){

            } else {
                list.add(new Diner(2, 1, i, 6, 2));
            }
        }
        return list;
    }
}
