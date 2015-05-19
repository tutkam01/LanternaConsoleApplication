package tutka.mateusz.models;

import java.util.TreeMap;

import com.googlecode.lanterna.input.KeyStroke;

public class Command {
	private TreeMap<Position, KeyStroke> postionKeyMap;
	
	public Command(){
		postionKeyMap = new TreeMap<Position, KeyStroke>();
	}
	
	public TreeMap<Position, KeyStroke> getPositionKeyMap(){
		return postionKeyMap;
	}
	
}


