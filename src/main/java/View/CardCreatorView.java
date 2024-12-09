package View;

import Model.FlashCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Die CardCreatorView-Klasse stellt die grafische Benutzeroberfläche (GUI) für das Erstellen
 * von Lernkarten bereit. Benutzer können Fragen und Antworten eingeben und die Daten speichern.
 */
public class CardCreatorView {
    private JTextArea questionArea; // Textbereich für die Eingabe der Frage
    private JTextArea answerArea; // Textbereich für die Eingabe der Antwort
    private JButton nextButton; // Button für das Hinzufügen der nächsten Karte
    private JMenuItem saveMenuItem; // Menüpunkt zum Speichern der Karten
    private JFrame frame; // Hauptfenster der GUI

    /**
     * Konstruktor für die CardCreatorView.
     * Initialisiert die Benutzeroberfläche und ihre Komponenten.
     */
    public CardCreatorView() {
        //Initialisierung des Hauptfensters
        frame = new JFrame("Lernkarten-Ersteller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Hauptpanel-Einrichtung
        JPanel mainPanel = new JPanel();
        Font greatFont = new Font("Arial", Font.BOLD, 21);

        //Initialisierung der Textfelder für Frage und Antwort
        questionArea = new JTextArea(10, 25);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(greatFont);
        JScrollPane qJScrollPane = new JScrollPane(questionArea);
        qJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        answerArea = new JTextArea(10, 25);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setFont(greatFont);
        JScrollPane aJScrollPane = new JScrollPane(answerArea);
        aJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //Erstellen von Buttons und Labels
        nextButton = new JButton("Nächste Karte");
        JLabel qJLabel = new JLabel("Frage");
        JLabel aJLabel = new JLabel("Antwort");

        //Hinzufügen der Komponenten zum Hauptpanel
        mainPanel.add(qJLabel);
        mainPanel.add(qJScrollPane);
        mainPanel.add(aJLabel);
        mainPanel.add(aJScrollPane);
        mainPanel.add(nextButton);

        //Menüeinrichtung
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");
        saveMenuItem = new JMenuItem("Speichern");

        menuBar.add(fileMenu);
        fileMenu.add(saveMenuItem);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 650);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Gibt den Text aus dem Fragefeld zurück.
     *
     * @return Der Text aus dem Fragefeld.
     */
    public String getQuestionText() {
        return questionArea.getText();
    }

    /**
     * Gibt den Text aus dem Antwortfeld zurück.
     *
     * @return Der Text aus dem Antwortfeld.
     */
    public String getAnswerText() {
        return answerArea.getText();
    }

    /**
     * Löscht den Inhalt der Textfelder und setzt den Fokus auf das Fragefeld.
     */
    public void clearCard() {
        questionArea.setText("");
        answerArea.setText("");
        questionArea.requestFocus();
    }

    /**
     * Öffnet einen Dialog zum Speichern der Karten in einer Datei.
     *
     * @param flashCardList Die Liste der zu speichernden FlashCards.
     */
    public void promptForFile(List<FlashCard> flashCardList) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Speichern als");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textdateien (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Automatisch ".txt" hinzufügen, falls nicht vorhanden
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            saveFlashCardsToFile(fileToSave, flashCardList);
        }
    }

    /**
     * Speichert die gegebenen FlashCards in eine Datei.
     *
     * @param file          Die Datei, in der die FlashCards gespeichert werden sollen.
     * @param flashCardList Die Liste der zu speichernden FlashCards.
     */
    private void saveFlashCardsToFile(File file, List<FlashCard> flashCardList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (FlashCard card : flashCardList) {
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Lernkarten erfolgreich gespeichert.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Fehler beim Speichern der Lernkarten.");
            e.printStackTrace();
        }
    }

    /**
     * Setzt einen Listener für den "Nächste Karte"-Button.
     *
     * @param listener Der ActionListener, der auf den Button reagiert.
     */
    public void setNextButtonListener(ActionListener listener) {

        nextButton.addActionListener(listener);
    }

    /**
     * Setzt einen Listener für das "Speichern"-Menüelement.
     *
     * @param listener Der ActionListener, der auf das Menüelement reagiert.
     */
    public void setSaveMenuItemListener(ActionListener listener) {

        saveMenuItem.addActionListener(listener);
    }

}