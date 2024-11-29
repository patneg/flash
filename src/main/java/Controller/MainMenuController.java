package Controller;



import Model.CardManager;
import View.MainMenuView;
import Main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Controller-Klasse, die die Interaktionen zwischen der Benutzeroberfläche und den zugrunde liegenden Datenlogik verarbeitet
// Controller-Klasse, die die Interaktionen zwischen der Benutzeroberfläche und den zugrunde liegenden Datenlogik verarbeitet
public class MainMenuController {
    private MainMenuView mainMenuView; // Instanz der Ansicht (View) des Hauptmenüs, die die Benutzeroberfläche darstellt
    private CardManager cardManager; // Instanz des CardManager, der für die Verwaltung von Karten verantwortlich ist

    public MainMenuController(MainMenuView mainMenuView, CardManager cardManager) {
        if (mainMenuView == null || cardManager == null) {
            throw new IllegalArgumentException("View.MainMenuView und CardManager dürfen nicht null sein.");
        }

        this.mainMenuView = mainMenuView;
        this.cardManager = cardManager;

        // Verknüpfung der Listener mit den Buttons
        try {
            this.mainMenuView.setCombinedEditorPlayerListener(new CombinedEditorPlayerListener());
            this.mainMenuView.setCreateButtonListener(new CreateButtonListener());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Initialisieren der Listener: " + e.getMessage(), "Initialisierungsfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Listener, die auf Benutzerinteraktionen reagieren
    private class CombinedEditorPlayerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Editor & Player öffnen
                Main.openCardPlayerEditor(cardManager);
                mainMenuView.close(); // Hauptmenü schließen
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Fehler beim Öffnen des Editors/Players: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class CreateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Kartenersteller öffnen
                Main.openCardCreator();
                mainMenuView.close(); // Hauptmenü schließen
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Fehler beim Öffnen des Kartenerstellers: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
