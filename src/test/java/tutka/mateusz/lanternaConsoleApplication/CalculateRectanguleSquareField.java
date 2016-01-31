package tutka.mateusz.lanternaConsoleApplication;

import tutka.mateusz.interfaces.Method;

public class CalculateRectanguleSquareField implements Method {
    
	public String execute(String... args) throws Exception {
	  Long	squareField;
	  try{	
		  squareField = Long.parseLong(args[0]) * Long.parseLong(args[1]);
	  }catch(NumberFormatException e){
		  return "Please provide parameters as integers.. \n For example set length 3 set height 6";
	  }
      return "   Square field: " + String.valueOf(squareField);
	}

}
