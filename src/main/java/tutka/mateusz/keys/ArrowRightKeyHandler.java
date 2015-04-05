package tutka.mateusz.keys;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;

public class ArrowRightKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		userTerminal.moveCursorBy(1, 0);
	}

}
