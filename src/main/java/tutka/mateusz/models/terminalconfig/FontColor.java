package tutka.mateusz.models.terminalconfig;

import java.io.Serializable;

public class FontColor implements Serializable{
	private static final long serialVersionUID = 1L;
	private int R;
	private int G;
	private int B;
	
	public FontColor(int R, int G, int B){
		this.R = R;
		this.G = G;
		this.B = B;
	}
	
	public int getR() {
		return R;
	}
	public int getG() {
		return G;
	}
	public int getB() {
		return B;
	}
	
	public void setR(int r) {
		R = r;
	}
	
	public void setG(int g) {
		G = g;
	}
	
	public void setB(int b) {
		B = b;
	}
	
}
