package HotelSimulatie.Interfaces;

public interface ICleaningService {
    void needsCleaning(ICleanable unitToBeCleaned, boolean priority);
}
