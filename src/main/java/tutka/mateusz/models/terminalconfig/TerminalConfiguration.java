package tutka.mateusz.models.terminalconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;

import tutka.mateusz.terminal.UserTerminal;

public class TerminalConfiguration implements Serializable {
	private static final long serialVersionUID = 2L;
	private FontColor fontColor = new FontColor(192, 192, 192);
	private KeyWordsColor keyWordsColor = new KeyWordsColor(192, 192, 192);
	private FontStyle fontStyle = FontStyle.PLAIN;
	private int fontSize = 14;
	
	public TerminalConfiguration(){
		
	}
	public TerminalConfiguration(FontColor fontColor, KeyWordsColor keyWordsColor, FontStyle fontStyle, int fontSize){
		this.fontColor = fontColor;
		this.keyWordsColor = keyWordsColor;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
	}

	public FontStyle getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(FontStyle fontStyle) {
		this.fontStyle = fontStyle;
	}
	public FontColor getFontColor() {
		return fontColor;
	}

	public void setFontColor(FontColor fontColor) {
		this.fontColor = fontColor;
	}

	public KeyWordsColor getKeyWordsColor() {
		return keyWordsColor;
	}

	public void setKeyWordsColor(KeyWordsColor keyWordsColor) {
		this.keyWordsColor = keyWordsColor;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	 public static TerminalConfiguration deserializeConfiguration(){
	    	URL fontColorURL = UserTerminal.class.getResource("/configuration.ser");
	    	ObjectInputStream os = null;
	    	try {
				FileInputStream fis = new FileInputStream(new File(fontColorURL.toURI()));
				os = new ObjectInputStream(fis);
				return (TerminalConfiguration)os.readObject();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    	
	    	return null;
	    }
	 
	 public static void serializeConfiguration(TerminalConfiguration terminalConfiguration){
	    	try {
	    		URL configuration = UserTerminal.class.getResource("/configuration.ser");
	    		FileOutputStream fs = new FileOutputStream(new File(configuration.toURI()));
	    		
				ObjectOutputStream os = new ObjectOutputStream(fs);
				os.writeObject(terminalConfiguration);
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} 
	    	
	    }
}
