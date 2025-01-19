package model.Statistics;

import javafx.scene.paint.Color;
import model.MapElements.Animal.Animal;

import java.util.Arrays;


public class GameStatistics {
    private static final int yearDays = 12;

    public Color getAnimalColor(Animal animal) {
        int energy = Math.min(100 ,animal.getEnergyLevel());

        return new Color( 100 - energy, energy, 0, 0.6);
    }


}
