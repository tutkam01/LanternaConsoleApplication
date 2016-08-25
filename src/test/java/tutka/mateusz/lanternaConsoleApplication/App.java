package tutka.mateusz.lanternaConsoleApplication;

import java.io.IOException;

import tutka.mateusz.console_application.Application;
/**
 * This is example how to use this project to build your own console application.
 * Please refer also to {@link CalculateRectanguleSquareField} and {@link CalculateCircleSquareField}
 */
public class App{
	public static void main(String[] args) throws InterruptedException, IOException {
		Application application = Application.getInstance()
											 .withHeight(Application.DEFAULT)
											 .withLength(900)
											 .withApplicationConsoleWelcomeText("Hello console application,   \n this is example of welcome text!")
											 .withHelpText("To calculate square field for \n"
											 		     + " RECTANGLE: set length x set height y, where both x and y are integer parameters. \n" +
													       " CIRCLE: set radius, where radius is integer parameter.");
		
		application.getApplicationCommandBuilder().withKeyWord("set length")
												  .withKeyWord("set height")
												  .withMethod(new CalculateRectanguleSquareField()).build();
		
		application.getApplicationCommandBuilder().withKeyWord("set radius")
		                                          .withMethod(new CalculateCircleSquareField())
		                                          .build();
		
		application.run();
	}
}
