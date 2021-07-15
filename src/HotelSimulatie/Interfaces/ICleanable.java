package HotelSimulatie.Interfaces;

import HotelSimulatie.Enums.STATUS;

public interface ICleanable {
    void setCleaningNotifyUnit(ICleaningService cleaningNotifyUnit);

    STATUS getStatus();

    void setStatus(STATUS status);
}
