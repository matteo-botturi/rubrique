package fr.mb.rubrique.utility;

import fr.mb.rubrique.bean.DirectoryBean;
import javafx.stage.Stage;

/**
 * Utility class for updating the title of the main application window.
 */
public class TitleUpdater {

    /** The default base title of the application */
    private static final String DEFAULT_BASE_TITLE = "Gestion de contact";

    /** Default name for a new, unnamed file */
    private static final String DEFAULT_NEW_FILE_NAME = "New File";

    private TitleUpdater() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Updates the title of the main application window.
     *
     * @param primaryStage The main application window.
     * @param directoryBean The DirectoryBean that contains the current file state.
     * @param baseTitle The base title of the application.
     */
    public static void updateTitle(Stage primaryStage, DirectoryBean directoryBean, String baseTitle, boolean isCenterOpened) {
        String title = generateTitle(directoryBean, baseTitle, isCenterOpened);
        primaryStage.setTitle(title);
    }

    /**
     * Convenience method to update the title with the default base title.
     *
     * @param primaryStage The main application window.
     * @param directoryBean The DirectoryBean that contains the current file state.
     */
    public static void updateTitle(Stage primaryStage, DirectoryBean directoryBean, boolean isCenterOpened) {
        updateTitle(primaryStage, directoryBean, DEFAULT_BASE_TITLE, isCenterOpened);
    }

    /**
     * Generates the title based on the provided DirectoryBean and base title.
     *
     * If the file is not saved, the title includes an asterisk (*). If the file does not
     * have a name, "New File" is displayed instead of the file name.
     *
     * @param directoryBean The DirectoryBean that contains the current file state.
     * @param baseTitle The base title of the application.
     * @return A formatted title string.
     */
    private static String generateTitle(DirectoryBean directoryBean, String baseTitle, boolean isCenterOpened) {
        String title = baseTitle != null ? baseTitle : DEFAULT_BASE_TITLE;

		if (directoryBean != null && isCenterOpened) {

			String fileName = directoryBean.getFileName() != null ? directoryBean.getFile().getAbsolutePath()
					: DEFAULT_NEW_FILE_NAME;

			title += " - " + fileName;

			if (!directoryBean.isSaved() && !title.endsWith("*"))
				title += "*";

		}
		return title;
    }
}
