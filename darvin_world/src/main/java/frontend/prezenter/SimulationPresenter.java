package frontend.prezenter;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class SimulationPresenter {
    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    public void initialize() {
        var series = new XYChart.Series<Number, Number>();
        series.setName("Dynamiczna seria");
        series.getData().add(new XYChart.Data<>(4, 20));
        series.getData().add(new XYChart.Data<>(5, 25));
        lineChart.getData().add(series);

    }
}
