package fr.mb.rubrique;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import fr.mb.rubrique.bean.DirectoryBean;
import fr.mb.rubrique.model.Person;
import fr.mb.rubrique.utility.TitleUpdater;
import fr.mb.rubrique.view.MenuController;
import fr.mb.rubrique.view.PersonEditDialogController;
import fr.mb.rubrique.view.PersonOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	
	private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());

    private static final String ROOT_LAYOUT_FXML = "view/RootLayout.fxml";
    private static final String PERSON_OVERVIEW_FXML = "view/PersonOverview.fxml";
    private static final String PERSON_EDIT_DIALOG_FXML = "view/PersonEditDialog.fxml";
    private static final String APP_ICON_PATH = "file:resources/images/icon.png";

	private Stage primaryStage;
	private BorderPane rootLayout;
	private DirectoryBean directoryBean;
	
	/**
	 * The data as an observable list of Persons.
	 */
	private ObservableList<Person> personData = FXCollections.observableArrayList();
	
	/**
     * Constructor. Initializes the directory bean.
     */
	public MainApp() {
		directoryBean = new DirectoryBean();
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Rubrique de contacts");
		this.primaryStage.getIcons().add(new Image(APP_ICON_PATH));
		
		initRootLayout();
		primaryStage.show();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from FXML file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(ROOT_LAYOUT_FXML));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			// Pass the MainApp reference to the controller
			MenuController menuController = loader.getController();
			menuController.setMainApp(this);
			
			// Set the initial state of the directory bean
			directoryBean.setSaved(true);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to load root layout.", e);
		}
	}

	/**
	 * Shows the person overview inside the root layout.
	 */
	public void showPersonOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(PERSON_OVERVIEW_FXML));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			
			// Give the controller access to the MainApp.
	        PersonOverviewController controller = loader.getController();
	        controller.setMainApp(this);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Failed to load person overview.", e);
		}
	}
	
	/**
	 * Opens a dialog to edit details for the specified person. If the user
	 * clicks OK, the changes are saved into the provided person object and true
	 * is returned.
	 * 
	 * @param person the person object to be edited
	 * @return true if the user clicked OK, false otherwise.
	 */
	public boolean showPersonEditDialog(Person person) {
	    try {
	        // Load the FXML file and create a new stage for the pop-up dialog.
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource(PERSON_EDIT_DIALOG_FXML));
	        AnchorPane page = (AnchorPane) loader.load();

	        // Create the dialog Stage.
	        Stage dialogStage = new Stage();
	        dialogStage.setTitle("Edit Person");
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        // Set the person into the controller.
	        PersonEditDialogController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setPerson(person);

	        // Show the dialog and wait until the user closes it
	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } catch (IOException e) {
	    	LOGGER.log(Level.SEVERE, "Failed to load person edit dialog.", e);
            return false;
	    }
	}
	
	/**
	 * Returns the data as an observable list of Persons. 
	 * @return
	 */
	public ObservableList<Person> getPersonData() {
		return personData;
	}

	/**
     * Returns the main stage.
     *
     * @return the primary stage
     */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
     * Returns the root layout (BorderPane) of the application.
     * 
     * @return the root layout
     */
    public BorderPane getRootLayout() {
        return rootLayout;
    }
    
    /**
     * Returns the directory bean which manages contact data.
     * 
     * @return the directory bean
     */
    public DirectoryBean getDirectoryBean() {
        return directoryBean;
    }

    /**
     * Sets the directory bean and updates person data.
     *
     * @param directoryBean the new directory bean
     */
	public void setDirectoryBean(DirectoryBean directoryBean) {
		this.directoryBean = directoryBean;
		if (directoryBean != null)
			this.personData = directoryBean.getContacts();	
	}

	public static void main(String[] args) {
		launch();
	}
}