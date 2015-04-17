package tutka.mateusz.models;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public Position getLastPosition(){
		Position lastPosition = null;
		Iterator<Entry<Position, KeyStroke>> iterator = postionKeyMap.entrySet().iterator();
		
		while(iterator.hasNext()){
			lastPosition = iterator.next().getKey();
		}
		
		return lastPosition;
	}
	
}


