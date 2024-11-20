package fr.mb.rubrique.view;

import fr.mb.rubrique.model.Person;
import fr.mb.rubrique.utility.AlertHelper;
import fr.mb.rubrique.utility.DateUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Dialog to edit details of a person.
 * 
 * @author Matteo Botturi
 */
public class PersonEditDialogController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;


    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;
    
    private static final String BIRTHDAY_PROMPT = "dd.mm.yyyy";

    /**
     * Initializes the controller class. Automatically called after the FXML file is loaded.
     */
    @FXML
    private void initialize() {
        birthdayField.setPromptText(BIRTHDAY_PROMPT);
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage the stage for this dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     *
     * @param person the person to be edited
     */
    public void setPerson(Person person) {
        this.person = person;
        populateFieldsFromPerson(person);
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return true if OK was clicked, false otherwise
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks OK. Saves the data if valid.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            updatePersonFromFields();
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel. Closes the dialog without saving.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (isFieldEmpty(firstNameField)) errorMessage.append("No valid first name!\n");
        if (isFieldEmpty(lastNameField)) errorMessage.append("No valid last name!\n");
        if (isFieldEmpty(streetField)) errorMessage.append("No valid street!\n");
        if (!isValidPostalCode(postalCodeField.getText())) errorMessage.append("No valid postal code (must be an integer)!\n");
        if (isFieldEmpty(cityField)) errorMessage.append("No valid city!\n");
        if (!isValidBirthday(birthdayField.getText())) errorMessage.append("No valid birthday. Use the format " + BIRTHDAY_PROMPT + "!\n");

        if (errorMessage.length() == 0)
            return true;
        else {
            AlertHelper.showError("Invalid Fields", "Please correct invalid fields", errorMessage.toString());
            return false;
        }
    }
    
    /**
     * Populates the fields with data from the person object.
     *
     * @param person the person whose data will populate the fields
     */
    private void populateFieldsFromPerson(Person person) {
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtility.format(person.getBirthday()));
    }
    
    /**
     * Updates the person object with data from the fields.
     */
    private void updatePersonFromFields() {
        person.setFirstName(firstNameField.getText());
        person.setLastName(lastNameField.getText());
        person.setStreet(streetField.getText());
        person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
        person.setCity(cityField.getText());
        person.setBirthday(DateUtility.parse(birthdayField.getText()));
    }
    
    /**
     * Checks if a field is empty.
     *
     * @param field the text field to check
     * @return true if the field is empty, false otherwise
     */
    private boolean isFieldEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    /**
     * Validates the postal code.
     *
     * @param postalCode the postal code to validate
     * @return true if the postal code is valid, false otherwise
     */
    private boolean isValidPostalCode(String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty())
            return false;
        try {
            Integer.parseInt(postalCode);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates the birthday.
     *
     * @param birthday the birthday to validate
     * @return true if the birthday is valid, false otherwise
     */
    private boolean isValidBirthday(String birthday) {
        return birthday != null && DateUtility.validDate(birthday);
    }
}