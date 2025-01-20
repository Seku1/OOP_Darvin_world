package model.ObservationCSV;

import java.io.IOException;

public interface NewDayObserver {
    void newDay(int day) throws IOException;
}
