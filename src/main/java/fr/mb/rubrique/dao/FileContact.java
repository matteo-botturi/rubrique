package fr.mb.rubrique.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles reading and writing lines of text to and from a file.
 */
public class FileContact {
	
	private final File file;
	private static final Logger logger = Logger.getLogger(FileContact.class.getName());
	
	/**
     * Constructor that takes a file to manipulate lines of text.
     *
     * @param file the file to read from or write to
     */
    public FileContact(File file) {
        this.file = file;
    }
    
    /**
     * Reads all lines from the file and returns them as a list.
     *
     * @return a list of strings, each representing a line in the file
     */
    public List<String> read() {
        List<String> lines = new ArrayList<>();
        
        if (!file.exists()) {
        	logger.warning(() -> "File does not exist: " + file.getAbsolutePath());
        	return lines;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
        	logger.log(Level.SEVERE, "Failed to read from file: " + file.getAbsolutePath(), e);
        }
        
        return lines;
    }
    
    /**
     * Writes a list of lines to the file, overwriting the existing content.
     *
     * @param lines the list of lines to write to the file
     */
    public void write(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
        	logger.log(Level.SEVERE, "Failed to write to file: " + file.getAbsolutePath(), e);
        }
    }
}