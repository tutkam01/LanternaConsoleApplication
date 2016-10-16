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

import org.apache.commons.lang3.StringUtils;

import tutka.mateusz.terminal.UserTerminal;

public class TerminalConfiguration implements Serializable {
	private static final long serialVersionUID = 2L;
	private FontColor fontColor = new FontColor(192, 192, 192);
	private KeyWordsColor keyWordsColor = new KeyWordsColor(192, 192, 192);
	private FontStyle fontStyle = FontStyle.PLAIN;
	private int fontSize = 14;
	private static String configFilePath = System.getProperty("terminalConfigFilePath");
	
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
	    	ObjectInputStream os = null;
	    	try {
				FileInputStream fis = new FileInputStream(new File(configFilePath + File.separator + "configuration.ser"));
				os = new ObjectInputStream(fis);
				return (TerminalConfiguration)os.readObject();
			} catch (FileNotFoundException e) {
				return new TerminalConfiguration();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(os != null)	os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	    	
	    	return null;
	    }
	 
	 public static void serializeConfiguration(TerminalConfiguration terminalConfiguration){
	    	try {
	    		FileOutputStream fs = new FileOutputStream(configFilePath + File.separator + "configuration.ser");
	    		
				ObjectOutputStream os = new ObjectOutputStream(fs);
				os.writeObject(terminalConfiguration);
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			} 
	    	
	    }
}
