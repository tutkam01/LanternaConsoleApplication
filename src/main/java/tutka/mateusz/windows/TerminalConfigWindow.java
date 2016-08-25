package tutka.mateusz.windows;

import java.io.IOException;

import tutka.mateusz.models.terminalconfig.FontStyle;
import tutka.mateusz.models.terminalconfig.TerminalConfiguration;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Border;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Panels;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

public class TerminalConfigWindow extends CommonWindow {
	private TerminalConfiguration terminalConfiguration;
	
	public TerminalConfigWindow(TerminalConfiguration terminalConfiguration){
		this.terminalConfiguration = terminalConfiguration;
	}

    @Override
    public void init(WindowBasedTextGUI textGUI) {
        final BasicWindow window = new BasicWindow("Terminal configuration panel");
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        Panel leftPanel = new Panel();
        Panel middlePanel = new Panel();
        Panel rightPanel = new Panel();
        
        final TextBox fontColor_R = new TextBox();
        fontColor_R.setText(String.valueOf(terminalConfiguration.getFontColor().getR()));
        final TextBox fontColor_G = new TextBox();
        fontColor_G.setText(String.valueOf(terminalConfiguration.getFontColor().getG()));
        final TextBox fontColor_B = new TextBox();
        fontColor_B.setText(String.valueOf(terminalConfiguration.getFontColor().getB()));
        
        leftPanel.addComponent(fontColor_R.withBorder(Borders.singleLine("R")));
        leftPanel.addComponent(fontColor_G.withBorder(Borders.singleLine("G")));
        leftPanel.addComponent(fontColor_B.withBorder(Borders.singleLine("B")));
        
        final TextBox keyWordColor_R = new TextBox();
        keyWordColor_R.setText(String.valueOf(terminalConfiguration.getKeyWordsColor().getR()));
        final TextBox keyWordColor_G = new TextBox();
        keyWordColor_G.setText(String.valueOf(terminalConfiguration.getKeyWordsColor().getG()));
        final TextBox keyWordColor_B = new TextBox();
        keyWordColor_B.setText(String.valueOf(terminalConfiguration.getKeyWordsColor().getB()));

        middlePanel.addComponent(keyWordColor_R.withBorder(Borders.singleLine("R")));
        middlePanel.addComponent(keyWordColor_G.withBorder(Borders.singleLine("G")));
        middlePanel.addComponent(keyWordColor_B.withBorder(Borders.singleLine("B")));
        
        final FixedRadioBoxList radioBoxList = new FixedRadioBoxList(new TerminalSize(14, 4));
        radioBoxList.addItem(FontStyle.PLAIN);
        radioBoxList.addItem(FontStyle.BOLD);
        radioBoxList.addItem(FontStyle.ITALIC);
        radioBoxList.setCheckedItemIndex(terminalConfiguration.getFontStyle().getStyle());
        
        final TextBox fontSize = new TextBox();
        fontSize.setText(String.valueOf(terminalConfiguration.getFontSize()));
        
        rightPanel.addComponent(radioBoxList.withBorder(Borders.singleLine("Font style")));
        rightPanel.addComponent(fontSize.withBorder(Borders.singleLine("Font size")));
        
        mainPanel.addComponent(leftPanel.withBorder(Borders.singleLine("Font color")));
        mainPanel.addComponent(middlePanel.withBorder(Borders.singleLine("Key words color")));
        mainPanel.addComponent(rightPanel.withBorder(Borders.singleLine("Font style & size")));

        window.setComponent(
                Panels.vertical(
                    mainPanel.withBorder(Borders.singleLine("Font settings")),
                    new Button("OK", new Runnable(){

						public void run() {
							if(getColor(fontColor_R) != -1 && getColor(fontColor_R) != terminalConfiguration.getFontColor().getR()){
								terminalConfiguration.getFontColor().setR(getColor(fontColor_R));
							}
							if (getColor(fontColor_G) != -1 && getColor(fontColor_G) != terminalConfiguration.getFontColor().getG()){
								terminalConfiguration.getFontColor().setG(getColor(fontColor_G));
							}
							if (getColor(fontColor_B) != -1 && getColor(fontColor_B) != terminalConfiguration.getFontColor().getB()){
								terminalConfiguration.getFontColor().setB(getColor(fontColor_B));
							}
							
							if(getColor(keyWordColor_R) != -1 && getColor(keyWordColor_R) != terminalConfiguration.getKeyWordsColor().getR()){
								terminalConfiguration.getKeyWordsColor().setR(getColor(keyWordColor_R));
							}
							if (getColor(keyWordColor_G)!= -1 && getColor(keyWordColor_G) != terminalConfiguration.getKeyWordsColor().getG()){
								terminalConfiguration.getKeyWordsColor().setG(getColor(keyWordColor_G));
							}
							if (getColor(keyWordColor_B)!= -1 && getColor(keyWordColor_B) != terminalConfiguration.getKeyWordsColor().getB()){
								terminalConfiguration.getKeyWordsColor().setB(getColor(keyWordColor_B));
							}
							
							if((FontStyle)radioBoxList.getCheckedItem() != terminalConfiguration.getFontStyle()){
								terminalConfiguration.setFontStyle((FontStyle)radioBoxList.getCheckedItem());
							}
							
							if(getFontSize(fontSize.getText()) != -1 && getFontSize(fontSize.getText()) != terminalConfiguration.getFontSize()){
								terminalConfiguration.setFontSize(Integer.valueOf(fontSize.getText()).intValue());
							}
							
							TerminalConfiguration.serializeConfiguration(terminalConfiguration);
							window.close();
						}

						
                    	
                    })));
        textGUI.addWindow(window);
    }
    
    private int getColor(final TextBox fontColor_textbox) {
    	 try{
    		int colourCode = Integer.parseInt(fontColor_textbox.getText());
    		if(colourCode > -1 && colourCode <256) return colourCode; 
    	 }catch(Exception e){
    		return -1; 
    	 }
    	 
    	 return -1;
	}
    
    private int getFontSize(String fontSize){
    	try{
    		int size = Integer.parseInt(fontSize);
    		if(size>0 && size <129) return size;
    	}catch(Exception e){
    		return -1;
    	}
    	
    	return -1;
    }

}
