package Main;

import Controller.CardCreatorController;
import View.CardCreatorView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialisierung der CardCreatorView und des zugehörigen Controllers
            CardCreatorView cardCreatorView = new CardCreatorView();
            if (cardCreatorView == null) {
                System.out.println("CardCreatorView ist null!");
            } else {
                System.out.println("CardCreatorView erfolgreich initialisiert.");
                new CardCreatorController(cardCreatorView);
            }
        });
    }
}