package Main;

import Controller.CardCreatorController;
import Controller.CardPlayerEditorController;
import Controller.MainMenuController;
import Model.CardManager;
import View.CardCreatorView;
import View.CardPlayerEditorView;
import View.MainMenuView;

import javax.annotation.Nonnull;
import javax.swing.*;

/**
 * Hauptklasse der Anwendung. Verwaltet die Initialisierung des Hauptmenüs, des Karten-Erstellers
 * sowie des Karten-Editors und Players.
 */
public class Main {


    private static final String OPEN_CARD_CREATOR_MESSAGE = "Karten-Ersteller wird geöffnet...";
    private static final String CARD_CREATOR_INITIALIZED = "CardCreatorView erfolgreich initialisiert.";
    private static final String CARD_CREATOR_NULL_MESSAGE = "CardCreatorView ist null!";
    private static final String CARD_EDITOR_INITIALIZED = "Editor & Player erfolgreich initialisiert.";

    /**
     * Einstiegspunkt der Anwendung. Initialisiert das Hauptmenü und den CardManager.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialisierung des CardManagers
            CardManager cardManager = new CardManager();

            // Erstellen und Verknüpfen von View und Controller für das Hauptmenü
            MainMenuView mainMenuView = new MainMenuView();
            new MainMenuController(mainMenuView, cardManager);
        });
    }

    /**
     * Öffnet den Karten-Ersteller und initialisiert dessen Controller.
     * Falls die View nicht erstellt werden kann, wird eine IllegalStateException geworfen.
     *
     * @throws IllegalStateException Falls die CardCreatorView nicht initialisiert werden kann.
     */
    public static void openCardCreator() {
        System.out.println(OPEN_CARD_CREATOR_MESSAGE); // Debug-Ausgabe für die Konsole

        // Initialisierung der CardCreatorView
        CardCreatorView cardCreatorView = new CardCreatorView();

        // Validierung der View-Instanz
        if (cardCreatorView == null) {
            throw new IllegalStateException(CARD_CREATOR_NULL_MESSAGE);
        } else {
            System.out.println(CARD_CREATOR_INITIALIZED);
            new CardCreatorController(cardCreatorView);
        }
    }

    /**
     * Öffnet den Karten-Editor und initialisiert dessen Controller und View.
     *
     * @param cardManager Die Instanz des CardManagers; darf nicht null sein.
     * @throws IllegalArgumentException Falls der cardManager null ist.
     */
    public static void openCardPlayerEditor(@Nonnull CardManager cardManager) {
        if (cardManager == null) {
            throw new IllegalArgumentException("CardManager darf nicht null sein.");
        }

        // Initialisierung des Controllers und der View für den Karten-Editor
        CardPlayerEditorController controller = new CardPlayerEditorController(cardManager);
        CardPlayerEditorView editorView = new CardPlayerEditorView(cardManager);

        System.out.println(CARD_EDITOR_INITIALIZED); // Debug-Ausgabe für die Konsole
    }
}
