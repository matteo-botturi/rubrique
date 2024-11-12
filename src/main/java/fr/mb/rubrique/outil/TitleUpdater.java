package fr.mb.rubrique.outil;

import javafx.stage.Stage;

public class TitleUpdater {

    public static void updateTitle(Stage primaryStage, DirectoryBean directoryBean) {
        String baseTitle = "Rubrique de contacts";
        String title = baseTitle;
        
        if (directoryBean != null) {
            String fileName = directoryBean.getFileName() != null ? directoryBean.getFileName() : "New File";

            title += " - " + fileName;

            if (!directoryBean.isSaved())
                title += "*";
        } 
        primaryStage.setTitle(title);
    }
}