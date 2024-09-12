package Pages;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.asis.MainClass;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Driver_manager.DriverManager;

public class CreateNewFilePage extends MainClass {

    private String name = "Notice of assessment";
    private String downloadDirPath = "downloaded_files";
    private JavascriptExecutor js;
    private WebDriverWait wait;
    private ExecutorService executorService;

    @FindBy(xpath = "//option[contains(text(),'100')]")
    private WebElement pages100;

    @FindBy(xpath = "//button[@title='Next page']")
    private WebElement next;

    @FindBy(xpath = "//a[.//span[contains(text(), 'Notice of assessment')]]")
    private List<WebElement> noticeList; // Changed to List to fetch all elements at once

    public CreateNewFilePage() {
        PageFactory.initElements(DriverManager.getDriver(), this);
        this.js = (JavascriptExecutor) DriverManager.getDriver();
        this.wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));  // Reduced wait time to 10 seconds
        this.executorService = Executors.newFixedThreadPool(4); // Allows 4 concurrent threads for downloads
    }

    public void click100Pages() {
        wait.until(ExpectedConditions.elementToBeClickable(pages100)).click();
    }

    public void createFolder() {
        try {
            File downloadDir = new File(downloadDirPath);
            if (!downloadDir.exists()) {
                downloadDir.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void noticeOfAssessment() {
        // Pre-fetch all required elements to interact with
        noticeList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//a[.//span[contains(text(), 'Notice of assessment')]]")));
        
        // Use multithreading for concurrent downloads
        for (WebElement ele : noticeList) {
            executorService.execute(() -> downloadFile(ele)); // Execute download task concurrently
        }
        executorService.shutdown();  // Shut down the executor after all tasks are submitted
    }

    private void downloadFile(WebElement ele) {
        try {
            js.executeScript("arguments[0].scrollIntoView(true);", ele);
            wait.until(ExpectedConditions.elementToBeClickable(ele)).click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Click intercepted, retrying...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".blockUI.blockOverlay")));
            js.executeScript("arguments[0].scrollIntoView(true);", ele);
            wait.until(ExpectedConditions.elementToBeClickable(ele)).click(); // Retry click after overlay disappears
        } catch (Exception e) {
            System.out.println("Failed to click on element: " + e.getMessage());
        }
    }

    public void moveDownloadedFileToFolder() {
        wait.until(ExpectedConditions.elementToBeClickable(next)).click();
        try {
            if (!noticeList.isEmpty()) {  // Check if the list is not empty
                noticeOfAssessment();
            }
        } catch (Exception e) {
            System.out.println("No element found or unable to interact with it: " + e.getMessage());
        }
    }
}
