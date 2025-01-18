package model.Others;
import model.Maps.WorldMap;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0;



    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (System.out) {
            System.out.println("map id: " + worldMap.getID() + " updateCount: " + (++updateCount) + " message: " + message);
            System.out.println(worldMap);
        }
    }
}
