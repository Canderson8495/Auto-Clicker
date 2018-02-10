package pkg;
import java.awt.*;
import javafx.animation.*;
import javafx.concurrent.*;
import java.awt.event.InputEvent;
//NOTICE: This is class in **other file** (here is just for example)
public class TimerClick extends Service<Void> {
	long milliseconds;
	int repetitions;
	
	public TimerClick (int hours, int minutes, int seconds, int milliseconds, int repetitions){
		this.milliseconds = (hours*60*60*1000)+(minutes*60*1000)+(seconds*1000)+milliseconds;
		this.repetitions = repetitions;
	}
	
	
	@Override
	protected Task<Void> createTask() {
		return new Task<Void>() {
            @Override
			protected 
            Void call() throws Exception{
            	Robot bot = new Robot();
            	int mask = InputEvent.BUTTON1_DOWN_MASK;
            	int count = 0;
			while(! isCancelled() && count < repetitions) {
	            try {
	            	Thread.sleep(milliseconds);
	            	bot.mousePress(mask);
	            	bot.mouseRelease(mask);
		            count++;
	            }catch(InterruptedException t) {
	            	System.out.println("Task Interrupted");
	            	break;
	            }
	            if (isCancelled()) {
		               break;
		        }
	        }
			System.out.println("Service: END");
	        return null;
            }
		};
	}
	
}
