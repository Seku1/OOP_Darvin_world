package frontend.prezenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import model.Maps.AbstractWorldMap;
import model.Simulations.Simulation;

public class SimulationPresenter {
    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Canvas mapCanvas;

    private AbstractWorldMap map;
    private Simulation simulation;
    private Thread simulationThread;
    private int simulationStep = 0;
    private XYChart.Series<Number, Number> animalSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> plantSeries = new XYChart.Series<>();

    @FXML
    public void initialize() {
        // Inicjalizacja wykresu
        animalSeries.setName("Liczba zwierząt");
        plantSeries.setName("Liczba roślin");

        lineChart.getData().addAll(animalSeries, plantSeries);
        lineChart.setAnimated(false);
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
            simulation.setThreadSleep(10);
            simulationThread.setDaemon(true);
            simulationThread.start();
        }
    }

    private void updateView() {
        simulationStep++;

        // Wywołaj aktualizację widoku w wątku JavaFX
        Platform.runLater(() -> {
            // Aktualizacja mapy
            drawMap();

            // Aktualizacja wykresu
            int animalCount = map.getAnimals().size();
            int plantCount = map.getPlantMap().size();

            animalSeries.getData().add(new XYChart.Data<>(simulationStep, animalCount));
            plantSeries.getData().add(new XYChart.Data<>(simulationStep, plantCount));

        });
    }

    private void drawMap() {
        if (map == null) return;

        GraphicsContext gc = mapCanvas.getGraphicsContext2D();

        // Wyczyszczenie mapy
        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

        // Ustal rozmiar jednej komórki w zależności od wielkości mapy
        double cellWidth = mapCanvas.getWidth() / (map.getWidth() + 1);
        double cellHeight = mapCanvas.getHeight() / (map.getHight() + 1);

        // Rysowanie roślin
        gc.setFill(Color.GREEN);
        map.getPlantMap().forEach((position, plant) -> {
            double x = position.getX() * cellWidth;
            double y = position.getY() * cellHeight;
            gc.fillRect(x, y, cellWidth, cellHeight);
        });

        // Rysowanie zwierząt
        gc.setFill(Color.RED);
        map.getAnimalPositions().forEach(position -> {
            double x = position.getX() * cellWidth;
            double y = position.getY() * cellHeight;
            gc.fillOval(x, y, cellWidth, cellHeight);
        });

        // Logowanie dla sprawdzenia, czy rysowanie działa
        System.out.println("Map drawn at step: " + simulationStep);
    }

}
