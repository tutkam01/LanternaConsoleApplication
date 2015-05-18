package tutka.mateusz.keys;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.input.KeyStroke;

public class KeyToHandle extends KeyStroke{
	private SGR layout;
	
	public KeyToHandle(Character character, SGR layout, boolean ctrlDown, boolean altDown) {
		super(character, ctrlDown, altDown);
		this.layout = layout;
	}
	
	public SGR getLayout(){
		return layout;
	}

}
