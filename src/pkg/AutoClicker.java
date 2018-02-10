package pkg;

//Really need this.
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.animation.PauseTransition;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class AutoClicker extends Application {
    public static void sleep(int hours, int minutes,int seconds,int milliseconds) throws InterruptedException {
    	long time = (hours*60*60*1000)+(minutes*60*1000)+(seconds*1000)+milliseconds;
    	Thread.sleep(time);
    }
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	primaryStage.setTitle("Cameron's Auto Clicker");
    	//Creation of 4 different types of pane.
    	GridPane grid = new GridPane();
    	BorderPane pane = new BorderPane();
    	HBox startStop = new HBox(10);
    	VBox clickInterval = new VBox(5);
    	Scene scene = new Scene(pane,300,300);
    	//GridPane grid Properties
    	grid.setAlignment(Pos.TOP_LEFT);
    	grid.setPadding(new Insets(40,10,10,40));
    	grid.setVgap(20);
    	//HBox startStop Properties
    	startStop.setPrefWidth(250);
    	startStop.setPrefHeight(30);
    	startStop.setAlignment(Pos.CENTER);
    	startStop.setPadding(new Insets(15,25,0,25));
    	//VBox clickInterval Properties
    	clickInterval.setAlignment(Pos.CENTER_LEFT);
    	clickInterval.setPadding(new Insets(10,10,20,10));
    	
    	//Creation of 4 text fields
    	TextField hours = new TextField("0");
    	hours.setPrefSize(40, 15);
    	TextField minutes = new TextField("0");
    	minutes.setPrefSize(40, 15);
    	TextField seconds = new TextField("0");
    	seconds.setPrefSize(40, 15);
    	TextField milliseconds = new TextField("500");
    	milliseconds.setPrefSize(40, 15);
    	//Creation of label for the 4 text fields
    	Node intervals[] = {new Label("Hours"), hours, new Label("Minutes"), minutes,new Label("Seconds"),seconds,new Label("Milliseconds"),milliseconds};
    	//Creation of the radio buttons placed in the grid
    	RadioButton rb1 = new RadioButton("Repeat");
    	RadioButton rb2 = new RadioButton("Repeat for ");
    	final ToggleGroup group = new ToggleGroup();
    	rb1.setToggleGroup(group);
    	rb1.setSelected(true);
    	rb2.setToggleGroup(group);
    	TextField repetitions = new TextField("0");
    	repetitions.setPrefSize(30, 15);
    	
    	
    	//Creation of the start Stop buttons
    	Button start = new Button("Start");
    	Button stop = new Button("Stop");
    	stop.setDisable(true);
    	start.setMinSize(startStop.getPrefWidth()/2, startStop.getPrefHeight());
    	stop.setMinSize(startStop.getPrefWidth()/2, startStop.getPrefHeight());    	
    	//Adding all of the items into their respective panes
    	clickInterval.getChildren().addAll(intervals);
    	grid.add(rb1,0,1);
    	grid.add(rb2, 0, 0);
    	grid.add(new Label(" Times"), 2, 0);
    	grid.add(repetitions, 1, 0);
    	startStop.getChildren().addAll(start,stop);
    	pane.setLeft(clickInterval);
    	pane.setTop(startStop);
    	pane.setCenter(grid);
    	//Giving action to my start button
    	start.setOnAction(new EventHandler<ActionEvent>() {
    	TimerClick timerClick;
    		@Override
    		//creating an handle event inside of the event handler
    		public void handle(ActionEvent e) {
    			stop.setDisable(false);
    			start.setDisable(true);
    			if(Integer.parseInt(repetitions.getText())== 0){
    				timerClick = new TimerClick(Integer.parseInt(hours.getText()), Integer.parseInt(minutes.getText()),Integer.parseInt(seconds.getText()),Integer.parseInt(milliseconds.getText()),Integer.MAX_VALUE);
    			}else {
    				timerClick = new TimerClick(Integer.parseInt(hours.getText()), Integer.parseInt(minutes.getText()),Integer.parseInt(seconds.getText()),Integer.parseInt(milliseconds.getText()),Integer.parseInt(repetitions.getText()));
    			}
    				//So today I learned about java concurrent.
    			//If i run a loop in here, it'll freeze the UI
    			//so what i need to do is create a worker/service thread.
    			
    			timerClick.start();
    			//Seems like i'll have to use a hook into the windows os to recover mouse movements because apparently java doesn't allow any inputs to unfocused windows.
    			scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
    	            @Override
    	            public void handle(KeyEvent event) {
    	                if(event.getCode() == KeyCode.F9) {
    	                	stop.fire();
    	                }else if(event.getCode() == KeyCode.F10) {
    	                	start.fire();
    	                }
    	            }
    	        });
    			timerClick.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
    				@Override
    				public void handle(WorkerStateEvent t) {
    					stop.setDisable(true);
    					start.setDisable(false);
    				}
    			});
    			stop.setOnAction(new EventHandler<ActionEvent>() {
    				@Override
    	    		//creating an handle event inside of the event handler
    	    		public void handle(ActionEvent e) {
    	    			start.setDisable(false);
    	    			stop.setDisable(true);
    	    			PauseTransition pause = new PauseTransition(Duration.millis(10));
    	    			pause.setOnFinished(event -> {
    	    				timerClick.cancel();
    	    				System.out.println("THis command must be called after 'Service END'");
    	    			});
    	    		pause.play();
    	    		}
    	    	});
    		}
    	});
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.F10) {
                	start.fire();
                }
            }
        });
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	start.requestFocus();
    	
    }
}