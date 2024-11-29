package Controller;

import Model.FlashCard;
import View.CardCreatorView;

import jakarta.annotation.Nonnull; // Alternativ: org.jetbrains.annotations oder jakarta.annotation
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Controller für die CardCreatorView.
 * Verarbeitet Benutzereingaben und verwaltet die Erstellung der Lernkarten.
 */
public class CardCreatorController {

    private static final String ERROR_EMPTY_FIELDS = "Frage und Antwort dürfen nicht leer sein!";
    private static final String ERROR_ADD_CARD = "Fehler beim Hinzufügen der Karte: ";
    private static final String ERROR_NO_CARDS_TO_SAVE = "Keine Karten zum Speichern vorhanden!";
    private static final String ERROR_SAVE_CARDS = "Fehler beim Speichern der Karten: ";

    private CardCreatorView cardCreatorView;
    private final ArrayList<FlashCard> flashCardList;

    /**
     * Konstruktor für den CardCreatorController.
     *
     * @param cardCreatorView Die View, die vom Controller verwaltet wird. Darf nicht null sein.
     */
    public CardCreatorController(@Nonnull CardCreatorView cardCreatorView) {
        if (cardCreatorView == null) {
            throw new IllegalArgumentException("CardCreatorView darf nicht null sein.");
        }
        this.cardCreatorView = cardCreatorView;
        this.flashCardList = new ArrayList<>();
    }

    /**
     * Erstellt einen ActionListener für den "Nächste Karte"-Button.
     *
     * @return Ein ActionListener-Objekt.
     */
    public ActionListener createNextCardListener() {
        return new NextCardListener();
    }

    /**
     * Erstellt einen ActionListener für das "Speichern"-Menüelement.
     *
     * @return Ein ActionListener-Objekt.
     */
    public ActionListener createSaveMenuListener() {
        return new SaveMenuListener();
    }

    /**
     * Setzt die View für den Controller.
     *
     * @param cardCreatorView Die neue View. Darf nicht null sein.
     */
    public void setCardCreatorView(@Nonnull CardCreatorView cardCreatorView) {
        if (cardCreatorView == null) {
            throw new IllegalArgumentException("CardCreatorView darf nicht null sein.");
        }
        this.cardCreatorView = cardCreatorView;
    }

    /**
     * Innere Klasse für den "Nächste Karte"-Listener.
     * Verarbeitet die Eingaben für die nächste Karte.
     */
    private class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                String questionText = cardCreatorView.getQuestionText();
                String answerText = cardCreatorView.getAnswerText();

                if (questionText.isEmpty() || answerText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, ERROR_EMPTY_FIELDS, "Eingabefehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                FlashCard card = new FlashCard(questionText, answerText);
                flashCardList.add(card);
                cardCreatorView.clearCard();

                System.out.println("Anzahl der Karten: " + flashCardList.size());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, ERROR_ADD_CARD + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Innere Klasse für den "Speichern"-Menülistener.
     * Verarbeitet die Eingaben und speichert die Karten in eine Datei.
     */
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
                    JOptionPane.showMessageDialog(null, ERROR_NO_CARDS_TO_SAVE, "Speicherfehler", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Öffnen des Datei-Dialogs
                cardCreatorView.promptForFile(flashCardList);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, ERROR_SAVE_CARDS + e.getMessage(), "Speicherfehler", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
