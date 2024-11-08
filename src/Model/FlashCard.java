package Model;

//Repräsentiert eine Lernkarte mit einer Frage und ANtwort
public class FlashCard {
    private String question;
    private String answer;

    //Konstruktkur für eine FlashCard
    public FlashCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    //Überschreiben der toString()-Methode, um die Frage anzuzeigen
    @Override
    public String toString() {
        return question;
    }

    //Unit Test

    //Konstruktor erstellt Karte bei gültigen Inputs
    public static void main(String[] args) {
        FlashCard testCard;
        try {
            // Arrange
            String question = "Was ist die Hauptstadt von Deutschland?";
            String answer = "Berlin";

            // Act
            testCard = new FlashCard(question, answer);

            // Assert
            assert testCard.getQuestion().equals(question) : "Frage sollte korrekt gesetzt sein.";
            assert testCard.getAnswer().equals(answer) : "Antwort sollte korrekt gesetzt sein.";
            System.out.println("Test bestanden: Konstruktor erstellt FlashCard mit gültigen Parametern.");
        } catch (Exception e) {
            System.err.println("Test fehlgeschlagen: Konstruktor mit gültigen Parametern hat eine Ausnahme geworfen. " + e.getMessage());
        }

        //Konstruktor wirft Ausnahme, wenn Frage null ist
        try {
            // Act
            new FlashCard(null, "Antwort");
            System.err.println("Test fehlgeschlagen: Konstruktor hat keine Ausnahme geworfen, obwohl Frage null ist.");
        } catch (IllegalArgumentException e) {
            // Assert
            System.out.println("Test bestanden: Konstruktor hat Ausnahme geworfen, als Frage null war.");
        }
    }
}