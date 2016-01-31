package tutka.mateusz.keys;

import org.apache.commons.lang3.StringUtils;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Caret;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

public class F1KeyHandler implements KeyHandler {
	private static final String EXIT_HINT = "Press F1 to leave help mode..";
	private boolean privateMode = false;
	private Position initialPosition;
	
	String helpText;
	public F1KeyHandler(String helpText){
		this.helpText = helpText;
	}
	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		if(privateMode){
			userTerminal.getTerminal().getSwingTerminal().getVirtualTerminal().getCurrentTextBuffer().getLineBuffer().clear();
			userTerminal.exitPrivateMode();
			userTerminal.setCaretPosition(initialPosition);
			
			privateMode = false;
		}else{
			
				initialPosition = Caret.getInstance().getPosition();
				userTerminal.enterPrivateMode();
				userTerminal.setCaretPosition(new Position(0, 0));
				if(StringUtils.isNotBlank(helpText)){
					userTerminal.showHelp(helpText + "\n" + EXIT_HINT);
				}else{
					userTerminal.showHelp(EXIT_HINT);
				}
				privateMode = true;
			
		}
	}

}
