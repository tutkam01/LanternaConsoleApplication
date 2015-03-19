package tutka.mateusz.console_application;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tutka.mateusz.interfaces.Method;
import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

/**
 * Hello world!
 *
 */
public class Application {
private Map<String, Method> commandToMethodMap;
private List<KeyStroke> keys;
private KeyStroke currentKey;	
private UserTerminal userTerminal;
	
	public Application(){
		keys = new ArrayList<KeyStroke>();
		userTerminal = new UserTerminal();
		commandToMethodMap = new HashMap<String, Method>();
	}
	
    public Map<String, Method> getCommandToMethodMap() {
		return commandToMethodMap;
	}

	public void run() throws InterruptedException, IOException{
//		userTerminal.startUserTerminal();
		userTerminal.setVisible(true);
		
		while(true) {
		    Thread.sleep(1);
		    currentKey = userTerminal.readInput();
			if (currentKey != null) {
				if (currentKey.getKeyType() == KeyType.Enter) {
					handleEnterKey();
				}else if(currentKey.getKeyType() == KeyType.Escape){
//					userTerminal.setVisible(false);
					userTerminal.dispatchEvent(new WindowEvent(userTerminal, WindowEvent.WINDOW_CLOSING));
					break;
				}else{
					sendCharacterToConsole();
					shiftCaret();
					keys.add(currentKey);
					if(isCurrentKeySpacebar()){
						handleKeyWords();
					}else {
						userTerminal.getWord().addKey(currentKey);						
					}
				}
			}   
		     
		 }
		System.out.println("koniec");
	}

	private boolean isCurrentKeySpacebar() {
		return currentKey.getCharacter() == ' ';
	}

	private void shiftCaret() {
		userTerminal.getCaret().setX(userTerminal.getCaret().getX() + 1);
	}

	private void sendCharacterToConsole() {
		userTerminal.getTerminal().putCharacter(currentKey.getCharacter());
		userTerminal.getTerminal().flush();
	}

//	private void exitProgramme() {
//		try {
//			userTerminal.getScreen().stopScreen();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void handleEnterKey() {
		int[] carretPosition = new int[]{0, (userTerminal.getCaret().getY()<16)?(userTerminal.getCaret().getY() + 1): 16};
//		userTerminal.getTerminal().setCursorPosition(carretPosition[0], carretPosition[1]);
		System.out.println(userTerminal.getTerminal().getCursor().toString());
		System.out.println(userTerminal.getTerminal().getTerminalSize());
		userTerminal.getTerminal().putCharacter('\n');
		userTerminal.getTerminal().flush();
		userTerminal.getCaret().setX(carretPosition[0]);
		userTerminal.getCaret().setY(carretPosition[1]);
		userTerminal.getWord().resetWord();
	}
	
	private void handleKeyWords(){
		for(String keyWord: commandToMethodMap.keySet()){
			if(keyWord.contains(" ")){
				for(String word: getSubKeyWords(keyWord)){
					if(getInputString().equalsIgnoreCase(word)){
						userTerminal.getWord().addKey(currentKey);
						return;
					}
				}					
			}
			
			if(getInputString().equalsIgnoreCase(keyWord)){
				userTerminal.putString(getInputString());
				userTerminal.getWord().resetWord();
				return;
			}
		}
		
		userTerminal.getWord().resetWord();
	}

	private List<String> getSubKeyWords(String keyWord) {
		List<String> subKeyWords = new ArrayList<String>();
		String[] parts = keyWord.split(" ");
		String subKeyWord = parts[0];
		subKeyWords.add(subKeyWord);
		for(int i=1;i<parts.length-1;i++){
			subKeyWord = subKeyWord + " " + parts[i];
			subKeyWords.add(subKeyWord);
		}
		return subKeyWords;
	}
	
	private String getInputString(){
		return userTerminal.returnString();
	}
}
