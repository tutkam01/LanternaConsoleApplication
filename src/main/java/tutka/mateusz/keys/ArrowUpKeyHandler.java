package tutka.mateusz.keys;

import java.util.Map.Entry;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Command;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;

public class ArrowUpKeyHandler implements KeyHandler {
	
	private static int counter = 1;
	private static int verticalShifOfStartPoint = 0;
	private static int currentCommandLines = 0;
	
	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		if(counter > userTerminal.getCommandsHistory().size()){
			counter = 1;
		}
		Command commandHistory = userTerminal.getCommandsHistory().get(userTerminal.getCommandsHistory().size() - counter);
		
		int historyCommandLines = 0;
		
		if(!userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty()){
			historyCommandLines = commandHistory.getPositionKeyMap().size()/userTerminal.getColumnsNumber();
			if(currentCommandLines<userTerminal.getCurrentCommand().getPositionKeyMap().size()/userTerminal.getColumnsNumber()){
				currentCommandLines = userTerminal.getCurrentCommand().getPositionKeyMap().size()/userTerminal.getColumnsNumber();
			}
		}
		if(!userTerminal.getCommandsHistory().isEmpty()) {
			int potentialVerticalShiftOfStartPoint = (userTerminal.getCaret().getY() == userTerminal.getRowsNumber() - 1)?commandHistory.getPositionKeyMap().size()/userTerminal.getColumnsNumber():0;
			
			userTerminal.sendCommandToConsole(commandHistory);
			userTerminal.getCurrentCommand().getPositionKeyMap().clear();
			
			int y_shift = userTerminal.getCurrentCommand().getCommandStartAbsolutePosition().getY()- commandHistory.getCommandStartAbsolutePosition().getY();
			for(Entry<Position, KeyStroke> entry: commandHistory.getPositionKeyMap().entrySet()){
				userTerminal.getCurrentCommand().getPositionKeyMap().put(new Position(entry.getKey().getX(), entry.getKey().getY() + y_shift), entry.getValue());
			}
			
			if(potentialVerticalShiftOfStartPoint > verticalShifOfStartPoint){
				verticalShifOfStartPoint = potentialVerticalShiftOfStartPoint;
				
				int modifiedX = userTerminal.getCurrentCommand().getCommandStartPosition().getX();
				int modifiedY = userTerminal.getCurrentCommand().getCommandStartPosition().getY() - verticalShifOfStartPoint;
				
				userTerminal.getCurrentCommand().setCommandStartPosition(new Position(modifiedX, modifiedY));

			}
						
			counter++;
		}
		
		Position currentCursorPostion = userTerminal.getFollowingPosition(userTerminal.getCurrentCommand().getPositionKeyMap().lastKey());
		userTerminal.getCaret().setX(currentCursorPostion.getX());
		userTerminal.getCaret().setAbsolute_x(currentCursorPostion.getX());
		userTerminal.getCaret().setAbsolute_y(currentCursorPostion.getY());
		
		if((userTerminal.getCaret().getAbsolute_y() > userTerminal.getRowsNumber() - 1)){
			userTerminal.getCaret().setY((currentCommandLines > historyCommandLines)?userTerminal.getRowsNumber() - 1 + historyCommandLines-currentCommandLines: userTerminal.getRowsNumber() - 1);
		}else{
			userTerminal.getCaret().setY(userTerminal.getCaret().getAbsolute_y());
		}
		
		userTerminal.getTerminal().setCursorPosition(userTerminal.getCaret().getX(), userTerminal.getCaret().getY());
		System.out.println(userTerminal.getCurrentCommand().getPositionKeyMap());

	}
	
	public static void resetCounter(){
		counter = 1;
	}
	
	public static void resetVerticalShiftOfStartPoint(){
		verticalShifOfStartPoint = 0;
	}
	
	public static void resetCurrentCommandLinesNumber(){
		currentCommandLines = 0;
	}

}
