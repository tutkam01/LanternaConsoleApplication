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
 * Application
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
		userTerminal.setVisible(true);
		
		while(true) {
		    Thread.sleep(1);
		    currentKey = userTerminal.readInput();
		    
			if (currentKey != null) {
				System.out.println(getColumnsNumber());
				if (currentKey.getKeyType() == KeyType.Enter) {
					handleEnterKey();
				}else if(currentKey.getKeyType() == KeyType.Escape){
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
	}

	private boolean isCurrentKeySpacebar() {
		return currentKey.getCharacter() == ' ';
	}

	private void shiftCaret() {
		handleEndOfTerminalRow();
		
		userTerminal.getCaret().setX(userTerminal.getCaret().getX() + 1);
		userTerminal.getCaret().setAbsolute_x(userTerminal.getCaret().getAbsolute_x() + 1);
		
		System.out.println("X = " + userTerminal.getCaret().getX());
		System.out.println("Y = " + userTerminal.getCaret().getY());
	}

	private void handleEndOfTerminalRow() {
		if(userTerminal.getCaret().getX() == getColumnsNumber()-1){
			int[] carretPosition = new int[]{-1, (userTerminal.getCaret().getY()<getRowsNumber()-1)?(userTerminal.getCaret().getY() + 1): getRowsNumber()-1};
			userTerminal.getCaret().setX(carretPosition[0]);
			userTerminal.getCaret().setY(carretPosition[1]);
			int[] absoluteCarretPosition = new int[]{-1, userTerminal.getCaret().getAbsolute_y() + 1};
			userTerminal.getCaret().setAbsolute_x(absoluteCarretPosition[0]);
			userTerminal.getCaret().setAbsolute_y(absoluteCarretPosition[1]);
			
			
		}
	}

	private void sendCharacterToConsole() {
		userTerminal.getTerminal().putCharacter(currentKey.getCharacter());
		userTerminal.getTerminal().flush();
	}

	private void handleEnterKey() {
		int[] carretPosition = new int[]{0, (userTerminal.getCaret().getY()<getRowsNumber()-1)?(userTerminal.getCaret().getY() + 1): getRowsNumber()-1};
		int[] absoluteCarretPosition = new int[]{0, userTerminal.getCaret().getAbsolute_y() + 1};
		userTerminal.getTerminal().putCharacter('\n');
		userTerminal.getTerminal().flush();
		userTerminal.getCaret().setX(carretPosition[0]);
		userTerminal.getCaret().setY(carretPosition[1]);
		userTerminal.getCaret().setAbsolute_x(absoluteCarretPosition[0]);
		userTerminal.getCaret().setAbsolute_y(absoluteCarretPosition[1]);
		userTerminal.getWord().resetWord();
	}
	
	private int getColumnsNumber() {
		return userTerminal.getTerminal().getTerminalSize().getColumns();
	}
	
	private int getRowsNumber() {
		return userTerminal.getTerminal().getTerminalSize().getRows();
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
