package HotelSimulatie.SimLogic;

public class Constants {
    private static final int UNITPIXELWIDTH = 63;
    private static final int UNITPIXELHEIGHT = 63;
    private static final int maxRoomStars = 5;
    private static int numberofcleaners = 2;
    private static int roomCleaningTime = 5;
    private static int fitnessNoTimeFallback = 15;
    private static int dinerTime = 7;
    private static int maxDinerOccupants = 3;
    private static boolean liftActive = false;
    private static int dieAfterWaiting = 2;
    private static int hteSpeed = 1;
    private static int stairsPassingTime = 2;

    public static int getUNITPIXELWIDTH() {
        return UNITPIXELWIDTH;
    }

    public static int getUNITPIXELHEIGHT() {
        return UNITPIXELHEIGHT;
    }

    public static boolean isLiftActive() {
        return liftActive;
    }

    public static void setLiftActive(boolean liftActive) {
        Constants.liftActive = liftActive;
    }

    public static int getNumberofcleaners() {
        return numberofcleaners;
    }

    public static void setNumberofcleaners(int numberofcleaners) {
        Constants.numberofcleaners = numberofcleaners;
    }

    public static int getMaxRoomStars() {
        return maxRoomStars;
    }

    public static int getRoomCleaningTime() {
        return roomCleaningTime;
    }

    public static void setRoomCleaningTime(int roomCleaningTime) {
        Constants.roomCleaningTime = roomCleaningTime;
    }

    public static int getFitnessNoTimeFallback() {
        return fitnessNoTimeFallback;
    }

    public static void setFitnessNoTimeFallback(int fitnessNoTimeFallback) {
        Constants.fitnessNoTimeFallback = fitnessNoTimeFallback;
    }

    public static int getDinerTime() {
        return dinerTime;
    }

    public static void setDinerTime(int dinerTime) {
        Constants.dinerTime = dinerTime;
    }

    public static int getMaxDinerOccupants() {
        return maxDinerOccupants;
    }

    public static void setMaxDinerOccupants(int maxDinerOccupants) {
        Constants.maxDinerOccupants = maxDinerOccupants;
    }

    public static int getDieAfterWaiting() {
        return dieAfterWaiting;
    }

    public static void setDieAfterWaiting(int dieAfterWaiting) {
        Constants.dieAfterWaiting = dieAfterWaiting;
    }

    public static int getHteSpeed() {
        return hteSpeed;
    }

    public static void setHteSpeed(int hteSpeed) {
        Constants.hteSpeed = hteSpeed;
    }

    public static int getStairsPassingTime() {
        return stairsPassingTime;
    }

    public static void setStairsPassingTime(int stairsPassingTime) {
        Constants.stairsPassingTime = stairsPassingTime;
    }
}
