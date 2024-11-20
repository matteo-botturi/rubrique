package fr.mb.rubrique.bean;

import java.util.List;
import fr.mb.rubrique.dao.ParameterDAO;

public class ParameterBean {
	private static final int MAX_RECENT_FILES = 5;
    private String lastDirectory; 
    private List<String> recentFiles; 
    private final ParameterDAO parameterDAO;

    /**
     * Default constructor that initializes the parameter bean with values from the DAO.
     */
    public ParameterBean() {
        this.parameterDAO = new ParameterDAO();
        this.lastDirectory = parameterDAO.getLastDirectory();
        this.recentFiles = parameterDAO.getRecentFiles();
    }

    /**
     * Gets the last accessed directory.
     *
     * @return the last accessed directory as a String
     */
    public String getLastDirectory() {
        return lastDirectory;
    }

    /**
     * Sets the last accessed directory and saves it to the DAO.
     *
     * @param lastDirectory the last directory to set
     */
    public void setLastDirectory(String lastDirectory) {
        this.lastDirectory = lastDirectory;
        parameterDAO.saveLastDirectory(lastDirectory);
    }

    /**
     * Returns an immutable list of recent files.
     *
     * @return a list of recent file paths
     */
    public List<String> getRecentFiles() {
        return recentFiles;
    }

    /**
     * Adds a file to the recent files list, ensuring the list does not exceed the maximum size.
     *
     * @param filePath the file path to add
     */
    public void addRecentFile(String filePath) {
        if (!recentFiles.contains(filePath)) {
            if (recentFiles.size() >= MAX_RECENT_FILES) 
                recentFiles.remove(0);
            recentFiles.add(filePath);
            parameterDAO.saveRecentFiles(recentFiles);
        }
    }
    
    /**
     * Clears all recent files and saves the updated list to the DAO.
     */
    public void clearRecentFiles() {
        recentFiles.clear();
        parameterDAO.saveRecentFiles(recentFiles);
    }
}