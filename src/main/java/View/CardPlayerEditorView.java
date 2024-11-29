package View;

import Model.CardManager;
import Model.FlashCard;
import Controller.CardPlayerEditorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

// Klasse für die grafische Benutzeroberfläche
public class CardPlayerEditorView {
    private JFrame frame;
    private JTextArea questionArea, answerArea;
    private JButton showAnswerButton, nextButton, prevButton, importButton, saveButton, deleteButton;
    private JLabel progressLabel;
    private DefaultListModel<FlashCard> listModel;
    private JList<FlashCard> cardList;
    private CardManager cardManager;
    private CardPlayerEditorController controller;

    // Konstruktor der Klasse View.CardPlayerEditorView
    public CardPlayerEditorView(CardManager cardManager) {
        this.cardManager = cardManager;
        initializeUI();
    }

    // Formatierung und Gestaltung
    private void initializeUI() {
        frame = new JFrame("Editor & Player"); // Fenster der Anwendung
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Anwendung wird beendet, wenn Fenster geschlossen wird
        frame.setSize(1500, 1100);
        frame.setLayout(new BorderLayout(10, 10));

        // Komponenten
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

        // Controller wird erstellt für die Interaktion zwischen UI und Flashkarten
        controller = new CardPlayerEditorController(
                cardManager, questionArea, answerArea, saveButton, showAnswerButton, listModel, cardList, progressLabel
        );


        setupLeftPanel(); // Linke Seite des Fensters
        setupMainPanel(); // Hauptteil des Fensters


        frame.setVisible(true); // Rahmen wird sichtbar
        frame.setLocationRelativeTo(null); // Fenster erscheint in der Mitte des Bildschirms
        controller.updateCardList();
        controller.addTextChangeListeners();
    }

    // Methode für das linke Panel im Fenster
    private void setupLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));

        // Oberes Panel mit Button für Import
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel overviewLabel = new JLabel("Kartenübersicht");
        importButton = new JButton("Kartenset importieren");
        importButton.setFont(new Font("Arial", Font.BOLD, 14));
        File testFile = null;
        importButton.addActionListener(e -> controller.importCardSet(frame, testFile));

        topPanel.add(overviewLabel);
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

    // Methode für das Haupt-Panel im Fenster
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

    private ActionListener createNavigationAction(int direction) {
        return e -> {
            int newIndex = cardList.getSelectedIndex() + direction;
            if (newIndex >= 0 && newIndex < listModel.size()) {
                cardList.setSelectedIndex(newIndex);
            }
        };
    }
}
