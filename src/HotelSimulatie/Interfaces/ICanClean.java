package HotelSimulatie.Interfaces;

public interface ICanClean {

    int getTaskLoad();

    void addTask(ICleanable cleanable, boolean priority);

    boolean containsTaskAlready(ICleanable iCleanable);

}
