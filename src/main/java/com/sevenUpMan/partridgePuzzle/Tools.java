package com.sevenUpMan.partridgePuzzle;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Tools {

    public static final boolean WRITE_FILES = true;

    private static HashMap<String, BufferedWriter> writers = new HashMap<>();

    private Tools() {
        // Prevent instantiation
    }

    /**
     * Write the given text to a file in the project's "output" folder that must already exist.
     * No error is thrown on failure.
     * 
     * @param fileName
     * @param text
     */
    public static void writeFile(String fileName, String text) {

        // If writing files is disabled, do nothing
        if (!WRITE_FILES) {
            return;
        }

        if (!writers.containsKey(fileName)) {
            try {
                // Determine the output folder path
                Path outputPath = Paths.get("output");

                // Create the resources directory if it doesn't exist
                if (!Files.exists(outputPath)) {
                    Files.createDirectories(outputPath);
                }

                // Construct the full file path
                Path filePath = outputPath.resolve(fileName);

                BufferedWriter newWriter = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(filePath.toFile()), StandardCharsets.UTF_8));
                writers.put(fileName, newWriter);
            } catch (IOException ex) {
                // Report
                return;
            }
        }

        BufferedWriter writer = writers.get(fileName);

        try {
            // Open the file if not already opened
            if (writer == null) {
                throw new IOException("Writer is null after creation attempt");
            }
            writer.write(text);
        } catch (IOException ex) {
            // Report
        }
    }

    public static void closeFile(String fileName) {
        if (writers.containsKey(fileName)) {
            BufferedWriter writer = writers.get(fileName);
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception ex) {
                /* ignore */
            }
            writers.remove(fileName);
        }
    }

    public static void closeAllFiles() {
        for (BufferedWriter writer : writers.values()) {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception ex) {
                /* ignore */
            }
        }
    }

    /**
     * Read the content of a file from the project's resources folder.
     * Returns null if the file cannot be read.
     * 
     * @param fileName the name of the file in the resources directory
     * @return the content of the file as a String, or null on error
     */
    public static String readFile(String fileName) {
        try {
            // Determine the resources folder path
            Path resourcesPath = Paths.get("src", "main", "resources");
            Path filePath = resourcesPath.resolve(fileName);

            // Read all lines and join them with newlines
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return null;
        }
    }

}