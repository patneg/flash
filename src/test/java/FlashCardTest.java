import static org.junit.jupiter.api.Assertions.*;

import Model.FlashCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die FlashCard-Klasse.
 * Testet Konstruktor, Getter, Setter und Methoden wie toString, sowie die Validierung von Eingaben.
 * @author Christ Solèr
 */
class FlashCardTest {

    /**
     * Standardkonstruktor für die Testklasse FlashCardTest.
     * Initialisiert die Testumgebung für die FlashCard-Klasse.
     */
    public FlashCardTest() {
        // Standardkonstruktor ohne zusätzliche Initialisierung
    }
    private FlashCard flashCard;

    /**
     * Setzt vor jedem Test eine neue Instanz von FlashCard mit Standardwerten.
     */
    @BeforeEach
    public void setUp() {
        // Initialisierung einer FlashCard-Instanz mit Beispielwerten
        flashCard = new FlashCard("Frage?", "Antwort!");
    }

    /**
     * Testet den Konstruktor und die Getter-Methoden.
     * Überprüft, ob die FlashCard mit den korrekten Werten initialisiert wird.
     */
    @Test
    public void testConstructorAndGetters() {
        // Überprüft die initialisierte Frage
        assertEquals("Frage?", flashCard.getQuestion(), "Frage sollte korrekt initialisiert sein.");
        // Überprüft die initialisierte Antwort
        assertEquals("Antwort!", flashCard.getAnswer(), "Antwort sollte korrekt initialisiert sein.");
    }

    /**
     * Testet die Methode setQuestion.
     * Überprüft, ob die Frage korrekt aktualisiert wird.
     */
    @Test
    public void testSetQuestion() {
        // Setzt eine neue Frage
        flashCard.setQuestion("Neue Frage?");
        // Überprüft, ob die Frage korrekt gesetzt wurde
        assertEquals("Neue Frage?", flashCard.getQuestion(), "Frage sollte korrekt aktualisiert werden.");
    }

    /**
     * Testet die Methode setAnswer.
     * Überprüft, ob die Antwort korrekt aktualisiert wird.
     */
    @Test
    public void testSetAnswer() {
        // Setzt eine neue Antwort
        flashCard.setAnswer("Neue Antwort!");
        // Überprüft, ob die Antwort korrekt gesetzt wurde
        assertEquals("Neue Antwort!", flashCard.getAnswer(), "Antwort sollte korrekt aktualisiert werden.");
    }

    /**
     * Testet die toString-Methode.
     * Überprüft, ob die Ausgabe dem erwarteten Format entspricht.
     */
    @Test
    public void testToString() {
        // Erwarteter Ausgabe-String
        String expected = "Frage: Frage?, Antwort: Antwort!"; // Passe dies an die toString-Implementierung an
        // Überprüft, ob die tatsächliche Ausgabe korrekt ist
        assertEquals(expected, flashCard.toString(), "Die toString-Ausgabe sollte korrekt sein.");
    }

    /**
     * Testet die Validierung der Frage.
     * Überprüft, ob eine IllegalArgumentException geworfen wird, wenn die Frage leer ist.
     */
    @Test
    public void testEmptyQuestion() {
        // Erwartet, dass eine IllegalArgumentException geworfen wird, wenn die Frage leer ist
        Exception exception = assertThrows(IllegalArgumentException.class, () -> flashCard.setQuestion(""));
        // Überprüft, ob die Exception die richtige Fehlermeldung hat
        assertEquals("Die Frage darf nicht null oder leer sein.", exception.getMessage());
    }

    /**
     * Testet die Validierung der Antwort.
     * Überprüft, ob eine IllegalArgumentException geworfen wird, wenn die Antwort leer ist.
     */
    @Test
    public void testEmptyAnswer() {
        // Erwartet, dass eine IllegalArgumentException geworfen wird, wenn die Antwort leer ist
        Exception exception = assertThrows(IllegalArgumentException.class, () -> flashCard.setAnswer(""));
        // Überprüft, ob die Exception die richtige Fehlermeldung hat
        assertEquals("Die Antwort darf nicht null oder leer sein.", exception.getMessage());
    }
}
