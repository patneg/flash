package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenuView {
    private JFrame frame;
    private JButton combinedEditorPlayerButton;
    private JButton createButton;

    public MainMenuView() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Flash - Hauptmenü");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel für die Button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));

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

    // Methode für die Listeners der Buttons
    public void setCombinedEditorPlayerListener(ActionListener listener) {
        combinedEditorPlayerButton.addActionListener(listener);
    }

    public void setCreateButtonListener(ActionListener listener) {
        createButton.addActionListener(listener);
    }

    public void close() {
        frame.dispose();
    }
}
