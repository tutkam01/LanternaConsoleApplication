package tutka.mateusz.lanternaConsoleApplication;

import tutka.mateusz.interfaces.Method;

/**
 * This is example how to provide implementation of your calculation method, fulfilling {@link tutka.mateusz.interfaces.Method} contract.
 *
 */
public class CalculateCircleSquareField implements Method {

	public String execute(String... args) throws Exception {
		long radius;
		try{
			radius = Long.parseLong(args[0]);
		}catch(NumberFormatException e){
			return "Please provide radius as integer.. \n For example set radius 100";
		}
		return String.valueOf(String.format("Square field of circle is: %f", Math.PI * radius * radius));
	}

}
