package tutka.mateusz.windows;

import java.io.IOException;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.TextGUIThread;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;

public abstract class CommonWindow {
	public void run() throws IOException, InterruptedException {
	        Screen screen = new TerminalScreenFactory().createScreen();
	        screen.startScreen();
	        MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
	        textGUI.setBlockingIO(false);
	        textGUI.setEOFWhenNoWindows(true);
	        textGUI.isEOFWhenNoWindows();   //No meaning, just to silence IntelliJ:s "is never used" alert

	        try {
	            init(textGUI);
	            TextGUIThread guiThread = textGUI.getGUIThread();
	            guiThread.start();
	            guiThread.waitForStop();
	        }
	        finally {
	            screen.stopScreen();
	        }
	    }

	    public abstract void init(WindowBasedTextGUI textGUI);
}
