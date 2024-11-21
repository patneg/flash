package Controller;

import Model.FlashCard;
import View.CardCreatorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


//Controller für die CardCreatorView. Verarbeitet Benutzereingaben und verwaltet die Erstellung der Lernkarten
public class CardCreatorController {
    private CardCreatorView cardCreatorView;
    private ArrayList<FlashCard> flashCardList;

    //Konstruktur für den CardcreatorController mit der View
    //@param cardCreatorView die zu steuernde CardCreatorView; darf nicht null sein
    //@throws IllegalArgumentException, wenn cardCreatorView null ist
    public CardCreatorController(CardCreatorView cardCreatorView) {
        if (cardCreatorView == null) {
            System.out.println("Fehler: CardCreatorView ist null im CardCreatorController!");
            throw new IllegalArgumentException("CardCreatorView darf nicht null sein");
        } else {
            System.out.println("CardCreatorView erfolgreich an CardCreatorController übergeben.");
        }

        this.cardCreatorView = cardCreatorView;
        this.flashCardList = new ArrayList<>();

        // Verbinden der Event-Handler
        this.cardCreatorView.setNextButtonListener(createNextCardListener());
        this.cardCreatorView.setSaveMenuItemListener(createSaveMenuListener());
    }

    //Erstellt einen ActionListener für den "Nächste Karte" Button
    private ActionListener createNextCardListener() {

        return new NextCardListener();
    }

    //Erstellt einen ActionListener für das "Speichern" Menüelement
    private ActionListener createSaveMenuListener() {

        return new SaveMenuListener();
    }

    //innere Klasse für den "Nächste Karte" Listener
    private class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            String questionText = cardCreatorView.getQuestionText();
            String answerText = cardCreatorView.getAnswerText();

            if (!questionText.isEmpty() && !answerText.isEmpty()) {
                FlashCard card = new FlashCard(questionText, answerText);
                flashCardList.add(card);
                cardCreatorView.clearCard();
                System.out.println("Anzahl der Karten: " + flashCardList.size());
            } else {
                System.out.println("Frage und Antwort dürfen nicht leer sein!");
            }
        }
    }


    //innere Klasse für den "Speichern" Menü-Listener
    private class SaveMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            //Einfügen der aktuellen Karte wenn nicht leer
            String questionText = cardCreatorView.getQuestionText();
            String answerText = cardCreatorView.getAnswerText();
            if (!questionText.isEmpty() && !answerText.isEmpty()) {
                FlashCard card = new FlashCard(questionText, answerText);
                flashCardList.add(card);
                cardCreatorView.clearCard();
            }

            if (flashCardList.isEmpty()) {
                System.out.println("Keine Karten zum Speichern vorhanden.");
                return;
            }

            // Öffnen des Datei-Dialogs
            cardCreatorView.promptForFile(flashCardList);
        }
    }
    
    //Unit Tests
    public static void main(String[] args) {
        //Testet, ob eine neue FlashCard zur Liste hinzugefügt wird, wenn die Felder nicht leer sind.
        // Arrange
        CardCreatorView view = new CardCreatorView();
        CardCreatorController controller = new CardCreatorController(view);
        view.getQuestionArea().setText("Was ist 2 + 2?");
        view.getAnswerArea().setText("4");

        // Act
        controller.createNextCardListener().actionPerformed(null);

        // Assert
        assert controller.flashCardList.size() == 1 : "FlashCard sollte zur Liste hinzugefügt werden.";
        FlashCard card = controller.flashCardList.get(0);
        assert card.getQuestion().equals("Was ist 2 + 2?") : "Frage sollte korrekt sein.";
        assert card.getAnswer().equals("4") : "Antwort sollte korrekt sein.";
        System.out.println("Test nextCardListener erfolgreich bestanden.");
    }
}