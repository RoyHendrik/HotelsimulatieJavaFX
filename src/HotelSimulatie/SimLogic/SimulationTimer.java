package HotelSimulatie.SimLogic;

import HotelSimulatie.Interfaces.IHTEUpdate;
import HotelSimulatie.Models.Event;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SimulationTimer {

    protected List<Event> hteEvents = new ArrayList<>();
    protected List<IHTEUpdate> observers = new ArrayList<>();
    private final int interval;
    private int loopNumber = -1;
    private Timeline timeLine;

    public SimulationTimer(int interval) {
        this.interval = interval;
    }

    public void pauseTimer() {
        timeLine.pause();
    }

    public void resumeTimer() {
        timeLine.play();
    }

    public void startTimer() {

        timeLine = new Timeline(new KeyFrame(Duration.seconds(this.interval), (ActionEvent ae) -> {
            loopNumber++;
            hteEvents = new ArrayList<>();

            if (observers.isEmpty()) {
                return;
            }

            for (int i = 0; i < observers.size(); i++) {
                observers.get(i).update();
            }
        }));

        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
    }

    public void subscribe(IHTEUpdate subscriber) {
        observers.add(subscriber);
    }

    public int getLoopNumber() {
        return loopNumber;
    }
}
