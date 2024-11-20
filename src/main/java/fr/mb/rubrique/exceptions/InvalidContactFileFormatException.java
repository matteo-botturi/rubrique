package fr.mb.rubrique.exceptions;

/**
 * Exception thrown when a contact file does not conform to the expected format.
 */
public class InvalidContactFileFormatException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidContactFileFormatException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidContactFileFormatException(String message) {
        super(message);
    }
}