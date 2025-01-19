package frontend.prezenter;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ConfigurationPrezenter {
    private int height = 600;
    private int width = 800;
    @FXML
    private ComboBox<String> configurationComboBox, mapVariantBox, behaviorVariantBox;
    @FXML
    private TextField heightField, widthField, initialPlantCountField, plantEnergyField,
            dailyPlantGrowthField, initialAnimalCountField, initialAnimalEnergyField,
            energyToReproduceField, energyForChildField, minMutationsField,
            maxMutationsField, genomeLengthField;

    private static final String CONFIG_DIR = "configurations";

    @FXML
    public void initialize() {
        loadAvailableConfigurations();
        loadBehaviorVariantBox();
        loadMapVariantBox();
    }

    private void loadAvailableConfigurations() {
        try {
            Files.createDirectories(Paths.get(CONFIG_DIR)); // Ensure directory exists
            List<String> configFiles = Files.list(Paths.get(CONFIG_DIR))
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
            configurationComboBox.setItems(FXCollections.observableArrayList(configFiles));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void loadConfigurationFromFile() {
        String selectedConfig = configurationComboBox.getValue();
        if (selectedConfig == null) return;

        Path filePath = Paths.get(CONFIG_DIR, selectedConfig);
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            heightField.setText(reader.readLine());
            widthField.setText(reader.readLine());
            mapVariantBox.setValue(reader.readLine());
            initialPlantCountField.setText(reader.readLine());
            plantEnergyField.setText(reader.readLine());
            dailyPlantGrowthField.setText(reader.readLine());
            initialAnimalCountField.setText(reader.readLine());
            initialAnimalEnergyField.setText(reader.readLine());
            energyToReproduceField.setText(reader.readLine());
            energyForChildField.setText(reader.readLine());
            minMutationsField.setText(reader.readLine());
            maxMutationsField.setText(reader.readLine());
            genomeLengthField.setText(reader.readLine());
            behaviorVariantBox.setValue(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveConfiguration() {
        String fileName = "config_" + System.currentTimeMillis() + ".txt";
        Path filePath = Paths.get(CONFIG_DIR, fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            // Zapisanie wszystkich danych do pliku
            writer.write(heightField.getText() + "\n");
            writer.write(widthField.getText() + "\n");
            writer.write(mapVariantBox.getValue() + "\n");
            writer.write(initialPlantCountField.getText() + "\n");
            writer.write(plantEnergyField.getText() + "\n");
            writer.write(dailyPlantGrowthField.getText() + "\n");
            writer.write(initialAnimalCountField.getText() + "\n");
            writer.write(initialAnimalEnergyField.getText() + "\n");
            writer.write(energyToReproduceField.getText() + "\n");
            writer.write(energyForChildField.getText() + "\n");
            writer.write(minMutationsField.getText() + "\n");
            writer.write(maxMutationsField.getText() + "\n");
            writer.write(genomeLengthField.getText() + "\n");
            writer.write(behaviorVariantBox.getValue() + "\n");
            showAlert("Informacja", "Zapisano konfiguracje: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadAvailableConfigurations(); // Refresh ComboBox
    }

    @FXML
    private void onDeleteConfiguration() {
        String selectedConfig = configurationComboBox.getValue();
        if (selectedConfig == null) {
            showAlert("Brak wybranej konfiguracji", "Proszę wybrać konfigurację do usunięcia.");
            return;
        }

        Path filePath = Paths.get(CONFIG_DIR, selectedConfig);
        try {
            Files.delete(filePath);
            showAlert("Sukces", "Konfiguracja została usunięta.");
            loadAvailableConfigurations(); // Refresh ComboBox
        } catch (IOException e) {
            showAlert("Błąd", "Nie udało się usunąć konfiguracji: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onSelectConfiguration() {
        String selectedConfig = configurationComboBox.getValue();
        if (selectedConfig != null) {
            loadConfigurationFromFile();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadBehaviorVariantBox() {
        behaviorVariantBox.setItems(FXCollections.observableArrayList(
                "Pełna predestynacja ",
                "Starość nie radość"
        ));
    }

    private void loadMapVariantBox() {
        mapVariantBox.setItems(FXCollections.observableArrayList(
                "Kula ziemska",
                "Bieguny"
        ));
    }

    public void onStart(ActionEvent actionEvent) {
        if(validateConfiguration()){

        }else{
            showAlert("Błąd", "Podaj wszystkie parametry!!!");

        }
    }

    private boolean validateConfiguration() {
        return nonNull(heightField.getText()) && !heightField.getText().trim().isEmpty() &&
                nonNull(widthField.getText()) && !widthField.getText().trim().isEmpty() &&
                nonNull(initialPlantCountField.getText()) && !initialPlantCountField.getText().trim().isEmpty() &&
                nonNull(plantEnergyField.getText()) && !plantEnergyField.getText().trim().isEmpty() &&
                nonNull(dailyPlantGrowthField.getText()) && !dailyPlantGrowthField.getText().trim().isEmpty() &&
                nonNull(initialAnimalCountField.getText()) && !initialAnimalCountField.getText().trim().isEmpty() &&
                nonNull(initialAnimalEnergyField.getText()) && !initialAnimalEnergyField.getText().trim().isEmpty() &&
                nonNull(energyToReproduceField.getText()) && !energyToReproduceField.getText().trim().isEmpty() &&
                nonNull(energyForChildField.getText()) && !energyForChildField.getText().trim().isEmpty() &&
                nonNull(minMutationsField.getText()) && !minMutationsField.getText().trim().isEmpty() &&
                nonNull(maxMutationsField.getText()) && !maxMutationsField.getText().trim().isEmpty() &&
                nonNull(genomeLengthField.getText()) && !genomeLengthField.getText().trim().isEmpty() &&
                nonNull(mapVariantBox.getValue()) &&
                nonNull(behaviorVariantBox.getValue());
    }
}
