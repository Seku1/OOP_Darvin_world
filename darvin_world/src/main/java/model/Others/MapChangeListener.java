package model.Others;
import model.Maps.WorldMap;

public interface MapChangeListener {
    void mapChanged(WorldMap worldMap, String message);
}
