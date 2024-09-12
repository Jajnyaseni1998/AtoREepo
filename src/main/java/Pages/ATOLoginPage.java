package Pages;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.asis.MainClass;

import Driver_manager.DriverManager;

public class ATOLoginPage extends MainClass{	

	private byte[] screenshotBytes;

	@FindBy(xpath="//a[@id='btn-myGovID']")
	private WebElement myGOV;
	@FindBy(xpath= "//input[@type='email']")
	private WebElement emailAddress;
	@FindBy(xpath= "//button[@type='submit']")
	private WebElement loginButton;

	public ATOLoginPage(){	
		PageFactory.initElements(DriverManager.getDriver(), this);       
	}

	public void clickOnMyGOVButton() throws InterruptedException {
		//		Thread.sleep(10000);
		myGOV.click();
	}

	public void sendingEmailAddress() throws InterruptedException {
//		Thread.sleep(5000);
		String user_id="divya@fortunaadvisors.com.au";
		wait.until(ExpectedConditions.elementToBeClickable(emailAddress));
		emailAddress.sendKeys(user_id);
	}

	public void clickOnLoginButton() throws InterruptedException {
		wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		loginButton.click();
		Thread.sleep(3000);
//		screenshotBytes = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
//		return screenshotBytes;
	}
}