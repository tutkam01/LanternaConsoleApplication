package tutka.mateusz.models.terminalconfig;

public enum FontStyle {
	PLAIN(0), BOLD(1), ITALIC(2);
	
	private int style;
	private FontStyle(int style){
		this.style = style;
	}
	public int getStyle(){
		return style;
	}
}
