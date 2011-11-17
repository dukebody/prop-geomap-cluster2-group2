import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class MainTest {
	public static void main(String[] args) {
		TestSuite suite = new TestSuite();
		TestRunner runner = new TestRunner();
		//suite.addTestSuite(TrieTest.class);
        //suite.addTestSuite(BordersFileParserTest.class);
        suite.addTestSuite(CountryControllerTest.class);
		runner.doRun(suite);
	}

}
