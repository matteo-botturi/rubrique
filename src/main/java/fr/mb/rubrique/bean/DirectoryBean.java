package fr.mb.rubrique.bean;

import java.io.File;
import java.util.List;
import fr.mb.rubrique.dao.ContactDAO;
import fr.mb.rubrique.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class DirectoryBean {
	
	private final ObservableList<Person> contacts;
	private FilteredList<Person> filteredContacts;
	private SortedList<Person> sortedContacts;
	private File file;
    private ContactDAO contactDAO;
    private Person personSelected;
    private Person personSearched;
    private boolean saved;

    /**
     * Constructor that initializes the contact list and DAO.
     *
     * @param file the file to use for reading and writing contacts
     */
    public DirectoryBean(File file) {
        this.contacts = FXCollections.observableArrayList();
        initialize(file);
    }
    
    /** Default constructor for an empty directory. */
    public DirectoryBean() {
        this.contacts = FXCollections.observableArrayList();
        initialize(null);
    }
    
    /**
     * Initializes the directory bean with shared logic for both constructors.
     *
     * @param file the file to use, or null for an empty directory
     */
    private void initialize(File file) {
        this.file = file;
        this.contactDAO = file != null ? new ContactDAO(file) : null;

        if (contactDAO != null) {
            List<Person> allContacts = contactDAO.getAllContacts();
            contacts.setAll(allContacts);
        }

        this.filteredContacts = new FilteredList<>(contacts, p -> true);
        this.sortedContacts = new SortedList<>(filteredContacts);
        this.personSearched = new Person();
        this.saved = (file != null);

        // Mark as unsaved when the contact list changes
        contacts.addListener((ListChangeListener<Person>) change -> saved = false);
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
     * Returns the sorted list of contacts.
     *
     * @return an ObservableList of sorted Person objects
     */
    public ObservableList<Person> getSortedContacts() {
        return sortedContacts;
    }
    
    /** @return the file */
	public File getFile() {
		return file;
	}
	
	 /**
     * Sets a new file and updates the DAO.
     *
     * @param selectedFile the new file to associate with this directory
     */
    public void setFile(File selectedFile) {
        if (selectedFile != null) {
            this.file = selectedFile;
            this.contactDAO = new ContactDAO(selectedFile);
        }
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
     * Returns the selected person in the directory.
     *
     * @return the selected Person object
     */
    public Person getPersonSelected() {
        return personSelected;
    }
    
    /** @param personSelected the personSelected to set */
	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}

    /**
     * Sets the search criteria for the first name and filters the contacts.
     *
     * @param name the name to search for
     */
    public void setNamePersonSearched(String name) {
        personSearched.setFirstName(name != null ? name.toUpperCase() : null);
        filterContacts();
    }

    /**
     * Sets the search criteria for the last name and filters the contacts.
     *
     * @param surname the surname to search for
     */
    public void setSurnamePersonSearched(String surname) {
        personSearched.setLastName(surname != null ? surname.toUpperCase() : null);
        filterContacts();
    }

    /** Filters the contacts based on the search criteria. */
    private void filterContacts() {
        filteredContacts.setPredicate(person -> {
            if (person == null) return false;

            boolean matchName = personSearched.getFirstName() == null ||
                                person.getFirstName().toUpperCase().contains(personSearched.getFirstName());
            boolean matchSurname = personSearched.getLastName() == null ||
                                   person.getLastName().toUpperCase().contains(personSearched.getLastName());

            return matchName && matchSurname;
        });
    }

	/**
     * Checks if changes are saved.
     *
     * @return true if saved, false otherwise
     */
    public boolean isSaved() {
        return saved;
    }

    /** @param saved the saved to set */
	public void setSaved(boolean saved) {
		this.saved = saved;
	}

    /** Saves all contacts to the associated file. */
    public void save() {
        if (file != null && contactDAO != null) {
            contactDAO.saveContacts(contacts);
            setSaved(true);
        }
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
    
    /**
     * Update an existing contact to the list.
     *
     * @param person the Person object to update
     */
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
    
    /** Reloads contacts from the last save point*/
    public void reload() {
        List<Person> allContacts = contactDAO.getAllContacts();
        contacts.setAll(allContacts);
        saved = true;
    }
}