import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Testklasse für die CardPlayerEditorController-Klasse.
 * Testet verschiedene Funktionen des Controllers, wie das Importieren, Speichern,
 * Löschen und Aktualisieren von Karten sowie die Benutzeroberfläche.
 * @author Christ Solèr
 */
class CardPlayerEditorControllerTest {

    private Model.CardManager cardManager;
    private JTextArea questionArea;
    private JTextArea answerArea;
    private JButton saveButton;
    private JButton showAnswerButton;
    private DefaultListModel<Model.FlashCard> listModel;
    private JList<Model.FlashCard> cardList;
    private JLabel progressLabel;
    private Controller.CardPlayerEditorController controller; // Deklaration der Instanzvariable

    /**
     * Dummy-Implementierung von CardManager für Testzwecke.
     * Simuliert das Verhalten eines echten CardManagers ohne Dateioperationen.
     */
    class DummyCardManager extends Model.CardManager {
        private List<Model.FlashCard> cards = new ArrayList<>();
        private File currentFile;

        @Override
        public void loadCardsFromFile(File file) {
            this.currentFile = file;
            cards.add(new Model.FlashCard("Question 1", "Answer 1")); // Beispielkarte hinzufügen
        }

        @Override
        public void saveCardsToFile(File file) {
            this.currentFile = file; // Simuliertes Speichern
        }

        @Override
        public void updateFlashCard(int index, Model.FlashCard card) {
            cards.set(index, card);
        } // Aktualisiert die Karte an der angegebenen Position

        @Override
        public void deleteFlashCard(int index) {
            cards.remove(index);
        } // Löscht die Karte an der angegebenen Position

        @Override
        public List<Model.FlashCard> getFlashCards() {
            return cards;
        } // Gibt die Liste der Karten zurück

        @Override
        public int size() {
            return cards.size();
        } // Gibt die Anzahl der Karten zurück
    }

    /**
     * Initialisiert die Testumgebung vor jedem Test.
     * Erstellt die benötigten UI-Komponenten und den Controller.
     */
    @BeforeEach
    void setUp() {
        // Initialisierung der Dummy-Implementierung von CardManager
        cardManager = new DummyCardManager();
        // Erstellen der UI-Komponenten
        questionArea = new JTextArea();
        answerArea = new JTextArea();
        saveButton = new JButton();
        showAnswerButton = new JButton();
        listModel = new DefaultListModel<>();
        cardList = new JList<>(listModel);
        progressLabel = new JLabel();

        // Initialisierung des Controllers
        controller = new Controller.CardPlayerEditorController(cardManager, questionArea, answerArea, saveButton, showAnswerButton, listModel, cardList, progressLabel);
    }

    /**
     * Testet das Importieren eines Kartensets.
     *
     * @throws IOException Wenn ein Fehler beim Zugriff auf die Testdatei auftritt.
     */
    @Test
    void importCardSet() throws IOException {
        System.out.println("Teste: Importieren eines Kartensets...");

        // Erstellen einer Testdatei (nur simuliert, hier ohne echten Inhalt)
        File testFile = new File("testfile.txt");

        // Ruft die Import-Funktion des Controllers auf -> Einfach irgendetwas auswählen, test sollte "Passed" sein
        controller.importCardSet(new JFrame(), testFile);

        // Überprüft, ob genau eine Karte importiert wurde
        assertEquals(1, cardManager.size(), "Es sollte genau eine Karte importiert werden.");
        System.out.println("✅ Karte erfolgreich importiert.");

        // Validiert den Inhalt der importierten Karte
        assertEquals("Question 1", cardManager.getFlashCards().get(0).getQuestion(), "Die importierte Frage sollte 'Question 1' sein.");
        System.out.println("✅ Inhalt der importierten Karte ist korrekt.");
    }

    /**
     * Testet das Hinzufügen von Textänderungs-Listenern und die Aktivierung des Speichern-Buttons.
     */
    @Test
    void addTextChangeListeners() {
        System.out.println("Teste: Aktivieren des Speichern-Buttons bei Textänderungen...");

        // Listener hinzufügen, die Textänderungen überwachen
        controller.addTextChangeListeners();

        // Simuliert das Ändern des Inhalts der Frage und Antwort
        questionArea.setText("Neue Frage");
        answerArea.setText("Neue Antwort");

        // Überprüft, ob der Speichern-Button aktiviert wurde
        assertTrue(saveButton.isEnabled(), "Der Speichern-Button sollte nach einer Textänderung aktiviert sein.");
        System.out.println("✅ Speichern-Button wurde aktiviert.");
    }

