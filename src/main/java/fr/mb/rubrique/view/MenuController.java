package fr.mb.rubrique.view;

import java.io.File;
import java.util.Optional;
import fr.mb.rubrique.MainApp;
import fr.mb.rubrique.model.Person;
import fr.mb.rubrique.outil.DirectoryBean;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MenuController {

    private MainApp mainApp;

    @FXML
    private MenuItem saveMenuItem;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Disable save option initially if no file is open
        saveMenuItem.setDisable(true);

        // Initialize other menu items or settings
        initializeLayout();
    }

    /**
     * Sets the reference to the main application.
     *
     * @param mainApp the main application reference
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes layout and actions for closing the application.
     */
    private void initializeLayout() {
        // Get the primary stage from mainApp and set the onCloseRequest action
        Stage primaryStage = mainApp.getPrimaryStage();
        primaryStage.setOnCloseRequest(event -> {
            if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
                // Prompt the user to save changes
                boolean shouldSave = promptToSave();
                if (shouldSave)
                    mainApp.getDirectoryBean().save();
            }
            actualizeTitle();
        });

        // Set up the initial layout with no content in the center
        mainApp.getRootLayout().setCenter(null);
    }

    /**
     * Prompts the user to save changes if there are unsaved modifications.
     *
     * @return true if the user chooses to save, false otherwise
     */
    private boolean promptToSave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("You have unsaved changes.");
        alert.setContentText("Do you want to save your changes before proceeding?");
        
        ButtonType saveButton = new ButtonType("Save");
        ButtonType discardButton = new ButtonType("Don't Save");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        
        alert.getButtonTypes().setAll(saveButton, discardButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == saveButton)
                return true;
            else if (result.get() == discardButton)
                return false;
        }
        return false;
    }

    /**
     * Updates the window title by appending the file name, if applicable.
     */
    private void actualizeTitle() {
        String title = mainApp.getPrimaryStage().getTitle();
        

        if (mainApp.getDirectoryBean() != null) {
            String fileName = mainApp.getDirectoryBean().getFileName();
        
            if (!title.contains(fileName))
                title += " - " + fileName;
        } 
        mainApp.getPrimaryStage().setTitle(title); 
    }
    
    private File chooseFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Contact File");
        
        FileChooser.ExtensionFilter contactFilter = new FileChooser.ExtensionFilter("Contact Files (*.contact)", "*.contact");
        FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(contactFilter, textFilter);
        
        return fileChooser.showOpenDialog(primaryStage);
    }
    
    @FXML
    private void openFile() {
        if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
            boolean shouldSave = promptToSave();
            if (shouldSave)
                mainApp.getDirectoryBean().save();
        }

        File selectedFile = chooseFile(mainApp.getPrimaryStage());
        if (selectedFile != null) {
            mainApp.setDirectoryBean(selectedFile);
            
            actualizeTitle(); 
        }
    }
    
    @FXML
    private void closeFile() {
        DirectoryBean directoryBean = mainApp.getDirectoryBean();
        if (directoryBean != null && !directoryBean.isSaved()) {
            boolean shouldSave = promptToSave();
            if (shouldSave)
                directoryBean.save();
        }
        mainApp.setDirectoryBean(null); 
        mainApp.getRootLayout().setCenter(null); 
        actualizeTitle();
    }
    
    @FXML
    private void save() {
        DirectoryBean directoryBean = mainApp.getDirectoryBean();
        if (directoryBean != null && !directoryBean.isSaved())
            directoryBean.save();
    }
    
    @FXML
    private void newFile() {
        if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
            boolean shouldSave = promptToSave();
            if (shouldSave)
                mainApp.getDirectoryBean().save();
        }
        mainApp.setDirectoryBean(new File("")); 
        mainApp.getDirectoryBean().getContacts().clear(); 
        actualizeTitle();
    }
    
    @FXML
    private void ajouter() {
        Person newPerson = new Person(); 
        boolean okClicked = mainApp.showPersonEditDialog(newPerson);
        if (okClicked)
            mainApp.getDirectoryBean().addContact(newPerson);
    }
    
    @FXML
    private void modifier() {
        Person selectedPerson = mainApp.getDirectoryBean().getPersonSelected();
        
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            
            if (okClicked)
            	mainApp.getDirectoryBean().updateContact(selectedPerson);
        } else { 
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Contact Selected");
            alert.setContentText("Please select a contact in the table.");
            alert.showAndWait();
        }
    }
}