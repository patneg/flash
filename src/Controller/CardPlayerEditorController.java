package Controller;

import Model.CardManager;
import Model.FlashCard;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;
import java.io.IOException;

// Controller für den CardPlayerEditor. Importiert Datensets und verwaltet die Änderungen der Karten.
public class CardPlayerEditorController {
    private CardManager cardManager; // Klasse zum Laden, Speichern und Bearbeiten von Karten
    private DefaultListModel<FlashCard> listModel; // Verwaltet die Liste mit Flash-karten
    private JTextArea questionArea; //Textfeld für Frage
    private JTextArea answerArea; //Textfeld für Antwort
    private JButton saveButton; //Button um zu sichern
    private JButton showAnswerButton; // Add the showAnswerButton
    private JList<FlashCard> cardList; // Zeigt alle Karten an
    private JLabel progressLabel; // Zeigt den Fortschritt an 1/10
    private int currentCardIndex; // Speichert den Index der aktuell angezeigten Karte innerhalb der Liste
    private File currentFile; // File in welchem die Karten gespeichert werden

    // Der Konstruktor CardPlayerEditorController wird verwendet,
    // um die Klasse zu instanziieren und alle Abhängigkeiten bereitzustellen
    public CardPlayerEditorController(CardManager cardManager, JTextArea questionArea, JTextArea answerArea,
                                      JButton saveButton, JButton showAnswerButton, DefaultListModel<FlashCard> listModel,
                                      JList<FlashCard> cardList, JLabel progressLabel) {
        this.cardManager = cardManager;
        this.questionArea = questionArea;
        this.answerArea = answerArea;
        this.saveButton = saveButton;
        this.showAnswerButton = showAnswerButton;
        this.listModel = listModel;
        this.cardList = cardList;
        this.progressLabel = progressLabel;
        this.currentCardIndex = 0;
    }
    public void importCardSet(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                cardManager.loadCardsFromFile(selectedFile);
                currentFile = selectedFile;
                updateCardList();
                JOptionPane.showMessageDialog(frame, "Card set imported successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Error importing card set.");
                e.printStackTrace();
            }
        }
    }
    public CardPlayerEditorController(CardManager cardManager) {
    }

    // Methode zur Überwachung der questionArea und answerArea auf Änderungen
    public void addTextChangeListeners() {
        DocumentListener docListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checkForChanges();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checkForChanges();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                checkForChanges();
            }
        };
        questionArea.getDocument().addDocumentListener(docListener);
        answerArea.getDocument().addDocumentListener(docListener);
    }

    // Methode zur Prüfung von Veränderungen und Aktivierung des Save-Buttons
    private void checkForChanges() {
        if (currentCardIndex >= 0 && currentCardIndex < cardManager.size()) {
            FlashCard currentCard = cardManager.getFlashCards().get(currentCardIndex);
            boolean hasChanges = !questionArea.getText().equals(currentCard.getQuestion()) ||
                    !answerArea.getText().equals(currentCard.getAnswer());
            saveButton.setEnabled(hasChanges);
        }
    }

    // Zeigt die Antwort und deaktiviert danach den Antwort-Button
    public void showAnswer() {
        answerArea.setVisible(true);
        showAnswerButton.setEnabled(false);
    }
    // Methode für den Import eines Kartensets


    // Methode zur Speicherung von Änderungen und Anpassung der Benutzeroberfläche
    public void saveChanges() throws IOException {
        if (currentCardIndex >= 0 && currentCardIndex < cardManager.size() && currentFile != null) {
            FlashCard updatedCard = new FlashCard(questionArea.getText(), answerArea.getText());
            cardManager.updateFlashCard(currentCardIndex, updatedCard);
            cardManager.saveCardsToFile(currentFile);
            updateCardList();

            // Deaktiviert den Speichern-Button nach dem Speichern
            saveButton.setEnabled(false);

            // Feedback für den User
            JOptionPane.showMessageDialog(null, "Erfolgreich gespeichert!", "Speichern bestätigen", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Methode für die Löschung von Karten und Speicherung der Änderungen
    public void deleteCurrentCard() throws IOException {
        if (currentCardIndex >= 0 && currentCardIndex < cardManager.size()) {
            int confirmation = JOptionPane.showConfirmDialog(null, "Sind Sie sicher, dass Sie diese Karte löschen möchten?", "Löschen bestätigen", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                cardManager.deleteFlashCard(currentCardIndex);
                if (currentFile != null) {
                    cardManager.saveCardsToFile(currentFile);
                }
                updateCardList(); // Aktualisiert die Liste, um die gelöschte Karte nicht mehr anzuzeigen
                JOptionPane.showMessageDialog(null, "Karte gelöscht und Änderungen erfolgreich gespeichert!");

                // Wenn der currentCardIndex grösser als 0 ist, wird er um 1 verringert und zeigt die vorherige Karte an
                if (currentCardIndex > 0) {
                    currentCardIndex--;
                }
                // Wenn der cardManager nach dem Löschen nicht leer ist, wird die Karte geladen welche nun in dieser Position ist
                if (!cardManager.isEmpty()) {
                    loadCardFromList(currentCardIndex);
                } else {
                    questionArea.setText("");
                    answerArea.setText("");
                    progressLabel.setText("0/0");
                    saveButton.setEnabled(false);
                    showAnswerButton.setEnabled(false); // Antwort Anzeigen deaktivieren, falls keine Karten mehr vorhanden
                }
            }
        }
    }

    // Die Methode lädt eine Karte aus der Liste anhand eines Indexwerts und aktualisiert die Benutzeroberfläche
    public void loadCardFromList(int index) {
        if (index >= 0 && index < cardManager.size()) {
            FlashCard card = cardManager.getFlashCards().get(index);
            currentCardIndex = index;
            questionArea.setText(card.getQuestion());
            answerArea.setText(card.getAnswer());
            answerArea.setVisible(false); // Verstecken der Antwort, wenn eine neue Karte geladen wird
            showAnswerButton.setEnabled(true); // Aktivieren des Antwort-Buttons, wenn neue Karte geladen wird
            updateProgressLabel();
            saveButton.setEnabled(false); // Speichern-Button wird zurückgesetzt, da keine Änderungen vorgenommen wurden
        }
    }

    // Methode zur Aktualisierung der angezeigten Karten in der Benutzeroberfläche
    public void updateCardList() {
        listModel.clear();
        for (FlashCard card : cardManager.getFlashCards()) {
            listModel.addElement(card);
        }
        updateProgressLabel(); // Fortschrittsanzeige wird aktualisiert
    }

    // Methode für die Aktualisierung der Fortschrittsanzeige
    private void updateProgressLabel() {
        progressLabel.setText((currentCardIndex + 1) + "/" + cardManager.size());

    }

    // Unit Test


}

