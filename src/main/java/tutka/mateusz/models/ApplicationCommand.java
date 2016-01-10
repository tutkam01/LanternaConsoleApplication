package tutka.mateusz.models;

import java.util.ArrayList;
import java.util.List;

import tutka.mateusz.interfaces.Method;

public class ApplicationCommand {
	private List<String> commandKeyWords = new ArrayList<String>();
	private Method calledMethod;
	
	public List<String> getCommandKeyWords() {
		return commandKeyWords;
	}
	public Method getCalledMethod() {
		return calledMethod;
	}
	
	public void setCalledMethod(Method calledMethod){
		this.calledMethod = calledMethod;
	}
	
}
