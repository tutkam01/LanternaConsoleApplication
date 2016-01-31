package tutka.mateusz.terminal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.lang3.StringUtils;

import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.keys.ArrowDownKeyHandler;
import tutka.mateusz.keys.ArrowLeftKeyHandler;
import tutka.mateusz.keys.ArrowRightKeyHandler;
import tutka.mateusz.keys.ArrowUpKeyHandler;
import tutka.mateusz.keys.BackspaceKeyHandler;
import tutka.mateusz.keys.CharacterKeyHandler;
import tutka.mateusz.keys.DeleteKeyHandler;
import tutka.mateusz.keys.EnterKeyHandler;
import tutka.mateusz.keys.EscapeKeyHandler;
import tutka.mateusz.keys.F1KeyHandler;
import tutka.mateusz.keys.F2KeyHandler;
import tutka.mateusz.keys.HighlightedKey;
import tutka.mateusz.keys.TabKeyHandler;
import tutka.mateusz.models.Caret;
import tutka.mateusz.models.ConsoleCommand;
import tutka.mateusz.models.Position;
import tutka.mateusz.models.Word;
import tutka.mateusz.models.terminalconfig.FontStyle;
import tutka.mateusz.models.terminalconfig.TerminalConfiguration;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.TextColor.RGB;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.ResizeListener;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.ScrollingSwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalColorConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalDeviceConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalDeviceConfiguration.CursorStyle;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

@SuppressWarnings("serial")
public class UserTerminal extends JFrame implements ResizeListener{
	private final ScrollingSwingTerminal scrollingSwingTerminal;
	private javax.swing.JPanel panelTerminalContainer;
	private Caret caret;
    private Word word;
    private Map<KeyType, KeyHandler> keys;
    private Set<String> keyWords;
    private ArrayList<ConsoleCommand> commandsHistory;
    private ConsoleCommand currentCommand;
    private TerminalConfiguration terminalConfiguration;
    private int height;
    private int length;    
    private boolean isPrivateMode = false;

	/**
     * Creates new user's terminal.
     */	
    public UserTerminal(Set<String> keyWords, int height, int length) {
    	this.caret = Caret.getInstance();
    	this.word = new Word();
    	this.keyWords = keyWords;
    	this.commandsHistory = new ArrayList<ConsoleCommand>();
    	this.currentCommand = new ConsoleCommand();
    	this.height = height;
    	this.length = length;
    	
    	keys = new HashMap<KeyType, KeyHandler>();
    	keys.put(KeyType.Enter, new EnterKeyHandler());
    	keys.put(KeyType.ArrowLeft, new ArrowLeftKeyHandler());
    	keys.put(KeyType.ArrowRight, new ArrowRightKeyHandler());
    	keys.put(KeyType.Escape, new EscapeKeyHandler());
    	keys.put(KeyType.Character, new CharacterKeyHandler(keyWords));
    	keys.put(KeyType.Delete, new DeleteKeyHandler());
    	keys.put(KeyType.Backspace, new BackspaceKeyHandler());
    	keys.put(KeyType.ArrowUp, new ArrowUpKeyHandler());
    	keys.put(KeyType.ArrowDown, new ArrowDownKeyHandler());
    	keys.put(KeyType.F2, new F2KeyHandler());
//    	keys.put(KeyType.F1, new F1KeyHandler());
    	keys.put(KeyType.Tab, new TabKeyHandler());
    	
        initComponents();
//        terminalConfiguration = new TerminalConfiguration();
        terminalConfiguration = TerminalConfiguration.deserializeConfiguration();
//        terminalConfiguration.setFontStyle(FontStyle.PLAIN);
//        terminalConfiguration = new TerminalConfiguration();
//        terminalConfiguration.getFontColor().setR(192);
//        TerminalConfiguration.serializeConfiguration(terminalConfiguration);
        SwingTerminalDeviceConfiguration deviceConfig =  new SwingTerminalDeviceConfiguration(2000, 500, CursorStyle.UNDER_BAR, getFontColorRGBschema(), true).withLineBufferScrollbackSize(150);
        
        scrollingSwingTerminal = new ScrollingSwingTerminal(
        		deviceConfig,
//                SwingTerminalFontConfiguration.newInstance(new Font("Courier New", terminalConfiguration.getFontStyle().getStyle(), terminalConfiguration.getFontSize())),
        		SwingTerminalFontConfiguration.getDefaultFontConfigWithCustomSize(terminalConfiguration.getFontSize(), terminalConfiguration.getFontStyle().getStyle()),
                SwingTerminalColorConfiguration.DEFAULT);
        scrollingSwingTerminal.setForegroundColor(getFontColorRGBschema());
        panelTerminalContainer.add(scrollingSwingTerminal, BorderLayout.CENTER);
        scrollingSwingTerminal.addResizeListener(this);
        pack();
    }
    
