package tutka.mateusz.keys;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;

public class TabKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		
		for(String keyWord: userTerminal.getKeyWords()){
			String partialString = userTerminal.returnString();
			if(!partialString.isEmpty() && keyWord.startsWith(partialString)){
				
				userTerminal.putString(keyWord);
				
				for(int i = 0; i < keyWord.length() - partialString.length() + 1; i++){
					userTerminal.shiftCaret();
				}
				
				userTerminal.getWord().resetWord();
				
				return;
			}
		}
		
	}

}