    /**
     * Testet die Anzeige der Antwort.
     */
    @Test
    void showAnswer() {
        System.out.println("Teste: Anzeige der Antwort...");

        // Ruft die Funktion auf, um die Antwort anzuzeigen
        controller.showAnswer();

        // Überprüft, ob das Antwortfeld sichtbar gemacht wurde
        assertTrue(answerArea.isVisible(), "Das Antwortfeld sollte sichtbar sein.");
        // Überprüft, ob der 'Antwort anzeigen'-Button deaktiviert wurde
        assertFalse(showAnswerButton.isEnabled(), "Der 'Antwort anzeigen'-Button sollte deaktiviert sein.");
        System.out.println("✅ Antwortfeld wird korrekt angezeigt.");
    }

    /**
     * Testet das Speichern von Änderungen.
     *
     * @throws IOException Wenn ein Fehler beim Speichern auftritt.
     */    @Test
    void testSaveChanges() throws IOException {
        System.out.println("Teste: Speichern von Änderungen...");

        // Hinzufügen einer Testkarte
        Model.FlashCard card = new Model.FlashCard("Originale Frage", "Originale Antwort");
        cardManager.getFlashCards().add(card);
        listModel.addElement(card);

        // Markieren der ersten Karte als ausgewählt
        cardList.setSelectedIndex(0);

        // Setzt die aktuelle Datei und den Index
        controller.setCurrentFile(new File("dummyfile.txt"));
        controller.setCurrentCardIndex(0);

        // Simuliert Änderungen an Frage und Antwort
        questionArea.setText("Aktualisierte Frage");
        answerArea.setText("Aktualisierte Antwort");

        // Ruft die Funktion zum Speichern der Änderungen auf
        controller.saveChanges();

        // Überprüft, ob die Änderungen korrekt gespeichert wurden
        assertEquals("Aktualisierte Frage", cardManager.getFlashCards().get(0).getQuestion(), "Die Frage sollte aktualisiert worden sein.");
        assertEquals("Aktualisierte Antwort", cardManager.getFlashCards().get(0).getAnswer(), "Die Antwort sollte aktualisiert worden sein.");
        // Überprüft, ob der Speichern-Button deaktiviert wurde
        assertFalse(saveButton.isEnabled(), "Der Speichern-Button sollte nach dem Speichern deaktiviert sein.");
        System.out.println("✅ Änderungen wurden erfolgreich gespeichert.");
    }

    /**
     * Testet das Laden einer Karte aus der Liste.
     */
    @Test
    void testLoadCardFromList() {
        System.out.println("Teste: Laden einer Karte aus der Liste...");

        // Hinzufügen einer Testkarte
        Model.FlashCard card = new Model.FlashCard("Frage", "Antwort");
        cardManager.getFlashCards().add(card);

        // Ruft die Funktion zum Laden der Karte auf
        controller.loadCardFromList(0);

        // Überprüft, ob die Inhalte korrekt geladen wurden
        assertEquals("Frage", questionArea.getText(), "Die Frage sollte korrekt geladen werden.");
        assertEquals("Antwort", answerArea.getText(), "Die Antwort sollte korrekt geladen werden.");
        // Überprüft, ob das Antwortfeld zunächst nicht sichtbar ist
        assertFalse(answerArea.isVisible(), "Die Antwort sollte zunächst nicht sichtbar sein.");
        // Überprüft, ob der 'Antwort anzeigen'-Button aktiviert ist
        assertTrue(showAnswerButton.isEnabled(), "Der 'Antwort anzeigen'-Button sollte aktiviert sein.");
        System.out.println("✅ Karte wurde erfolgreich geladen.");
    }

