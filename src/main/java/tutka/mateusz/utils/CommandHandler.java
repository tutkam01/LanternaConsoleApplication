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
	
   public void handleCommand(KeyStroke keyToHandle, boolean addCurrentKeyToShiftedPart){
		
		if(userTerminal.isCommandNotEmpty() && userTerminal.isCurrentCursorPositionInTheMiddleOfCommand()){
			System.out.println("Caret: " + userTerminal.getCaret().getPosition().toString());
			System.out.println("End command: " + userTerminal.getCurrentCommand().getPositionKeyMap().lastKey().toString());
			
			TreeMap<Position, KeyStroke> commandAfterShift = getCommandAfterShift(userTerminal, keyToHandle, addCurrentKeyToShiftedPart);
			userTerminal.getCurrentCommand().getPositionKeyMap().clear();
			userTerminal.getCurrentCommand().getPositionKeyMap().putAll(commandAfterShift);
			
		}else{
//			Position position =  new Position(getKeyToHandlePosition(userTerminal).getX() - 1, getKeyToHandlePosition(userTerminal).getY());
			addCurrentCharacterToCommand(userTerminal, keyToHandle, getPrecedingPosition(getCaretPosition(userTerminal)));
		}
	}
   
   
   private void addCurrentCharacterToCommand(UserTerminal userTerminal, KeyStroke keyToHandle, Position position) {
		userTerminal.getCurrentCommand().getPositionKeyMap().put(position, keyToHandle);
	}

  protected TreeMap<Position, KeyStroke> getCommandAfterShift(UserTerminal userTerminal, KeyStroke keyToHandle, boolean addCurrentKeyToShiftedPart) {
		SortedMap<Position, KeyStroke> notShiftedPart = getNotShiftedPart(userTerminal);
		SortedMap<Position, KeyStroke> toShiftPart = getToShiftPart(userTerminal);		
		
		TreeMap<Position, KeyStroke> afterShift = new TreeMap<Position, KeyStroke>();
		afterShift.putAll(notShiftedPart);			
		addCurrentKeyToShifedPart(afterShift, keyToHandle, addCurrentKeyToShiftedPart);
		shiftToShiftPart(userTerminal, afterShift, toShiftPart);
		setCursorToCurrentPosition(userTerminal);
		return afterShift;
	}

	private void addCurrentKeyToShifedPart(TreeMap<Position, KeyStroke> afterShift, KeyStroke keyToHandle, boolean addCurrentKeyToShiftedPart) {
		if(addCurrentKeyToShiftedPart) afterShift.put(getPrecedingPosition(getCaretPosition(userTerminal)), keyToHandle);
	}

	private void setCursorToCurrentPosition(UserTerminal userTerminal) {
		userTerminal.getTerminal().setCursorPosition(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
	}

	public Position getCaretPosition(UserTerminal userTerminal) {
		return new Position(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
	}
	
	public Position getPrecedingPosition(Position referencePosition){
		if(referencePosition.getX() == 0 && referencePosition.getY() > 0){
			return new Position(userTerminal.getColumnsNumber() - 1, referencePosition.getY() - 1);
		}
			
		return new Position(referencePosition.getX() - 1, referencePosition.getY());
	}
	
	public Position getFollowingPosition(Position referencePosition){
		if(referencePosition.getX() == userTerminal.getColumnsNumber()-1){
			return new Position(0, referencePosition.getY() + 1);
		}
			
		return new Position(referencePosition.getX() + 1, referencePosition.getY());
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
