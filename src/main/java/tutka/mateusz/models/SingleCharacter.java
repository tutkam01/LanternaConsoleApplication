package tutka.mateusz.models;

import com.googlecode.lanterna.input.KeyStroke;
//not used
public class SingleCharacter {
	private int x;
	private int y;
	private KeyStroke keyStroke;
	
	public SingleCharacter(int x, int y, KeyStroke keyStroke){
		this.x = x;
		this.y = y;
		this.keyStroke = keyStroke;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
}
