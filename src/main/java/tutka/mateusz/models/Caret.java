package tutka.mateusz.models;


public class Caret {
	private static final Caret caretInstance = new Caret(0,0);
	private int x;
	private int y;
	private int absolute_x;
	private int absolute_y;
	private Position position;
	
	public static Caret getInstance(){
		return caretInstance;
	}
	
	
	private Caret(int x, int y){
		this.x = x;
		this.y = y;
		this.absolute_x = x;
		this.absolute_y = y;
	}
	
	public Position getPosition(){
		return new Position(x, y);
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
	
	public int getAbsolute_x() {
		return absolute_x;
	}


	public void setAbsolute_x(int absolute_x) {
		this.absolute_x = absolute_x;
	}


	public int getAbsolute_y() {
		return absolute_y;
	}


	public void setAbsolute_y(int absolute_y) {
		this.absolute_y = absolute_y;
	}
	
	public Position getPrecedingPosition(int columnsNumber){
		if(x == 0 && y > 0){
			return new Position(columnsNumber - 1, y - 1);
		}
			
		return new Position(x - 1, y);
	}
	
	public Position getFollowingPosition(int columnsNumber){
		if(x ==columnsNumber - 1){
			return new Position(0, y + 1);
		}
			
		return new Position(x + 1, y);
	}
	
}
