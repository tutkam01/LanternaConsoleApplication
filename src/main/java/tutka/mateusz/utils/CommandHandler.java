package tutka.mateusz.utils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.keys.HighlightedKey;
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
		return userTerminal.getCaretPosition();
	}
	
	public Position getPrecedingPosition(Position referencePosition){
		return userTerminal.getPrecedingPosition(referencePosition);
	}
	
	public Position getFollowingPosition(Position referencePosition){
		return userTerminal.getFollowingPosition(referencePosition);
	}

	private void shiftToShiftPart(UserTerminal userTerminal,TreeMap<Position, KeyStroke> afterShift, SortedMap<Position, KeyStroke> toShiftPart) {
		for(Map.Entry<Position, KeyStroke> entry: toShiftPart.entrySet()){
			Position newPosition = calculateNewCharactersPosition(userTerminal, entry);
			afterShift.put(newPosition, entry.getValue());
			sendCharatcerBackToConsole(userTerminal, entry);
		}
		
		
	}

	private void sendCharatcerBackToConsole(UserTerminal userTerminal, 	Map.Entry<Position, KeyStroke> entry) {
		if(entry.getValue() instanceof HighlightedKey){
			highlightCharacter(userTerminal, entry);
		}else{
			userTerminal.getTerminal().putCharacter(entry.getValue().getCharacter());
		}
	}

	private void highlightCharacter(UserTerminal userTerminal, Map.Entry<Position, KeyStroke> entry) {
		HighlightedKey specialKey = (HighlightedKey)entry.getValue();
		if(specialKey.getLayout() != null){
			userTerminal.getTerminal().enableSGR(specialKey.getLayout());
			userTerminal.getTerminal().putCharacter(entry.getValue().getCharacter());
			userTerminal.getTerminal().disableSGR(specialKey.getLayout());
		}
	}
	
		

	protected abstract Position calculateNewCharactersPosition(UserTerminal userTerminal, Map.Entry<Position, KeyStroke> entry);
	protected abstract SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal);
	protected abstract SortedMap<Position, KeyStroke> getNotShiftedPart(UserTerminal userTerminal);	
	
 
}
