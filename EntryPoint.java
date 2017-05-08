package CBTTestNG.TestNG;

import org.testng.TestListenerAdapter;

import com.beust.testng.TestNG;

public class EntryPoint {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { TestNGBasic.class, TestNGLogin.class, TestNGTodo.class , TestNGDD.class});
		
		testng.addListener(tla);
		testng.run();
	}
}
