package Main;

import Controller.CardCreatorController;
import Controller.CardPlayerEditorController;
import Controller.MainMenuController;
import Model.CardManager;
import View.CardCreatorView;
import View.CardPlayerEditorView;
import View.MainMenuView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;

/**
 * Hauptklasse, die die Anwendung initialisiert und das Hauptmenü, den Karten-Ersteller sowie den Karten-Editor verwaltet.
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

            // Erstellen des MainMenuView und MainMenuControllers
            MainMenuView mainMenuView = new MainMenuView();
            new MainMenuController(mainMenuView, cardManager);
        });
    }

    /**
     * Öffnet den Karten-Ersteller und initialisiert dessen Controller.
     * Falls die View nicht erstellt werden kann, wird eine IllegalStateException geworfen.
     */
    public static void openCardCreator() {
        System.out.println(OPEN_CARD_CREATOR_MESSAGE); // Debug-Ausgabe
        CardCreatorView cardCreatorView = new CardCreatorView();

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
     * @param cardManager Die Instanz des CardManagers. Falls null, wird eine IllegalArgumentException geworfen.
     * @throws IllegalArgumentException Falls der cardManager null ist.
     */
    public static void openCardPlayerEditor(@Nonnull CardManager cardManager) {
        if (cardManager == null) {
            throw new IllegalArgumentException("CardManager darf nicht null sein.");
        }

        // Initialisierung des Controllers und der View
        CardPlayerEditorController controller = new CardPlayerEditorController(cardManager);
        CardPlayerEditorView editorView = new CardPlayerEditorView(cardManager);

        System.out.println(CARD_EDITOR_INITIALIZED);
    }
}
