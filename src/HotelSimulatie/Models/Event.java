package HotelSimulatie.Models;

import HotelSimulatie.Enums.EVENTTYPES;

public class Event {
    private final EVENTTYPES type;
    private final int time;
    private int guestId;
    private int stars;
    private int duration;

    public Event(EVENTTYPES type, int time) {
        this.type = type;
        this.time = time;
    }

    public Event(EVENTTYPES type, int time, int info) {
        this.type = type;
        this.time = time;
        if (type.equals(EVENTTYPES.START_CINEMA)) {
            this.duration = info;
        } else {
            this.guestId = info;
        }
    }

    public Event(EVENTTYPES type, int time, int guestId, int info) {
        this.type = type;
        this.time = time;
        this.guestId = guestId;
        if (type.equals(EVENTTYPES.CHECK_IN)) {
            this.stars = info;
        } else {
            this.duration = info;
        }
    }

    public EVENTTYPES getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getDuration() {
        return duration;
    }
}
