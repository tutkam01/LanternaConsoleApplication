package tutka.mateusz.keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import tutka.mateusz.console_application.Application;
import tutka.mateusz.interfaces.KeyHandler;
import tutka.mateusz.interfaces.Method;
import tutka.mateusz.models.Caret;
import tutka.mateusz.models.ConsoleCommand;
import tutka.mateusz.models.Position;
import tutka.mateusz.terminal.UserTerminal;

import com.googlecode.lanterna.input.KeyStroke;

public class EnterKeyHandler implements KeyHandler {

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		boolean matched = false;
//		int[] carretPosition = new int[]{0, (userTerminal.getCaret().getY()<userTerminal.getRowsNumber()-1)?(userTerminal.getCaret().getY() + 1): userTerminal.getRowsNumber()-1};
//		int[] absoluteCarretPosition = new int[]{0, userTerminal.getCaret().getAbsolute_y() + 1};
//		userTerminal.getTerminal().putCharacter('\n');
//		userTerminal.getTerminal().flush();
//		userTerminal.getCaret().setX(carretPosition[0]);
//		userTerminal.getCaret().setY(carretPosition[1]);
//		userTerminal.getCaret().setAbsolute_x(absoluteCarretPosition[0]);
//		userTerminal.getCaret().setAbsolute_y(absoluteCarretPosition[1]);
//		userTerminal.getWord().resetWord();
		
		userTerminal.breakLine();
		
		List<String> methodArguments = new ArrayList<String>();
		Method calledMethod = null;
		for(Map.Entry<String, Method> entry: Application.getInstance().getCommandToMethodMap().entrySet()){
			Pattern pattern = Pattern.compile(entry.getKey().trim(), Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(userTerminal.getCurrentCommand().toString());
			
			if(!userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty() && matcher.find() && userTerminal.getCurrentCommand().toString().equalsIgnoreCase(matcher.group(0))){
//				System.out.println(matcher.group(0));
//				System.out.println(matcher.group(1));
//				System.out.println(matcher.group(2));
				matched = true;
				calledMethod = entry.getValue();
				for(int groupCounter=1; ;groupCounter++ ){
					try{
						methodArguments.add(matcher.group(groupCounter));
					}catch(IndexOutOfBoundsException e){
						break;
					}
				}
				
			}
			
		}
		
		if (matched && calledMethod != null){
			try{
				String poterntialResult = calledMethod.execute(methodArguments.toArray(new String[0]));
				if(!poterntialResult.isEmpty()) userTerminal.sendResultToConsole(poterntialResult);
				userTerminal.breakLine();
			}catch(Exception e){
				if(StringUtils.isNotBlank(e.getMessage())){
					userTerminal.sendResultToConsole(e.getMessage());
				}else{
					userTerminal.sendResultToConsole(e.toString());
				}
			}
		}else if(matched && calledMethod == null){
			userTerminal.clearScreen();
		}else{
			//no match, do nothing.
		}
		
		
		
		
		
		if(!userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty()) userTerminal.getCommandsHistory().add(new ConsoleCommand(userTerminal.getCurrentCommand()));
		
		ArrowUpDownKeyHandler.resetCounter();
		ArrowUpKeyHandler.resetVerticalShiftOfStartPoint();
		ArrowUpKeyHandler.resetCurrentCommandLinesNumber();
		ArrowDownKeyHandler.resetVerticalShiftOfStartPoint();
		ArrowDownKeyHandler.resetCurrentCommandLinesNumber();
		
		userTerminal.getCurrentCommand().getPositionKeyMap().clear();
		
//		userTerminal.getCurrentCommand().setCommandStartPosition(new Position(carretPosition[0], carretPosition[1]));
//		userTerminal.getCurrentCommand().setCommandStartAbsolutePosition(new Position(absoluteCarretPosition[0], absoluteCarretPosition[1]));
		userTerminal.getCurrentCommand().setCommandStartPosition(new Position(Caret.getInstance().getX(), Caret.getInstance().getY()));
		userTerminal.getCurrentCommand().setCommandStartAbsolutePosition(new Position(Caret.getInstance().getAbsolute_x(), Caret.getInstance().getAbsolute_y()));

	}

}
