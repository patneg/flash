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


    public void setCurrentFile(File file) {
        this.currentFile = file;
    }

    public void setCurrentCardIndex(int index) {
        this.currentCardIndex = index;
    }
    public void importCardSet(JFrame frame, File testFile) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Kartensets (*.txt)", "txt")); // Optional: Dateitypen einschränken

        try {
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Validieren, ob die Datei existiert und lesbar ist
                if (!selectedFile.exists() || !selectedFile.canRead()) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Die ausgewählte Datei ist nicht lesbar oder existiert nicht.",
                            "Fehler beim Importieren",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Versuchen, Karten aus der Datei zu laden
                try {
                    cardManager.loadCardsFromFile(selectedFile);
                    if (cardManager.isEmpty()) { // Überprüfen, ob keine Karten geladen wurden
                        JOptionPane.showMessageDialog(
                                frame,
                                "Die Datei enthält keine gültigen Karten.",
                                "Importfehler",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    currentFile = selectedFile;
                    updateCardList();
                    JOptionPane.showMessageDialog(frame, "Karten erfolgreich importiert!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Fehler beim Importieren der Datei: " + e.getMessage(),
                            "Importfehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Die Datei hat ein ungültiges Format und konnte nicht geladen werden.",
                            "Importfehler",
                            JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage(),
                    "Importfehler",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
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
    public void saveChanges() {
        try {
            if (currentCardIndex < 0 || currentCardIndex >= cardManager.size()) {
                throw new IllegalStateException("Kein gültiger Kartenindex vorhanden.");
            }
            if (currentFile == null) {
                throw new IllegalStateException("Keine Datei zum Speichern verfügbar.");
            }

            FlashCard updatedCard = new FlashCard(questionArea.getText(), answerArea.getText());
            cardManager.updateFlashCard(currentCardIndex, updatedCard);
            cardManager.saveCardsToFile(currentFile);
            updateCardList();

            // Deaktiviert den Speichern-Button nach dem Speichern
            saveButton.setEnabled(false);

            // Feedback für den User
            JOptionPane.showMessageDialog(null, "Änderungen erfolgreich gespeichert!", "Speichern bestätigen", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException | IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Speichern: " + e.getMessage(), "Speicherfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Methode für die Löschung von Karten und Speicherung der Änderungen
    public void deleteCurrentCard() {
        try {
            if (currentCardIndex < 0 || currentCardIndex >= cardManager.size()) {
                throw new IllegalStateException("Kein gültiger Kartenindex vorhanden.");
            }

            int confirmation = JOptionPane.showConfirmDialog(null, "Sind Sie sicher, dass Sie diese Karte löschen möchten?", "Löschen bestätigen", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                cardManager.deleteFlashCard(currentCardIndex);
                if (currentFile != null) {
                    cardManager.saveCardsToFile(currentFile);
                }
                updateCardList(); // Aktualisiert die Liste, um die gelöschte Karte nicht mehr anzuzeigen
                JOptionPane.showMessageDialog(null, "Karte gelöscht und Änderungen erfolgreich gespeichert!");

                // Aktualisieren der Benutzeroberfläche
                if (currentCardIndex > 0) {
                    currentCardIndex--;
                }
                if (!cardManager.isEmpty()) {
                    loadCardFromList(currentCardIndex);
                } else {
                    resetUI();
                }
            }
        } catch (IllegalStateException | IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Löschen: " + e.getMessage(), "Löschfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



    // Methode zum Zurücksetzen der UI, wenn keine Karten mehr vorhanden sind
    public void resetUI() {
        questionArea.setText("");
        answerArea.setText("");
        progressLabel.setText("0/0");
        saveButton.setEnabled(false);
        showAnswerButton.setEnabled(false);
    }


    // Die Methode lädt eine Karte aus der Liste anhand eines Indexwerts und aktualisiert die Benutzeroberfläche
    public void loadCardFromList(int index) {
        try {
            if (index < 0 || index >= cardManager.size()) {
                throw new IllegalArgumentException("Ungültiger Kartenindex.");
            }

            FlashCard card = cardManager.getFlashCards().get(index);
            currentCardIndex = index;
            questionArea.setText(card.getQuestion());
            answerArea.setText(card.getAnswer());
            answerArea.setVisible(false); // Antwortfeld verstecken
            showAnswerButton.setEnabled(true); // Antwort anzeigen aktivieren
            updateProgressLabel(); // Fortschrittsanzeige aktualisieren
            saveButton.setEnabled(false); // Speichern deaktivieren
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Laden der Karte: " + e.getMessage(), "Ladefehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    // Methode zur Aktualisierung der angezeigten Karten in der Benutzeroberfläche
    public void updateCardList() {
        try {
            listModel.clear();
            for (FlashCard card : cardManager.getFlashCards()) {
                listModel.addElement(card);
            }
            updateProgressLabel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Aktualisieren der Kartenliste: " + e.getMessage(), "Listenfehler", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Methode für die Aktualisierung der Fortschrittsanzeige
    public void updateProgressLabel() {
        if (cardManager.isEmpty()) {
            progressLabel.setText("0/0");
        } else {
            progressLabel.setText((currentCardIndex + 1) + "/" + cardManager.size());
        }
    }
}

