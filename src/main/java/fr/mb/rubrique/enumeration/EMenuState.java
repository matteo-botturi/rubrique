package fr.mb.rubrique.enumeration;

/**
 * Enumeration representing the state of the application for menu item updates.
 */
public enum EMenuState {
    INITIALIZED,  // Initial state (e.g., after Close or app start)
    NEW_FILE,     // When a new file is created
    FILE_OPENED,  // When a file is opened
    FILE_SAVED    // After a file is saved
}