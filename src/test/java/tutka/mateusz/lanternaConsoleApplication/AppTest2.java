package tutka.mateusz.lanternaConsoleApplication;

import java.io.IOException;

import tutka.mateusz.console_application.Application;

public class AppTest2 {
	public static void main(String[] args) throws InterruptedException, IOException {
		Application application = new Application();
    	application.getCommandToMethodMap().put("add node", null);
    	application.getCommandToMethodMap().put("command", null);
		 
    	application.run();
	}
}
