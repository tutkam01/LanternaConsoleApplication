package tutka.mateusz.console_application;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.interfaces.Method;
import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

/**
 * Application
 *
 */
public class Application {
private Map<String, Method> commandToMethodMap;
private KeyStroke currentKey;	
private UserTerminal userTerminal;
	
	public Application(){
		commandToMethodMap = new HashMap<String, Method>();
	}
	
    public Map<String, Method> getCommandToMethodMap() {
		return commandToMethodMap;
	}

	public void run() throws InterruptedException, IOException{
		userTerminal = new UserTerminal(getKeyWords());
		userTerminal.startUserTerminal();
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
	
	private Set<String> getKeyWords(){
		Set<String> keyWords = new HashSet<String>();
		
		for(String keyWord: commandToMethodMap.keySet()){
			keyWords.add(keyWord);
		}
		
		return keyWords;
	}

}
