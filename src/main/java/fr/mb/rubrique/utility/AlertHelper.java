package fr.mb.rubrique.utility;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility class for creating and displaying different types of alerts in the application.
 * Provides methods for information, error, and confirmation dialogs.
 */
public class AlertHelper {
	
	private AlertHelper() {
		throw new UnsupportedOperationException("Utility class");
	}

	/**
     * Displays an information alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param message the message to display in the alert
     */
    public static void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an error alert with the specified title and message.
     *
     * @param title   the title of the alert
     * @param header  the header text of the alert
     * @param message the message to display in the alert
     */
    public static void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Displays an error alert without a header.
     *
     * @param title   the title of the alert
     * @param message the content message of the alert
     */
    public static void showError(String title, String message) {
        showError(title, null, message); // Calls the overloaded method with no header
    }
    
    

    /**
     * Displays a confirmation alert with the specified title, message, and button options.
     *
     * @param title       the title of the alert
     * @param message     the message to display in the alert
     * @param buttonTypes the button options to display
     * @return an {@link Optional} containing the button clicked by the user
     */
    public static Optional<ButtonType> showConfirmation(String title, String message, ButtonType... buttonTypes) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(buttonTypes);
        return alert.showAndWait();
    }
}