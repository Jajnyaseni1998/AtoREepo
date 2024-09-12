package StepDefinition;



 

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import Pages.MoveRecentFiles;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
 
public class FileStepDefinition {
 
    private static final String DOWNLOADS_DIR = System.getProperty("user.home") + "/Downloads";
    private static final String TARGET_DIR = System.getProperty("user.home") + "/TargetFolder";
    private Set<String> filesToMove = new HashSet<>();
 
    @Given("I want to move recent files")
    public void i_want_to_move_recent_files() {
        // This step can be used to set up any preconditions
        filesToMove.clear();
        File downloadsFolder = new File(DOWNLOADS_DIR);
        File[] files = downloadsFolder.listFiles();
        if (files != null) {
            long cutoffTime = System.currentTimeMillis() - 20 * 60 * 1000; // 20 minutes ago
            for (File file : files) {
                if (file.isFile() && file.lastModified() >= cutoffTime) {
                    filesToMove.add(file.getName());
                }
            }
        }
    }
 
    @When("I execute the file move operation")
    public void i_execute_the_file_move_operation() throws InterruptedException {
        MoveRecentFiles.main(new String[]{}); // Execute the main method
    }
 
    @Then("The recent files should be moved successfully")
    public void the_recent_files_should_be_moved_successfully() {
        File targetFolder = new File(TARGET_DIR);
        File downloadsFolder = new File(DOWNLOADS_DIR);
 
        // Verify files have been moved to the target folder
        File[] targetFiles = targetFolder.listFiles();
        if (targetFiles != null) {
            Set<String> targetFileNames = new HashSet<>();
            for (File file : targetFiles) {
                if (file.isFile()) {
                    targetFileNames.add(file.getName());
                }
            }
            // Check if files that should have been moved are present in the target folder
            for (String fileName : filesToMove) {
                assertTrue("File not found in target folder: " + fileName,
                        targetFileNames.contains(fileName));
            }
 
            // Check if files are no longer in the source folder
            File[] remainingFiles = downloadsFolder.listFiles();
            if (remainingFiles != null) {
                for (File file : remainingFiles) {
                    assertFalse("File still present in downloads folder: " + file.getName(),
                            filesToMove.contains(file.getName()));
                }
            }
        } else {
            assertTrue("Target folder is empty or does not exist.", targetFiles != null && targetFiles.length > 0);
        }
    }
}