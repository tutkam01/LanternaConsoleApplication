package tutka.mateusz.utils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

public abstract class CommandHandler {
	private UserTerminal userTerminal;
	
	public CommandHandler(UserTerminal userTerminal){
		this.userTerminal = userTerminal;
	}
	
   public void handleCommand(KeyStroke keyToHandle){
		
		if(userTerminal.isCommandNotEmpty() && userTerminal.isCurrentCursorPositionInTheMiddleOfCommand()){
			System.out.println("Caret: " + userTerminal.getCaret().getPosition().toString());
			System.out.println("End command: " + userTerminal.getCurrentCommand().getPositionKeyMap().lastKey().toString());
			
			TreeMap<Position, KeyStroke> commandAfterShift = getCommandAfterShift(userTerminal,keyToHandle);
			userTerminal.getCurrentCommand().getPositionKeyMap().putAll(commandAfterShift);
			
		}else{
			Position position = getKeyToHandlePosition(userTerminal);
			addCurrentCharacterToCommand(userTerminal, keyToHandle, position);
		}
	}
   
   
   private void addCurrentCharacterToCommand(UserTerminal userTerminal, KeyStroke keyToHandle, Position position) {
		userTerminal.getCurrentCommand().getPositionKeyMap().put(position, keyToHandle);
	}

	private TreeMap<Position, KeyStroke> getCommandAfterShift(UserTerminal userTerminal, KeyStroke keyToHandle) {
		SortedMap<Position, KeyStroke> notShiftedPart = getNotShiftedPart(userTerminal);
		SortedMap<Position, KeyStroke> toShiftPart = getToShiftPart(userTerminal);		
		
		TreeMap<Position, KeyStroke> afterShift = new TreeMap<Position, KeyStroke>();
		afterShift.putAll(notShiftedPart);			
		afterShift.put(getKeyToHandlePosition(userTerminal), keyToHandle);			
		shiftToShiftPart(userTerminal, afterShift, toShiftPart);
		setCursorToCurrentPosition(userTerminal);
		return afterShift;
	}

	private void setCursorToCurrentPosition(UserTerminal userTerminal) {
		userTerminal.getTerminal().setCursorPosition(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
	}

	public Position getKeyToHandlePosition(UserTerminal userTerminal) {
		return new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
	}

	private void shiftToShiftPart(UserTerminal userTerminal,TreeMap<Position, KeyStroke> afterShift, SortedMap<Position, KeyStroke> toShiftPart) {
		for(Map.Entry<Position, KeyStroke> entry: toShiftPart.entrySet()){
			Position newPosition = calculateNewCharactersPosition(userTerminal, entry);
			afterShift.put(newPosition, entry.getValue());
			userTerminal.getTerminal().putCharacter(entry.getValue().getCharacter());
		}
	}

	protected abstract Position calculateNewCharactersPosition(UserTerminal userTerminal, Map.Entry<Position, KeyStroke> entry);
	protected abstract SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal);
	protected abstract SortedMap<Position, KeyStroke> getNotShiftedPart(UserTerminal userTerminal);	
	
 
}
