package fr.mb.rubrique.utility;

import java.io.File;
import javafx.stage.FileChooser;

/**
 * Utility class for creating and configuring {@link FileChooser} instances.
 * Provides a consistent way to set up file choosers with predefined filters and directories.
 */
public class FileChooserHelper {
	
	private FileChooserHelper() {
		throw new UnsupportedOperationException("Utility class");
	}

	/**
     * Creates a new {@link FileChooser} with the specified title and initial directory.
     * Adds filters for text files and contact files.
     *
     * @param title               the title of the file chooser window
     * @param initialDirectoryPath the initial directory to display in the file chooser, or null
     * @return a configured {@link FileChooser} instance
     */
    public static FileChooser createFileChooser(String title, String initialDirectoryPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
            new FileChooser.ExtensionFilter("Contact Files (*.contact)", "*.contact")
        );
        if (initialDirectoryPath != null) {
            File initialDirectory = new File(initialDirectoryPath);
            if (initialDirectory.exists())
                fileChooser.setInitialDirectory(initialDirectory);
        }
        return fileChooser;
    }
}