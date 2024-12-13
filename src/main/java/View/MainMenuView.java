package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * Die Klasse MainMenuView stellt die grafische Benutzeroberfläche für das Hauptmenü der Anwendung bereit.
 * Das Hauptmenü bietet Optionen zum Öffnen des Editors &amp; Players sowie des Lernkarten-Erstellers.
 * @author Patrick Negri
 */
public class MainMenuView {
    private JFrame frame; // Hauptfenster der Anwendung
    private JButton combinedEditorPlayerButton; // Button zum Öffnen des Editors & Players
    private JButton createButton; // Button zum Öffnen des Lernkarten-Erstellers

    /**
     * Konstruktor für die MainMenuView-Klasse.
     * Initialisiert die Benutzeroberfläche.
     */
    public MainMenuView() {
        initializeUI();
    }

    /**
     * Initialisiert die grafische Benutzeroberfläche des Hauptmenüs.
     */
    private void initializeUI() {
        frame = new JFrame("Flash - Hauptmenü");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Anwendung wird beendet, wenn das Fenster geschlossen wird
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel für die Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10)); // Vertikale Anordnung der Buttons

        // Button für Editor & Player sowie Kartenersteller
        combinedEditorPlayerButton = new JButton("Öffne Editor & Player");
        createButton = new JButton("Lernkarten-Ersteller");

        // Button hinzufügen
        buttonPanel.add(combinedEditorPlayerButton);
        buttonPanel.add(createButton);

        // Button-Panel wird in der Mitte des Fensters platziert
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null); // Center the window
    }

    /**
     * Fügt einen Listener für den Button "Öffne Editor &amp; Player" hinzu.
     *
     * @param listener Der ActionListener, der die Aktion des Buttons verarbeitet.
     */
    public void setCombinedEditorPlayerListener(ActionListener listener) {
        combinedEditorPlayerButton.addActionListener(listener);
    }

    /**
     * Fügt einen Listener für den Button "Lernkarten-Ersteller" hinzu.
     *
     * @param listener Der ActionListener, der die Aktion des Buttons verarbeitet.
     */
    public void setCreateButtonListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    /**
     * Schließt das Hauptmenü-Fenster.
     */
    public void close() {
        frame.dispose();
    }
}
