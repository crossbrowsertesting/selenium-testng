package TestNG;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

//Optional EntryPoint file for CI/CD

public class EntryPoint {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { TestNGBasic.class, TestNGLogin.class, TestNGTodo.class});
		
		testng.addListener(tla);
		testng.run();
	}
}
