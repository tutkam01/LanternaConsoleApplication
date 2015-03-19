package tutka.mateusz.lanternaConsoleApplication;

import java.io.IOException;

import tutka.mateusz.console_application.Application;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     * @throws InterruptedException 
     */
    public void testApp() throws InterruptedException, IOException
    {
//        assertTrue( true );
    	Application application = new Application();
    	application.getCommandToMethodMap().put("add node", null);
    	application.getCommandToMethodMap().put("command", null);
		 
    	application.run();
    }
}
