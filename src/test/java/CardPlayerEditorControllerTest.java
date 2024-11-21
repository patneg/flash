import Model.CardManager;
import Model.FlashCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardPlayerEditorControllerTest {

    private Model.CardManager cardManager;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JButton saveButton;
    private JButton showAnswerButton;
    private DefaultListModel<Model.FlashCard> listModel;
    private JList<Model.FlashCard> cardList;
    private JLabel progressLabel;
    private Controller.CardPlayerEditorController controller;

    // Dummy implementation of CardManager for testing
    class DummyCardManager extends CardManager {
        private List<Model.FlashCard> cards = new ArrayList<>();
        private File currentFile;

        @Override
        public void loadCardsFromFile(File file) {
            this.currentFile = file;
            cards.add(new Model.FlashCard("Question 1", "Answer 1")); // Add a sample card
        }

        @Override
        public void saveCardsToFile(File file) {
            this.currentFile = file; // Simulate saving
        }

        @Override
        public void updateFlashCard(int index, Model.FlashCard card) {
            cards.set(index, card);
        }

        @Override
        public void deleteFlashCard(int index) {
            cards.remove(index);
        }

        @Override
        public List<FlashCard> getFlashCards() {
            return cards;
        }

        @Override
        public int size() {
            return cards.size();
        }
    }

    @BeforeEach
    void setUp() {
        // Initialisierung der benötigten Komponenten und Dummy-Implementierung
        cardManager = new DummyCardManager();
        questionArea = new JTextArea();
        answerArea = new JTextArea();
        saveButton = new JButton();
        showAnswerButton = new JButton();
        listModel = new DefaultListModel<>();
        cardList = new JList<>(listModel);
        progressLabel = new JLabel();
        controller = new Controller.CardPlayerEditorController(cardManager, questionArea, answerArea, saveButton, showAnswerButton, listModel, cardList, progressLabel);
    }

    @Test
    void importCardSet() throws IOException {
        // Testet das Importieren eines Kartensets und überprüft,
        // ob die Methode zum Laden von Karten aufgerufen wird und die aktuelle Datei korrekt gesetzt wird
        File testFile = new File("testfile.txt");
        controller.importCardSet(new JFrame(), testFile);

        // Überprüfen, ob die Karte geladen wurde
        assertEquals(1, cardManager.size());
        assertEquals("Question 1", cardManager.getFlashCards().get(0).getQuestion());
    }

    @Test
    void addTextChangeListeners() {
        // Testet, ob der Speichern-Button aktiviert wird,
        // wenn Änderungen im Frage- oder Antwortfeld vorgenommen werden
        controller.addTextChangeListeners();

        // Simuliere Textänderungen
        questionArea.setText("Neue Frage");
        answerArea.setText("Neue Antwort");

        // Überprüfen, ob der Speichern-Button aktiviert ist
        assertTrue(saveButton.isEnabled());
    }

    @Test
    void showAnswer() {
        // Testet, ob das Antwortfeld sichtbar wird
        // und der Anzeigen-Button deaktiviert wird
        controller.showAnswer();

        assertTrue(answerArea.isVisible());
        assertFalse(showAnswerButton.isEnabled());
    }

    @Test
    void saveChanges() throws IOException {
        // Vorbereiten der Karte im Dummy-CardManager
        Model.FlashCard card = new Model.FlashCard("Frage", "Antwort");
        cardManager.getFlashCards().add(card);

        questionArea.setText("Aktualisierte Frage");
        answerArea.setText("Aktualisierte Antwort");

        controller.saveChanges();

        // Überprüfen, ob die Karte aktualisiert wurde
        assertEquals("Aktualisierte Frage", cardManager.getFlashCards().get(0).getQuestion());
        assertEquals("Aktualisierte Antwort", cardManager.getFlashCards().get(0).getAnswer());
        assertFalse(saveButton.isEnabled());
    }

    @Test
    void deleteCurrentCard() throws IOException {
        // Vorbereiten der Karte im Dummy-CardManager
        Model.FlashCard card = new Model.FlashCard("Frage", "Antwort");
        cardManager.getFlashCards().add(card);

        controller.deleteCurrentCard();

        // Überprüfen, ob die Karte gelöscht wurde
        assertEquals(0, cardManager.size());
        assertEquals("", questionArea.getText());
        assertEquals("", answerArea.getText());
    }

    @Test
    void loadCardFromList() {
        // Vorbereiten der Karte im Dummy-CardManager
        Model.FlashCard card = new Model.FlashCard("Frage", "Antwort");
        cardManager.getFlashCards().add(card);

        controller.loadCardFromList(0);

        // Überprüfen, ob die Frage und die Antwort in den Textbereichen aktualisiert werden
        assertEquals("Frage", questionArea.getText());
        assertEquals("Antwort", answerArea.getText());
        assertFalse(answerArea.isVisible());
        assertTrue(showAnswerButton.isEnabled());
    }

    @Test
    void updateCardList() {
        // Vorbereiten der Karten im Dummy-CardManager
        cardManager.getFlashCards().add(new Model.FlashCard("Frage 1", "Antwort 1"));
        cardManager.getFlashCards().add(new Model.FlashCard("Frage 2", "Antwort 2"));

        controller.updateCardList();

        // Überprüfen, ob die Kartenliste in der UI korrekt aktualisiert wird
        assertEquals(2, listModel.getSize());
        assertEquals("Frage 1", listModel.get(0).getQuestion());
        assertEquals("Frage 2", listModel.get(1).getQuestion());
        assertEquals("1/2", progressLabel.getText());
    }
}