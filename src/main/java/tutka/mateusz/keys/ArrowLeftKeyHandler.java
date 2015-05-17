package tutka.mateusz.keys;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;

public class ArrowLeftKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		if(userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty() || userTerminal.getCaretPosition().compareTo(userTerminal.getCurrentCommand().getPositionKeyMap().firstKey()) == 0) return;
		
		if(userTerminal.getCaret().getX() == 0 && userTerminal.getCaret().getY()!=0){
			int[] carretPosition = new int[]{userTerminal.getColumnsNumber()-1, userTerminal.getCaret().getY() - 1};
			userTerminal.getCaret().setX(carretPosition[0]);
			userTerminal.getCaret().setY(carretPosition[1]);
			int[] absoluteCarretPosition = new int[]{userTerminal.getColumnsNumber()-1, userTerminal.getCaret().getAbsolute_y() - 1};
			userTerminal.getCaret().setAbsolute_x(absoluteCarretPosition[0]);
			userTerminal.getCaret().setAbsolute_y(absoluteCarretPosition[1]);
			
			userTerminal.getTerminal().setCursorPosition(carretPosition[0], carretPosition[1]);
		}else{
			userTerminal.moveCursorBy(-1, 0);
		}
		
		userTerminal.getWord().resetWord();
		
	}

}
