package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Klasse für die Verwaltung von Flash-Karten (FlashCards)
public class CardManager {
    private List<Model.FlashCard> flashCards; // Liste zur Speicherung der Flash-Karten

    // Konstruktor: Initialisiert die Liste für Flash-Karten
    public CardManager() {
        flashCards = new ArrayList<>();
    }

    // Methode zum Laden von Karten aus einer Datei
    // @param file Die Datei, aus der Karten geladen werden sollen
    // @throws IOException, wenn ein Fehler beim Lesen der Datei auftritt
    public void loadCardsFromFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            // Wenn die Datei nicht existiert oder null ist, wird eine Ausnahme ausgelöst
            throw new FileNotFoundException("Die angegebene Datei existiert nicht: " + (file != null ? file.getAbsolutePath() : "null"));
        }

        flashCards.clear(); // Löscht alle vorhandenen Karten vor dem Laden
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) { // Datei Zeile für Zeile lesen
                String[] parts = line.split("/"); // Trennt die Frage und die Antwort mit "/"
                if (parts.length >= 2) {
                    // Fügt eine neue FlashCard hinzu, wenn die Zeile korrekt formatiert ist
                    flashCards.add(new Model.FlashCard(parts[0].trim(), parts[1].trim()));
                } else {
                    // Überspringt ungültige Zeilen und protokolliert sie in der Konsole
                    System.out.println("Ungültige Zeile übersprungen: " + line);
                }
            }
        } catch (IOException e) {
            // Wenn ein Fehler beim Lesen auftritt, wird dieser weitergegeben
            throw new IOException("Fehler beim Laden der Datei: " + e.getMessage(), e);
        }
    }

    // Methode zum Speichern der aktuellen Karten in eine Datei
    // @param file Die Datei, in der die Karten gespeichert werden sollen
    // @throws IOException, wenn ein Fehler beim Schreiben der Datei auftritt
    public void saveCardsToFile(File file) throws IOException {
        if (file == null) {
            // Wenn die Datei null ist, wird eine Ausnahme ausgelöst
            throw new IllegalArgumentException("Datei darf nicht null sein.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Model.FlashCard card : flashCards) {
                // Schreibt jede FlashCard im Format "Frage/Antwort" in die Datei
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine(); // Fügt einen Zeilenumbruch hinzu
            }
        } catch (IOException e) {
            // Wenn ein Fehler beim Speichern auftritt, wird dieser weitergegeben
            throw new IOException("Fehler beim Speichern der Datei: " + e.getMessage(), e);
        }
    }

    // Statische Methode zum Speichern von Karten in eine Datei
    // @param selectedFile Die Datei, in der die Karten gespeichert werden sollen
    // @param flashCardList Die Liste der Karten, die gespeichert werden sollen
    public static void saveFile(File selectedFile, List<Model.FlashCard> flashCardList) {
        if (selectedFile == null || flashCardList == null) {
            // Wenn die Datei oder die Kartenliste null ist, wird eine Warnung ausgegeben
            System.out.println("Datei oder Kartenliste darf nicht null sein.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
            for (Model.FlashCard card : flashCardList) {
                // Schreibt jede FlashCard im Format "Frage/Antwort" in die Datei
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine(); // Fügt einen Zeilenumbruch hinzu
            }
        } catch (IOException e) {
            // Protokolliert Fehler beim Speichern
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Gibt die Liste der Flash-Karten zurück
    public List<Model.FlashCard> getFlashCards() {
        return flashCards;
    }

    // Fügt eine neue Flash-Karte hinzu
    // @param card Die Karte, die hinzugefügt werden soll
    public void addFlashCard(Model.FlashCard card) {
        if (card == null) {
            // Wenn die Karte null ist, wird eine Ausnahme ausgelöst
            throw new IllegalArgumentException("FlashCard darf nicht null sein.");
        }
        flashCards.add(card); // Fügt die Karte zur Liste hinzu
    }

    // Löscht eine Karte basierend auf ihrem Index
    // @param index Der Index der zu löschenden Karte
    public void deleteFlashCard(int index) {
        if (index < 0 || index >= flashCards.size()) {
            // Wenn der Index ungültig ist, wird eine Ausnahme ausgelöst
            throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
        }
        flashCards.remove(index); // Entfernt die Karte aus der Liste
    }

    // Aktualisiert eine Karte basierend auf ihrem Index
    // @param index Der Index der zu aktualisierenden Karte
    // @param card Die neue Karte, die die alte ersetzen soll
    public void updateFlashCard(int index, Model.FlashCard card) {
        if (index < 0 || index >= flashCards.size()) {
            // Wenn der Index ungültig ist, wird eine Ausnahme ausgelöst
            throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
        }
        if (card == null) {
            // Wenn die Karte null ist, wird eine Ausnahme ausgelöst
            throw new IllegalArgumentException("FlashCard darf nicht null sein.");
        }
        flashCards.set(index, card); // Aktualisiert die Karte in der Liste
    }

    // Überprüft, ob die Liste der Karten leer ist
    public boolean isEmpty() {
        return flashCards.isEmpty();
    }

    // Gibt die Anzahl der Karten in der Liste zurück
    public int size() {
        return flashCards.size();
    }
}
