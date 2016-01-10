package tutka.mateusz.models;

import java.util.Map.Entry;
import java.util.TreeMap;

import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;

public class ConsoleCommand {
	private TreeMap<Position, KeyStroke> postionKeyMap;
	private Position commandStartPosition;
	private Position commandStartAbsolutePosition;
	
	public ConsoleCommand(){
		postionKeyMap = new TreeMap<Position, KeyStroke>();
		commandStartPosition = new Position(0, 0);
		commandStartAbsolutePosition = new Position(0, 0);
	}
	
	public ConsoleCommand(ConsoleCommand command){
		this();
		
		for(Entry<Position, KeyStroke> entry: command.getPositionKeyMap().entrySet()){
			postionKeyMap.put(entry.getKey(), entry.getValue());
		}
		
		commandStartPosition = command.getCommandStartPosition();
		commandStartAbsolutePosition =  command.getCommandStartAbsolutePosition();
	}
	
	public String toString(){
		StringBuffer commandString = new StringBuffer();
		for(KeyStroke keyStroke: postionKeyMap.values()){
			commandString.append(keyStroke.getCharacter());
		}
		
		return commandString.toString();
	}
	
	public Position getCommandStartPosition() {
		return commandStartPosition;
	}

	public void setCommandStartPosition(Position commandStartPosition) {
		this.commandStartPosition = commandStartPosition;
	}

	public Position getCommandStartAbsolutePosition() {
		return commandStartAbsolutePosition;
	}

	public void setCommandStartAbsolutePosition(
			Position commandStartAbsolutePosition) {
		this.commandStartAbsolutePosition = commandStartAbsolutePosition;
	}

	
	public TreeMap<Position, KeyStroke> getPositionKeyMap(){
		return postionKeyMap;
	}
	
	
}


