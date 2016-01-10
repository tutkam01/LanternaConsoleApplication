package tutka.mateusz.lanternaConsoleApplication;

import tutka.mateusz.interfaces.Method;

public class CalculateSquareField implements Method {
    
	public String execute(String... args) throws Exception {
	  Long	squareField;
	  try{	
		  squareField = Long.valueOf(args[0]).longValue() * Long.valueOf(args[1]).longValue();
	  }catch(NumberFormatException e){
		  return "Please provide parameters as integers.. \n test";
	  }
      return "   Square field: " + String.valueOf(squareField);
	}

}
