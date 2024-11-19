package fr.mb.rubrique.outil;

import java.util.List;
import fr.mb.rubrique.dao.ParameterDAO;

public class ParameterBean {
	
	private static final int MAX_RECENT_FILES = 5;
    private String lastDirectory; 
    private List<String> recentFiles; 
    private final ParameterDAO parameterDAO;

    public ParameterBean() {
        this.parameterDAO = new ParameterDAO();
        this.lastDirectory = parameterDAO.getLastDirectory();
        this.recentFiles = parameterDAO.getRecentFiles();
    }

    public String getLastDirectory() {
        return lastDirectory;
    }

    public void setLastDirectory(String lastDirectory) {
        this.lastDirectory = lastDirectory;
        parameterDAO.saveLastDirectory(lastDirectory);
    }

    public List<String> getRecentFiles() {
        return recentFiles;
    }

    public void addRecentFile(String filePath) {
        if (!recentFiles.contains(filePath)) {
            if (recentFiles.size() >= MAX_RECENT_FILES) 
                recentFiles.remove(0);
            recentFiles.add(filePath);
            parameterDAO.saveRecentFiles(recentFiles);
        }
    }
    
    public void clearRecentFiles() {
        recentFiles.clear();
        parameterDAO.saveRecentFiles(recentFiles);
    }
}