	private RGB getFontColorRGBschema() {
		return new TextColor.RGB(terminalConfiguration.getFontColor().getR(), terminalConfiguration.getFontColor().getG(), terminalConfiguration.getFontColor().getB());
	}
	private RGB getKeyWordsColorRGBschema() {
		return new TextColor.RGB(terminalConfiguration.getKeyWordsColor().getR(), terminalConfiguration.getKeyWordsColor().getG(), terminalConfiguration.getKeyWordsColor().getB());
	}
	
	public TerminalConfiguration getTerminalConfiguration(){
		return this.terminalConfiguration;
	}
    
    public ArrayList<ConsoleCommand> getCommandsHistory() {
		return commandsHistory;
	}

	public ConsoleCommand getCurrentCommand() {
		return currentCommand;
	}
	
	public void setCurrentCommand(ConsoleCommand command){
		this.currentCommand = command;
	}
	
	public boolean isPrivateModeEnabled(){
		return isPrivateMode;
	}

	private void initComponents() {
        panelTerminalContainer = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTerminalContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Terminal, hit F2 to customise."));
        panelTerminalContainer.setLayout(new java.awt.BorderLayout());
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTerminalContainer, javax.swing.GroupLayout.DEFAULT_SIZE, length, Short.MAX_VALUE)
        )));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTerminalContainer, javax.swing.GroupLayout.DEFAULT_SIZE, height, Short.MAX_VALUE)
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
    
    private String changeStyleToBold(String stringToFormat){
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
    	List<Character> characters = returnCharacters(changeStyleToBold(stringToSend));
    	try{
//    		scrollingSwingTerminal.setCursorPosition(word.getStartCaretPosition(), caret.getY());
    		scrollingSwingTerminal.setCursorPosition(word.getStartPosition().getX(), word.getStartPosition().getY());
    	}catch(IndexOutOfBoundsException e){
    		scrollingSwingTerminal.clearScreen();
    		scrollingSwingTerminal.setCursorPosition(0, 0);
    		caret.setX(0);
    		caret.setY(0);
    		caret.setAbsolute_x(0);
    		caret.setAbsolute_y(0);
    		
    	}
    	scrollingSwingTerminal.enableSGR(SGR.BOLD);
    	scrollingSwingTerminal.setForegroundColor(getKeyWordsColorRGBschema());
    	
//    	Position position = new Position(word.getStartCaretPosition(), caret.getAbsolute_y());
    	Position position = new Position(word.getStartPosition().getX(), word.getStartAbsolutePosition().getY());
		for(Character character: characters){
			scrollingSwingTerminal.putCharacter(character);
			
			getCurrentCommand().getPositionKeyMap().put(position, new HighlightedKey(character, SGR.BOLD, false, false));
			position = getFollowingPosition(position);
		}
		scrollingSwingTerminal.disableSGR(SGR.BOLD);
		scrollingSwingTerminal.setForegroundColor(getFontColorRGBschema());
		scrollingSwingTerminal.putCharacter(' ');
		getCurrentCommand().getPositionKeyMap().put(position, new KeyStroke(' ', false, false));
    	
    	
    }
    
    public void sendResultToConsole(String result){
    	sendTextToConsole(result);
    }
    
    public void showApplicationWelcomeText(String welcomeText){
    	sendTextToConsole(" " + welcomeText);
    	breakLine();
    }
    
    public void showHelp(String helpText){
//    	scrollingSwingTerminal.clearScreen();
    	for(int i=0;i<helpText.length();i++){
    		scrollingSwingTerminal.putCharacter(helpText.charAt(i));
    		handleEndOfTerminalRow();
    	}
    	scrollingSwingTerminal.putCharacter('\n');
    	scrollingSwingTerminal.putCharacter('\n');
//    	sendTextToConsole(helpText);
//    	breakLine();
//    	breakLine();
    }
    
	private void sendTextToConsole(String text) {
		List<Character> characters = returnCharacters(text);
    	for(Character character: characters){
    		if(character == '\n'){
    			breakLine();
    		}else{
    			scrollingSwingTerminal.putCharacter(character);
    			scrollingSwingTerminal.flush();
    			shiftCaret();
    		}
    	}
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
	
	public void sendCommandToConsole(ConsoleCommand command){
		
		scrollingSwingTerminal.setCursorPosition(getCurrentCommand().getCommandStartPosition().getX(), getCurrentCommand().getCommandStartPosition().getY());
		
		for(Entry<Position, KeyStroke> entry: command.getPositionKeyMap().entrySet()){
			if(entry.getValue() instanceof HighlightedKey){
				scrollingSwingTerminal.enableSGR(SGR.BOLD);
				scrollingSwingTerminal.setForegroundColor(getKeyWordsColorRGBschema());
				scrollingSwingTerminal.putCharacter(entry.getValue().getCharacter());
				scrollingSwingTerminal.disableSGR(SGR.BOLD);
				scrollingSwingTerminal.setForegroundColor(getFontColorRGBschema());
			}else{
				scrollingSwingTerminal.putCharacter(entry.getValue().getCharacter());
			}
		}
		
		if(!currentCommand.getPositionKeyMap().isEmpty() && command.getPositionKeyMap().size()<(currentCommand.getPositionKeyMap().size())){
			int numberOfCharactersToErase = currentCommand.getPositionKeyMap().size() - command.getPositionKeyMap().size();
			
			for(int i=0; i<numberOfCharactersToErase; i++){
				scrollingSwingTerminal.putCharacter(' ');
			}
			
		}
		
	}
	
	public void setCaretPosition(Position position){
		scrollingSwingTerminal.setCursorPosition(position.getX(), position.getY());
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
			getCurrentCommand().getCommandStartPosition().setX(0);
			getCurrentCommand().getCommandStartPosition().setY(caret.getAbsolute_y());
			getCommandsHistory().clear();
		}catch(IndexOutOfBoundsException e){
			//terminal shrinked hiding cursor
			caret.setY(getRowsNumber()-1);
			getCurrentCommand().getCommandStartPosition().setY(getRowsNumber()-1);
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
		return getPrecedingPosition(caret.getAbsolutePosition()).compareTo(currentCommand.getPositionKeyMap().lastKey())<=0;
	}
	
    public Position getCaretPosition(){
    	return caret.getAbsolutePosition();
    }
    
    public void breakLine(){
    	int[] carretPosition = new int[]{0, (caret.getY()<getRowsNumber()-1)?(caret.getY() + 1): getRowsNumber()-1};
		int[] absoluteCarretPosition = new int[]{0, caret.getAbsolute_y() + 1};
		scrollingSwingTerminal.putCharacter('\n');
		scrollingSwingTerminal.flush();
		caret.setX(carretPosition[0]);
		caret.setY(carretPosition[1]);
		caret.setAbsolute_x(absoluteCarretPosition[0]);
		caret.setAbsolute_y(absoluteCarretPosition[1]);
		word.resetWord();
    }
    
    public void enterPrivateMode(){
    	isPrivateMode = true;
    	scrollingSwingTerminal.enterPrivateMode();
    }
    
    public void exitPrivateMode(){
    	isPrivateMode = false;
    	scrollingSwingTerminal.exitPrivateMode();
    }
    
    private void resetCaret(){
    	caret.setAbsolute_x(0);
    	caret.setAbsolute_y(0);
    	caret.setX(0);
    	caret.setY(0);
    }
    
    public void clearScreen() {
    	int beforeCleanSize = scrollingSwingTerminal.getSwingTerminal().getVirtualTerminal().getCurrentTextBuffer().getLineBuffer().size();
    	scrollingSwingTerminal.getSwingTerminal().getVirtualTerminal().getCurrentTextBuffer().getLineBuffer().clear();
    	for(int line = 0; line < beforeCleanSize; line++){
    		ArrayList<TextCharacter> newLine = new ArrayList<TextCharacter>(200);
            newLine.add(TextCharacter.DEFAULT_CHARACTER);
    		scrollingSwingTerminal.getSwingTerminal().getVirtualTerminal().getCurrentTextBuffer().getLineBuffer().add(newLine);
    	}
    	System.out.println(String.format("before clean y: %s", caret.getY()));
    	System.out.println(String.format("before clean abs_y: %s", caret.getAbsolute_y()));
    	moveCursorBy(0, -caret.getY());
    	resetCaret();
    	System.out.println(String.format("after clean x: %s", caret.getX()));
    	System.out.println(String.format("after clean y: %s", caret.getY()));
    	System.out.println(String.format("after clean abs_x: %s", caret.getAbsolute_x()));
    	System.out.println(String.format("after clean abs_y: %s", caret.getAbsolute_y()));
    	 SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				scrollingSwingTerminal.repaint();
				
			}
		});
    	
//    	scrollingSwingTerminal.clearScreen();
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

	public Set<String> getKeyWords() {
		return keyWords;
	}


	
    
    
}
