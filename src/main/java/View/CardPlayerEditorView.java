package View;

import Model.CardManager;
import Model.FlashCard;
import Controller.CardPlayerEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Diese Klasse stellt die grafische Benutzeroberfläche für den Karten-Editor und Player bereit.
 * Sie ermöglicht das Bearbeiten, Navigieren und Speichern von Karten sowie das Importieren von Kartensets.
 */
public class CardPlayerEditorView {
    private JFrame frame; // Hauptfenster der Anwendung
    private JTextArea questionArea, answerArea; // Textbereiche für Frage und Antwort
    private JButton showAnswerButton, nextButton, prevButton, importButton, saveButton, deleteButton; // Buttons der Benutzeroberfläche
    private JLabel progressLabel; // Label zur Anzeige des Fortschritts (z. B. "1/10")
    private DefaultListModel<FlashCard> listModel; // Modell zur Verwaltung der Kartenliste
    private JList<FlashCard> cardList; // Liste der angezeigten Karten
    private CardManager cardManager; // Karten-Manager für die Kartenlogik
    private CardPlayerEditorController controller; // Controller für die Interaktion zwischen GUI und Logik

    /**
     * Konstruktor für die Klasse CardPlayerEditorView.
     *
     * @param cardManager Der CardManager, der die Karten verwaltet; darf nicht null sein.
     */
    public CardPlayerEditorView(CardManager cardManager) {
        this.cardManager = cardManager;
        initializeUI();
    }

    /**
     * Initialisiert die Benutzeroberfläche der Anwendung.
     */
    private void initializeUI() {
        frame = new JFrame("Editor & Player"); // Fenster der Anwendung
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Anwendung wird beendet, wenn Fenster geschlossen wird
        frame.setSize(1500, 1100);
        frame.setLayout(new BorderLayout(10, 10));

        // Initialisierung der Komponenten
        questionArea = new JTextArea(10, 50);
        answerArea = new JTextArea(10, 50);
        saveButton = new JButton("Änderungen speichern");
        saveButton.setFont(new Font("Arial", Font.BOLD, 24));
        saveButton.setEnabled(false);

        showAnswerButton = new JButton("Antwort zeigen");
        showAnswerButton.setFont(new Font("Arial", Font.BOLD, 24));

        listModel = new DefaultListModel<>();
        cardList = new JList<>(listModel);
        progressLabel = new JLabel("0/0", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 24));

        // Initialisierung des Controllers
        controller = new CardPlayerEditorController(
                cardManager, questionArea, answerArea, saveButton, showAnswerButton, listModel, cardList, progressLabel
        );

        // Aufbau der GUI
        setupLeftPanel(); // Linke Seite des Fensters
        setupMainPanel(); // Hauptteil des Fensters


        frame.setVisible(true); // Rahmen wird sichtbar
        frame.setLocationRelativeTo(null); // Fenster erscheint in der Mitte des Bildschirms
        controller.updateCardList(); // Initiale Aktualisierung der Kartenliste
        controller.addTextChangeListeners(); // Hinzufügen von Listenern für Textänderungen
    }

    /**
     * Erstellt und konfiguriert das linke Panel für die Kartenliste.
     */
    private void setupLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // Oberes Panel mit Button für Import
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        importButton = new JButton("Kartenset importieren");
        importButton.setFont(new Font("Arial", Font.BOLD, 14));
        File testFile = null;
        importButton.addActionListener(e -> controller.importCardSet(frame, testFile));


        topPanel.add(importButton);

        // Kartenliste Formatierung
        cardList.setFont(new Font("Arial", Font.PLAIN, 18));

        // Renderer für die Liste festlegen, sodass nur die Frage angezeigt wird
        cardList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Standard-Renderer verwenden
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // Nur die Frage der FlashCard anzeigen
                if (value instanceof FlashCard) {
                    FlashCard card = (FlashCard) value;
                    renderer.setText(card.getQuestion()); // Nur die Frage setzen
                }
                return renderer;
            }
        });

        cardList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !cardList.isSelectionEmpty()) {
                controller.loadCardFromList(cardList.getSelectedIndex());
            }
        });

        JScrollPane listScrollPane = new JScrollPane(cardList); // Scrollen bei langer Liste
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(listScrollPane, BorderLayout.CENTER);

        frame.add(leftPanel, BorderLayout.WEST);
    }

    /**
     * Erstellt und konfiguriert das Hauptpanel für die Frage- und Antwortanzeige sowie die Buttons.
     */
    private void setupMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        Font mFont = new Font("Arial", Font.BOLD, 32);

        // Formatierung Frage-Fenster
        questionArea.setFont(mFont);
        questionArea.setWrapStyleWord(true);
        questionArea.setLineWrap(true);

        // Formatierung Antwort-Fenster
        answerArea.setFont(mFont);
        answerArea.setWrapStyleWord(true);
        answerArea.setLineWrap(true);

        JPanel qaPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JScrollPane questionScrollPane = new JScrollPane(questionArea);
        JScrollPane answerScrollPane = new JScrollPane(answerArea);
        qaPanel.add(questionScrollPane);
        qaPanel.add(answerScrollPane);

        // Button Formatierung
        showAnswerButton.addActionListener(e -> controller.showAnswer()); // Methode in Controller wird aufgerufen wenn Button gedrückt wird

        prevButton = new JButton("Vorherige Karte");
        prevButton.setFont(new Font("Arial", Font.BOLD, 24));
        prevButton.addActionListener(createNavigationAction(-1));

        nextButton = new JButton("Nächste Karte");
        nextButton.setFont(new Font("Arial", Font.BOLD, 24));
        nextButton.addActionListener(createNavigationAction(1));

        saveButton.addActionListener(e -> {
            controller.saveChanges();
        });

        deleteButton = new JButton("Karte löschen");
        deleteButton.setFont(new Font("Arial", Font.BOLD, 24));
        deleteButton.addActionListener(e -> {
            controller.deleteCurrentCard();
        });

        // Panel für die Button unten
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        buttonPanel.add(prevButton);
        buttonPanel.add(showAnswerButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(qaPanel, BorderLayout.CENTER);
        mainPanel.add(progressLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Erstellt eine Aktion für die Navigation durch die Karten.
     *
     * @param direction Die Richtung der Navigation (-1 für vorherige Karte, 1 für nächste Karte).
     * @return Ein ActionListener, der die Navigation durchführt.
     */
    private ActionListener createNavigationAction(int direction) {
        return e -> {
            boolean hasUnsavedChanges = saveButton.isEnabled(); // Überprüfen, ob der Speichern-Button aktiv ist
            if (hasUnsavedChanges) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Es gibt ungespeicherte Änderungen. Bitte speichern Sie zuerst, bevor Sie fortfahren.",
                        "Ungespeicherte Änderungen",
                        JOptionPane.WARNING_MESSAGE
                );
                return; // Navigation abbrechen
            }

            int newIndex = cardList.getSelectedIndex() + direction;
            if (newIndex >= 0 && newIndex < listModel.size()) {
                cardList.setSelectedIndex(newIndex);
            }
        };
    }

}
