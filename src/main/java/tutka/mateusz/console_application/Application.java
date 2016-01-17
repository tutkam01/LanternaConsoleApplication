package tutka.mateusz.console_application;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.interfaces.Method;
import tutka.mateusz.keys.F1KeyHandler;
import tutka.mateusz.models.ApplicationCommand;
import tutka.mateusz.models.Caret;
import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

/**
 * Application
 *
 */
public class Application {
private Map<String, Method> commandToMethodMap;
private Set<String> keyWords = new HashSet<String>();
private KeyStroke currentKey;	
private UserTerminal userTerminal;
private static Application application;
private String applicationConsoleWelcomeText;
private String helpText;
	
	private Application(){
		commandToMethodMap = new HashMap<String, Method>();
	}
	
	public static Application getInstance(){
		if(application!=null){
			return application;
		}
		
		application =  new Application();
		
		return application;
	}
	
	public Application withApplicationConsoleWelcomeText(String welcomeText){
		this.applicationConsoleWelcomeText = welcomeText;
		return this;
	}
	
	public Application withHelpText(String helpText){
		this.helpText = helpText;
		return this;
	}
	
    public ApplicationCommandBuilder getApplicationCommandBuilder(){
    	return new ApplicationCommandBuilder();
    }

	public void run() throws InterruptedException, IOException{
		userTerminal = new UserTerminal(getKeyWords());
		userTerminal.getKeys().put(KeyType.F1, new F1KeyHandler(helpText));
		userTerminal.startUserTerminal();
		if(StringUtils.isNotBlank(applicationConsoleWelcomeText)){
			userTerminal.showApplicationWelcomeText(applicationConsoleWelcomeText + "\n");
			userTerminal.getCurrentCommand().setCommandStartPosition(Caret.getInstance().getPosition());
			userTerminal.getCurrentCommand().setCommandStartAbsolutePosition(Caret.getInstance().getAbsolutePosition());
		}
		handleInputStream();
	}

	private void handleInputStream() throws InterruptedException, IOException {
		while(true) {
			try{
//		    Thread.sleep(1);
		    currentKey = userTerminal.readInput();
			}catch(Exception e){
				System.out.println(e);
			}
		    
		    for(Map.Entry<KeyType, KeyHandler> entry: userTerminal.getKeys().entrySet()){
		    	if(currentKey != null && currentKey.getKeyType().equals(entry.getKey())){
		    		entry.getValue().handleKey(currentKey, userTerminal);
		    	}
		    }
		    
		}
	}
	
	public Set<String> getKeyWords(){
//		Set<String> keyWords = new HashSet<String>();
//		
//		for(String keyWord: commandToMethodMap.keySet()){
//			keyWords.add(keyWord);
//		}
		
		return keyWords;
	}
	
	
	public class ApplicationCommandBuilder {
		private ApplicationCommand builtCommand = new ApplicationCommand();
		
		public ApplicationCommandBuilder withKeyWord(String keyWord){
			builtCommand.getCommandKeyWords().add(keyWord);
			return this;
		}
		
		public ApplicationCommandBuilder withMethod(Method methodToCall){
			builtCommand.setCalledMethod(methodToCall);
			return this;
		}
		
		public void build(){
			keyWords.addAll(builtCommand.getCommandKeyWords());
			
			StringBuilder commandMask = new StringBuilder();
			for(String keyWord: builtCommand.getCommandKeyWords()){
				commandMask.append(keyWord).append(" (.+) ");
			}
			
			commandToMethodMap.put(commandMask.toString(), builtCommand.getCalledMethod());
		}

	}


	public Map<String, Method> getCommandToMethodMap() {
		return commandToMethodMap;
	}
	
	

}
