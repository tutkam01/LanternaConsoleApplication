package tutka.mateusz.keys;

import java.util.Map;
import java.util.TreeMap;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Command;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

public class EnterKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		int[] carretPosition = new int[]{0, (userTerminal.getCaret().getY()<userTerminal.getRowsNumber()-1)?(userTerminal.getCaret().getY() + 1): userTerminal.getRowsNumber()-1};
		int[] absoluteCarretPosition = new int[]{0, userTerminal.getCaret().getAbsolute_y() + 1};
		userTerminal.getTerminal().putCharacter('\n');
		userTerminal.getTerminal().flush();
		userTerminal.getCaret().setX(carretPosition[0]);
		userTerminal.getCaret().setY(carretPosition[1]);
		userTerminal.getCaret().setAbsolute_x(absoluteCarretPosition[0]);
		userTerminal.getCaret().setAbsolute_y(absoluteCarretPosition[1]);
		userTerminal.getWord().resetWord();
		
		
		
		if(!userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty()) userTerminal.getCommandsHistory().add(new Command(userTerminal.getCurrentCommand()));
		ArrowUpKeyHandler.resetCounter();
		ArrowUpKeyHandler.resetVerticalShiftOfStartPoint();
		ArrowUpKeyHandler.resetCurrentCommandLinesNumber();
//		System.out.println(userTerminal.getCurrentCommand().getPositionKeyMap());
		userTerminal.getCurrentCommand().getPositionKeyMap().clear();
		
		userTerminal.getCurrentCommand().setCommandStartPosition(new Position(carretPosition[0], carretPosition[1]));
		userTerminal.getCurrentCommand().setCommandStartAbsolutePosition(new Position(absoluteCarretPosition[0], absoluteCarretPosition[1]));

	}

}
