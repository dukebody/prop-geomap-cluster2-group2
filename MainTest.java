import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class MainTest {
	public static void main(String[] args) {
		TestSuite suite = new TestSuite();
		TestRunner runner = new TestRunner();
		suite.addTestSuite(TrieTest.class);
        //suite.addTestSuite(BordersFileParserTest.class);
        suite.addTestSuite(CountryControllerTest.class);
        suite.addTestSuite(BorderPointTest.class);
        suite.addTestSuite(ToponymTest.class);
        suite.addTestSuite(ToponymsControllerTest.class);
        //suite.addTestSuite(PointTest.class);
        org.junit.runner.JUnitCore.main("TrieTest", "BordersFileParserTest", "BorderPointTest", "PointTest", "ToponymTest", "ToponymsControllerTest");
		//runner.doRun(suite);
	}

}
