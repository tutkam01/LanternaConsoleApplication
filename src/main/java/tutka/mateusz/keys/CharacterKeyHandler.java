package tutka.mateusz.keys;

import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;
import tutka.mateusz.utils.ConsoleCommandHandler;

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
		
		ArrowUpKeyHandler.resetCounter();
		ConsoleCommandHandler commandHandler = new ConsoleCommandHandler(userTerminal) {

			@Override
			protected Position calculateNewCharactersPosition(UserTerminal userTerminal, Entry<Position, KeyStroke> entry) {
				return getFollowingPosition(entry.getKey());
			}

			@Override
			protected SortedMap<Position, KeyStroke> getToShiftPart(UserTerminal userTerminal) {
				System.out.println(getCaretPosition(userTerminal));
				return userTerminal.getCurrentCommand().getPositionKeyMap().tailMap(getPrecedingPosition(getCaretPosition(userTerminal)), true);
			}

			@Override
			protected SortedMap<Position, KeyStroke> getNotShiftedPart(UserTerminal userTerminal) {
				System.out.println(getCaretPosition(userTerminal));
				return userTerminal.getCurrentCommand().getPositionKeyMap().headMap(getPrecedingPosition(getCaretPosition(userTerminal)), false);
			}
		};
		
		commandHandler.handleCommand(keyToHandle, true);
	}
	
	
}
