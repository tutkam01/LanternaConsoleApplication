package tutka.mateusz.keys;

import java.util.Map.Entry;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.ConsoleCommand;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

public class ArrowDownKeyHandler extends ArrowUpDownKeyHandler implements KeyHandler {
	private static int currentCommandLines = 0;
	
	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		if(userTerminal.getCommandsHistory().isEmpty()) return;
		counter--;
		if(counter <= 0){
			counter = userTerminal.getCommandsHistory().size();
		}
		
		if(counter > userTerminal.getCommandsHistory().size()){
			counter = 1;
		}
		
		ConsoleCommand commandHistory = userTerminal.getCommandsHistory().get(userTerminal.getCommandsHistory().size() - counter);
		
		int historyCommandLines = 0;
		
		handleHistoryCommand(userTerminal, commandHistory, historyCommandLines);

	}

	private void handleHistoryCommand(UserTerminal userTerminal, ConsoleCommand commandHistory, int historyCommandLines) {
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
				int correctedverticalShifOfStartPoint = potentialVerticalShiftOfStartPoint - verticalShifOfStartPoint;
				verticalShifOfStartPoint =  potentialVerticalShiftOfStartPoint;
				int modifiedX = userTerminal.getCurrentCommand().getCommandStartPosition().getX();
				int modifiedY = userTerminal.getCurrentCommand().getCommandStartPosition().getY() - correctedverticalShifOfStartPoint;
				
				userTerminal.getCurrentCommand().setCommandStartPosition(new Position(modifiedX, modifiedY));

			}
						
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
	
	public static void resetVerticalShiftOfStartPoint(){
		verticalShifOfStartPoint = 0;
	}
	
	public static void resetCurrentCommandLinesNumber(){
		currentCommandLines = 0;
	}

}
