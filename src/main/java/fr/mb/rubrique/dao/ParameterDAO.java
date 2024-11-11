package fr.mb.rubrique.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParameterDAO {

    private static final String FILE_NAME = "contact.ini";
    private final FileContact fileContact;

    public ParameterDAO() {
        String homeDirectory = System.getProperty("user.home");
        File parameterFile = new File(homeDirectory, FILE_NAME);
        this.fileContact = new FileContact(parameterFile);
    }

    public String getLastDirectory() {
        List<String> lines = fileContact.read();
        for (String line : lines) {
            if (line.startsWith("directory="))
                return line.substring("directory=".length());
        }
        return "";
    }

    public List<String> getRecentFiles() {
        List<String> lines = fileContact.read();
        List<String> recentFiles = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("recent=")) 
                recentFiles.add(line.substring("recent=".length()));
        }
        return recentFiles;
    }

    public void saveLastDirectory(String directory) {
        List<String> lines = fileContact.read();
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            if (line.startsWith("directory=")) {
                updatedLines.add("directory=" + directory);
                found = true;
            } else
                updatedLines.add(line);
        }
        if (!found)
            updatedLines.add("directory=" + directory);
        fileContact.write(updatedLines);
    }

    public void saveRecentFiles(List<String> recentFiles) {
        List<String> lines = fileContact.read();
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (!line.startsWith("recent="))
                updatedLines.add(line);
        }
        
        for (String file : recentFiles) {
            updatedLines.add("recent=" + file);
        }
        fileContact.write(updatedLines);
    }
}