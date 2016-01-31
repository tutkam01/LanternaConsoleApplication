package tutka.mateusz.lanternaConsoleApplication;

import java.io.IOException;

import tutka.mateusz.console_application.Application;

public class AppTest2 {
	public static void main(String[] args) throws InterruptedException, IOException {
		Application application = Application.getInstance()
											 .withHeight(Application.DEFAULT)
											 .withLength(Application.DEFAULT)
											 .withApplicationConsoleWelcomeText("Hello console application,   \n this is example of welcome text!")
											 .withHelpText("To calculate square field for \n"
											 		     + " RECTANGLE: set length x set height y, where both x and y are integer parameters");
		
		application.getApplicationCommandBuilder().withKeyWord("set length")
												  .withKeyWord("set height")
												  .withMethod(new CalculateSquareField()).build();
		
		application.run();
	}
}
