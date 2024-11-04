package fr.mb.rubrique.outil;

import java.io.File;
import java.util.List;
import fr.mb.rubrique.dao.ContactDAO;
import fr.mb.rubrique.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DirectoryBean {

	private final File file;
    private final ObservableList<Person> contacts;
    private final ContactDAO contactDAO;
    private Person personSelected;
    private boolean saved;

    /**
     * Constructor that initializes the contact list and DAO.
     *
     * @param file the file to use for reading and writing contacts
     */
    public DirectoryBean(File file) {
    	this.file = file;
        this.contactDAO = new ContactDAO(file);

        // Retrieve all contacts and initialize the ObservableList
        List<Person> allContacts = contactDAO.getAllContacts();
        this.contacts = FXCollections.observableArrayList(allContacts);
        
        // Initially mark as saved since it's just been loaded from file
        this.saved = true;
    }

    /**
     * Returns the list of all contacts.
     *
     * @return an ObservableList of Person objects
     */
    public ObservableList<Person> getContacts() {
        return contacts;
    }
    
    /**
     * Returns the selected person in the directory.
     *
     * @return the selected Person object
     */
    public Person getPersonSelected() {
        return personSelected;
    }
    
    /**
     * Checks if changes are saved.
     *
     * @return true if saved, false otherwise
     */
    public boolean isSaved() {
        return saved;
    }

    /**
     * Returns the name of the file associated with this directory.
     *
     * @return the name of the file, or null if no file is associated
     */
    public String getFileName() {
        return file != null ? file.getName() : null;
    }

    /**
     * Saves all contacts to the file.
     */
    public void save() {
        contactDAO.saveContacts(contacts);
        this.saved = true;
    }

    /**
     * Adds a new contact to the list if it does not already exist.
     *
     * @param person the Person object to add
     */
    public void addContact(Person person) {
        if (!contacts.contains(person)) {
            contacts.add(person);
            this.saved = false;
        }
    }
    
    public void updateContact(Person updatedPerson) {
        if (personSelected != null) {
            int index = contacts.indexOf(personSelected);
            if (index != -1) {
                contacts.set(index, updatedPerson);
                this.saved = false;
            }
        }
    }
   
    /**
     * Removes a contact from the list.
     *
     * @param person the Person object to remove
     */
    public void removeContact(Person person) {
        contacts.remove(person);
        this.saved = false;
    }
}