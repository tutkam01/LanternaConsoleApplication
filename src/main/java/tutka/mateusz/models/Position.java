package tutka.mateusz.models;

public class Position implements Comparable<Position> {
	private int x;
	private int y;
	
	public Position(int x, int y){
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

	public int compareTo(Position arg0) {
		if(this.y == arg0.y){
			if(this.x == arg0.x){
				return 0;
			}else if(this.x < arg0.x){
				return -1;
			}else{
				return 1;
			}
		}else{
			if(this.y < arg0.y){
				return -1;
			}
			
			return 1;
		}
	}
	
	@Override
	public String toString(){
		return "[" + x + ", " + y + "]"; 
	}

	
}
