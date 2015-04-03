package tutka.mateusz.terminal;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.UIManager;

import tutka.mateusz.models.Caret;
import tutka.mateusz.models.Word;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.ResizeListener;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalColorConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalDeviceConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalDeviceConfiguration.CursorStyle;

@SuppressWarnings("serial")
public class UserTerminal extends JFrame implements ResizeListener{
	private final ScrollingSwingTerminal scrollingSwingTerminal;
	private javax.swing.JPanel panelTerminalContainer;
	private Caret caret;
    private Word word;

    /**
     * Creates new form ScrollingSwingTerminalTest
     */
    public UserTerminal() {
    	this.caret = Caret.getInstance();
    	this.word = new Word();
        initComponents();
        SwingTerminalDeviceConfiguration deviceConfig =  new SwingTerminalDeviceConfiguration(2000, 500, CursorStyle.REVERSED, new TextColor.RGB(255, 255, 255), true).withLineBufferScrollbackSize(150);
        
        scrollingSwingTerminal = new ScrollingSwingTerminal(
        		deviceConfig,
                SwingTerminalFontConfiguration.DEFAULT,
                SwingTerminalColorConfiguration.DEFAULT);
        panelTerminalContainer.add(scrollingSwingTerminal, BorderLayout.CENTER);
        scrollingSwingTerminal.addResizeListener(this);
        pack();
    }
    
    private void initComponents() {
        panelTerminalContainer = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTerminalContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Terminal"));
        panelTerminalContainer.setLayout(new java.awt.BorderLayout());
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTerminalContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        )));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTerminalContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
        ));

        pack();

    }
    
    public String returnString(List<KeyStroke> keys){
    	String string = "";
    	for(KeyStroke key: keys){
    		string = string + key.getCharacter();
    	}
    	return string;
    }
    
    public String returnString(){
    	return returnString(getWord().getKeys());
    }
    
    private String changeStyle(String stringToFormat){
    	return stringToFormat.toUpperCase();
    }
    
    private List<Character> returnCharacters(String givenString){
    	List<Character> characters = new ArrayList<Character>();
    	for(int i=0; i<givenString.length(); i++){
    		characters.add(givenString.charAt(i));
    	}
    	return characters;
    }
    
    public void putString(String stringToSend){
    	List<Character> characters = returnCharacters(changeStyle(stringToSend));
    	scrollingSwingTerminal.setCursorPosition(this.word.getStartCaretPosition(), this.caret.getY());
    	scrollingSwingTerminal.enableSGR(SGR.BOLD);
		for(Character character: characters){
			scrollingSwingTerminal.putCharacter(character);
		}
		scrollingSwingTerminal.disableSGR(SGR.BOLD);
		scrollingSwingTerminal.putCharacter(' ');
    	
    	
    }
    
	public KeyStroke readInput() throws IOException {
		return scrollingSwingTerminal.readInput();
	}
    
    public ScrollingSwingTerminal getTerminal(){
    	return scrollingSwingTerminal;
    }
    
    public Caret getCaret(){
    	return this.caret;
    }

	public Word getWord() {
		return this.word;
	}
	
	public void onResized(Terminal terminal, TerminalSize newSize) {
//		scrollingSwingTerminal.setCursorPosition(caret.getX(), caret.getY());
//		scrollingSwingTerminal.setCursorPosition(22, 2);
		scrollingSwingTerminal.setCursorPosition(caret.getAbsolute_x(), caret.getAbsolute_y());
		caret.setY(caret.getAbsolute_y());
	}
    
    public void startUserTerminal() {
        /* Set the Nimbus look and feel */
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch(javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
			public void run() {
                new UserTerminal().setVisible(true);
            }
        });
    }

	
    
    
}
