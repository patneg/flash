package Model;

/**
 * Repräsentiert eine Lernkarte (FlashCard) mit einer Frage und einer Antwort.
 * Diese Klasse bietet Methoden zum Abrufen und Setzen von Frage und Antwort,
 * sowie eine Validierung der Eingaben.
 * @author Christ Solèr
 * @author Patrick Negri
 * @author Olivia Stefanovic
 */
public class FlashCard {
    private String question; // Die Frage der FlashCard
    private String answer;   // Die Antwort der FlashCard

    /**
     * Konstruktor für die FlashCard-Klasse.
     *
     * @param question Die Frage der FlashCard (darf nicht null oder leer sein).
     * @param answer   Die Antwort der FlashCard (darf nicht null oder leer sein).
     * @throws IllegalArgumentException Wenn die Frage oder Antwort null oder leer ist.
     */
    public FlashCard(String question, String answer) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Frage darf nicht null oder leer sein.");
        }
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Antwort darf nicht null oder leer sein.");
        }
        this.question = question;
        this.answer = answer;
    }

    /**
     * Gibt die Frage der FlashCard zurück.
     *
     * @return Die Frage als String.
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setzt eine neue Frage für die FlashCard.
     *
     * @param question Die neue Frage (darf nicht null oder leer sein).
     * @throws IllegalArgumentException Wenn die Frage null oder leer ist.
     */
    public void setQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Frage darf nicht null oder leer sein.");
        }
        this.question = question;
    }

    /**
     * Gibt die Antwort der FlashCard zurück.
     *
     * @return Die Antwort als String.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Setzt eine neue Antwort für die FlashCard.
     *
     * @param answer Die neue Antwort (darf nicht null oder leer sein).
     * @throws IllegalArgumentException Wenn die Antwort null oder leer ist.
     */
    public void setAnswer(String answer) {
        if (answer == null || answer.trim().isEmpty()) {
            throw new IllegalArgumentException("Die Antwort darf nicht null oder leer sein.");
        }
        this.answer = answer;
    }

    /**
     * Überschreibt die toString()-Methode, um die FlashCard darzustellen.
     *
     * @return Ein String, der die Frage und Antwort der FlashCard repräsentiert.
     */
    @Override
    public String toString() {
        return "Frage: " + question + ", Antwort: " + answer;
    }
}
