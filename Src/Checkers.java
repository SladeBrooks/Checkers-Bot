//javafx imports
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.GridPane;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.event.EventHandler;

//file reading imports
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.scene.input.MouseEvent;

/**
 * The Checkers class creates the GUI using javafx implementations by extending application. It also handles turns. It creates and istance of the board 
 * and the agent. Handles mouse input.
 *
 * @author Slade Brooks
 * @version 1
 */
public class Checkers extends Application{
    Board board = new Board();
    Agent agent = new Agent();
    
    /**
    * Start method used in application, is overriden. Takes the Stage, sets it up and impliments a game loop in it.
    *
    * @param  primaryStage the primary stage to be shown.
    */
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("CheckersBot3000");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //the game loop that handles the Agents turn and checks for a winner
        AnimationTimer agentTurn = new AnimationTimer()
        {
            @Override
            public void handle(long arg0) 
            {   //checks the board for a winner and prints the winner confirmation if one exists
                if(board.winCheck()){
                    //new Alert(Alert.AlertType.CONFIRMATION, "Winner.").showAndWait();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("WINNER!");
                    String winMessage = ((board.getTurn() == PieceColor.RED? PieceColor.WHITE : PieceColor.RED) +" wins!!!");
                    alert.setHeaderText(winMessage);
                    alert.setContentText("Start new game?");
                    alert.showAndWait();
                    primaryStage.close();
                }   
                //if no winner and agents turn then implement agents turn.
                else if(board.getTurn() == PieceColor.WHITE && !board.winCheck()){
                    Move agentMove = agent.successor(board);
                    board.moveIfLegal(agentMove);
                }
            }
        };
        agentTurn.start();
    }
    /**
    * Creates the content for the UI, this is a part of the application implementation and required for this way of using javafx. This method creates
    * a flowpane containing a Pane representing the board and buttons with correct utility implemented.
    *
    * @return Parent a FlowPane containing the board and all the neccessary buttons.
    */
    public Parent createContent(){
        
        FlowPane window = new FlowPane();
        
        Button rules = createRuleButton();//displays rules when clicked.
        Button help = createHelpButton();//highlights movable pieces when hovered.
        Button hard = createDifficultyButton("Difficulty:Hard", 3);//sets agent depth to 3.
        Button medium = createDifficultyButton("Difficulty:Medium", 2);//sets agent depth to 2.
        Button easy = createDifficultyButton("Difficulty:Easy", 1);//sets agent depth to 1.
        
        Pane grid = new Pane();//contains the board tiles and pieces
        grid.getChildren().add(board.tiles);
        grid.getChildren().add(board.pieces);
        grid.setPrefSize(800,800);
        //below when a board tile is clicked, the click co-ords are sent to board to proccess.
        grid.setOnMouseClicked(e ->{
            int x = (int)(e.getSceneX()/100);//x co-ord of click.
            int y = (int)(e.getSceneY()/100);//y co-ord of click.
            //only sends clicks if there isnt a winner.
            if(!board.winCheck()){
                board.recieveMouseClick(x,y);//send click.
            }
        });
        
       
        window.getChildren().add(grid);//ads board to window
        window.getChildren().addAll(rules,help,easy,medium,hard);//adds buttons to window
        return window;
    }
    
    /**
    * Creates a button that when clicked displays the rules in a pop up dialog box. The rules are sources from a text file contained in the project
    * folder.
    *
    * @return Button rule buton that when click displays the rules from the rules.txt file.
    */
    private Button createRuleButton(){
        Button button = new Button("Rules");//creates button.
        //set the action that occurs when the button is clicked.
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        VBox dialogVbox = new VBox(20);
                        
                        String content = "";
                        try{
                            content = new String ( Files.readAllBytes( Paths.get("rules.txt") ) );//reads the rules file and saves into content.
                        }catch(Exception e){System.out.println(e);}
                        dialogVbox.getChildren().add(new Text(content));//adds the content to the dialog box
                        Scene dialogScene = new Scene(dialogVbox, 700, 850);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                });
        return button;
    }
    /**
    * Creates a button that when hovered over, highlights movable pieces green for the player. When mouse leaves button, pieces are unhighlighted.
    *
    * @return Button help button.
    */
    private Button createHelpButton(){
        Button button = new Button("Movable Pieces");
        //adds the mouse enters functionality to highlight pieces.
        button.addEventHandler(MouseEvent.MOUSE_ENTERED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
           board.highlightMovablePieces();
          }
        });
        //adds the mouse leaves functionality to unhighlight pieces.
        button.addEventHandler(MouseEvent.MOUSE_EXITED,
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent e) {
           board.unHighlightPieces();
          }
        });
                
        return button;
    }
    /**
    * Creates a button that when clicked changes the minimax depth of the agent(changes difficulty).
    *
    * @param buttonName the buttons name when displayed in GUI.
    * @param the depth that the button sets the agent too.
    * 
    * @return Button with name that changes difficulty of agent.
    */
    private Button createDifficultyButton(String buttonName, int difficulty){
        Button button = new Button(buttonName);
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        agent.setDepth(difficulty);//implements depth change in agent.
                    }
                });
        return button;
    }
    
    /**
    * Main method used to launch the application.
    *
    * @param args not used.
    */
    public static void main(String[] args){
        launch(args);
    }
        
}
