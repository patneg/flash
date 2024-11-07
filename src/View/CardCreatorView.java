package View;

import Model.FlashCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

//View für die Erstellung von Lernkarten. Stellt die UI-Komponenten für die Eingabe von Fragen/Antworten
public class CardCreatorView {
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JTextArea question;
    private JTextArea answer;
    private JButton nextButton;
    private JMenuItem saveMenuItem;
    private JFrame frame;

    //Konstruktor für die CardCreatorView. Initialisiert das GUI und seine Komponenten.
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

    //Getter-Methoden, um Benutzereingaben abzurufen
    public String getQuestionText() {
        return questionArea.getText();
    }

    public String getAnswerText() {
        return answerArea.getText();
    }

    public void clearCard() {
        questionArea.setText("");
        answerArea.setText("");
        questionArea.requestFocus();
    }

    //Methode zum Speichern der Lernkarten in einer Datei
    public void promptForFile(List<FlashCard> flashCardList) {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showSaveDialog(frame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            saveFlashCardsToFile(fileToSave, flashCardList);
        }
    }

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

    //Methoden zum Setzen der Listener
    public void setNextButtonListener(ActionListener listener) {

        nextButton.addActionListener(listener);
    }

    public void setSaveMenuItemListener(ActionListener listener) {

        saveMenuItem.addActionListener(listener);
    }

    //Unit Tests
    // Getter für die JTextArea-Komponenten
    public JTextArea getQuestionArea() {
        return question;
    }

    public JTextArea getAnswerArea() {
        return answer;
    }
    public static void main(String[] args) {
        //überprüft, ob die Methode clearCard die Textfelder für die Frage/Antwort leert.
        // Arrange
        CardCreatorView view = new CardCreatorView();
        view.getQuestionArea().setText("Testfrage");
        view.getAnswerArea().setText("Testantwort");

        // Act
        view.clearCard();

        // Assert
        assert view.getQuestionText().isEmpty() : "Fragefeld sollte leer sein.";
        assert view.getAnswerText().isEmpty() : "Antwortfeld sollte leer sein.";
        System.out.println("Test clearCard erfolgreich bestanden.");
    }
}