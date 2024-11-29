import static org.junit.jupiter.api.Assertions.*;

import Model.FlashCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FlashCardTest {

    private FlashCard flashCard;

    @BeforeEach
    public void setUp() {
        // Initialisierung einer FlashCard-Instanz vor jedem Test
        flashCard = new FlashCard("Frage?", "Antwort!");
    }

    @Test
    public void testConstructorAndGetters() {
        // Test für den Konstruktor und die Getter-Methoden
        assertEquals("Frage?", flashCard.getQuestion(), "Frage sollte korrekt initialisiert sein.");
        assertEquals("Antwort!", flashCard.getAnswer(), "Antwort sollte korrekt initialisiert sein.");
    }

    @Test
    public void testSetQuestion() {
        // Test für das Setzen einer neuen Frage
        flashCard.setQuestion("Neue Frage?");
        assertEquals("Neue Frage?", flashCard.getQuestion(), "Frage sollte korrekt aktualisiert werden.");
    }

    @Test
    public void testSetAnswer() {
        // Test für das Setzen einer neuen Antwort
        flashCard.setAnswer("Neue Antwort!");
        assertEquals("Neue Antwort!", flashCard.getAnswer(), "Antwort sollte korrekt aktualisiert werden.");
    }

    @Test
    public void testToString() {
        // Test der toString-Methode (falls vorhanden)
        String expected = "Frage: Frage?, Antwort: Antwort!"; // Passe dies an die toString-Implementierung an
        assertEquals(expected, flashCard.toString(), "Die toString-Ausgabe sollte korrekt sein.");
    }

    @Test
    public void testEmptyQuestion() {
        // Erwartet, dass eine Exception geworfen wird
        Exception exception = assertThrows(IllegalArgumentException.class, () -> flashCard.setQuestion(""));
        assertEquals("Die Frage darf nicht null oder leer sein.", exception.getMessage());
    }

    @Test
    public void testEmptyAnswer() {
        // Erwartet, dass eine Exception geworfen wird
        Exception exception = assertThrows(IllegalArgumentException.class, () -> flashCard.setAnswer(""));
        assertEquals("Die Antwort darf nicht null oder leer sein.", exception.getMessage());
    }
}