import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CardPlayerEditorControllerTest {

    private Model.CardManager cardManager;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JButton saveButton;
    private JButton showAnswerButton;
    private DefaultListModel<Model.FlashCard> listModel;
    private JList<Model.FlashCard> cardList;
    private JLabel progressLabel;

    // Dummy implementation of CardManager for testing
    class DummyCardManager extends Model.CardManager {
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
        public List<Model.FlashCard> getFlashCards() {
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
        Controller.CardPlayerEditorController controller = new Controller.CardPlayerEditorController(cardManager, questionArea, answerArea, saveButton, showAnswerButton, listModel, cardList, progressLabel);
    }

    @Test
    void importCardSet() throws IOException {
        System.out.println("Teste: Importieren eines Kartensets...");
        File testFile = new File("testfile.txt");
        controller.importCardSet(new JFrame(), testFile);

        assertEquals(1, cardManager.size(), "Es sollte genau eine Karte importiert werden.");
        System.out.println("✅ Karte erfolgreich importiert.");

        assertEquals("Question 1", cardManager.getFlashCards().get(0).getQuestion(), "Die importierte Frage sollte 'Question 1' sein.");
        System.out.println("✅ Inhalt der importierten Karte ist korrekt.");
    }

    @Test
    void addTextChangeListeners() {
        System.out.println("Teste: Aktivieren des Speichern-Buttons bei Textänderungen...");
        controller.addTextChangeListeners();

        questionArea.setText("Neue Frage");
        answerArea.setText("Neue Antwort");

        assertTrue(saveButton.isEnabled(), "Der Speichern-Button sollte nach einer Textänderung aktiviert sein.");
        System.out.println("✅ Speichern-Button wurde aktiviert.");
    }

    @Test
    void showAnswer() {
        System.out.println("Teste: Anzeige der Antwort...");
        controller.showAnswer();

        assertTrue(answerArea.isVisible(), "Das Antwortfeld sollte sichtbar sein.");
        assertFalse(showAnswerButton.isEnabled(), "Der 'Antwort anzeigen'-Button sollte deaktiviert sein.");
        System.out.println("✅ Antwortfeld wird korrekt angezeigt.");
    }

    @Test
    void testSaveChanges() throws IOException {
        System.out.println("Teste: Speichern von Änderungen...");
        Model.FlashCard card = new Model.FlashCard("Originale Frage", "Originale Antwort");
        cardManager.getFlashCards().add(card);
        listModel.addElement(card);
        cardList.setSelectedIndex(0);

        controller.setCurrentFile(new File("dummyfile.txt"));
        controller.setCurrentCardIndex(0);

        questionArea.setText("Aktualisierte Frage");
        answerArea.setText("Aktualisierte Antwort");

        controller.saveChanges();

        assertEquals("Aktualisierte Frage", cardManager.getFlashCards().get(0).getQuestion(), "Die Frage sollte aktualisiert worden sein.");
        assertEquals("Aktualisierte Antwort", cardManager.getFlashCards().get(0).getAnswer(), "Die Antwort sollte aktualisiert worden sein.");
        assertFalse(saveButton.isEnabled(), "Der Speichern-Button sollte nach dem Speichern deaktiviert sein.");
        System.out.println("✅ Änderungen wurden erfolgreich gespeichert.");
    }

    @Test
    void deleteCurrentCard() throws IOException {
        System.out.println("Teste: Löschen der aktuellen Karte...");
        Model.FlashCard card = new Model.FlashCard("Frage", "Antwort");
        cardManager.getFlashCards().add(card);

        controller.deleteCurrentCard();

        assertEquals(0, cardManager.size(), "Nach dem Löschen sollte die Liste leer sein.");
        assertEquals("", questionArea.getText(), "Das Fragefeld sollte geleert sein.");
        assertEquals("", answerArea.getText(), "Das Antwortfeld sollte geleert sein.");
        System.out.println("✅ Karte wurde erfolgreich gelöscht.");
    }

    @Test
    void loadCardFromList() {
        System.out.println("Teste: Laden einer Karte aus der Liste...");
        Model.FlashCard card = new Model.FlashCard("Frage", "Antwort");
        cardManager.getFlashCards().add(card);

        controller.loadCardFromList(0);

        assertEquals("Frage", questionArea.getText(), "Die geladene Frage sollte 'Frage' sein.");
        assertEquals("Antwort", answerArea.getText(), "Die geladene Antwort sollte 'Antwort' sein.");
        assertFalse(answerArea.isVisible(), "Das Antwortfeld sollte nach dem Laden unsichtbar sein.");
        assertTrue(showAnswerButton.isEnabled(), "Der 'Antwort anzeigen'-Button sollte aktiviert sein.");
        System.out.println("✅ Karte wurde erfolgreich geladen.");
    }

    @Test
    void updateCardList() {
        System.out.println("Teste: Aktualisierung der Kartenliste...");
        cardManager.getFlashCards().add(new Model.FlashCard("Frage 1", "Antwort 1"));
        cardManager.getFlashCards().add(new Model.FlashCard("Frage 2", "Antwort 2"));

        controller.updateCardList();

        assertEquals(2, listModel.getSize(), "Die Liste sollte zwei Karten enthalten.");
        assertEquals("Frage 1", listModel.get(0).getQuestion(), "Die erste Frage sollte 'Frage 1' sein.");
        assertEquals("Frage 2", listModel.get(1).getQuestion(), "Die zweite Frage sollte 'Frage 2' sein.");
        assertEquals("1/2", progressLabel.getText(), "Die Fortschrittsanzeige sollte '1/2' sein.");
        System.out.println("✅ Kartenliste wurde erfolgreich aktualisiert.");
    }
}
