package tutka.mateusz.keys;

import java.io.IOException;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.terminal.UserTerminal;
import tutka.mateusz.windows.TerminalConfigWindow;

public class F2KeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		try {
			new TerminalConfigWindow(userTerminal.getTerminalConfiguration()).run();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
