package model.Observers;

import model.Maps.AbstractWorldMap;
import java.io.IOException;

public interface MapChangeObserver {
    void mapChanged(AbstractWorldMap map, String message) throws IOException;
}
