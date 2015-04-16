package tutka.mateusz.keys;

import java.util.Set;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Position;
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
		
		Position position = new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
		userTerminal.getCurrentCommand().getPositionKeyMap().put(position, keyToHandle);
	}
	
	private void handleCommandShift(UserTerminal userTerminal){
		if(userTerminal.getCaret().getPosition().compareTo(userTerminal.getCurrentCommand().getPositionKeyMap().lastKey())<=0){
			
		}
	}

}
