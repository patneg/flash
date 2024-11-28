package Controller;

import Model.FlashCard;
import View.CardCreatorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


//Controller für die View.CardCreatorView. Verarbeitet Benutzereingaben und verwaltet die Erstellung der Lernkarten
// Controller für die View.CardCreatorView. Verarbeitet Benutzereingaben und verwaltet die Erstellung der Lernkarten
public class CardCreatorController {
    private CardCreatorView cardCreatorView;
    private ArrayList<FlashCard> flashCardList;

    // Erstellt einen ActionListener für den "Nächste Karte"-Button
    public ActionListener createNextCardListener() {
        return new NextCardListener();
    }

    public CardCreatorController(CardCreatorView cardCreatorView) {
        this.flashCardList = new ArrayList<>();
    }

    // Erstellt einen ActionListener für das "Speichern"-Menüelement
    public ActionListener createSaveMenuListener() {
        return new SaveMenuListener();
    }

    // Innere Klasse für den "Nächste Karte"-Listener
    private class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                String questionText = cardCreatorView.getQuestionText();
                String answerText = cardCreatorView.getAnswerText();

                if (questionText.isEmpty() || answerText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Frage und Antwort dürfen nicht leer sein!", "Eingabefehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                FlashCard card = new FlashCard(questionText, answerText);
                flashCardList.add(card);
                cardCreatorView.clearCard();

                System.out.println("Anzahl der Karten: " + flashCardList.size());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Fehler beim Hinzufügen der Karte: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Innere Klasse für den "Speichern"-Menülistener
    private class SaveMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                // Einfügen der aktuellen Karte, falls nicht leer
                String questionText = cardCreatorView.getQuestionText();
                String answerText = cardCreatorView.getAnswerText();

                if (!questionText.isEmpty() && !answerText.isEmpty()) {
                    FlashCard card = new FlashCard(questionText, answerText);
                    flashCardList.add(card);
                    cardCreatorView.clearCard();
                }

                if (flashCardList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Keine Karten zum Speichern vorhanden!", "Speicherfehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Öffnen des Datei-Dialogs
                cardCreatorView.promptForFile(flashCardList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Fehler beim Speichern der Karten: " + e.getMessage(), "Speicherfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void setCardCreatorView(CardCreatorView cardCreatorView) {
        if (cardCreatorView == null) {
            throw new IllegalArgumentException("View.CardCreatorView darf nicht null sein.");
        }
        this.cardCreatorView = cardCreatorView;
    }
}
