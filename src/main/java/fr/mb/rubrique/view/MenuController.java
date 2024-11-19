package fr.mb.rubrique.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import fr.mb.rubrique.MainApp;
import fr.mb.rubrique.exceptions.InvalidContactFileFormatException;
import fr.mb.rubrique.outil.DirectoryBean;
import fr.mb.rubrique.outil.ParameterBean;
import fr.mb.rubrique.outil.TitleUpdater;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	startClock(); // Avvia l'orologio all'inizializzazione
        // Disable save option initially if no file is open
        //saveMenuItem.setDisable(true);
        parameterBean = new ParameterBean();
        disableItems(true);
        genererMenuFichierRecent();
    }
    
    /**
     * Avvia la `Timeline` per aggiornare l'orologio ogni secondo
     */
    private void startClock() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(1000), e -> updateClock()) // Aggiorna ogni secondo
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Ripetizione indefinita
        timeline.play(); // Avvia la timeline
    }

    /**
     * Metodo per aggiornare il testo dell'orologio con la data e ora attuali
     */
    private void updateClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE dd MMM yyyy, HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        labelOrologe.setText(formattedDateTime);
    }
    
    /**
     * Mostra l'orologio al centro se `showInCenter` è true, altrimenti nel bottom
     */
    private void showClockInCenter(boolean showInCenter) {
        if (showInCenter) {
            mainApp.getRootLayout().setCenter(labelOrologe);
            mainApp.getRootLayout().setBottom(null); // Rimuove dal Bottom
        } else {
            mainApp.getRootLayout().setBottom(labelOrologe);
            //mainApp.getRootLayout().setCenter(null); // Rimuove dal Center
        }
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
     * Initializes layout and actions for closing the application.
     */
    private void initializeLayout() {
        // Get the primary stage from mainApp and set the onCloseRequest action
        Stage primaryStage = mainApp.getPrimaryStage();
        primaryStage.setOnCloseRequest(event -> {
            if (directoryBean != null && !directoryBean.isSaved()) {
                // Prompt the user to save changes
                boolean shouldSave = promptToSave();
                if (shouldSave)
                	directoryBean.save();
            }
            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
        });

        // Set up the initial layout with no content in the center
        mainApp.getRootLayout().setCenter(null);
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
        mainApp.getRootLayout().setBottom(labelOrologe); // Aggiunge l'orologio in fondo alla finestra
        updateClock(); // Imposta l'orologio inizialmente
    }
    
    /**
     * Disattiva o attiva i MenuItem in base allo stato del file
     */
    private void disableItems(boolean disable) {
        saveMenuItem.setDisable(disable);          
        saveAsMenuItem.setDisable(disable);        
        menuRecentsFiles.setDisable(parameterBean.getRecentFiles().isEmpty());
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
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
            new FileChooser.ExtensionFilter("Contact Files (*.contact)", "*.contact")
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
    	showClockInCenter(false);
        if (directoryBean != null && !directoryBean.isSaved()) {
            boolean shouldSave = promptToSave();
            if (shouldSave) {
            	directoryBean.save();
            }
        }
        mainApp.setDirectoryBean(new DirectoryBean());
        directoryBean = mainApp.getDirectoryBean();
        mainApp.showPersonOverview();
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
        disableItems(false);
    }
    
    @FXML
    private void handleOpen() {
        if (directoryBean != null && !directoryBean.isSaved()) {
            Boolean saveResponse = promptToSave();
            if (saveResponse == null) 
                return;
            else if (saveResponse)
                directoryBean.save();
        }

        // Apri il file chooser
        FileChooser fileChooser = chooseFile("Open Contact File", parameterBean.getLastDirectory());
        File selectedFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        // Tenta di caricare il file selezionato
        if (selectedFile != null) {
            openFile(selectedFile.getAbsolutePath());
        }
        disableItems(false);
    }

	private void loadFile(File selectedFile) throws IOException{
		mainApp.setDirectoryBean(new DirectoryBean(selectedFile));
		directoryBean = mainApp.getDirectoryBean();
		mainApp.showPersonOverview();
		TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
		parameterBean.setLastDirectory(selectedFile.getParent());
		parameterBean.addRecentFile(selectedFile.getAbsolutePath());
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
        if (directoryBean != null) {
            // Verifica se il file ha subito modifiche
            if (!directoryBean.isSaved()) {
                // Mostra un alert per confermare il salvataggio
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Salvataggio Modifiche");
                alert.setHeaderText("Modifiche trovate");
                alert.setContentText("Il file \"" + (directoryBean.getFileName() != null ? directoryBean.getFileName() : "Nuovo File") + "\" ha subito modifiche. Vuoi salvare?");
                
                ButtonType saveButton = new ButtonType("Salva");
                ButtonType dontSaveButton = new ButtonType("Non Salvare", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(saveButton, dontSaveButton);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == saveButton) {
                    // Salva il file se l'utente ha scelto "Salva"
                    if (directoryBean.getFileName() == null) {
                        return saveAs(); // Chiede di specificare un percorso se è un nuovo file
                    } else {
                        directoryBean.save();
                        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
                        return true;
                    }
                } else {
                    // L'utente ha scelto "Non Salvare" o ha chiuso l'alert
                    return false;
                }
            } else if (directoryBean.getFileName() == null) {
                // Se il file non ha un nome, richiede di specificarlo con "Salva con nome"
                return saveAs();
            } else {
                // Salva direttamente se il file è stato modificato ma ha già un nome
                directoryBean.save();
                TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
                showSaveSuccessAlert();
                return true;
            }
        }
        return false; // Restituisce false se non c'è nessun `DirectoryBean` da salvare
    }
    
 // Metodo per mostrare l'alert "Salvataggio eseguito"
    private void showSaveSuccessAlert() {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Salvataggio Completato");
        successAlert.setHeaderText(null);
        successAlert.setContentText("Salvataggio eseguito con successo.");
        successAlert.showAndWait();
    }
    
    private boolean saveAs() {
        FileChooser fileChooser = chooseFile("Save Contact File As", System.getProperty("user.home"));
        File selectedFile = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (selectedFile != null) {            
        	directoryBean.setFile(selectedFile);
        	directoryBean.save();
            TitleUpdater.updateTitle(mainApp.getPrimaryStage(), directoryBean);
            parameterBean.setLastDirectory(selectedFile.getParent());
            parameterBean.addRecentFile(selectedFile.getAbsolutePath());
            genererMenuFichierRecent();
            
            return true;
        }
        else
            return false;         
    }

    @FXML
    private void handleClose() {
    	//showClockInCenter(true);
        if (directoryBean != null && !directoryBean.isSaved()) {
            Boolean shouldSave = promptToSave();

            if (shouldSave == null)
                return;
            else if (shouldSave) { 
                if (!save())
                    return;
            }
        }
        mainApp.setDirectoryBean(null);
        directoryBean = mainApp.getDirectoryBean();
        mainApp.getRootLayout().setCenter(null);
        initializeLayout();
        TitleUpdater.updateTitle(mainApp.getPrimaryStage(), null);
        disableItems(true);
        
    }

    @FXML
    private void handleExit() {
        if (directoryBean != null && !directoryBean.isSaved()) {
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
    
    public void genererMenuFichierRecent() {
    	
        menuRecentsFiles.getItems().clear();
        
        List<String> fichiersRecents = parameterBean.getRecentFiles();
        for (String fichierRecent : fichiersRecents) {
            MenuItem menuItem = new MenuItem(fichierRecent);
            
            menuItem.setOnAction(e -> openFile(fichierRecent));
            
            menuRecentsFiles.getItems().add(menuItem);
        }
    }
    
    public void openFile(String path) {
        File file = new File(path);
        try {
            // Verifica che il file esista e rispetti il formato atteso
            if (!file.exists()) {
                throw new FileNotFoundException("File non trovato: " + file.getAbsolutePath());
            }
            
            if (!isValidContactFile(file)) {
                throw new InvalidContactFileFormatException("Il file non rispetta il formato dei contatti");
            }

            // Se il file è valido, lo carica
            loadFile(file);
            parameterBean.setLastDirectory(file.getParent());
            parameterBean.addRecentFile(file.getAbsolutePath());
            genererMenuFichierRecent();

        } catch (FileNotFoundException e) {
            showAlert("Errore: File non trovato", "Il file \"" + file.getAbsolutePath() + "\" non è più disponibile.");
            
            // Rimuove il file dalla lista dei recenti
            parameterBean.getRecentFiles().remove(file.getAbsolutePath());
            genererMenuFichierRecent();

        } catch (InvalidContactFileFormatException e) {
            showAlert("Errore: Formato File non corretto", "Il file \"" + file.getAbsolutePath() + "\" non rispetta il formato richiesto.");

        } catch (IOException e) {
            showAlert("Errore di lettura del file", "Si è verificato un errore durante l'apertura del file: " + file.getAbsolutePath());
        }
    }
    
    private boolean isValidContactFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return line != null && line.contains("|"); // Esempio di controllo del formato
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}