package CBTTestNG.TestNG;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestNGBasic {
	
	private RemoteWebDriver driver;
	
	@BeforeSuite
	public void setup() throws MalformedURLException {
		String username = "you%40@yourdomain.com";
		String authkey = "yourauthkey";
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browser_api_name", "Chrome56x64");
		caps.setCapability("os_api_name", "Win10-E14");
		
		driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"), caps);
	}
	
	@Test
	public void basicTest() {
		// load the page url
        System.out.println("Loading Url");
        driver.get("http://crossbrowsertesting.github.io/selenium_example_page.html");
        
        // maximize the window - DESKTOPS ONLY
        //System.out.println("Maximizing window");
        //driver.manage().window().maximize();
        
        // Check the page title (try changing to make the assertion fail!)
        System.out.println("Checking title");
        
        Assert.assertEquals(driver.getTitle(), "Selenium Test Example Page");
        System.out.println("TestFinished");
	}
	
	@AfterSuite
	public void tearDown() {
		driver.quit();
	}
	
}
