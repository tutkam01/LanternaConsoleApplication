package tutka.mateusz.keys;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

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
			handleCommandShift(userTerminal);
		}else {
			userTerminal.getWord().addKey(keyToHandle);						
		}
		
		Position position = new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
		userTerminal.getCurrentCommand().getPositionKeyMap().put(position, keyToHandle);
	}
	
	private void handleCommandShift(UserTerminal userTerminal){
		

		if(userTerminal.getCaret().getPosition().compareTo(userTerminal.getCurrentCommand().getPositionKeyMap().lastKey())<=0){
			TreeMap<Position, KeyStroke> afterShift = new TreeMap<Position, KeyStroke>();
			SortedMap<Position, KeyStroke> notShiftedPart = userTerminal.getCurrentCommand().getPositionKeyMap().headMap(new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY()));
			afterShift.putAll(notShiftedPart);
			
			afterShift.put(new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY()), new KeyStroke(' ', false, false));
			
			SortedMap<Position, KeyStroke> shiftedPart = userTerminal.getCurrentCommand().getPositionKeyMap().tailMap(new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY()));
			
			for(Map.Entry<Position, KeyStroke> entry: shiftedPart.entrySet()){
				Position newPosition = new Position(entry.getKey().getX() + 1, entry.getKey().getY());
				afterShift.put(newPosition, entry.getValue());
				//zmenic polozenie kursora?
				userTerminal.getTerminal().putCharacter(entry.getValue().getCharacter());
			}
			userTerminal.getTerminal().setCursorPosition(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
			userTerminal.getCurrentCommand().getPositionKeyMap().putAll(afterShift);
			
		}
	}

}
