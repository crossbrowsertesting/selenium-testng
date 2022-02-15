<h1><strong>Getting Started with TestNG and CrossBrowserTesting</strong></h1>
<p><a href="https://testng.org/doc/">TestNG</a> is a Java testing framework inspired by JUnit. TestNG introduces new functionalities that make it more powerful and easier to cover all categories of testing, including unit, functional, and end-to-end.</p>

***Setting Up Using Maven***

<p><em>The easisest way to setup a TestNG example test is to clone our <a href="https://github.com/crossbrowsertesting/selenium-testng">TestNG Github Repository</a></a>.</em></p>

`git clone https://github.com/crossbrowsertesting/selenium-testng.git`

<p><em>Then cd into the new folder.</em></p>

`cd selenium-testng`

<p><em>Now open that folder with your choice of IDE, using Maven</em></p>

<p><em>Go into [selenium-testng/src/java], there will be 3 files. Each one is a different test with the capabilties listed there. You will want to input your email and authkey from your <a href="https://app.crossbrowsertesting.com/account">CBT account</a> into the file before you run it.</em></p>

<p><em>Remember to use "%40" instead of "@" to avoid the MalformedURLException when entering your email.</em></p>

<p><em>Now you are ready to run your first TestNG test. You can right click any of the three files to run them indavidually, or you can use Maven to run them using 'profiles'.</em></p>

<p><em>To run all three tests at the same time, run the below command.</em></p>

`mvn test -P parallel`

<p><em>To run a single test using Maven, run the below command. The ending of this command is dependant on which test you prefer to run.</em></p>

`mvn test -P login / title / todo`










***Setting Up Without Maven***

<p>In this guide we will use TestNG for testing using the <a href="https://www.seleniumhq.org/">Selenium Webdriver</a> and <a href="https://www.java.com/en/">Java</a> programming language.</p>

