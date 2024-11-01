package fr.mb.rubrique.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles reading and writing lines of text to and from a file.
 */
public class FileContact {
	
	private final File file;
	
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
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    
    /**
     * Writes a list of lines to the file, overwriting the existing content.
     *
     * @param lines the list of lines to write to the file
     */
    public void write(List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.append(line);
                writer.newLine(); // Adds "0d0a" for line break
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}