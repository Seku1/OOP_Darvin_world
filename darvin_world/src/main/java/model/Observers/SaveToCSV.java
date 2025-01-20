package model.Observers;

import model.Maps.AbstractWorldMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SaveToCSV implements MapChangeObserver {

    private final String fileName = "map.csv";

    // Inicjalizacja pliku CSV z nagłówkami
    public void init() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("dzien, liczba_zwierząt, liczba_roslin, liczba_wolnych_pol, sredni_poziom_energi_zwierzakow, najczestrzy_genom, przewidywana_dlugosc_zycia, srednia_ilosc_dzieci");
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się zainicjalizować pliku CSV: " + e.getMessage(), e);
        }
    }

    // Zapis statystyk po każdej zmianie mapy
    @Override
    public void mapChanged(AbstractWorldMap map, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) { // Tryb "append"
            // Pobieranie statystyk z mapy
            int day = map.getDayNumber();
            int animalCount = map.getAnimals().size();
            int plantCount = map.getPlants().size();
            int emptyFields = Math.max(map.getWidth() * map.getHight() - map.getOccupiedPositions().size(), 0);
            double averageEnergy = map.getAverageEnergyLevel();
            String dominantGenome = map.getDominantGenome(); // Metoda do pobrania najczęstszego genomu
            double predictedLifeSpan = map.getPredictedLifeSpan(); // Metoda do przewidywania długości życia
            double averageChildCount = map.();

            // Zapis danych w formacie CSV
            writer.write(String.format("%d,%d,%d,%d,%.2f,%s,%.2f,%.2f",
                    day, animalCount, plantCount, emptyFields, averageEnergy, dominantGenome, predictedLifeSpan, averageChildCount));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas zapisywania danych do CSV: " + e.getMessage(), e);
        }
    }
}
