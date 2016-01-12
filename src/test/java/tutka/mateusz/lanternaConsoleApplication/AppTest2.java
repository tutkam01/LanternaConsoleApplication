package tutka.mateusz.lanternaConsoleApplication;

import java.io.IOException;

import tutka.mateusz.console_application.Application;

public class AppTest2 {
	public static void main(String[] args) throws InterruptedException, IOException {
		Application application = Application.getInstance()
											 .withApplicationConsoleWelcomeText("Hello console application,   \n this is example of welcome text!")
											 .withHelpText("help text");
		
		application.getApplicationCommandBuilder().withKeyWord("set length")
												  .withKeyWord("set height")
												  .withMethod(new CalculateSquareField()).build();
		
		
		
//    	application.getCommandToMethodMap().put("add node", null);
//    	application.getCommandToMethodMap().put("command", null);
//    	application.getCommandToMethodMap().put("set", null);
//    	application.getCommandToMethodMap().put("as child of", null);
//    	application.getCommandToMethodMap().put("nowa dluga komenda", null);
		 
    	application.run();
	}
}
