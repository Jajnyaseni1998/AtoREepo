package Pages;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class MoveRecentFiles {
 
    private static final Logger logger = Logger.getLogger(MoveRecentFiles.class.getName());
    private static final String PROPERTIES_FILE = System.getProperty("user.home") + "/lastMove.properties";
    private static final String DOWNLOADS_DIR = System.getProperty("user.home") + "/Downloads";
    private static final String TARGET_DIR = System.getProperty("user.home") + "/TargetFolder";
    private static final long TIME_PERIOD_MS = 20 * 60 * 1000; // 20 minutes in milliseconds
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
 
    public static void main(String[] args) throws InterruptedException {
        File downloadsFolder = new File(DOWNLOADS_DIR);
        File targetFolder = new File(TARGET_DIR);
 
        // Ensure the target directory exists
        if (!targetFolder.exists()) {
            if (targetFolder.mkdirs()) {
                logger.info("Target folder created: " + TARGET_DIR);
            } else {
                logger.severe("Failed to create target folder: " + TARGET_DIR);
                return;
            }
        }
 
        // Get the current time
        long currentTime = System.currentTimeMillis();
        System.out.println(formatDate(currentTime) +" current time ");
        long cutoffTime = currentTime - TIME_PERIOD_MS;
        logger.info("Current time: " + formatDate(currentTime));
        logger.info("Cutoff time: " + formatDate(cutoffTime));
 
        // Get files in the Downloads folder that were modified within the last 30 minutes
        File[] filesToMove = getFilesToMove(downloadsFolder, cutoffTime);
 
        if (filesToMove.length > 0) {
            for (File file : filesToMove) {
                Path sourcePath = file.toPath();
                Path targetPath = targetFolder.toPath().resolve(file.getName());
               
                try {
                    Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    logger.info("File moved successfully: " + file.getName());
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error moving file: " + file.getName(), e);
                }
            }
 
            updateLastMoveTime(currentTime); // Optionally, update the timestamp in the properties file
        } else {
            logger.info("No recent files found to move.");
        }
    }
 
    // Load the last move timestamp from the properties file
    private static long loadLastMoveTime() {
        Properties properties = new Properties();
        File file = new File(PROPERTIES_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
                return Long.parseLong(properties.getProperty("lastMoveTime", "0"));
            } catch (IOException | NumberFormatException e) {
                logger.log(Level.SEVERE, "Error loading last move time", e);
            }
        }
        return 0;
    }
 
    // Update the last move timestamp in the properties file
    private static void updateLastMoveTime(long currentTime) {
        Properties properties = new Properties();
        properties.setProperty("lastMoveTime", String.valueOf(currentTime));
        try (FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE)) {
            properties.store(fos, null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error updating last move time", e);
        }
    }
 
    // Method to get files in a directory that were modified within a specific time period
    private static File[] getFilesToMove(File folder, long cutoffTime) {
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return new File[0];
        }
 
        // Print file names and modification times for debugging
        logger.info("Files in Downloads folder:");
        for (File file : files) {
            if (file.isFile()) {
                logger.info("File: " + file.getName() + ", Last Modified: " + formatDate(file.lastModified()));
            }
        }
 
        // Filter files that were modified within the specified time period
        return java.util.Arrays.stream(files)
                .filter(file -> file.isFile() && file.lastModified() >= cutoffTime)
                .toArray(File[]::new);
    }
 
    // Helper method to format timestamps as human-readable dates
    private static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }
}