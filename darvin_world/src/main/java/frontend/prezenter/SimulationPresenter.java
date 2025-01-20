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
import model.Maps.AbstractWorldMap;
import model.Others.Vector2d;
import model.Simulations.Simulation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SimulationPresenter {
    @FXML
    public Button pausePlayButton;
    @FXML
    public Label dayLabel, animalCountLabel, plantCountLabel, averageLifespanLabel, mostFrequentGenome, emptyCountLabel,
            averageEnergyLevel, averageChildCount, animalGenomeLabel, animalActiveGenomeLabel, howMuchEatenLabel,
            animalChildCountLabel, animalAgeLabel, livesLabel, animalDescendantCountLabel;
    public Button additionalButton;
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

    @FXML
    public void initialize() {
        animalSeries.setName("Liczba zwierząt");
        plantSeries.setName("Liczba roślin");
        lineChart.getData().addAll(animalSeries, plantSeries);
        lineChart.setAnimated(false);

        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(MAX_DAYS_DISPLAYED);
        yAxis.setAutoRanging(true);
    }

    public void setSimulation(Simulation simulation, AbstractWorldMap map) {
        this.simulation = simulation;
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
                animalSeries.getData().removeFirst();
                plantSeries.getData().removeFirst();

                xAxis.setLowerBound(dayNumber + 1 - MAX_DAYS_DISPLAYED);
                xAxis.setUpperBound(dayNumber);
                rescaleYAxis();
            }
            updateMapStats();
            if (followedAnimal != null) {
                updateAnimalStats(followedAnimal);
            }
        });
    }

    private void updateAnimalStats(Animal animal) {
        animalGenomeLabel.setText("Genom: " + formatGenes(animal.getGenes()));
        animalActiveGenomeLabel.setText("Aktywny gen: " + animal.getActiveGenom());
        howMuchEatenLabel.setText("Liczba zjedzonych roślin: " + animal.getEatenPlants());
        averageEnergyLevel.setText("Ilość dzieci: " + animal.getChildren());
        animalDescendantCountLabel.setText("Ilość potomstwa" + animal.getDescendant());
        animalAgeLabel.setText("Wiek zwierzaka: " + animal.getLiveDays());
        livesLabel.setText("Status życia: " + (animal.isDead()? "Martwy": "Żywy"));
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
        plantCountLabel.setText("Liczba rolin : " + map.getPlants().size());
        emptyCountLabel.setText("Liczba wolnych pól to: " + Math.max(map.getWidth() * map.getHight() - map.getOccupiedPositions().size(), 0));
        averageEnergyLevel.setText("Średnii poziom energi zwierzaków: " + map.getAverageEnergyLevel());
        averageChildCount.setText("Średnia ilość dzieci to: " + simulation.getAverageChildCount());
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
                Animal animal = map.getAnimalsAtPosition(clickedPosition).getFirst();
                if (animal != null) {
                    followedAnimal = animal;
                    updateAnimalStats(animal);
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
            } else {
                simulation.play();
                pausePlayButton.setText("Pauza");
                additionalButton.setVisible(false);
            }
        }
    }


    public void handleAdditionalButtonAction() {
//        GraphicsContext gc = mapCanvas.getGraphicsContext2D();
//
//        double cellWidth = mapCanvas.getWidth() / (map.getWidth() + 1);
//        double cellHeight = mapCanvas.getHeight() / (map.getHight() + 1);
//        map.getAnimalPositions().forEach(position -> {
//            Animal animal = map.getAnimalsAtPosition(position).stream()
//                    .filter(animal1 -> animal1.getGenes() == map.getMostFrequentGenome())
//                    .toList().getFirst();
//            gc.setFill(getColor(animal));
//            double x = position.getX() * cellWidth;
//            double y = position.getY() * cellHeight;
//            gc.fillOval(x, y, cellWidth, cellHeight);
//            gc.setFill(Color.MAGENTA);
//            gc.fillRect(x, y, cellWidth, cellHeight);
//        });
    }
}
