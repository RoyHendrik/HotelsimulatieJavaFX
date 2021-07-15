package HotelSimulatie.SimLogic;

import HotelSimulatie.Constructor.*;
import HotelSimulatie.Interfaces.ICanClean;
import HotelSimulatie.Interfaces.ICleanable;
import HotelSimulatie.Interfaces.IEventReceiver;
import HotelSimulatie.Models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static HotelSimulatie.Models.Lobby.lobby;


public class Scenes {
    private final VisualBuilder visualBuilder = new VisualBuilder();
    private String layoutPath;
    private String eventPath;
    private Scene simScene;
    private MediaPlayer player;
    private boolean musicPlaying;


    //the file reader which reads json files.
    private final FileReader fileReader = FileReader.getFileReader();

    public Button createWhiteBTNWithStyle(String text) {
        Button btn = new Button(text);
        btn.setPadding(new Insets(10, 10, 10, 10));
        btn.setMinWidth(150);
        VBox.setMargin(btn, new Insets(0, 0, 5, 0));
        btn.setStyle("    -fx-background-color: \n" +
                "        #c3c4c4,\n" +
                "        linear-gradient(#d6d6d6 50%, white 100%),\n" +
                "        radial-gradient(center 50% -40%, radius 200%, #e6e6e6 45%, rgba(230,230,230,0) 50%);\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0,1,1;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 3, 0.0 , 0 , 1 );");
        return btn;
    }