Capabilities should be given before your TestSuite has begin. Note that your username will have to contain %40 rather than the '@' character to avoid the MalformedURLException. The browser_api_name and os_api_name can be pulled from an [API call](https://crossbrowsertesting.com/apidocs/v3/selenium.html#!/default/get_selenium_browsers) to get possible combinations. 

```

    @BeforeSuite
    public void setup() throws MalformedURLException {
        String username = "you%40yourdomain.com";
        String authkey = "yourauthkey";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "Chrome");	// Automatically pulls the latest version of Chrome
		caps.setCapability("platform", "Windows 10");	// To specify a version, add setCapability("version", "desired version")
        
        driver = new RemoteWebDriver(new URL("http://" + username + ":" + authkey +"@hub.crossbrowsertesting.com:80/wd/hub"), caps);
    }

```

***Creating Test Cases***

Actual test cases are prefixed by an @Test annotation as shown below. At this point, you can add any tests you'd like, and they will be run against the driver instantiated in the setup method. Note that we can use TestNG's static Assert methods to assert attributes of our webpage. 

```

@Test
    public void todoTest() {
        System.out.println("Loading Url");
                driver.get("http://crossbrowsertesting.github.io/todo-app.html");

                // maximize the window - DESKTOPS ONLY
                //System.out.println("Maximizing window");
                //driver.manage().window().maximize();
                
                System.out.println("Checking Box");
                driver.findElement(By.name("todo-4")).click();

                System.out.println("Checking Another Box");
                driver.findElement(By.name("todo-5")).click();
                
                // If both clicks worked, then the following List should be have length 2
                List<WebElement> elems = driver.findElements(By.className("done-true"));
                // So we'll assert that this is correct.
                Assert.assertEquals(2, elems.size());
                
                System.out.println("Entering Text");
                driver.findElement(By.id("todotext")).sendKeys("Run your first Selenium Test");
                driver.findElement(By.id("addbutton")).click();
                
                // Let's also assert that the todo we added is present in the list.
                String spanText = driver.findElementByXPath("/html/body/div/div/div/ul/li[6]/span").getText();
                Assert.assertEquals("Run your first Selenium Test", spanText);
                
                System.out.println("Archiving old todos");
                driver.findElement(By.linkText("archive")).click();
                
                // If our archive link worked, then the following list should have length 4.
                elems = driver.findElements(By.className("done-false"));
                // So will assert that this is true as well.
                Assert.assertEquals(4, elems.size());
                
                System.out.println("TestFinished");
    }
```

***TearDowns***

Tear downs can be annotated with @AfterSuite. While it goes without saying, you should always call driver.quit() in the teardown methods of your suite. Additionally, you can use [our API](https://crossbrowsertesting.com/apidocs/v3/selenium.html#!/default/put_selenium_selenium_test_id) to generate a test score in our App based on whether or not your test ran successfully. 

```
    @AfterSuite
    public void tearDown() {
        driver.quit();
    }


```

***Parallel Testing***

Parallel testing is fast and simple with TestNG. I do so by making modifications to my testng.xml configuration file. This can be done two ways, by suite:

```
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >
<suite name="CBT - Suite">
    <test name="Login">
        <classes>
            <class name="CBTTestNG.TestNG.TestNGTitleCheck" />
            <class name="CBTTestNG.TestNG.TestNGLogin" />
            <class name="CBTTestNG.TestNG.TestNGTodo" />
        </classes>
    </test>
</suite>

```

As well as by capabilties. Note that the @parameter prefix will have to be added to your setup methods:

```

<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite thread-count="2" name="Suite" parallel="tests">
  <test name="FirstTest">
  <parameter name="os" value="Win10"/>
  <parameter name="browser" value="Chrome76"/>
    <classes>
      <class name="CBTTestNG.TestNG.TestNGTodo"/>
    </classes>
  </test>
  <test name="SecondTest">
  <parameter name="os" value="Mac10.14"/>
  <parameter name="browser" value="Chrome76"/>
    <classes>
      <class name="CBTTestNG.TestNG.TestNGTodo"/>
    </classes>
  </test>
</suite>
```

Parameters reflected in test cases:

```

@BeforeClass
  @org.testng.annotations.Parameters(value={"os", "browser"})
  public void setUp(String os,String browser) throws Exception {
    DesiredCapabilities capability = new DesiredCapabilities();
    capability.setCapability("os_api_name", os);
    capability.setCapability("browser_api_name", browser);
    capability.setCapability("name", "TestNG-Parallel");
    driver = new RemoteWebDriver(
      new URL("http://" + username + ":" + authkey + "@hub.crossbrowsertesting.com:80/wd/hub"),
      capability);
  }  

```

Additionally, test classes can be added and executed in parallel using an EntryPoint class, as shown below. This is especially useful for exporting your tests as a JAR so that it can be ran quickly from a continuous integration environment like Jenkins. Adding classes is simple:

```
import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class EntryPoint {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { TestNGTitleCheck.class, TestNGLogin.class, TestNGTodo.class , TestNGDD.class});
        
        testng.addListener(tla);
        testng.run();
    }
}

```

***Data Driven Development with TestNG***

Data driven development is becoming more and more popular and necessary, especially . TestNG and Java together give us an easy to work-with platform for importing data from Excel spreadsheets or a database. The example shown below does just that, and the same data can be used in CrossBrowserTesting to perform data-driven development quickly and efficiently:

```
    public Sheet getSpreadSheet() {
        File file = new File("//Path//To//Test.xlsx");
        
        FileInputStream inputStream = null;
        Workbook wb = null;
        try {
            inputStream = new FileInputStream(file);
            wb = WorkbookFactory.create(inputStream);
            System.out.println(wb.toString());
        } catch (IOException ex) {
            System.out.println("Error Message " + ex.getMessage());
        } catch (InvalidFormatException e) {
            System.out.println("Invalid File format!");
        }
    
        Sheet mySheet = wb.getSheet("MySheet");
        
        return mySheet;
    }
    @Test
    public void loginPage() {
        driver.get("http://crossbrowsertesting.github.io/login-form.html");
        
        Sheet mySheet = getSpreadSheet();
        
        String user = mySheet.getRow(0).getCell(0).toString();
        String pass = mySheet.getRow(0).getCell(1).toString();
        
        // the first time around, it should not work!
        driver.findElementByName("username").sendKeys(user);
        
        // then by entering the password
        System.out.println("Entering password");
        driver.findElementByName("password").sendKeys(pass);
        
        // then by clicking the login button
        System.out.println("Logging in");
        driver.findElementByCssSelector("div.form-actions > button").click();
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.textToBePresentInElement(By.xpath("/html/body/div/div/div/div[1]"), "Username or password is incorrect"));
        
        // however, with the correct credentials, it should!
        driver.get("http://crossbrowsertesting.github.io/login-form.html");
        
        user = mySheet.getRow(1).getCell(0).toString();
        pass = mySheet.getRow(1).getCell(1).toString();
        
        driver.findElementByName("username").sendKeys(user);
        
        // then by entering the password
        System.out.println("Entering password");
        driver.findElementByName("password").sendKeys(pass);
        
        // then by clicking the login button
        System.out.println("Logging in");
        driver.findElementByCssSelector("div.form-actions > button").click();
        
        // let's wait here to ensure the page has loaded completely
        wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"logged-in-message\"]/h2")));
        
        String welcomeMessage = driver.findElementByXPath("//*[@id=\"logged-in-message\"]/h2").getText();
        Assert.assertEquals("Welcome tester@crossbrowsertesting.com", welcomeMessage);
        
        System.out.println("TestFinished");
    }
```

We hit some major points here, but there's a ton of documentation out there. I'd definitely recommend checking out [TestNG's documentation](http://testng.org/doc/documentation-main.html) as well as [CBT's own documentation](https://crossbrowsertesting.com/apidocs/v3/selenium.html#!/default/get_selenium_browsers) for working with our API. If you ever have any questions or concerns, don't hesitate to [reach out to us!](mailto:support@crossbrowsertesting.com) 
