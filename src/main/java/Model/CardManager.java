package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasse zur Verwaltung von FlashCards. Bietet Funktionen zum Hinzufügen, Löschen,
 * Aktualisieren und Speichern von FlashCards in Dateien.
 * @author Christ Solèr
 * @author Patrick Negri
 * @author Olivia Stefanovic
 */
public class CardManager {
    private List<Model.FlashCard> flashCards; // Liste zur Speicherung der FlashCards

    /**
     * Konstruktor: Initialisiert die Liste für FlashCards.
     */
    public CardManager() {
        flashCards = new ArrayList<>();
    }

    /**
     * Lädt FlashCards aus einer Datei und ersetzt die aktuelle Liste.
     *
     * @param file Die Datei, aus der die Karten geladen werden sollen; darf nicht null sein.
     * @throws IOException Wenn ein Fehler beim Lesen der Datei auftritt.
     * @throws FileNotFoundException Wenn die Datei nicht existiert oder null ist.
     */
    public void loadCardsFromFile(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("Die angegebene Datei existiert nicht: " +
                    (file != null ? file.getAbsolutePath() : "null"));
        }

        flashCards.clear(); // Löscht die aktuelle Liste vor dem Laden
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("/"); // Erwartet Format "Frage/Antwort"
                if (parts.length >= 2) {
                    flashCards.add(new Model.FlashCard(parts[0].trim(), parts[1].trim()));
                } else {
                    System.out.println("Ungültige Zeile übersprungen: " + line);
                }
            }
        } catch (IOException e) {
            throw new IOException("Fehler beim Laden der Datei: " + e.getMessage(), e);
        }
    }

    /**
     * Speichert die aktuelle Liste der FlashCards in eine Datei.
     *
     * @param file Die Datei, in der die Karten gespeichert werden sollen; darf nicht null sein.
     * @throws IOException Wenn ein Fehler beim Schreiben der Datei auftritt.
     */
    public void saveCardsToFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Datei darf nicht null sein.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Model.FlashCard card : flashCards) {
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Fehler beim Speichern der Datei: " + e.getMessage(), e);
        }
    }

    /**
     * Speichert eine gegebene Liste von FlashCards in eine Datei.
     *
     * @param selectedFile  Die Datei, in der die Karten gespeichert werden sollen; darf nicht null sein.
     * @param flashCardList Die Liste der Karten, die gespeichert werden sollen; darf nicht null sein.
     */
    public static void saveFile(File selectedFile, List<Model.FlashCard> flashCardList) {
        if (selectedFile == null || flashCardList == null) {
            System.out.println("Datei oder Kartenliste darf nicht null sein.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile))) {
            for (Model.FlashCard card : flashCardList) {
                writer.write(card.getQuestion() + "/" + card.getAnswer());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gibt die Liste der FlashCards zurück.
     *
     * @return Die Liste der FlashCards.
     */
    public List<Model.FlashCard> getFlashCards() {
        return flashCards;
    }

    /**
     * Fügt eine neue FlashCard zur Liste hinzu.
     *
     * @param card Die Karte, die hinzugefügt werden soll; darf nicht null sein.
     * @throws IllegalArgumentException Wenn die Karte null ist.
     */
    public void addFlashCard(Model.FlashCard card) {
        if (card == null) {
            throw new IllegalArgumentException("FlashCard darf nicht null sein.");
        }
        flashCards.add(card);
    }

    /**
     * Löscht eine FlashCard basierend auf ihrem Index.
     *
     * @param index Der Index der zu löschenden Karte; muss innerhalb des gültigen Bereichs liegen.
     * @throws IndexOutOfBoundsException Wenn der Index ungültig ist.
     */
    public void deleteFlashCard(int index) {
        if (index < 0 || index >= flashCards.size()) {
            throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
        }
        flashCards.remove(index);
    }

    /**
     * Aktualisiert eine FlashCard basierend auf ihrem Index.
     *
     * @param index Der Index der zu aktualisierenden Karte; muss innerhalb des gültigen Bereichs liegen.
     * @param card  Die neue FlashCard, die die alte ersetzen soll; darf nicht null sein.
     * @throws IndexOutOfBoundsException Wenn der Index ungültig ist.
     * @throws IllegalArgumentException  Wenn die Karte null ist.
     */
    public void updateFlashCard(int index, Model.FlashCard card) {
        if (index < 0 || index >= flashCards.size()) {
            throw new IndexOutOfBoundsException("Ungültiger Index: " + index);
        }
        if (card == null) {
            throw new IllegalArgumentException("FlashCard darf nicht null sein.");
        }
        flashCards.set(index, card);
    }

    /**
     * Überprüft, ob die Liste der FlashCards leer ist.
     *
     * @return true, wenn die Liste leer ist; ansonsten false.
     */
    public boolean isEmpty() {
        return flashCards.isEmpty();
    }

    /**
     * Gibt die Anzahl der FlashCards in der Liste zurück.
     *
     * @return Die Anzahl der FlashCards.
     */
    public int size() {
        return flashCards.size();
    }
}
