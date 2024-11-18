package Controller;

import Main.Main;
import View.MainMenuView;
import Model.CardManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Controller-Klasse, die die Interaktionen zwischen der Benutzeroberfläche und den zugrunde liegenden Datenlogik verarbeitet
public class MainMenuController {
    private MainMenuView mainMenuView; // Instanz der Ansicht (View) des Hauptmenüs, die die Benutzeroberfläche darstellt
    private CardManager cardManager; // Instanz des CardManager, der für die Verwaltung von Karten verantwortlich ist

    public MainMenuController(MainMenuView mainMenuView, CardManager cardManager) {
        this.mainMenuView = mainMenuView;
        this.cardManager = cardManager;

        this.mainMenuView.setCombinedEditorPlayerListener(new CombinedEditorPlayerListener()); // Verknüpft einen Listener mit einem Button, der mit dem Editor & Player zu tun hat
        this.mainMenuView.setCreateButtonListener(new CreateButtonListener()); // Verknüpft einen Listener mit einem Button zum Erstellen von Karten
    }

    // Listener, welche auf Benutzerinteraktionen reagieren
    private class CombinedEditorPlayerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Editor & Player öffnen
            Main.openCardPlayerEditor(cardManager);
            mainMenuView.close(); // Hauptmenü schliessen
        }
    }

    private class CreateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Kartenersteller öffnen
            Main.openCardCreator();
            mainMenuView.close(); // Hauptmenü schliessen
        }
    }
}
