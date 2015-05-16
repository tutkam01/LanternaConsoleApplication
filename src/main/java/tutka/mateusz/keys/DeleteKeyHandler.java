package tutka.mateusz.keys;

import java.util.SortedMap;
import java.util.Map.Entry;

import com.googlecode.lanterna.input.KeyStroke;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;
import tutka.mateusz.utils.CommandHandler;

public class DeleteKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		
		CommandHandler commandHandler = new CommandHandler(userTerminal) {

			@Override
			protected Position calculateNewCharactersPosition(UserTerminal userTerminal, Entry<Position, KeyStroke> entry) {
//				Position newPosition;
				
//				if(entry.getKey().getX() == 0 && entry.getKey().getY() > 0){
//					newPosition = new Position(userTerminal.getColumnsNumber()-1, entry.getKey().getY() - 1);
//				}else{
//				    newPosition = new Position(entry.getKey().getX() - 1, entry.getKey().getY());
//				}
				return getPrecedingPosition(entry.getKey());
				
			}

			private void eraseDeletedCharacterFromScreen(UserTerminal userTerminal) {
				Position currentPosition = userTerminal.getCaret().getPosition();
				Position characterToErasePosition = userTerminal.getCurrentCommand().getPositionKeyMap().lastKey();
				userTerminal.getTerminal().setCursorPosition(characterToErasePosition.getX(), characterToErasePosition.getY());
				userTerminal.getTerminal().putCharacter(' ');
				userTerminal.getTerminal().setCursorPosition(currentPosition.getX(), currentPosition.getY());
			}

			@Override
			protected SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal) {
				System.out.println(getCaretPosition(userTerminal));
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
