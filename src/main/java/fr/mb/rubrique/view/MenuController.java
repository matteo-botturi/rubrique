package fr.mb.rubrique.view;

import java.io.File;
import java.util.List;
import java.util.Optional;
import fr.mb.rubrique.MainApp;
import fr.mb.rubrique.bean.DirectoryBean;
import fr.mb.rubrique.bean.ParameterBean;
import fr.mb.rubrique.enumeration.EMenuItemType;
import fr.mb.rubrique.enumeration.EMenuState;
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
import javafx.scene.control.SeparatorMenuItem;
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
    private MenuItem reloadMenuItem;
    
    @FXML
    private MenuItem closeMenuItem;
    
    @FXML
    private MenuItem exitMenuItem;
    
    @FXML
    private MenuItem aboutItem;
    
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
    private MenuItem clearRecentFilesMenuItem;
    
    @FXML
    private Label labelOrologe;
    

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
    	parameterBean = new ParameterBean();
    	disableItems(true, EMenuItemType.CLEAR_RECENTS);
    	generateRecentFilesMenu();
    	ClockManager.startClock(labelOrologe);
    	
    	clearRecentFilesMenuItem.setOnAction(event -> handleClearRecentFiles());
    	
    	
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
        
    	updateMenuItems(EMenuState.INITIALIZED); // Stato iniziale
    }

    /**
     * Initializes the layout and sets up application close behavior.
     */
    private void initializeLayout() {
    	
        Stage primaryStage = mainApp.getPrimaryStage();
        
        primaryStage.setOnCloseRequest(event -> {
            if (!handleUnsavedChanges())
                event.consume();
        });
        TitleUpdater.updateTitle(primaryStage, directoryBean, false);
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

        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean, true);
        
        updateMenuItems(EMenuState.NEW_FILE);
    }
    
    /**
     * Handles opening an existing file.
     */
    @FXML
    private void handleOpen() {
        if (!handleUnsavedChanges()) return;

        FileChooser fileChooser = FileChooserHelper.createFileChooser("Open File", parameterBean.getLastDirectory());
        File selectedFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (selectedFile != null) 
            openFile(selectedFile.getAbsolutePath());
        
        updateMenuItems(EMenuState.FILE_OPENED);
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
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean, true);

        parameterBean.setLastDirectory(file.getParent());
        parameterBean.addRecentFile(filePath);
        generateRecentFilesMenu();
    }
    
    /**
     * Handles saving the current file.
     */
    @FXML
    private void handleSave() {
        if (!save())
            AlertHelper.showError("Save Failed", "Unable to save the file.");
        else
        	updateMenuItems(EMenuState.FILE_SAVED);
    }
    
    /**
     * Handles saving the current file as a new file.
     */
    @FXML
    private void handleSaveAs() {
        if (!saveAs())
            AlertHelper.showError("Save As Failed", "Unable to save the file as a new file.");
        else
        	updateMenuItems(EMenuState.FILE_SAVED);
    }
    
    private boolean save() {
        if (directoryBean.getFileName() == null) {
            return saveAs();
        }
        directoryBean.save();
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean, true);
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

            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean, true);
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

        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), null, true);

        updateMenuItems(EMenuState.INITIALIZED);
    }

    /**
     * Handles exiting the application.
     */
    @FXML
    private void handleExit() {
        if (!handleUnsavedChanges()) return;
        Platform.exit();
    }
    
    @FXML
    private void handleReload() {
        try {
            directoryBean.reload();

            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean, true);

            AlertHelper.showInfo("Reload Successful", "Contacts reloaded successfully.");
        } catch (RuntimeException e) {
            AlertHelper.showError("Reload Failed", "Could not reload contacts.", e.getMessage());
        }
    }
    
    @FXML
    private void handleAbout() {
    	AlertHelper.showInfo("Gestion de contacts", "Matteo Botturi - 2024");
    }
    
    /**
     * Disables or enables menu items based on the application state.
     *
     * @param disable true to disable items, false to enable
     */
    private void disableItems(boolean disable, EMenuItemType... exceptions) {
        saveMenuItem.setDisable(disable);
        saveAsMenuItem.setDisable(disable);
        closeMenuItem.setDisable(disable);
        reloadMenuItem.setDisable(disable);
        clearRecentFilesMenuItem.setDisable(disable || parameterBean.getRecentFiles().isEmpty());

        for (EMenuItemType exception : exceptions) {
            switch (exception) {
                case SAVE -> saveMenuItem.setDisable(!disable);
                case SAVE_AS -> saveAsMenuItem.setDisable(!disable);
                case CLOSE -> closeMenuItem.setDisable(!disable);
                case CLEAR_RECENTS -> clearRecentFilesMenuItem.setDisable(!disable && !parameterBean.getRecentFiles().isEmpty());
                case RELOAD -> reloadMenuItem.setDisable(!disable);
            }
        }
    }
    
    private void updateMenuItems(EMenuState state) {
        switch (state) {
            case INITIALIZED -> disableItems(true, EMenuItemType.CLEAR_RECENTS);
            case NEW_FILE -> disableItems(true, EMenuItemType.SAVE,EMenuItemType.SAVE_AS,EMenuItemType.CLOSE);
            case FILE_OPENED -> disableItems(true, EMenuItemType.SAVE,EMenuItemType.SAVE_AS,EMenuItemType.CLOSE,EMenuItemType.RELOAD);
            case FILE_SAVED -> disableItems(true,EMenuItemType.SAVE_AS,EMenuItemType.CLOSE,EMenuItemType.RELOAD);
        }
    }

    
    /**
     * Handles the action for clearing recent files.
     * Removes all recent files and updates the menu.
     */
    private void handleClearRecentFiles() {
        parameterBean.clearRecentFiles();
        generateRecentFilesMenu();
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

        if (!fichiersRecents.isEmpty()) {
            menuRecentsFiles.getItems().add(new SeparatorMenuItem());
            clearRecentFilesMenuItem.setDisable(false); 
        } else
            clearRecentFilesMenuItem.setDisable(true);

        menuRecentsFiles.getItems().add(clearRecentFilesMenuItem);
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