package tutka.mateusz.models;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.input.KeyStroke;

public class Word {
	private List<KeyStroke> wordKeys;
	int startCaretPosition;
	int endCaretPosition;

	public Word() {
		this.wordKeys = new ArrayList<KeyStroke>();
		this.startCaretPosition = 0;
	}

	public List<KeyStroke> getKeys() {
		return this.wordKeys;
	}
	
	public void resetWord(){
		this.wordKeys.clear();
		this.startCaretPosition = Caret.getInstance().getX();
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
}
