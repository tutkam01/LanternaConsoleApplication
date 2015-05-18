package tutka.mateusz.terminal;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.UIManager;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.keys.ArrowLeftKeyHandler;
import tutka.mateusz.keys.ArrowRightKeyHandler;
import tutka.mateusz.keys.CharacterKeyHandler;
import tutka.mateusz.keys.DeleteKeyHandler;
import tutka.mateusz.keys.EnterKeyHandler;
import tutka.mateusz.keys.EscapeKeyHandler;
import tutka.mateusz.keys.KeyToHandle;
import tutka.mateusz.models.Caret;
import tutka.mateusz.models.Command;
import tutka.mateusz.models.Position;
import tutka.mateusz.models.Word;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
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
    private Map<KeyType, KeyHandler> keys;
    private Set<String> keyWords;
    private ArrayList<Command> commandsHistory;
    private Command currentCommand;
    
    

    /**
     * Creates new user's terminal.
     */	
    public UserTerminal(Set<String> keyWords) {
    	this.caret = Caret.getInstance();
    	this.word = new Word();
    	this.keyWords = keyWords;
    	this.commandsHistory = new ArrayList<Command>();
    	this.currentCommand = new Command();
    	
    	keys = new HashMap<KeyType, KeyHandler>();
    	keys.put(KeyType.Enter, new EnterKeyHandler());
    	keys.put(KeyType.ArrowLeft, new ArrowLeftKeyHandler());
    	keys.put(KeyType.ArrowRight, new ArrowRightKeyHandler());
    	keys.put(KeyType.Escape, new EscapeKeyHandler());
    	keys.put(KeyType.Character, new CharacterKeyHandler(keyWords));
    	keys.put(KeyType.Delete, new DeleteKeyHandler());
    	
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
    
    public ArrayList<Command> getCommandsHistory() {
		return commandsHistory;
	}

	public Command getCurrentCommand() {
		return currentCommand;
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
    	try{
    		scrollingSwingTerminal.setCursorPosition(word.getStartCaretPosition(), caret.getY());
    	}catch(IndexOutOfBoundsException e){
    		scrollingSwingTerminal.clearScreen();
    		scrollingSwingTerminal.setCursorPosition(0, 0);
    		caret.setX(0);
    		caret.setY(0);
    		caret.setAbsolute_x(0);
    		caret.setAbsolute_y(0);
    		
    	}
    	scrollingSwingTerminal.enableSGR(SGR.BOLD);
    	
    	Position position = new Position(word.getStartCaretPosition(), caret.getY());
		for(Character character: characters){
			scrollingSwingTerminal.putCharacter(character);
			
			getCurrentCommand().getPositionKeyMap().put(position, new KeyToHandle(character, SGR.BOLD, false, false));
			position = getFollowingPosition(position);
		}
		scrollingSwingTerminal.disableSGR(SGR.BOLD);
		scrollingSwingTerminal.putCharacter(' ');
    	
    	
    }
    
    public Position getPrecedingPosition(Position referencePosition){
		if(referencePosition.getX() == 0 && referencePosition.getY() > 0){
			return new Position(getColumnsNumber() - 1, referencePosition.getY() - 1);
		}
			
		return new Position(referencePosition.getX() - 1, referencePosition.getY());
		
	}
    
    public Position getFollowingPosition(Position referencePosition){
		if(referencePosition.getX() == getColumnsNumber()-1){
			return new Position(0, referencePosition.getY() + 1);
		}
			
		return new Position(referencePosition.getX() + 1, referencePosition.getY());
	}
    
	public void sendCharacterToConsole(KeyStroke currentKey) {
		scrollingSwingTerminal.putCharacter(currentKey.getCharacter());
		scrollingSwingTerminal.flush();
	}
	
	public void shiftCaret() {
		handleEndOfTerminalRow();
		
		caret.setX(caret.getX() + 1);
		caret.setAbsolute_x(caret.getAbsolute_x() + 1);
		
		System.out.println("X = " + caret.getX());
		System.out.println("Y = " + caret.getY());
	}
	
	public void handleKeyWords(KeyStroke currentKey){
		for(String keyWord: keyWords){
			if(keyWord.contains(" ")){
				for(String wordString: getSubKeyWords(keyWord)){
					if(returnString().equalsIgnoreCase(wordString)){
						word.addKey(currentKey);
						return;
					}
				}					
			}
			
			if(returnString().equalsIgnoreCase(keyWord)){
				putString(returnString());
				getWord().resetWord();
				return;
			}
		}
		
		getWord().resetWord();
	}
	
	private List<String> getSubKeyWords(String keyWord) {
		List<String> subKeyWords = new ArrayList<String>();
		String[] parts = keyWord.split(" ");
		String subKeyWord = parts[0];
		subKeyWords.add(subKeyWord);
		for(int i=1;i<parts.length-1;i++){
			subKeyWord = subKeyWord + " " + parts[i];
			subKeyWords.add(subKeyWord);
		}
		return subKeyWords;
	}
	
	private void handleEndOfTerminalRow() {
		if(caret.getX() == getColumnsNumber()-1){
			int[] carretPosition = new int[]{-1, (caret.getY()<getRowsNumber()-1)?(caret.getY() + 1): getRowsNumber()-1};
			caret.setX(carretPosition[0]);
			caret.setY(carretPosition[1]);
			int[] absoluteCarretPosition = new int[]{-1, caret.getAbsolute_y() + 1};
			caret.setAbsolute_x(absoluteCarretPosition[0]);
			caret.setAbsolute_y(absoluteCarretPosition[1]);
		}
	}
    
    public void moveCursorBy(int shift_x, int shift_y){
		int[] carretPosition = new int[]{caret.getX() + shift_x, caret.getY() + shift_y};
		caret.setX(carretPosition[0]);
		caret.setY(carretPosition[1]);
		int[] absoluteCarretPosition = new int[]{caret.getAbsolute_x() + shift_x, caret.getAbsolute_y() + shift_y};
		caret.setAbsolute_x(absoluteCarretPosition[0]);
		caret.setAbsolute_y(absoluteCarretPosition[1]);
		
		try{
			scrollingSwingTerminal.setCursorPosition(carretPosition[0], carretPosition[1]);
		}catch(IndexOutOfBoundsException e){
			//position out of terminal's boundery.
			System.out.println(e);
		}
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
	
	public Map<KeyType, KeyHandler> getKeys(){
		return keys;
	}
	
	public void onResized(Terminal terminal, TerminalSize newSize) {
		try{
			scrollingSwingTerminal.setCursorPosition(caret.getAbsolute_x(), caret.getAbsolute_y());
		}catch(IndexOutOfBoundsException e){
			//terminal shrinked hiding cursor
			caret.setY(getRowsNumber()-1);
		}
		caret.setY(caret.getAbsolute_y());
	}
	
	public int getRowsNumber() {
		return scrollingSwingTerminal.getTerminalSize().getRows();
	}
	
	public int getColumnsNumber() {
		return scrollingSwingTerminal.getTerminalSize().getColumns();
	}
	
	public boolean isCurrentKeySpacebar(KeyStroke currentKey) {
		return currentKey.getCharacter() == ' ';
	}
	
	public boolean isCommandNotEmpty() {
		return !currentCommand.getPositionKeyMap().isEmpty();
	}
	
	public boolean isCurrentCursorPositionInTheMiddleOfCommand() {
		return caret.getPosition().compareTo(currentCommand.getPositionKeyMap().lastKey())<=0;
	}
	
    public Position getCaretPosition(){
    	return new Position(getCaret().getX(), getCaret().getY());
    }
    public void startUserTerminal() {
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
        
        setVisible(true);
    }

	
    
    
}
