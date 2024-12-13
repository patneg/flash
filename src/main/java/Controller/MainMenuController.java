package Controller;

import Model.CardManager;
import View.MainMenuView;
import Main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller-Klasse, die die Interaktionen zwischen der Benutzeroberfläche des Hauptmenüs
 * und der zugrunde liegenden Datenlogik (CardManager) verarbeitet.
 * @author Patrick Negri
 */
public class MainMenuController {
    private MainMenuView mainMenuView; // Instanz der Hauptmenü-Ansicht
    private CardManager cardManager; // Instanz zur Verwaltung von Karten

    /**
     * Konstruktor für den MainMenuController.
     *
     * @param mainMenuView Die Hauptmenü-Ansicht; darf nicht null sein.
     * @param cardManager  Der CardManager zur Verwaltung der Karten; darf nicht null sein.
     * @throws IllegalArgumentException Wenn mainMenuView oder cardManager null sind.
     */
    public MainMenuController(MainMenuView mainMenuView, CardManager cardManager) {
        if (mainMenuView == null || cardManager == null) {
            throw new IllegalArgumentException("View.MainMenuView und CardManager dürfen nicht null sein.");
        }

        this.mainMenuView = mainMenuView;
        this.cardManager = cardManager;

        // Listener für die Buttons im Hauptmenü hinzufügen
        try {
            this.mainMenuView.setCombinedEditorPlayerListener(new CombinedEditorPlayerListener());
            this.mainMenuView.setCreateButtonListener(new CreateButtonListener());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Fehler beim Initialisieren der Listener: " + e.getMessage(),
                    "Initialisierungsfehler",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Listener für den kombinierten Editor- und Player-Button.
     * Öffnet die Editor- und Player-Funktionalität und schließt das Hauptmenü.
     */
    private class CombinedEditorPlayerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Öffnen des Editors und Players
                Main.openCardPlayerEditor(cardManager);
                mainMenuView.close(); // Hauptmenü schließen
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Fehler beim Öffnen des Editors/Players: " + ex.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Listener für den Kartenerstellungs-Button.
     * Öffnet den Kartenersteller und schließt das Hauptmenü.
     */
    private class CreateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Öffnen des Kartenerstellers
                Main.openCardCreator();
                mainMenuView.close(); // Hauptmenü schließen
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Fehler beim Öffnen des Kartenerstellers: " + ex.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
