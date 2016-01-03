package tutka.mateusz.models;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.input.KeyStroke;

public class Word {
	private List<KeyStroke> wordKeys;
	int startCaretPosition;
	int endCaretPosition;
	
	Position startPosition;
	Position endPosition;
	Position startAbsolutePosition;
	Position endAbsolutePosition;
	
	

	public Word() {
		this.wordKeys = new ArrayList<KeyStroke>();
		this.startCaretPosition = 0;
		
		startPosition = new Position(0, 0);
		endPosition = new Position(0, 0);
		startAbsolutePosition = new Position(0,0);
		endAbsolutePosition = new Position(0, 0);
	}

	public List<KeyStroke> getKeys() {
		return this.wordKeys;
	}
	
	public void resetWord(){
		this.wordKeys.clear();
		this.startCaretPosition = Caret.getInstance().getX();
		
		startPosition = Caret.getInstance().getPosition();
		endPosition = Caret.getInstance().getPosition();
		startAbsolutePosition = Caret.getInstance().getAbsolutePosition();
		endAbsolutePosition = Caret.getInstance().getAbsolutePosition();
	}
	
	public void addKey(KeyStroke key){
		this.wordKeys.add(key);
	}
	
	public int getStartCaretPosition() {
		return startCaretPosition;
	}

	public void setStartCaretPosition(int startCaretPosition) {
		this.startCaretPosition = startCaretPosition;
	}

	public int getEndCaretPosition() {
		return endCaretPosition;
	}

	public void setEndCaretPosition(int endCaretPosition) {
		this.endCaretPosition = endCaretPosition;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public Position getEndPosition() {
		return endPosition;
	}

	public Position getStartAbsolutePosition() {
		return startAbsolutePosition;
	}

	public Position getEndAbsolutePosition() {
		return endAbsolutePosition;
	}
}
