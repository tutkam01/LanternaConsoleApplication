package tutka.mateusz.keys;

import java.util.Set;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;

public class CharacterKeyHandler implements KeyHandler {
	private Set<String> keyWords;
	
	public CharacterKeyHandler(Set<String> keyWords){
		this.keyWords = keyWords;
	}
	
	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		userTerminal.sendCharacterToConsole(keyToHandle);
		userTerminal.shiftCaret();
		if(userTerminal.isCurrentKeySpacebar(keyToHandle)){
			userTerminal.handleKeyWords(keyToHandle);
		}else {
			userTerminal.getWord().addKey(keyToHandle);						
		}
	}

}
