package Main;
import Controller.CardCreatorController;
import Controller.CardPlayerEditorController;
import Controller.MainMenuController;
import Model.CardManager;
import View.CardCreatorView;
import View.CardPlayerEditorView;
import View.MainMenuView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialisierung des CardManagers
            CardManager cardManager = new CardManager();

            // Erstellen des MainMenuView und MainMenuControllers
            MainMenuView mainMenuView = new MainMenuView();
            new MainMenuController(mainMenuView, cardManager);
        });
    }



    // Methode um den Karten-Ersteller zu öffnen
    public static void openCardCreator() {
        System.out.println("Lernkarten-Ersteller öffnen..."); // Debug
        CardCreatorView cardCreatorView = new CardCreatorView(); // Ensure this line executes correctly
        if (cardCreatorView == null) {
            System.out.println("CardCreatorView is null!"); // Debug if the instance is not created
        } else {
            System.out.println("CardCreatorView erfolgreich initialisiert.");
            new CardCreatorController(cardCreatorView);
        }
    }

    // Methode, um den Editor & Player zu starten
    public static void openCardPlayerEditor(CardManager cardManager) {
        if (cardManager == null) {
            cardManager = new CardManager();
        }

        // Controller erstellen
        CardPlayerEditorController controller = new CardPlayerEditorController(cardManager);

        // Controller der VIew übergeben
        CardPlayerEditorView editorView = new CardPlayerEditorView(cardManager);


        System.out.println("Editor & Player erfolgreich initialisiert.");
    }
}
