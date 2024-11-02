package fr.mb.rubrique.dao;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import fr.mb.rubrique.model.Person;
import fr.mb.rubrique.outil.DateOutil;
import javafx.collections.ObservableList;

public class ContactDAO {

    private final FileContact fileContact;

    /**
     * Constructor that takes a file to manage contact data.
     *
     * @param file the file to read from or write to
     */
    public ContactDAO(File file) {
        this.fileContact = new FileContact(file);
    }

    /**
     * Retrieves a list of contacts from the file.
     *
     * @return a list of Person objects
     */
    public List<Person> getAllContacts() {
        List<String> lines = fileContact.read();  // Reads lines from the file
        List<Person> contacts = new ArrayList<>();

        for (String line : lines) {
            contacts.add(stringToPerson(line));
        }
        return contacts;
    }

    /**
     * Saves a list of contacts to the file.
     *
     * @param contacts the list of contacts to save
     */
    public void saveContacts(ObservableList<Person> contacts) {
        List<String> lines = new ArrayList<>();
        for (Person person : contacts) {
            lines.add(toCSV(person));  // Converts each Person to a CSV string
        }
        fileContact.write(lines);
    }

    /**
     * Converts a string to a Person object.
     *
     * @param line the string to convert
     * @return a Person object
     */
    private Person stringToPerson(String line) {
        List<String> parsed = List.of(line.split("\\|"));  // Splits the string by the "|" delimiter
        
        // Assigns values while handling the possibility of missing fields
        String firstName = parsed.get(0);
        String lastName = parsed.size() > 1 ? parsed.get(1) : "";
        String birthday = parsed.size() > 2 ? parsed.get(2) : "";

        // Converts the birth date from string to LocalDate if present
        LocalDate birthDate = birthday.isEmpty() ? null : DateOutil.parse(birthday);
        
        return new Person(firstName, lastName, birthDate);
    }

    /**
     * Converts a Person object to a CSV-formatted string.
     *
     * @param person the Person object to convert
     * @return a CSV string representing the Person object
     */
    private String toCSV(Person person) {
        return person.getFirstName() + "|" + person.getLastName() + "|" + DateOutil.format(person.getBirthday());
    }
}