package tutka.mateusz.keys;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;

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
		
		handleCommand(userTerminal, keyToHandle);
	}
	
	private void handleCommand(UserTerminal userTerminal, KeyStroke keyToHandle){
		
		if(isCommandNotEmpty(userTerminal) && isCurrentCursorPositionInTheMiddleOfCommand(userTerminal)){
			System.out.println("Caret: " + userTerminal.getCaret().getPosition().toString());
			System.out.println("End command: " + userTerminal.getCurrentCommand().getPositionKeyMap().lastKey().toString());
			
			TreeMap<Position, KeyStroke> commandAfterShift = getCommandAfterShift(userTerminal,keyToHandle);
			userTerminal.getCurrentCommand().getPositionKeyMap().putAll(commandAfterShift);
			
		}else{
			Position position = getKeyToHandlePosition(userTerminal);
			addCurrentCharacterToCommand(userTerminal, keyToHandle, position);
		}
	}

	private void addCurrentCharacterToCommand(UserTerminal userTerminal, KeyStroke keyToHandle,
			Position position) {
		userTerminal.getCurrentCommand().getPositionKeyMap().put(position, keyToHandle);
	}

	private TreeMap<Position, KeyStroke> getCommandAfterShift(UserTerminal userTerminal,
			KeyStroke keyToHandle) {
		SortedMap<Position, KeyStroke> notShiftedPart = getNotShiftedPart(userTerminal);
		SortedMap<Position, KeyStroke> toShiftPart = getToShiftPart(userTerminal);		
		
		TreeMap<Position, KeyStroke> afterShift = new TreeMap<Position, KeyStroke>();
		afterShift.putAll(notShiftedPart);			
		afterShift.put(getKeyToHandlePosition(userTerminal), keyToHandle);			
		shiftToShiftPart(userTerminal, afterShift, toShiftPart);
		setCursorToCurrentPosition(userTerminal);
		return afterShift;
	}

	private boolean isCurrentCursorPositionInTheMiddleOfCommand(UserTerminal userTerminal) {
		return userTerminal.getCaret().getPosition().compareTo(userTerminal.getCurrentCommand().getPositionKeyMap().lastKey())<=0;
	}

	private boolean isCommandNotEmpty(UserTerminal userTerminal) {
		return !userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty();
	}

	private void setCursorToCurrentPosition(UserTerminal userTerminal) {
		userTerminal.getTerminal().setCursorPosition(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
	}

	private Position getKeyToHandlePosition(UserTerminal userTerminal) {
		return new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
	}

	private void shiftToShiftPart(UserTerminal userTerminal,TreeMap<Position, KeyStroke> afterShift, SortedMap<Position, KeyStroke> toShiftPart) {
		for(Map.Entry<Position, KeyStroke> entry: toShiftPart.entrySet()){
			Position newPosition = calculateNewCharactersPosition(userTerminal, entry);
			afterShift.put(newPosition, entry.getValue());
			userTerminal.getTerminal().putCharacter(entry.getValue().getCharacter());
		}
	}

	private Position calculateNewCharactersPosition(UserTerminal userTerminal,
			Map.Entry<Position, KeyStroke> entry) {
		Position newPosition;
		if(entry.getKey().getX() == userTerminal.getColumnsNumber()-1){
			newPosition = new Position(0, entry.getKey().getY() + 1);
		}else{
		    newPosition = new Position(entry.getKey().getX() + 1, entry.getKey().getY());
		}
		return newPosition;
	}

	private SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal) {
		return userTerminal.getCurrentCommand().getPositionKeyMap().tailMap(getKeyToHandlePosition(userTerminal));
	}

	private SortedMap<Position, KeyStroke> getNotShiftedPart(UserTerminal userTerminal) {
		return userTerminal.getCurrentCommand().getPositionKeyMap().headMap(getKeyToHandlePosition(userTerminal));
	}
	
	

}
