package fr.mb.rubrique.view;

import java.io.File;
import java.util.List;
import java.util.Optional;
import fr.mb.rubrique.MainApp;
import fr.mb.rubrique.bean.DirectoryBean;
import fr.mb.rubrique.bean.ParameterBean;
import fr.mb.rubrique.utility.AlertHelper;
import fr.mb.rubrique.utility.ClockManager;
import fr.mb.rubrique.utility.FileChooserHelper;
import fr.mb.rubrique.utility.TitleUpdater;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MenuController {

    private MainApp mainApp;
    
    private DirectoryBean directoryBean;
    
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
    
    @FXML
    private Menu menuRecentsFiles;
    
    @FXML
    private MenuItem recentFile1;
    
    @FXML
    private MenuItem recentFile2;
    
    @FXML
    private MenuItem recentFile3;
    
    @FXML
    private MenuItem recentFile4;
    
    @FXML
    private MenuItem recentFile5;
    
    @FXML
    private Label labelOrologe;
    

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
    	parameterBean = new ParameterBean();
    	disableItems(true);
    	generateRecentFilesMenu();
    	ClockManager.startClock(labelOrologe);
    }
    
    /**
     * Sets the reference to the main application.
     *
     * @param mainApp the main application reference
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        this.directoryBean = mainApp.getDirectoryBean();
        initializeLayout();
    }

    /**
     * Initializes the layout and sets up application close behavior.
     */
    private void initializeLayout() {
        Stage primaryStage = mainApp.getPrimaryStage();
        primaryStage.setOnCloseRequest(event -> {
            if (!handleUnsavedChanges()) {
                event.consume();
            }
        });

        TitleUpdater.updateTitle(primaryStage, directoryBean);
        disableItems(directoryBean == null || directoryBean.getContacts().isEmpty());
    }
    
    /**
     * Handles creating a new file.
     */
    @FXML
    private void handleNew() {
        if (!handleUnsavedChanges()) return;

        mainApp.setDirectoryBean(new DirectoryBean());
        directoryBean = mainApp.getDirectoryBean();
        mainApp.showPersonOverview();

        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
        disableItems(false);
    }
    
    /**
     * Handles opening an existing file.
     */
    @FXML
    private void handleOpen() {
        if (!handleUnsavedChanges()) return;

        FileChooser fileChooser = FileChooserHelper.createFileChooser("Open File", parameterBean.getLastDirectory());
        File selectedFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (selectedFile != null) {
            openFile(selectedFile.getAbsolutePath());
        }
    }
    
    private void openFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            AlertHelper.showError("File Not Found", "The file \"" + filePath + "\" does not exist.");
            parameterBean.getRecentFiles().remove(filePath);
            generateRecentFilesMenu();
            return;
        }

        mainApp.setDirectoryBean(new DirectoryBean(file));
        directoryBean = mainApp.getDirectoryBean();
        mainApp.showPersonOverview();
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);

        parameterBean.setLastDirectory(file.getParent());
        parameterBean.addRecentFile(filePath);
        generateRecentFilesMenu();
    }
    
    /**
     * Handles saving the current file.
     */
    @FXML
    private void handleSave() {
        if (!save()) {
            AlertHelper.showError("Save Failed", "Unable to save the file.");
        }
    }
    
    /**
     * Handles saving the current file as a new file.
     */
    @FXML
    private void handleSaveAs() {
        if (!saveAs())
            AlertHelper.showError("Save As Failed", "Unable to save the file as a new file.");
    }
    
    private boolean save() {
        if (directoryBean.getFileName() == null) {
            return saveAs();
        }
        directoryBean.save();
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
        AlertHelper.showInfo("Save Success", "The file was saved successfully.");
        return true;
    }
    
    private boolean saveAs() {
        FileChooser fileChooser = FileChooserHelper.createFileChooser("Save As", parameterBean.getLastDirectory());
        File selectedFile = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (selectedFile != null) {
            directoryBean.setFile(selectedFile);
            directoryBean.save();

            parameterBean.setLastDirectory(selectedFile.getParent());
            parameterBean.addRecentFile(selectedFile.getAbsolutePath());
            generateRecentFilesMenu();

            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
            return true;
        }
        return false;
    }

    /**
     * Handles closing the current file.
     */
    @FXML
    private void handleClose() {
        if (!handleUnsavedChanges()) return;

        mainApp.setDirectoryBean(null);
        directoryBean = mainApp.getDirectoryBean();
        mainApp.getRootLayout().setCenter(null);

        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), null);
        disableItems(true);
    }

    /**
     * Handles exiting the application.
     */
    @FXML
    private void handleExit() {
        if (!handleUnsavedChanges()) return;
        Platform.exit();
    }
    
    /**
     * Disables or enables menu items based on the application state.
     *
     * @param disable true to disable items, false to enable
     */
    private void disableItems(boolean disable) {
        saveMenuItem.setDisable(disable);          
        saveAsMenuItem.setDisable(disable);        
        menuRecentsFiles.setDisable(parameterBean.getRecentFiles().isEmpty());
    }
    
    /**
     * Generates the menu for recent files dynamically.
     */
    public void generateRecentFilesMenu() {
        menuRecentsFiles.getItems().clear();
        List<String> fichiersRecents = parameterBean.getRecentFiles();
        for (String fichierRecent : fichiersRecents) {
            MenuItem menuItem = new MenuItem(fichierRecent);
            menuItem.setOnAction(e -> openFile(fichierRecent));
            menuRecentsFiles.getItems().add(menuItem);
        }
    }
    
    private boolean handleUnsavedChanges() {
        if (directoryBean != null && !directoryBean.isSaved()) {
            Optional<ButtonType> result = AlertHelper.showConfirmation(
                "Unsaved Changes",
                "You have unsaved changes. Do you want to save before proceeding?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
            );

            if (result.isEmpty() || result.get() == ButtonType.CANCEL)
                return false;
            if (result.get() == ButtonType.YES)
                return save();
        }
        return true;
    }
}