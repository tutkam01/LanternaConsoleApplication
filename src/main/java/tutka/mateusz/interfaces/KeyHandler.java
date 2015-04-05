package tutka.mateusz.interfaces;

import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;

public interface KeyHandler {
	void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal);
}
