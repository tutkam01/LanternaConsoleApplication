package tutka.mateusz.keys;

import java.util.SortedMap;
import java.util.Map.Entry;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;
import tutka.mateusz.utils.ConsoleCommandHandler;

public class DeleteKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		ConsoleCommandHandler commandHandler = new ConsoleCommandHandler(userTerminal) {

			@Override
			protected Position calculateNewCharactersPosition(UserTerminal userTerminal, Entry<Position, KeyStroke> entry) {
				return getPrecedingPosition(entry.getKey());
			}

			private void eraseDeletedCharacterFromScreen(UserTerminal userTerminal) {
				Position currentPosition = userTerminal.getCaret().getPosition();
				Position characterToErasePosition = userTerminal.getCurrentCommand().getPositionKeyMap().lastKey();
				userTerminal.getTerminal().setCursorPosition(characterToErasePosition.getX(), (userTerminal.getCaret().getY()<userTerminal.getRowsNumber()-1)?userTerminal.getCaret().getY(): userTerminal.getRowsNumber()-1);
				userTerminal.getTerminal().putCharacter(' ');
				userTerminal.getTerminal().setCursorPosition(currentPosition.getX(), (userTerminal.getCaret().getY()<userTerminal.getRowsNumber()-1)?userTerminal.getCaret().getY(): userTerminal.getRowsNumber()-1);
			}

			@Override
			protected SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal) {
				eraseDeletedCharacterFromScreen(userTerminal);
				userTerminal.getCurrentCommand().getPositionKeyMap().remove(getCaretPosition(userTerminal));
				return userTerminal.getCurrentCommand().getPositionKeyMap().tailMap(getFollowingPosition(getCaretPosition(userTerminal)));
			}

			@Override
			protected SortedMap<Position, KeyStroke> getNotShiftedPart(UserTerminal userTerminal) {
				return userTerminal.getCurrentCommand().getPositionKeyMap().headMap(getCaretPosition(userTerminal));
			}
		};
		
		commandHandler.handleCommand(keyToHandle, false);

	}

}
