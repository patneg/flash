package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Klasse für die Verwaltung von Karten
public class CardManager {
    private List<Model.FlashCard> flashCards;

    public CardManager() {
        flashCards = new ArrayList<>();
    }

    // Methode, um Karten aus einer Datei zu laden
    public void loadCardsFromFile(File file) throws IOException {
        flashCards.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { // Datei wird Zeile für Zeile gelesen
                String[] parts = line.split("/");
                if (parts.length >= 2) { // Wenn die Zeile erfolgreich in mindestens zwei Teile (parts) aufgeteilt wurde, wird eine neue FlashCard erstellt
                    flashCards.add(new Model.FlashCard(parts[0].trim(), parts[1].trim()));
                }
            }
        }
    }

    // Speichern der aktuellen Karten in eine Datei
    public void saveCardsToFile(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Model.FlashCard card : flashCards) {
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine();
            }
        }
    }

    // Methode für die Speicherung der Karten in einer Datei
    public static void saveFile(File selectedFile, List<Model.FlashCard> flashCardList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
            for (Model.FlashCard card : flashCardList) {
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine();
            }
        } catch (Exception e) {
            System.out.println("Speicherung fehlgeschlagen.");
            e.printStackTrace();
        }
    }

    // Holt die Liste mit Flash-Karten
    public List<Model.FlashCard> getFlashCards() {
        return flashCards;
    }

    // Eine neue Karte hinzufügen
    public void addFlashCard(Model.FlashCard card) {
        flashCards.add(card);
    }

    // Löschen einer Karte im entsprechenden Index
    public void deleteFlashCard(int index) {
        if (index >= 0 && index < flashCards.size()) {
            flashCards.remove(index);
        }
    }

    // Karte aktualisieren im entsprechenden Index
    public void updateFlashCard(int index, Model.FlashCard card) {
        if (index >= 0 && index < flashCards.size()) {
            flashCards.set(index, card);
        }
    }

    // Prüfen, ob die Liste mit Karten leer ist
    public boolean isEmpty() {
        return flashCards.isEmpty();
    }

    // Anzahl der Karten erhalten
    public int size() {
        return flashCards.size();
    }
}

