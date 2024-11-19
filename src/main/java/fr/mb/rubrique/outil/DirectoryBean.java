package fr.mb.rubrique.outil;

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
    	this.file = file;
        this.contactDAO = new ContactDAO(file);

        // Retrieve all contacts and initialize the ObservableList
        List<Person> allContacts = contactDAO.getAllContacts();
        this.contacts = FXCollections.observableArrayList(allContacts);
        contacts.addListener((ListChangeListener<Person>) change -> saved = false);
        
        // Initially mark as saved since it's just been loaded from file
        this.saved = true;
        
        this.filteredContacts = new FilteredList<>(contacts, p -> true); // Predicate nullo, tutti i contatti
        this.sortedContacts = new SortedList<>(filteredContacts); // Lista ordinata basata sulla lista filtrata
        this.personSearched = new Person(); // Persona per i criteri di ricerca
    }
    
    public DirectoryBean() {
        this.contacts = FXCollections.observableArrayList();
        this.filteredContacts = new FilteredList<>(contacts, p -> true);
        this.sortedContacts = new SortedList<>(filteredContacts);
        this.personSearched = new Person();
        this.contacts.addListener((ListChangeListener<Person>) change -> saved = false);
        this.saved = false;
    }
    
    public ObservableList<Person> getSortedContacts() {
        return sortedContacts; // Ritorna la lista ordinata e filtrata
    }

    public void setNamePersonSearched(String name) {
        personSearched.setFirstName(name.toUpperCase());
        filtraContatti(); // Applica il filtro
    }

    public void setSurnamePersonSearched(String surname) {
        personSearched.setLastName(surname.toUpperCase());
        filtraContatti(); // Applica il filtro
    }

    private void filtraContatti() {
    	
        filteredContacts.setPredicate(person -> {
            boolean matchName = true;
            boolean matchSurname = true;

            if (personSearched.getFirstName() != null && !personSearched.getFirstName().isEmpty()) {
                matchName = person.getFirstName().toUpperCase().contains(personSearched.getFirstName());
            }
            if (personSearched.getLastName() != null && !personSearched.getLastName().isEmpty()) {
                matchSurname = person.getLastName().toUpperCase().contains(personSearched.getLastName());
            }
            
            
            return matchName && matchSurname;
        });
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
    
    public void setPersonSelected(Person person) {
        this.personSelected = person;
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
	 * @param saved the saved to set
	 */
	public void setSaved(boolean saved) {
		this.saved = saved;
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
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	public void setFile(File selectedFile) {
        if (selectedFile != null) {
            this.file = selectedFile;
            this.contactDAO = new ContactDAO(selectedFile);
        }
    }

    /**
     * Saves all contacts to the file.
     */
    public void save() {
    	if (file != null && contactDAO != null) {
            contactDAO.saveContacts(contacts);
            saved = true;
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
    
    public void reload() {
        List<Person> allContacts = contactDAO.getAllContacts();
        contacts.setAll(allContacts);
        saved = true;
    }

}