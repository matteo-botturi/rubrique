package fr.mb.rubrique.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParameterDAO {

    private static final String FILE_NAME = "contact.ini";
    private static final String DIRECTORY_KEY = "directory=";
    private static final String RECENT_KEY = "recent=";
    private final FileContact fileContact;

    public ParameterDAO() {
        String homeDirectory = System.getProperty("user.home");
        File parameterFile = new File(homeDirectory, FILE_NAME);
        this.fileContact = new FileContact(parameterFile);
    }
    
    public String getLastDirectory() {
        return getParameter(DIRECTORY_KEY).orElse("");
    }

    public List<String> getRecentFiles() {
        return getAllParameters(RECENT_KEY);
    }

    public void saveLastDirectory(String directory) {
        updateParameter(DIRECTORY_KEY, directory);
    }

    public void saveRecentFiles(List<String> recentFiles) {
        updateAllParameters(RECENT_KEY, recentFiles);
    }
    
    /**
     * Retrieves the first parameter line that starts with the specified key.
     *
     * @param key the key to look for in each line
     * @return an Optional containing the parameter value if found, or an empty Optional otherwise
     */
    private Optional<String> getParameter(String key) {
        List<String> lines = fileContact.read();
        for (String line : lines) {
            if (line.startsWith(key))
                return Optional.of(line.substring(key.length()));
        }
        return Optional.empty();
    }

    /**
     * Retrieves all parameter lines that start with the specified key.
     *
     * @param key the key to look for in each line
     * @return a list of parameter values found for the given key
     */
    private List<String> getAllParameters(String key) {
        List<String> lines = fileContact.read();
        List<String> parameters = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith(key))
                parameters.add(line.substring(key.length()));
        }
        return parameters;
    }

    /**
     * Updates or adds a single parameter with the specified key and value.
     *
     * @param key the key of the parameter to update
     * @param value the new value to set for the parameter
     */
    private void updateParameter(String key, String value) {
        List<String> lines = fileContact.read();
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            if (line.startsWith(key)) {
                updatedLines.add(key + value);
                found = true;
            } else
                updatedLines.add(line);
        }
        if (!found)
            updatedLines.add(key + value);
        fileContact.write(updatedLines);
    }

    /**
     * Updates or adds multiple parameters with the specified key and list of values.
     *
     * @param key the key of the parameters to update
     * @param values the list of values to set for the parameter key
     */
    private void updateAllParameters(String key, List<String> values) {
        List<String> lines = fileContact.read();
        List<String> updatedLines = new ArrayList<>();

        for (String line : lines) {
            if (!line.startsWith(key))
                updatedLines.add(line);
        }

        for (String value : values)
            updatedLines.add(key + value);
        fileContact.write(updatedLines);
    }
}