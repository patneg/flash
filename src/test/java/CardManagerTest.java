import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import Model.FlashCard; // Import der FlashCard-Klasse aus dem Model-Paket

/**
 * Testklasse für die CardManager-Klasse. Testet das Laden, Speichern, Hinzufügen,
 * Löschen, Aktualisieren und andere grundlegende Funktionen der Kartenverwaltung.
 * @author Christ Solèr
 * @author Patrick Negri
 * @author Olivia Stefanovic
 */
class CardManagerTest {
    private Model.CardManager cardManager;

    /**
     * Setzt vor jedem Test eine neue Instanz von CardManager.
     */
    @BeforeEach
    void setUp() {
        cardManager = new Model.CardManager();
    }

    /**
     * Testet das Laden von Karten aus einer Datei.
     *
     * @throws IOException Wenn ein Fehler beim Erstellen oder Lesen der Datei auftritt.
     */
    @Test
    void loadCardsFromFile() throws IOException {
        Path tempFile = Files.createTempFile("testCards", ".txt");
        Files.write(tempFile, List.of("Frage1/Antwort1", "Frage2/Antwort2"));

        // Sicherstellen, dass die Karteiliste anfangs leer ist
        assertTrue(cardManager.isEmpty(), "Die Karteiliste sollte zu Beginn leer sein.");
        System.out.println("✅ Vorher: Karteiliste ist leer.");

        cardManager.loadCardsFromFile(tempFile.toFile());

        // Überprüfen der geladenen Kartenanzahl und Inhalte
        assertEquals(2, cardManager.size(), "Die Karteiliste sollte jetzt zwei Karten enthalten.");
        System.out.println("✅ Nachher: Zwei Karten wurden geladen.");

        assertEquals("Frage1", cardManager.getFlashCards().get(0).getQuestion(), "Die erste Frage sollte 'Frage1' sein.");
        assertEquals("Antwort1", cardManager.getFlashCards().get(0).getAnswer(), "Die erste Antwort sollte 'Antwort1' sein.");
        System.out.println("✅ Inhalte der Karten wurden korrekt geladen.");

        Files.delete(tempFile); // Temporäre Datei löschen
    }

    /**
     * Testet das Speichern von Karten in eine Datei.
     *
     * @throws IOException Wenn ein Fehler beim Speichern oder Lesen der Datei auftritt.
     */
    @Test
    void saveCardsToFile() throws IOException {
        cardManager.addFlashCard(new FlashCard("Frage1", "Antwort1"));

        Path tempFile = Path.of("testSaveCards_" + System.currentTimeMillis() + ".txt");

        // Sicherstellen, dass die Datei vor dem Speichern nicht existiert
        assertFalse(Files.exists(tempFile), "Die Datei sollte vor dem Speichern nicht existieren.");
        System.out.println("✅ Datei existiert nicht vor dem Speichern.");

        cardManager.saveCardsToFile(tempFile.toFile());

        // Überprüfen, ob die Datei existiert und die Inhalte korrekt sind
        assertTrue(Files.exists(tempFile), "Die Datei sollte nach dem Speichern existieren.");
        System.out.println("✅ Datei wurde erfolgreich gespeichert.");

        List<String> lines = Files.readAllLines(tempFile);
        assertEquals(1, lines.size(), "Die Datei sollte genau eine Zeile enthalten.");
        assertEquals("Frage1/Antwort1", lines.get(0), "Die Zeile in der Datei sollte 'Frage1/Antwort1' sein.");
        System.out.println("✅ Inhalte der Datei sind korrekt.");

        Files.delete(tempFile); // Temporäre Datei löschen
    }

    /**
     * Testet das Hinzufügen einer FlashCard.
     */
    @Test
    void addFlashCard() {
        // Sicherstellen, dass die Karteiliste anfangs leer ist
        assertEquals(0, cardManager.size(), "Die Karteiliste sollte zu Beginn leer sein.");
        System.out.println("✅ Vorher: Karteiliste ist leer.");

        cardManager.addFlashCard(new FlashCard("Frage1", "Antwort1"));

        // Überprüfen, ob die Karte erfolgreich hinzugefügt wurde
        assertEquals(1, cardManager.size(), "Die Karteiliste sollte jetzt eine Karte enthalten.");
        System.out.println("✅ Karte wurde erfolgreich hinzugefügt.");
    }

    /**
     * Testet das Löschen einer FlashCard.
     */
    @Test
    void deleteFlashCard() {
        cardManager.addFlashCard(new FlashCard("Frage1", "Antwort1"));
        assertEquals(1, cardManager.size(), "Die Karteiliste sollte eine Karte enthalten, bevor sie gelöscht wird.");
        System.out.println("✅ Karte wurde hinzugefügt.");

        cardManager.deleteFlashCard(0);

        // Überprüfen, ob die Liste nach dem Löschen leer ist
        assertTrue(cardManager.isEmpty(), "Die Karteiliste sollte nach dem Löschen der Karte leer sein.");
        System.out.println("✅ Karte wurde erfolgreich gelöscht.");
    }

    /**
     * Testet das Aktualisieren einer FlashCard.
     */
    @Test
    void updateFlashCard() {
        cardManager.addFlashCard(new FlashCard("Frage1", "Antwort1"));
        assertEquals("Frage1", cardManager.getFlashCards().get(0).getQuestion(), "Die erste Frage sollte 'Frage1' sein.");
        System.out.println("✅ Initiale Karte wurde hinzugefügt.");

        cardManager.updateFlashCard(0, new FlashCard("Frage2", "Antwort2"));

        FlashCard updatedCard = cardManager.getFlashCards().get(0);
        assertEquals("Frage2", updatedCard.getQuestion(), "Die erste Frage sollte jetzt 'Frage2' sein.");
        assertEquals("Antwort2", updatedCard.getAnswer(), "Die erste Antwort sollte jetzt 'Antwort2' sein.");
        System.out.println("✅ Karte wurde erfolgreich aktualisiert.");
    }

    /**
     * Testet, ob die Karteiliste leer ist.
     */
    @Test
    void isEmpty() {
        // Sicherstellen, dass die Liste anfangs leer ist
        assertTrue(cardManager.isEmpty(), "Die Karteiliste sollte zu Beginn leer sein.");
        System.out.println("✅ Karteiliste ist anfangs leer.");

        cardManager.addFlashCard(new FlashCard("Frage1", "Antwort1"));

        // Überprüfen, ob die Liste nach dem Hinzufügen nicht mehr leer ist
        assertFalse(cardManager.isEmpty(), "Die Karteiliste sollte nicht mehr leer sein.");
        System.out.println("✅ Karteiliste ist nach Hinzufügen nicht leer.");
    }

    /**
     * Testet die Größe der Karteiliste.
     */
    @Test
    void size() {
        // Sicherstellen, dass die Liste anfangs Größe 0 hat
        assertEquals(0, cardManager.size(), "Die Karteiliste sollte zu Beginn leer sein.");
        System.out.println("✅ Karteiliste hat anfangs Größe 0.");

        cardManager.addFlashCard(new FlashCard("Frage1", "Antwort1"));

        // Überprüfen, ob die Größe der Liste korrekt ist
        assertEquals(1, cardManager.size(), "Die Karteiliste sollte jetzt eine Karte enthalten.");
        System.out.println("✅ Karteiliste hat jetzt Größe 1.");
    }
}
