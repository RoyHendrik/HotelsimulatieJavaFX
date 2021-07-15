package HotelSimulatie;


import HotelSimulatie.SimLogic.Scenes;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage window = primaryStage;
        Scenes scenes = new Scenes();
        scenes.mainMenu(window);
        primaryStage.show();
    }
}