    public void mainMenu(Stage window) {
        // Main Pane
        BorderPane base = visualBuilder.base();

        // Centered box
        VBox baseMenu = createBaseMenuWithStyle();

        //Buttons
        Button layoutBtn = createWhiteBTNWithStyle("layout");
        VBox.setMargin(layoutBtn, new Insets(0, 0, 5, 0));

        Button eventBtn = createWhiteBTNWithStyle("event");
        VBox.setMargin(eventBtn, new Insets(0, 0, 5, 0));

        Button startBtn = createWhiteBTNWithStyle("Start Simulation");
        VBox.setMargin(startBtn, new Insets(0, 0, 5, 0));
        startBtn.setStyle("-fx-background-color: linear-gradient(#80ff0a, #1fbe16);\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-text-fill: black;");

        // Lift is still in progress
        Button liftBtn = createWhiteBTNWithStyle("Activate Lift");
        VBox.setMargin(liftBtn, new Insets(0, 0, 5, 0));
        liftBtn.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-text-fill: white;");

        // Slider HTE speed
        Text hteText = new Text("Set HTE Speed");
        VBox.setMargin(hteText, new Insets(10, 0, 0, 0));

        Slider hteSlider = createSlider(1, 5, 1);
        hteSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            hteSlider.setValue(newVal.intValue());
            Constants.setHteSpeed((int) hteSlider.getValue());
        });

        // Slider Dining Time
        Text dinerText = new Text("Dining time");
        VBox.setMargin(dinerText, new Insets(10, 0, 0, 0));

        Slider dinerSlider = createSlider(1, 10, Constants.getDinerTime());
        dinerSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            dinerSlider.setValue(newVal.intValue());
            Constants.setDinerTime((int) dinerSlider.getValue());
        });

        //Slider RoomCleaningTime
        Text roomCleaningTimeText = new Text("Room Cleaning time");
        VBox.setMargin(roomCleaningTimeText, new Insets(10, 0, 0, 0));

        Slider roomCleaningTimeSlider = createSlider(1, 15, Constants.getRoomCleaningTime());
        roomCleaningTimeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            roomCleaningTimeSlider.setValue(newVal.intValue());
            Constants.setRoomCleaningTime((int) roomCleaningTimeSlider.getValue());
        });

        //Slider Hired Cleaners
        Text hiredCleanersText = new Text("Hired cleaners");
        VBox.setMargin(hiredCleanersText, new Insets(10, 0, 0, 0));

        Slider hiredCleanersSlider = createSlider(1, 5, Constants.getNumberofcleaners());
        hiredCleanersSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            hiredCleanersSlider.setValue(newVal.intValue());
            Constants.setNumberofcleaners((int) hiredCleanersSlider.getValue());
        });

        //Slider Max Diner Occupants
        Text maxDinerOccupantsText = new Text("Max occupants at the restaurant");
        VBox.setMargin(maxDinerOccupantsText, new Insets(10, 0, 0, 0));

        Slider maxDinerOccupantsSlider = createSlider(1, 5, Constants.getMaxDinerOccupants());
        maxDinerOccupantsSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            maxDinerOccupantsSlider.setValue(newVal.intValue());
            Constants.setMaxDinerOccupants((int) maxDinerOccupantsSlider.getValue());
        });

        //Slider Hired Cleaners
        Text dieHardText = new Text("Die Hard Time");
        VBox.setMargin(dieHardText, new Insets(10, 0, 0, 0));

        Slider dieHardSlider = createSlider(3, 10, Constants.getDieAfterWaiting());
        dieHardSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            dieHardSlider.setValue(newVal.intValue());
            Constants.setDieAfterWaiting((int) dieHardSlider.getValue());
        });

        //Slider Stairs Passing Time
        Text stairsPassingTimeText = new Text("Stairs passing Time");
        VBox.setMargin(stairsPassingTimeText, new Insets(10, 0, 0, 0));

        Slider stairsPassingTimeSlider = createSlider(1, 5, 1);
        stairsPassingTimeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            stairsPassingTimeSlider.setValue(newVal.intValue());
            Constants.setStairsPassingTime((int) stairsPassingTimeSlider.getValue());
        });

        //Slider Fitness No Time Specified Fallback
        Text fitnessTimeFallbackText = new Text("Fitness Time Fallback");
        VBox.setMargin(fitnessTimeFallbackText, new Insets(10, 0, 0, 0));

        Slider fitnessTimeFallbackSlider = createSlider(5, 30, 15);
        fitnessTimeFallbackSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            fitnessTimeFallbackSlider.setValue(newVal.intValue());
            Constants.setFitnessNoTimeFallback((int) fitnessTimeFallbackSlider.getValue());
        });

        VBox settings = new VBox(layoutBtn, eventBtn, hteText, hteSlider, dinerText, dinerSlider, hiredCleanersText, hiredCleanersSlider, roomCleaningTimeText, roomCleaningTimeSlider, dieHardText, dieHardSlider, liftBtn);
        settings.setAlignment(Pos.CENTER);
        settings.setMinSize(800, 600);

        VBox startButton = new VBox(startBtn);
        startButton.setAlignment(Pos.BOTTOM_RIGHT);
        startButton.setMinSize(200, 200);

        FilePicker layoutPicker = new FilePicker("layout", window);
        FilePicker eventPicker = new FilePicker("event", window);

        //the action for the button to obtain the layout-file location
        layoutBtn.setOnAction(event -> {
            layoutPicker.filePicker();
            layoutPath = layoutPicker.chooser();

        });

        //the action for the button to obtain the event-file location
        eventBtn.setOnAction(event -> {
            eventPicker.filePicker();
            eventPath = eventPicker.chooser();

        });

        //Start Simulation
        startBtn.setOnAction(event -> {
            if (layoutPath == null || eventPath == null) {
                return;
            }
            startSim(window);
        });

        baseMenu.getChildren().addAll(settings, startButton);
        base.setCenter(baseMenu);
        window.setTitle("Hotel Simulation");
        window.setScene(new Scene(base));
    }

    private Slider createSlider(int min, int max, int value) {
        Slider slider = new Slider(min, max, value);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMaxWidth(300);

        return slider;
    }

    private VBox createBaseMenuWithStyle() {
        VBox baseMenu = new VBox();
        baseMenu.setStyle("-fx-background-color: lightgray; " +
                "-fx-border-color: black;" +
                "-fx-border-width: 1 1 1 1;" +
                "-fx-spacing: 1;" +
                "-fx-opacity: 80");
        baseMenu.setPadding(new Insets(10));
        baseMenu.setAlignment(Pos.CENTER);
        baseMenu.setMaxWidth(350);
        baseMenu.setMaxHeight(200);
        return baseMenu;
    }

    public void startSim(Stage window) {
        if (!musicPlaying){
            bgMusic();
            musicPlaying = true;
        }
        // Generating visual Grid
        BorderPane base = visualBuilder.base();
        GridPane space = visualBuilder.simGridPane();

        //Generate all Objects
        //creates a new UnitBuilder, sends the filepath and the fileReader
        UnitBuilder unitBuilder = new UnitBuilder(layoutPath, fileReader);

        //put the units into the linked list
        LinkedList<Unit> allUnits = new LinkedList<>(unitBuilder.getArray());

        //Build
        space = visualBuilder.visualBuilder(space, allUnits);
        lobby.setAllRooms(visualBuilder.getAllRooms());

        Text hteTime = new Text("Time: 0");
        hteTime.setFont(Font.font("Arial", 20));
        BorderPane.setAlignment(hteTime, Pos.CENTER);
        BorderPane.setMargin(hteTime, new Insets(10, 0, 10, 0));

        HumanBuilder.setGridPane(space);
        HumanBuilder.employeeGenerator();

        EventBuilder eventBuilder = new EventBuilder(eventPath, fileReader);
        EventHandler eventHandler = EventHandler.getHandler();

        eventHandler.setHandler(eventBuilder.getArray(), Constants.getHteSpeed(), hteTime);
        allUnits.forEach(unit -> {
            if (unit.getClass().getSimpleName().equals("Cinema")) {
                eventHandler.addSubscriber((IEventReceiver) unit);
            }
        });
        eventHandler.startTimer();

        javafx.event.EventHandler<MouseEvent> onLobbyClick = e -> {
            statisticStage(window, eventHandler);
            eventHandler.pauseTimer();
        };
        lobby.getBgImage().addEventHandler(MouseEvent.MOUSE_CLICKED, onLobbyClick);

        base.setCenter(space);
        base.setTop(hteTime);
        base.setMinWidth(800);
        base.setMinHeight(800);
        this.simScene = new Scene(base);
        window.setScene(simScene);
        window.show();
    }

    private void statisticStage(Stage window, EventHandler eventHandler) {
        ArrayList<ICanClean> cleaners = lobby.getCleaners();
        HashMap<Integer, Human> visitors = lobby.getAllVisitors();

        BorderPane base = visualBuilder.base();

        VBox statsBase = new VBox();

        VBox humanStats = new VBox();
        humanStats.setAlignment(Pos.TOP_LEFT);

        VBox footerStats = new VBox();

        footerStats.setAlignment(Pos.CENTER_RIGHT);

        Button unPause = new Button("Go Back");
        VBox.setMargin(unPause, new Insets(0, 10, 10, 0));

        javafx.event.EventHandler<MouseEvent> unPauseSim = e -> {
            eventHandler.resumeTimer();
            window.setScene(simScene);
            window.show();
        };

        unPause.addEventHandler(MouseEvent.MOUSE_CLICKED, unPauseSim);
        unPause.setPadding(new Insets(10, 10, 10, 10));
        unPause.setMinWidth(150);
        unPause.setStyle("-fx-background-color: linear-gradient(#1dabff, #385fbe);\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-text-fill: white;");

        footerStats.getChildren().addAll(unPause);

        TilePane visitorTilePane = new TilePane();
        visitorTilePane.setPrefColumns(4);
        visitorTilePane.setPrefRows((int) Math.ceil((double) visitors.size() / 4));
        visitorTilePane.setHgap(2.0);
        visitorTilePane.setVgap(2.0);
        for (ICanClean cleaner : cleaners) {
            VBox cleanerVbox = new VBox();
            cleanerVbox.setStyle("-fx-border-color: #000;\n" + "-fx-background-color: #ddd;\n");
            Cleaner currentCleaner = (Cleaner) cleaner;
            VBox.setMargin(cleanerVbox, new Insets(2));

            Text cleanerName = new Text("Cleaner" + (cleaners.indexOf(cleaner) + 1));
            cleanerName.setFont(Font.font("Arial", 16));
            VBox.setMargin(cleanerName, new Insets(5));

            Text cleanerLocation = new Text(lobby.getVisualYPos() - currentCleaner.getVisualYPos() + ", " + currentCleaner.getVisualXPos());
            cleanerLocation.setFont(Font.font("Arial", 16));
            VBox.setMargin(cleanerLocation, new Insets(5));

            VBox tasks = new VBox();
            if (currentCleaner.getTaskLoad() > 0) {
                for (ICleanable task : currentCleaner.getTasks()) {
                    Text taskText = new Text(((Unit) task).getClass().getSimpleName() + ": " + (lobby.getVisualYPos() - ((Unit) task).getVisualYPos()) + ", " + ((Unit) task).getVisualXPos());
                    taskText.setFont(Font.font("Arial", 16));
                    VBox.setMargin(tasks, new Insets(5));
                    tasks.getChildren().add(taskText);
                }
            } else {
                Text noTaskText = new Text("Nothing to do");
                noTaskText.setFont(Font.font("Arial", 16));
                VBox.setMargin(noTaskText, new Insets(5));
                tasks.getChildren().add(noTaskText);
            }

            HBox imageContainer = new HBox();
            Image cleanerImg = ((Cleaner) cleaner).getBgImage().getImage();
            ImageView cleanerImage = new ImageView(cleanerImg);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.getChildren().add(cleanerImage);

            Text eventText = new Text("Event: " + (currentCleaner.getCurrentEventType() == null ? "No Event" : currentCleaner.getCurrentEventType().toString()));
            eventText.setFont(Font.font("Arial", 14));
            VBox.setMargin(eventText, new Insets(5));

            cleanerVbox.getChildren().addAll(imageContainer, cleanerName, eventText, cleanerLocation, tasks);
            visitorTilePane.getChildren().add(cleanerVbox);
        }

        visitors.forEach((integer, human) -> {
            VBox visitorVbox = new VBox();
            visitorVbox.setStyle("-fx-border-color: #000;\n" + "-fx-background-color: #ddd");
            Visitor visitor = (Visitor) visitors.get(integer);

            Text visitorIdText = new Text("Human" + visitor.getId());
            visitorIdText.setFont(Font.font("Arial", 16));
            VBox.setMargin(visitorIdText, new Insets(5));

            HBox imageContainer = new HBox();
            Image visitorImg = visitor.getBgImage().getImage();
            ImageView visitorImage = new ImageView(visitorImg);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.getChildren().add(visitorImage);

            Text visitorLocationText = new Text(lobby.getVisualYPos() - visitor.getVisualYPos() + ", " + visitor.getVisualXPos() + ", to wait: " + visitor.getHteToWait());
            visitorLocationText.setFont(Font.font("Arial", 12));
            VBox.setMargin(visitorLocationText, new Insets(5));

            Text visitorEventText = new Text(visitor.getCurrentEventType().toString());
            visitorEventText.setFont(Font.font("Arial", 12));
            VBox.setMargin(visitorEventText, new Insets(5));

            Text myRoomText = new Text("Room: " + (lobby.getVisualYPos() - visitor.getMyRoom().getVisualYPos()) + ", " + visitor.getMyRoom().getVisualXPos());
            myRoomText.setFont(Font.font("Arial", 12));
            VBox.setMargin(myRoomText, new Insets(5));

            if (visitor.isDead()) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.8);
                visitorImage.setEffect(colorAdjust);
                visitorEventText.setText("DEAD");
            }
            visitorVbox.getChildren().addAll(imageContainer, visitorIdText, myRoomText, visitorLocationText, visitorEventText);

            visitorTilePane.getChildren().add(visitorVbox);
        });

        humanStats.getChildren().add(visitorTilePane);
        statsBase.getChildren().addAll(humanStats);

        base.setBottom(footerStats);
        base.setCenter(statsBase);
        base.setMinWidth(800);
        base.setMinHeight(800);

        window.setScene(new Scene(base));
        window.show();
    }

    public void bgMusic() {
        try {
            String uriString = new File(System.getProperty("user.dir") + "/src/HotelSimulatie/BGMusic.mp3").toURI().toString();
            player = new MediaPlayer(new Media(uriString));
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.1);
            player.play();
        }catch (Exception e){
            System.out.println("Audio file not found!");
        }
    }
}
