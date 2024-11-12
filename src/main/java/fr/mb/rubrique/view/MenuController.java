package fr.mb.rubrique.view;

import java.io.File;
import java.util.List;
import java.util.Optional;
import fr.mb.rubrique.MainApp;
import fr.mb.rubrique.outil.DirectoryBean;
import fr.mb.rubrique.outil.ParameterBean;
import fr.mb.rubrique.outil.TitleUpdater;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuController {

    private MainApp mainApp;
    private ParameterBean parameterBean;

    @FXML
    private MenuItem newMenuItem;
    
    @FXML
    private MenuItem openMenuItem;
    
    @FXML
    private MenuItem saveMenuItem;
    
    @FXML
    private MenuItem saveAsMenuItem;
    
    @FXML
    private MenuItem closeMenuItem;
    
    @FXML
    private MenuItem exitMenuItem;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Disable save option initially if no file is open
        //saveMenuItem.setDisable(true);
        parameterBean = new ParameterBean();

        // Initialize other menu items or settings
        updateRecentFilesMenu();
    }

    /**
     * Sets the reference to the main application.
     *
     * @param mainApp the main application reference
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeLayout();
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
            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), mainApp.getDirectoryBean());
        });

        // Set up the initial layout with no content in the center
        mainApp.getRootLayout().setCenter(null);
    }

    /**
     * Prompts the user to save changes if there are unsaved modifications.
     *
     * @return true if the user chooses to save, false otherwise
     */
    private Boolean promptToSave() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
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
            return null;
        }
        return null;
    }
    
    private FileChooser chooseFile(String title, String initialDirectoryPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Contact Files (*.contact)", "*.contact"),
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt")
        );
        
        if (initialDirectoryPath != null && !initialDirectoryPath.isEmpty()) {
            File initialDirectory = new File(initialDirectoryPath);
            if (initialDirectory.exists()) {
                fileChooser.setInitialDirectory(initialDirectory);
            }
        }
        return fileChooser;
    }
    
    @FXML
    private void handleNew() {
        if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
            boolean shouldSave = promptToSave();
            if (shouldSave) {
                mainApp.getDirectoryBean().save();
            }
        }
        mainApp.setDirectoryBean(new DirectoryBean());
        mainApp.showPersonOverview();
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), mainApp.getDirectoryBean());
    }
    
    @FXML
    private void handleOpen() {
    	if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
            Boolean saveResponse = promptToSave();
            if (saveResponse == null) 
                return;
            else if (saveResponse)
                mainApp.getDirectoryBean().save();
        }
            
        FileChooser fileChooser = chooseFile("Open Contact File", parameterBean.getLastDirectory());
        File selectedFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (selectedFile != null) {
            mainApp.setDirectoryBean(new DirectoryBean(selectedFile));
            mainApp.setPersonData(mainApp.getDirectoryBean().getContacts());
            mainApp.showPersonOverview();
            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), mainApp.getDirectoryBean());
            parameterBean.setLastDirectory(selectedFile.getParent());
        }
    }
    
    @FXML
    private void handleSave() {
        boolean save = save();
    }
    
    @FXML
    private void handleSaveAs() {
        boolean saveAs = saveAs();
    }
    
    private boolean save() {
        if (mainApp.getDirectoryBean() != null) {
            if (mainApp.getDirectoryBean().getFileName() == null) {
                return saveAs();
            } else {
                mainApp.getDirectoryBean().save();
                TitleUpdater.updateTitle(mainApp.getPrimaryStage(), mainApp.getDirectoryBean());
                return true;
            }
        }
        return false;
    }
    
    private boolean saveAs() {
        FileChooser fileChooser = chooseFile("Save Contact File As", System.getProperty("user.home"));
        File selectedFile = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (selectedFile != null) {            
            mainApp.getDirectoryBean().setFile(selectedFile);
            mainApp.getDirectoryBean().save();
            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), mainApp.getDirectoryBean());
            parameterBean.setLastDirectory(selectedFile.getParent());
            return true;
        }
        else
            return false;         
    }

    @FXML
    private void handleClose() {
        if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
            Boolean shouldSave = promptToSave();

            if (shouldSave == null)
                return;
            else if (shouldSave) { 
                if (!save())
                    return;
            }
        }
        mainApp.setDirectoryBean(null);
        mainApp.getRootLayout().setCenter(null);
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), null);
    }

    @FXML
    private void handleExit() {
        if (mainApp.getDirectoryBean() != null && !mainApp.getDirectoryBean().isSaved()) {
            Boolean shouldSave = promptToSave();

            if (shouldSave == null) {
                return;
            } else if (shouldSave) {
                if (!save()) {
                    return;
                }
            }
            Platform.exit();
        }
        Platform.exit();
    }
    private void updateRecentFilesMenu() {
        List<String> recentFiles = parameterBean.getRecentFiles();
        // Codice per aggiornare la sezione "Recent Files" del menu
        // Ad esempio: crea dei MenuItem per ogni file recente e aggiungili al menu
    }
}