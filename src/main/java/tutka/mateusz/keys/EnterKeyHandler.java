package tutka.mateusz.keys;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
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
		
		userTerminal.getCommandsHistory().add(userTerminal.getCurrentCommand());
		userTerminal.getCurrentCommand().getPositionKeyMap().clear();

	}

}
