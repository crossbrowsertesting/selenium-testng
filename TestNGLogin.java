package CBTTestNG2.TestNG;

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

public class TestNGLogin {
	
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
	public void loginPage() {
		driver.get("http://crossbrowsertesting.github.io/login-form.html");
		
		driver.findElementByName("username").sendKeys("tester@crossbrowsertesting.com");
        
        // then by entering the password
        System.out.println("Entering password");
        driver.findElementByName("password").sendKeys("test123");
        
        // then by clicking the login button
        System.out.println("Logging in");
        driver.findElementByCssSelector("div.form-actions > button").click();
        
        // let's wait here to ensure the page has loaded completely
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"logged-in-message\"]/h2")));
        
        String welcomeMessage = driver.findElementByXPath("//*[@id=\"logged-in-message\"]/h2").getText();
        Assert.assertEquals("Welcome tester@crossbrowsertesting.com", welcomeMessage);
        
        System.out.println("TestFinished");
	}
	
	@AfterSuite
	public void tearDown() {
		driver.quit();
	}
	
}

