package tutka.mateusz.keys;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;

public class TabKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		
		String partialString = userTerminal.returnString();
		String match = "";
		int numberOfMatches = 0;
		
		if(partialString.isEmpty()) return;
		
		for(String keyWord: userTerminal.getKeyWords()){
			if(keyWord.startsWith(partialString)){
				numberOfMatches++;
				match = keyWord;
			}
		}
		
		if(numberOfMatches == 1){
			userTerminal.putString(match);
			int numberOfRemainingCharacters = getNumberOfRemainingCharacters(partialString, match);
			
			for(int i = 0; i < numberOfRemainingCharacters; i++){
				userTerminal.shiftCaret();
			}
			
			userTerminal.getWord().resetWord();
		}
		
	}

	private int getNumberOfRemainingCharacters(String partialString, String match) {
		return match.length() - partialString.length() + 1;
	}
	
}
