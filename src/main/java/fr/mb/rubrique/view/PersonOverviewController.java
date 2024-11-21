package fr.mb.rubrique.view;

import fr.mb.rubrique.MainApp;
import fr.mb.rubrique.bean.DirectoryBean;
import fr.mb.rubrique.model.Person;
import fr.mb.rubrique.utility.AlertHelper;
import fr.mb.rubrique.utility.DateUtility;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controller for the person overview view.
 * This class manages the interaction between the user and the contact list,
 * allowing users to view, search, add, edit, and delete contacts.
 */
public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;
    @FXML
    private TextField textFieldNomRecherche;
    @FXML
    private TextField textFieldPrenomRecherche;
    // Reference to the main application.
    private MainApp mainApp;
    
    private DirectoryBean directoryBean; 
    
    /**Constructor. Automatically called before the FXML file is loaded. */
    public PersonOverviewController() {}

    /**
     * Initializes the controller class.
     * Automatically called after the FXML file has been loaded.
     * Sets up the table columns, selection listeners, and search field listeners.
     */
    @FXML
    private void initialize() {
    	// Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        
        // Clear person details.
        showPersonDetails(null);
        
        // Add listeners for selection and search fields
        initializeListeners();
    }
    
    /**
     * Sets up listeners for the person table and search fields.
     */
    private void initializeListeners() {
        // Listener for table selection
        personTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> showPersonDetails(newValue)
        );

        // Listener for search by name
        textFieldNomRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            if (directoryBean != null) {
                directoryBean.setNamePersonSearched(newValue.trim());
            }
        });

        // Listener for search by surname
        textFieldPrenomRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            if (directoryBean != null) {
                directoryBean.setSurnamePersonSearched(newValue.trim());
            }
        });
    }

    /**
     * Sets the reference to the main application and initializes the data.
     * Populates the table with the sorted contacts from the directory bean.
     *
     * @param mainApp the main application reference
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.directoryBean = mainApp.getDirectoryBean();

        // Add observable list data to the table
        personTable.setItems(directoryBean.getSortedContacts());
    }
    
    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person to display, or null to clear the details
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtility.format(person.getBirthday()));
        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
    
    /**
     * Called when the user clicks on the delete button.
     * Removes the selected person from the table and the underlying data model.
     * If no person is selected, shows a warning alert.
     */
    @FXML
    private void handleDeletePerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) 
            //directoryBean.getContacts().remove(selectedPerson);
        	directoryBean.removeContact(selectedPerson);
        else {
            AlertHelper.showError(
                "No Selection",
                "No Person Selected",
                "Please select a person in the table."
            );
        }
    }
    
    /**
     * Called when the user clicks the new button.
     * Opens a dialog to edit details for a new person.
     * If the user confirms, the person is added to the data model.
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            //mainApp.getPersonData().add(tempPerson);
        	directoryBean.addContact(tempPerson);
        }
        
    }
    
    /**
     * Called when the user clicks the edit button.
     * Opens a dialog to edit details for the selected person.
     * If no person is selected, shows a warning alert.
     * If the user confirms the changes, updates the person details in the view.
     */
    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked)
                //showPersonDetails(selectedPerson);
            	directoryBean.updateContact(selectedPerson);
        } else {
            AlertHelper.showError(
                "No Selection",
                "No Person Selected",
                "Please select a person in the table."
            );
        }
    }
}