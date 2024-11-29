package Controller;

import Model.FlashCard;
import View.CardCreatorView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Controller für die CardCreatorView.
 * Verarbeitet Benutzereingaben und verwaltet die Erstellung der Lernkarten.
 */
public class CardCreatorController {
    private final CardCreatorView cardCreatorView; // Die zu steuernde View
    private final ArrayList<FlashCard> flashCardList; // Liste der erstellten Karten

    /**
     * Konstruktor für den CardCreatorController.
     *
     * @param cardCreatorView Die zu steuernde CardCreatorView; darf nicht null sein.
     * @throws IllegalArgumentException Wenn cardCreatorView null ist.
     */
    public CardCreatorController(CardCreatorView cardCreatorView) {
        if (cardCreatorView == null) {
            throw new IllegalArgumentException("CardCreatorView darf nicht null sein.");
        }
        this.cardCreatorView = cardCreatorView;
        this.flashCardList = new ArrayList<>();

        // Event-Handler mit der View verbinden
        this.cardCreatorView.setNextButtonListener(createNextCardListener());
        this.cardCreatorView.setSaveMenuItemListener(createSaveMenuListener());
    }

    /**
     * Erstellt einen ActionListener für den "Nächste Karte"-Button.
     *
     * @return Der ActionListener für den "Nächste Karte"-Button.
     */
    private ActionListener createNextCardListener() {
        return new NextCardListener();
    }

    /**
     * Erstellt einen ActionListener für das "Speichern"-Menüelement.
     *
     * @return Der ActionListener für das "Speichern"-Menüelement.
     */
    private ActionListener createSaveMenuListener() {
        return new SaveMenuListener();
    }

    /**
     * Innere Klasse für den "Nächste Karte"-Listener.
     */
    private class NextCardListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                String questionText = cardCreatorView.getQuestionText();
                String answerText = cardCreatorView.getAnswerText();

                // Validierung: Beide Felder dürfen nicht leer sein
                if (questionText.isEmpty() || answerText.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Frage und Antwort dürfen nicht leer sein!",
                            "Eingabefehler",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Neue Karte erstellen und hinzufügen
                FlashCard card = new FlashCard(questionText, answerText);
                flashCardList.add(card);
                cardCreatorView.clearCard();
                System.out.println("Karte hinzugefügt. Aktuelle Anzahl: " + flashCardList.size());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Fehler beim Hinzufügen der Karte: " + e.getMessage(),
                        "Fehler",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }

    /**
     * Innere Klasse für den "Speichern"-Menü-Listener.
     */
    private class SaveMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                String questionText = cardCreatorView.getQuestionText();
                String answerText = cardCreatorView.getAnswerText();

                // Aktuelle Karte hinzufügen, falls Felder gefüllt
                if (!questionText.isEmpty() && !answerText.isEmpty()) {
                    FlashCard card = new FlashCard(questionText, answerText);
                    flashCardList.add(card);
                    cardCreatorView.clearCard();
                }

                // Überprüfung, ob Karten vorhanden sind
                if (flashCardList.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Keine Karten zum Speichern vorhanden!",
                            "Speicherfehler",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Datei-Dialog zum Speichern öffnen
                cardCreatorView.promptForFile(flashCardList);
                System.out.println("Speichern erfolgreich initiiert.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Fehler beim Speichern der Karten: " + e.getMessage(),
                        "Speicherfehler",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        }
    }
}
