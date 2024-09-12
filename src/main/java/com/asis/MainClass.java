package com.asis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import Driver_manager.DriverManager;

public class MainClass {
	public WebDriver driver;

	public static String ATO_USER_NAME="";
	public static String USERNAME="";
	public static String SENDER_TO="";
	
	public static ArrayList<String> fetchCaptureA1G1B1Data=new ArrayList<>();


	public static ArrayList<HashMap<String, Double>> LAST_TABLE_DATA = new ArrayList<>();

	public static HashMap<String, String> CLIENT_XERO_DATA;

	// WebDriver wait instance
	public static WebDriverWait wait;

	// JavascriptExecutor instance
	public  static JavascriptExecutor js;


	/**
	 * Method to setup WebDriver
	 */
	public static void setupDriver(String browser) {
		DriverManager.setDriver(browser);
	}
	/**
	 * Method to launch the ATO site
	 */

	public void setProperties() {

		ATO_USER_NAME=System.getProperty("userName1");
		USERNAME=System.getProperty("userNameLaptop");
		SENDER_TO=System.getProperty("senderTo");


	}

	public static void lauchSite(String url) {
		DriverManager.getDriver().get(url);
		DriverManager.getDriver().manage().window().maximize();
		DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(20));
		js = (JavascriptExecutor) DriverManager.getDriver();
	}

	public void tearDown() {
		DriverManager.getDriver().quit();
	}	
}
