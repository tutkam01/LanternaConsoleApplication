package tutka.mateusz.keys;

import java.awt.event.WindowEvent;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;

public class EscapeKeyHandler implements KeyHandler{

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		userTerminal.dispatchEvent(new WindowEvent(userTerminal, WindowEvent.WINDOW_CLOSING));		
	}

}
