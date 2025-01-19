package frontend;

import javafx.application.Application;

public class Start {
    public static void main(String[] args) {
        try {
            Application.launch(ConfigurationApp.class, args);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }
    }
}
