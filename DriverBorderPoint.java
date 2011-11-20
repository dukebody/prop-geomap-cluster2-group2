import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class DriverBorderPoint {
	public static void main(String[] args) {
		TestSuite suite = new TestSuite();
		TestRunner runner = new TestRunner();
		suite.addTestSuite(BorderPointTest.class);
		runner.doRun(suite);
	}

}
