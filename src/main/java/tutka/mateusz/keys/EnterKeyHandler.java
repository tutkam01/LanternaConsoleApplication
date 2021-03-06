package tutka.mateusz.keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
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

	private static final int COMMAND_HISTORY_MAX_SIZE = 5;

	public void handleKey(KeyStroke keyToHandle, UserTerminal userTerminal) {
		
		boolean matched = false;
		
		userTerminal.breakLine();
		
		List<String> methodArguments = new ArrayList<String>();
		Method calledMethod = null;
		for(Map.Entry<String, Method> entry: Application.getInstance().getCommandToMethodMap().entrySet()){
			Pattern pattern = Pattern.compile(entry.getKey().trim(), Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(userTerminal.getCurrentCommand().toString());
			
			if(!userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty() && matcher.find() && userTerminal.getCurrentCommand().toString().equalsIgnoreCase(matcher.group(0))){
				matched = true;
				calledMethod = entry.getValue();
				for(int groupCounter=1; ;groupCounter++ ){
					try{
						methodArguments.add(matcher.group(groupCounter).trim());
					}catch(IndexOutOfBoundsException e){
						break;
					}
				}
				
			}
			
		}
		
		if (matched && calledMethod != null){
			    final CyclicBarrier gate = new CyclicBarrier(3);
				LoadingAnimation loadingAnimation = new LoadingAnimation(userTerminal, gate);
				loadingAnimation.start();
				Calculation calculation = new Calculation(calledMethod, methodArguments, userTerminal, gate, loadingAnimation);
				calculation.start();
				try {
					gate.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
		}else if(matched && calledMethod == null){
			userTerminal.clearScreen();
		}else{
			//no match, do nothing.
		}
		
		
		synchronized (userTerminal) {
			if (!userTerminal.getCurrentCommand().getPositionKeyMap().isEmpty()){
				userTerminal.getCommandsHistory().add(new ConsoleCommand(userTerminal.getCurrentCommand()));
				if(userTerminal.getCommandsHistory().size()>COMMAND_HISTORY_MAX_SIZE){
					userTerminal.getCommandsHistory().remove(0);
				}
			}
			if (matched && calledMethod != null && !userTerminal.wasResultAlreadyPrinted()){
				try {
					userTerminal.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	
			ArrowUpDownKeyHandler.resetCounter();
			ArrowUpKeyHandler.resetVerticalShiftOfStartPoint();
			ArrowUpKeyHandler.resetCurrentCommandLinesNumber();
			ArrowDownKeyHandler.resetVerticalShiftOfStartPoint();
			ArrowDownKeyHandler.resetCurrentCommandLinesNumber();
	
			userTerminal.getCurrentCommand().getPositionKeyMap().clear();
			userTerminal.getCurrentCommand().setCommandStartPosition(new Position(Caret.getInstance().getX(), Caret.getInstance().getY()));
			userTerminal.getCurrentCommand().setCommandStartAbsolutePosition(new Position(Caret.getInstance().getAbsolute_x(), Caret.getInstance().getAbsolute_y()));
			}
		}

	}
	
	class LoadingAnimation extends Thread{
		UserTerminal terminal;
		CyclicBarrier gate;
		LoadingAnimation(UserTerminal terminal, CyclicBarrier gate){
			this.terminal = terminal;
			this.gate = gate;
		}
		
		public void run(){
			try {
				gate.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			
			while (!terminal.isStopCalculationAnimation()) {
				setFrame('|');
				setFrame('/');
				setFrame('-');
				setFrame('\\');
			}
			synchronized (this) {
				notifyAll();
			}
		}

		private void setFrame(Character character) {
			terminal.sendCharacterToConsole(new KeyStroke(character, false, false));
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			terminal.shiftCaret();
			terminal.moveCursorBy(-1, 0);
		}
	}
	
	class Calculation extends Thread{
		Method method;
		List<String> arguments;
		UserTerminal terminal;
		CyclicBarrier gate;
		LoadingAnimation animation;
		
		Calculation(Method method, List<String> arguments, UserTerminal terminal, CyclicBarrier gate, LoadingAnimation animation) {
			super();
			this.method = method;
			this.arguments = arguments;
			this.terminal = terminal;
			this.gate = gate;
			this.animation = animation;
		}
		
		public void run() {
			try {
				terminal.setWasResultAlreadyPrinted(false);
				gate.await();
				String poterntialResult = method.execute(arguments.toArray(new String[0]));
				synchronized (animation) {
					terminal.setStopCalculationsTo(true);
					animation.wait();
				}
				if(StringUtils.isNotBlank(poterntialResult)){
					terminal.sendResultToConsole(poterntialResult);
				}
				terminal.setStopCalculationsTo(false);
			} catch (Exception e) {
				if(!terminal.isStopCalculationAnimation()){
					synchronized (animation) {
						try {
							terminal.setStopCalculationsTo(true);
							animation.wait();
						} catch (InterruptedException e1) {
							//life is brutal;-)
						}finally{
							terminal.setStopCalculationsTo(false);
						}
					}
				}
				if (StringUtils.isNotBlank(e.getMessage())) {
					terminal.sendResultToConsole(e.toString() + '\n' + e.getMessage());
				} else {
					terminal.sendResultToConsole(e.toString());
				}
			}
		}
		
		
	}
	

