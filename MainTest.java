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
        suite.addTestSuite(CitiesControllerTest.class);
        suite.addTestSuite(QuadTreeTest.class);
        //suite.addTestSuite(PointTest.class);
        runner.doRun(suite);
        org.junit.runner.JUnitCore.main("PointTest", "BordersFileParserTest");
    }

}
