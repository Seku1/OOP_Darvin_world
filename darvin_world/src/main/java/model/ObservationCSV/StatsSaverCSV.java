package model.ObservationCSV;

import model.Maps.AbstractWorldMap;
import model.Maps.WorldMap;
import model.Others.MapChangeListener;
import model.Simulations.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;

public class StatsSaverCSV implements MapChangeListener {
    private final String fileName;
    private final File simulationCSV;
    private final AbstractWorldMap map;
    private final Simulation simulation;
    public StatsSaverCSV(String fileName, AbstractWorldMap map, Simulation simulation) throws IOException{
        this.map = map;
        this.fileName = fileName;
        this.simulation = simulation;
        this.simulationCSV = new File(makeFullFileName(fileName));
        try (FileWriter writer = new FileWriter(simulationCSV, true)) {
            writer.append("Day;Animal count;Grass Count;Most common genes;Average energy;Average lifespan;Average children count\n");
        }
    }

    private void appendToCSV(String message) throws IOException {

        try (FileWriter writer = new FileWriter(simulationCSV, true)) {
            writer.append(makeLine(message));
        } throw new FileAlreadyExistsException(fileName + " already exists.");
    }

    private String makeFullFileName(String fileName){
        return System.getProperty("user.dir") +
                File.separator + "stats" +
                File.separator + fileName + ".csv";
    }

    private String makeLine(String message){
        return "%s;%d;%d;%s;%.2f;%.2f;%.2f\n".formatted(
                message.split(" ")[1],
                map.getAnimals().size(),
                map.getPlants().size(),
                map.findMostPopularGenomes()
                        .keySet().stream()
                        .findFirst()
                        .map(Arrays::toString),
                map.getAverageEnergyLevel(),
                map.getAverageLifeSpanOfDeadAnimals(),
                map.getAverageNumberOfChildren());
    }


    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        try {
            appendToCSV("Day: " + simulation.getDayNumber());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
