package frontend.prezenter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import model.MapElements.Animal.Animal;
import model.MapElements.Plant.PlantCreator;
import model.MapElements.Plant.PlantCreatorEquator;
import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;
import model.Simulations.Simulation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SimulationPresenter {
    @FXML
    public Button pausePlayButton;
    @FXML
    public Label dayLabel, animalCountLabel, plantCountLabel, averageLifespanLabel, mostFrequentGenome, emptyCountLabel,
            averageEnergyLevel, averageChildCount, animalGenomeLabel, animalActiveGenomeLabel, howMuchEatenLabel,
            animalChildCountLabel, animalAgeLabel, livesLabel;
    public Button additionalButton;
    public Button drawEquatorButton;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private NumberAxis xAxis, yAxis;
    @FXML
    private Canvas mapCanvas;

    private Animal followedAnimal;
    private AbstractWorldMap map;
    private Simulation simulation;
    private Thread simulationThread;
    private XYChart.Series<Number, Number> animalSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> plantSeries = new XYChart.Series<>();

    private static final int MAX_DAYS_DISPLAYED = 10;
    private PlantCreatorEquator plantCreator;

    @FXML
    public void initialize() {
        animalSeries.getData().clear();
        plantSeries.getData().clear();
        animalSeries.setName("Liczba zwierząt");
        plantSeries.setName("Liczba roślin");
        lineChart.getData().addAll(animalSeries, plantSeries);
        lineChart.setAnimated(false);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(MAX_DAYS_DISPLAYED);
        yAxis.setAutoRanging(true);
    }

    public void setSimulation(Simulation simulation, AbstractWorldMap map, PlantCreatorEquator plantCreator) {
        this.simulation = simulation;
        this.plantCreator = plantCreator;
        this.map = map;
        this.map.addObserver((worldMap, message) -> updateView());
        startSimulation();
    }

    private void startSimulation() {
        if (simulation != null) {
            simulationThread = new Thread(simulation);
            simulationThread.setDaemon(true);
            simulationThread.start();
        }
    }

    private void updateView() {
        Platform.runLater(() -> {
            drawMap();

            int dayNumber = simulation.getDayNumber();
            int animalCount = map.getAnimals().size();
            int plantCount = map.getPlantMap().size();

            animalSeries.getData().add(new XYChart.Data<>(dayNumber, animalCount));
            plantSeries.getData().add(new XYChart.Data<>(dayNumber, plantCount));

            if (dayNumber > MAX_DAYS_DISPLAYED) {

                xAxis.setLowerBound(dayNumber + 1 - MAX_DAYS_DISPLAYED);
                xAxis.setUpperBound(dayNumber);

                rescaleYAxis();
            }
            updateMapStats();
            if (followedAnimal != null && !followedAnimal.isDead()) {
                updateAnimalStats(followedAnimal);
            }
        });
    }

    private void updateAnimalStats(Animal animal) {
        synchronized (map){
        animalGenomeLabel.setText("Genom: " + formatGenes(animal.getGenes()));
        animalActiveGenomeLabel.setText("Aktywny gen: " + animal.getActiveGenom());
        howMuchEatenLabel.setText("Liczba zjedzonych roślin: " + animal.getEatenPlants());
        averageEnergyLevel.setText("Ilość dzieci: " + animal.getChildren());
        animalAgeLabel.setText("Wiek zwierzaka: " + animal.getLiveDays());
        livesLabel.setText("Status życia: " + (!animal.isDead()? "Martwy" : "Żywy"));
        }
    }

    private String formatGenes(int[] genes) {
        return Arrays.stream(genes)
                .limit(5)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(", ", "[", genes.length > 5 ? " ...]" : "]"));
    }

    private void updateMapStats(){
        dayLabel.setText("Dzień: " + simulation.getDayNumber());
        animalCountLabel.setText("Liczba zwierząt : " + map.getAnimals().size());
        plantCountLabel.setText("Liczba roślin : " + map.getPlants().size());
        emptyCountLabel.setText("Liczba wolnych pól to: " + Math.max(map.getWidth() * map.getHight() - map.getOccupiedPositions().size(), 0));
        updateMostPopularGenomeStats();
        averageLifespanLabel.setText("Średnia długość życia: " + map.getAverageLifeSpanOfDeadAnimals());
        averageEnergyLevel.setText("Średnii poziom energi zwierzaków: " + map.getAverageEnergyLevel());
        averageChildCount.setText("Średnia ilość dzieci to: " + simulation.getAverageChildCount());
    }

    private void updateMostPopularGenomeStats() {
        Map<int[], Integer> mostPopularGenomes = map.findMostPopularGenomes();
        if (!mostPopularGenomes.isEmpty()) {
            int[] popularGenome = mostPopularGenomes.keySet().iterator().next();  // Sprawdź, czy mapa nie jest pusta
            int count = mostPopularGenomes.get(popularGenome);
            mostFrequentGenome.setText("Najczęściej występujące geny: " + Arrays.toString(popularGenome) +
                    " (liczba zwierząt: " + count + ")");
        } else {
            mostFrequentGenome.setText("Najczęściej występujące geny: brak danych");
        }
    }


    private void rescaleYAxis() {
        List<XYChart.Data<Number, Number>> animalData = animalSeries.getData();
        List<XYChart.Data<Number, Number>> plantData = plantSeries.getData();

        int maxAnimals = animalData.stream()
                .mapToInt(data -> data.getYValue().intValue())
                .max()
                .orElse(0);

        int maxPlants = plantData.stream()
                .mapToInt(data -> data.getYValue().intValue())
                .max()
                .orElse(0);

        int maxY = Math.max(maxAnimals, maxPlants);
        int minY = 0;

        double margin = (maxY - minY) * 0.1;
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(Math.max(0, minY - margin));
        yAxis.setUpperBound(maxY + margin);
    }





    private void drawMap() {
        if (map == null) return;
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        double cellWidth = mapCanvas.getWidth() / (map.getWidth() + 1);
        double cellHeight = mapCanvas.getHeight() / (map.getHight() + 1);
        synchronized (map) {
            gc.setFill(Color.GREEN);
            map.getPlantMap().forEach((position, plant) -> {
                double x = position.getX() * cellWidth;
                double y = position.getY() * cellHeight;
                gc.fillRect(x, y, cellWidth, cellHeight);
            });

            map.getAnimalPositions().forEach(position -> {
                Animal animal = map.getAnimalsAtPosition(position).getFirst();
                gc.setFill(getColor(animal));
                double x = position.getX() * cellWidth;
                double y = position.getY() * cellHeight;
                gc.fillOval(x, y, cellWidth, cellHeight);
            });
            mapCanvas.setOnMouseClicked(event -> {
                double mouseX = event.getX();
                double mouseY = event.getY();
                int clickedX = (int) (mouseX / cellWidth);
                int clickedY = (int) (mouseY / cellHeight);
                Vector2d clickedPosition = new Vector2d(clickedX, clickedY);
                List<Animal> animalsAtPosition = map.getAnimalsAtPosition(clickedPosition);
                if (!animalsAtPosition.isEmpty()) {
                    Animal animal = animalsAtPosition.get(0);
                    followedAnimal = animal;
                    updateAnimalStats(animal);
                } else {
                    System.out.println("No animal found at this position.");
                }
            });

        }
    }

    private Color getColor(Animal animal) {
        int energyLevel = animal.getEnergyLevel();
        int saturated = 300;

        double green = Math.min(1.0, (double) energyLevel / saturated);
        green = Math.max(0, green);
        double red = 1 - green;
        if (green > 0.1){ green -= 0.1;}

        return new Color(red, green, 0.0, 1.0);
    }

    @FXML
    private void handlePausePlayAction() {
        if (simulation != null) {
            if (simulation.getRunning()) {
                simulation.pause();
                pausePlayButton.setText("Wznów");
                additionalButton.setVisible(true);
                drawEquatorButton.setVisible(true);
                drawMap();
            } else {
                simulation.play();
                pausePlayButton.setText("Pauza");
                additionalButton.setVisible(false);
                drawEquatorButton.setVisible(false);
            }
        }
    }


    public void handleAdditionalButtonAction() {
        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        double cellWidth = mapCanvas.getWidth() / (map.getWidth() + 1);
        double cellHeight = mapCanvas.getHeight() / (map.getHight() + 1);

        Map<int[], Integer> mostFrequentGenomes = map.findMostPopularGenomes();

        if (mostFrequentGenomes != null && !mostFrequentGenomes.isEmpty()) {
            map.getAnimalPositions().forEach(position -> {
                List<Animal> animalsAtPosition = map.getAnimalsAtPosition(position);

                animalsAtPosition.forEach(animal -> {
                    int[] animalGenome = animal.getGenes();
                    mostFrequentGenomes.forEach((genome, count) -> {
                        if (Arrays.equals(animalGenome, genome)) {
                            double x = position.getX() * cellWidth;
                            double y = position.getY() * cellHeight;


                            gc.setFill(Color.MAGENTA);
                            gc.fillRect(x, y, cellWidth, cellHeight);

                            gc.setFill(getColor(animal));
                            gc.fillOval(x, y, cellWidth, cellHeight);
                        }
                    });
                });
            });
        } else {
            System.out.println("Brak zwierząt z najczęściej występującymi genami.");
        }
    }

    @FXML
    public void handleDrawEquatorAction() {
        PlantCreatorEquator plantCreator = new PlantCreatorEquator(map);
        int equatorStart = plantCreator.getEquatorStart();
        int equatorEnd = plantCreator.getEquatorEnd();

        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        double cellWidth = mapCanvas.getWidth() / (map.getWidth() + 1);
        double cellHeight = mapCanvas.getHeight() / (map.getHight() + 1);

        gc.setFill(Color.YELLOW);

        for (int y = equatorStart; y <= equatorEnd; y++) {
            for (int x = map.getLowerLeft().getX(); x <= map.getUpperRight().getX(); x++) {
                double xPos = x * cellWidth;
                double yPos = y * cellHeight;
                gc.fillRect(xPos, yPos, cellWidth, cellHeight);
            }
        }
    }
}