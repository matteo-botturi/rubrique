package fr.mb.rubrique.outil;

import javafx.stage.Stage;

public class TitleUpdater {

    /**
     * Aggiorna il titolo della finestra principale.
     *
     * @param primaryStage La finestra principale dell'applicazione.
     * @param directoryBean Il DirectoryBean che contiene lo stato corrente del file.
     * @param baseTitle Il titolo di base dell'applicazione.
     */
    public static void updateTitle(Stage primaryStage, DirectoryBean directoryBean, String baseTitle) {
        String title = baseTitle != null ? baseTitle : "Gestion de contact";
        
        if (directoryBean != null && directoryBean.getFile() != null) {
            // Ottieni il percorso assoluto del file
            title += " - " + directoryBean.getFile().getAbsolutePath();

            // Aggiungi un asterisco se ci sono modifiche non salvate
            if (!directoryBean.isSaved() && !title.endsWith("*")) {
                title += "*";
            }
        }
        
        primaryStage.setTitle(title);
    }

    // Metodo di convenienza con il titolo di base predefinito
    public static void updateTitle(Stage primaryStage, DirectoryBean directoryBean) {
        updateTitle(primaryStage, directoryBean, "Gestion de contact");
    }
}