    /**
     * Testet das Löschen der aktuellen Karte.
     * Überprüft, ob nach dem Löschen die verbleibende Karte korrekt ist.
     */
    @Test
    void testDeleteCurrentCard() {
        System.out.println("Teste: Löschen der aktuellen Karte...");

        // Hinzufügen von zwei Testkarten
        Model.FlashCard card1 = new Model.FlashCard("Frage 1", "Antwort 1");
        Model.FlashCard card2 = new Model.FlashCard("Frage 2", "Antwort 2");
        cardManager.getFlashCards().add(card1);
        cardManager.getFlashCards().add(card2);

        // Löschen der ersten Karte
        controller.setCurrentCardIndex(0);
        controller.deleteCurrentCard();

        // Überprüfen der Ergebnisse
        assertEquals(1, cardManager.size(), "Es sollte noch eine Karte übrig sein.");
        assertEquals("Frage 2", cardManager.getFlashCards().get(0).getQuestion(), "Die verbliebene Karte sollte 'Frage 2' sein.");
        System.out.println("✅ Karte wurde erfolgreich gelöscht.");
    }

    /**
     * Testet das Aktualisieren der Kartenliste.
     * Überprüft, ob alle Karten korrekt in das Listenmodell geladen werden.
     */
    @Test
    void testUpdateCardList() {
        System.out.println("Teste: Aktualisieren der Kartenliste...");

        // Hinzufügen von zwei Testkarten
        Model.FlashCard card1 = new Model.FlashCard("Frage 1", "Antwort 1");
        Model.FlashCard card2 = new Model.FlashCard("Frage 2", "Antwort 2");
        cardManager.getFlashCards().add(card1);
        cardManager.getFlashCards().add(card2);

        // Aktualisieren der Kartenliste
        controller.updateCardList();

        // Überprüfen der Ergebnisse
        assertEquals(2, listModel.size(), "Die Kartenliste sollte zwei Karten enthalten.");
        assertEquals("Frage 1", listModel.get(0).getQuestion(), "Die erste Karte sollte korrekt geladen werden.");
        assertEquals("Frage 2", listModel.get(1).getQuestion(), "Die zweite Karte sollte korrekt geladen werden.");
        System.out.println("✅ Kartenliste wurde erfolgreich aktualisiert.");
    }

    /**
     * Testet das Zurücksetzen der Benutzeroberfläche.
     * Überprüft, ob alle Felder und Buttons korrekt zurückgesetzt werden.
     */
    @Test
    void testResetUI() {
        System.out.println("Teste: Zurücksetzen der Benutzeroberfläche...");

        // Simulieren einer UI mit Inhalten
        questionArea.setText("Frage");
        answerArea.setText("Antwort");
        progressLabel.setText("1/1");
        saveButton.setEnabled(true);
        showAnswerButton.setEnabled(true);

        // Zurücksetzen der UI
        controller.resetUI();

        // Überprüfen der Ergebnisse
        assertEquals("", questionArea.getText(), "Das Fragefeld sollte leer sein.");
        assertEquals("", answerArea.getText(), "Das Antwortfeld sollte leer sein.");
        assertEquals("0/0", progressLabel.getText(), "Die Fortschrittsanzeige sollte zurückgesetzt sein.");
        assertFalse(saveButton.isEnabled(), "Der Speichern-Button sollte deaktiviert sein.");
        assertFalse(showAnswerButton.isEnabled(), "Der 'Antwort anzeigen'-Button sollte deaktiviert sein.");
        System.out.println("✅ Benutzeroberfläche wurde erfolgreich zurückgesetzt.");
    }

    /**
     * Testet das Aktualisieren der Fortschrittsanzeige.
     * Überprüft, ob die Anzeige korrekt den aktuellen Fortschritt zeigt.
     */
    @Test
    void testUpdateProgressLabel() {
        System.out.println("Teste: Aktualisieren der Fortschrittsanzeige...");

        // Hinzufügen einer Testkarte
        Model.FlashCard card1 = new Model.FlashCard("Frage 1", "Antwort 1");
        cardManager.getFlashCards().add(card1);

        // Setze den aktuellen Index
        controller.setCurrentCardIndex(0);

        // Aktualisiere die Fortschrittsanzeige
        controller.updateProgressLabel();


        // Überprüfe die Fortschrittsanzeige
        assertEquals("1/1", progressLabel.getText(), "Die Fortschrittsanzeige sollte '1/1' anzeigen.");
        System.out.println("✅ Fortschrittsanzeige wurde erfolgreich aktualisiert.");
    }

}
