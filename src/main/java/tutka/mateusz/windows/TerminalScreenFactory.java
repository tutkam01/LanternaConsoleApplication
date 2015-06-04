package tutka.mateusz.windows;

import java.io.IOException;

import javax.swing.JFrame;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

public class TerminalScreenFactory {
	private final DefaultTerminalFactory factory;

    public TerminalScreenFactory() {
        this(SwingTerminalFrame.AutoCloseTrigger.CloseOnExitPrivateMode);
    }
    
    public TerminalScreenFactory(SwingTerminalFrame.AutoCloseTrigger autoCloseTrigger) {
        factory = new DefaultTerminalFactory();
        factory.setSwingTerminalFrameAutoCloseTrigger(autoCloseTrigger);
//        factory.setSuppressSwingTerminalFrame(args.length > 0 && "--no-swing".equals(args[0]));
    }
    
    public SwingTerminalFrame createSwingTerminal() {
        try {
            return (SwingTerminalFrame)createTerminal();
        }
        catch(Throwable e) {
            throw new IllegalStateException("Unable to create a SwingTerminalFrame", e);
        }
    }

    public Terminal createTerminal() throws IOException {
        Terminal terminal = factory.createTerminal();
        if(terminal instanceof SwingTerminalFrame) {
            ((SwingTerminalFrame)terminal).setVisible(true);
            ((SwingTerminalFrame)terminal).setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
        return terminal;
    }

    public Screen createScreen() throws IOException {
        return new TerminalScreen(createTerminal());
    }

    public GUIScreen createGUIScreen() throws IOException {
        return new GUIScreen(createScreen());
    }
}
