package HotelSimulatie.Constructor;

import HotelSimulatie.Main;
import HotelSimulatie.SimLogic.Scenes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class FilePickerTest {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final JFXPanel panel = new JFXPanel();
    private volatile boolean success = false;

    /**
     * Test that a JavaFX application launches.
     */
    @Test
    public void checkIfApplicationStarts() {
        Thread thread = new Thread(() -> {
            try {
                Application.launch(Main.class);
                success = true;
            } catch(Throwable t) {
                if(t.getCause() != null && t.getCause().getClass().equals(InterruptedException.class)) {
                    success = true;
                    return;
                }

                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, t);
            }
        });
        thread.setDaemon(true);
        thread.start();
        try {
            Thread.sleep(3000);  // Wait for 3 seconds before interrupting JavaFX application
        } catch(InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        thread.interrupt();
        try {
            thread.join(1); // Wait 1 second for our wrapper thread to finish.
        } catch(InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        assertTrue(success);
    }

    @Test
    public void filePickerTest() throws InterruptedException {
        AtomicReference<String> eventPath = new AtomicReference<>("");
        Thread thread = new Thread(() -> {
            new JFXPanel();
            Platform.runLater(new Runnable() {
                Main m;
                @Override
                public void run() {
                    //new Main();
                    Stage stage = new Stage();
                    VBox pane = new VBox();
                    Text text = new Text("Choose a file to complete this test!");
                    text.setFont(Font.font("Arial", 20));
                    Button filePicker = new Button("FilePicker");
                    pane.getChildren().addAll(text, filePicker);
                    stage.setScene(new Scene(pane));
                    stage.show();
                    FilePicker eventPicker = new FilePicker("event", stage);

                    filePicker.setOnAction(event -> {
                        eventPicker.filePicker();
                        running.set(false);
                        eventPath.set(eventPicker.chooser());
                        stage.close();
                    });
                }
            });
        });
        thread.start();
        running.set(true);
        while (running.get()) {
            Thread.sleep(100);
        }
        assertNotEquals("", eventPath);
    }
}