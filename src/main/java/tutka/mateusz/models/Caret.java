package tutka.mateusz.models;


public class Caret {
	private static final Caret caretInstance = new Caret(0,0);
	private int x;
	private int y;
	
	public static Caret getInstance(){
		return caretInstance;
	}
	
	
	private Caret(int x, int y){
		this.x = x;
		this.y = y;
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
