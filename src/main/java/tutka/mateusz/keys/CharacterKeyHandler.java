package tutka.mateusz.keys;

import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;
import tutka.mateusz.utils.CommandHandler;

import com.googlecode.lanterna.input.KeyStroke;

public class CharacterKeyHandler implements KeyHandler {
	private Set<String> keyWords;
	
	public CharacterKeyHandler(Set<String> keyWords){
		this.keyWords = keyWords;
	}
	
	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		userTerminal.sendCharacterToConsole(keyToHandle);
		userTerminal.shiftCaret();
		if(userTerminal.isCurrentKeySpacebar(keyToHandle)){
			userTerminal.handleKeyWords(keyToHandle);
		}else {
			userTerminal.getWord().addKey(keyToHandle);						
		}
		
		CommandHandler commandHandler = new CommandHandler(userTerminal) {

			@Override
			protected Position calculateNewCharactersPosition(UserTerminal userTerminal, Entry<Position, KeyStroke> entry) {
				Position newPosition;
				if(entry.getKey().getX() == userTerminal.getColumnsNumber()-1){
					newPosition = new Position(0, entry.getKey().getY() + 1);
				}else{
				    newPosition = new Position(entry.getKey().getX() + 1, entry.getKey().getY());
				}
				return newPosition;
			}

			@Override
			protected SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal) {
				return userTerminal.getCurrentCommand().getPositionKeyMap().tailMap(getKeyToHandlePosition(userTerminal));
			}

			@Override
			protected SortedMap<Position, KeyStroke> getNotShiftedPart(UserTerminal userTerminal) {
				return userTerminal.getCurrentCommand().getPositionKeyMap().headMap(getKeyToHandlePosition(userTerminal));
			}
		};
		
		commandHandler.handleCommand(keyToHandle);
	}
	
	
}
