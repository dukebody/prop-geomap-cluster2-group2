import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class DriverToponym {
	public static void main(String[] args) {
		TestSuite suite = new TestSuite();
		TestRunner runner = new TestRunner();
		suite.addTestSuite(ToponymTest.class);
		runner.doRun(suite);
	}

}
