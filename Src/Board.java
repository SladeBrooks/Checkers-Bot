import javafx.scene.Group;
import java.lang.Cloneable;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;//used for alerting the user after unallowed click/move attempt.
import java.util.ArrayList;
/**
 * Board represents the board and all associated data, it is designed so that the main Checkers class can interact with this only to effect the displayed
 * pieces and tiles on the board.
 *
 * @author Slade Brooks
 * @version 1
 */
public class Board{
    final static int TILE_SIZE = 100;//static tilesize, static to reduce parameters needed for other methods.
    private Tile[][] board = new Tile[8][8];//The board itself where the tiles are stored.
    public Group tiles = new Group();//left as public so UI can interact with directly.
    public Group pieces = new Group();////left as public so UI can interact with directly.
    private PieceColor currentTurn;//current player turn.
    private ArrayList<Move> availableMoves = new ArrayList<Move>();//used to set available moves for a piece for highlighting tiles.
    private Piece killerPiece = null;// used if the last move was a kill move
    /**
    * Constructor used when creating a Board as a deep copy of another Board. Copies Tile/Piece locations creating copies of the Tile/Pieces.
    *
    * @param  oldBoard the oldBoard to be copied.
    */
    public Board(Board oldBoard){
        currentTurn = oldBoard.currentTurn;//sets current turn to be the same. turn is an enum so deep copy is not needed.
        
        //iterates through board old board squares
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Tile tile = new Tile(oldBoard.getTile(x,y));//creates copy of each tile in oldBoard.
                board[x][y] = tile;
                tiles.getChildren().add(tile);//adds tile to group
                if(oldBoard.getTile(x,y).hasPiece()){//if piece exists on tile, creates copy and sets it appropriately.
                    Piece piece = new Piece(oldBoard.getTile(x,y).getPiece());
                    board[x][y].addPiece(piece);
                    pieces.getChildren().add(piece);
                }
            }
        }
    }
    /**
    * Constructor used when creating a new board with pieces set to default positions.
    */
    public Board(){
        currentTurn = PieceColor.RED;//Starting player/colour.
        
        //iterates through each square in the 8 x 8 board.
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Tile tile = new Tile(x,y,(((x+y)%2) == 1 ? Color.BLACK : Color.WHITE));//creates a new tile using formula to set colour.
                board[x][y] = tile;
                tiles.getChildren().add(tile);
                if(((y == 0 || y == 2) && ((x+1) % 2) == 0)||((y == 1) && ((x+2) % 2) == 0)){//checks formula to create white pieces appropriately.
                    Piece piece = new Piece(PieceType.PIECE, PieceColor.WHITE, x,y);
                    board[x][y].addPiece(piece);
                    pieces.getChildren().add(piece);
                }
                else if(((y == 7 || y == 5) && ((x+2) % 2) == 0)||((y == 6) && ((x+1) % 2) == 0)){//checks formula to create red pieces appropriately.
                    Piece piece = new Piece(PieceType.PIECE, PieceColor.RED, x, y);
                    board[x][y].addPiece(piece);
                    pieces.getChildren().add(piece);
                }
            }
        }
    }
    /**
    * Get method returning the current players turn in the form of a PieceColor enum.
    * 
    * @return PieceColor turn.
    */
    public PieceColor getTurn(){
        return this.currentTurn;
    }
    /**
    * Get method for getting a Piece by co-ordinates.
    * 
    * @return Piece piece at co-ords
    */
    public Piece getPiece(int x, int y){
        return board[x][y].getPiece();
    }
    /**
    * Get method for getting a Tile by co-ordinates.
    * 
    * @return Tile tile at co-ords
    */
    public Tile getTile(int x, int y){
        return board[x][y];
    }
    
    /**
    * Moves piece after checking move legality.Used by players to move pieces. Applies delay to the movePiece() method because this will only be used 
    * to take a turn not for internals of the agent. try catch statement used because the delay needs to throw exception to be used.
    * 
    * @param move the move that needs to be performed.
    */
    public void moveIfLegal(Move move) {
        if(move.isLegal(currentTurn)){
            try{
                movePiece(move,true);
            }catch(Exception e){}   
        }
    }
    /**
    * Moves piece, uses delay optionaly to segment kill moves. Delay is optional so this method can be used by the agents internals without using the
    * delay in calculations.
    * 
    * @param move the move that needs to be performed.
    * @param sleep boolean used to decide if the delay is needed
    */
    public void movePiece(Move move, Boolean sleep) throws InterruptedException{
        int x = move.getPiece().getX();//gets pieces x.
        int y = move.getPiece().getY();//gets pieces y
        
        int toX = (int)move.getTo().getX();//gets destinations x.
        int toY = (int)move.getTo().getY();//gets destinations y.
        
        //if piece reaches either end of board it is set to king.
        if(toY == 0 || toY == 7){
            move.getPiece().setKing();
        }
        
        move.getPiece().setXY(toX,toY);//sets pieces new coords
        this.getTile(x,y).removePiece();//removes piece from old tile
        move.getTo().addPiece(move.getPiece());//places piece on new tile
        move.getPiece().move(move.getPiece().getX(),move.getPiece().getY());//move piece on GUI, gets piece from Move then uses the pieces move().
        
        //different game logic is the move is a kill move, ensuring multi kill moves are possible.
        if(move.getType() == MoveType.KILL) {
            if(sleep){
                Thread.sleep(500);//used to delay between kills when not performing agents internals.
            }
            if(move.getKill().getPiece().type() == PieceType.KING){
                move.getPiece().setKing();//implements regicide, becomes king if king is killed.
            }
            pieces.getChildren().remove(move.getKill().getPiece());//removes killed piece from group
            move.getKill().removePiece();//removes killed piece from tile
            killerPiece = move.getPiece();//ensures that double kill possibility is checked.
            if(getAllMoves().size() < 1){
               currentTurn = (currentTurn == PieceColor.WHITE ? PieceColor.RED : PieceColor.WHITE); //changes turn only if another kill is not possible.
               killerPiece = null;//sets current kill piece to null if no kill is possible.
            }
        }
        //runs when move is not a kill move.
        else{
            currentTurn = (currentTurn == PieceColor.WHITE ? PieceColor.RED : PieceColor.WHITE);//change turn.
            killerPiece = null;
        }
    }
    
    /**
    * Creates a deep copy of a board then performes a deep copied move on the board. This is only used by agent internals as a gamestate node.
    * 
    * @param move the move that needs to be performed.
    */
    public Board getMoveResults(Move move){
        Board newBoard = null;
        newBoard = new Board(this);//deep copies current board
        Move newMove = new Move(move, newBoard);//deep copies move to be performed with associated objects from new copied board.
        
        //try statement needed for delay even though not used.
        try{
            newBoard.movePiece(newMove,false);//moves piece on the new board.
        }catch(Exception e){}
        
        return newBoard;
    }
    /**
    * Returns a value for the board to be used by the agent. Because agent colour doesnt change, white will always be the max in a minimax algorithm
    * therefore this method returns a higher value based on white having a higher score. The heuristic type greatly effects the agent playstyle,
    * this is a simple heuristic causing the agent to only value capturing pieces and creating Kings while minimising enemy Kings.
    * 
    * @return int value that is higher if white is performing well in terms of piece numbers otherwise lower.
    */
    public int getHeuristic(){
        int score = 0;
        //counts through all children adding 1 point for a white PIECE and 2 for a white KING, -1 point for red piece and -2 for red king.
        for(Node p: pieces.getChildren()){
            if(((Piece)p).getColor() == PieceColor.WHITE){
                if(((Piece)p).type() == PieceType.KING){
                    score++;
                }
                score++;
            }
            if(((Piece)p).getColor() == PieceColor.RED){
                if(((Piece)p).type() == PieceType.KING){
                    score--;
                }
                score++;
            }
        }
        return score;
    }
    /**
    * This method is used in highlighting available moves for a piece when clicked. Gets all available moves then stores all of those moves that
    * correspond to a specific piece in a field in this Board.
    *
    *@param Piece sets all available moves for this piece.
    */
    public void getAvailableMoves(Piece piece){
        ArrayList<Move> allMoves = getAllMoves();//get all possible moves for all pieces.
        availableMoves.clear();//clear old available moves.
        
        //iterates through all possible moves.
        for(Move m: allMoves){
            if(m.getPiece() == piece){
                availableMoves.add(m);//if that move starts with the chosen piece then move is added to available moves.
                m.getTo().highlight();//highlights the tile of the move.
            }
            
        }
    }
    /**
    * This method returns all possible move for the current player. Only kill moves if any are available otherwise all normal moves. Piece type is taken
    * into account in the move list. if the game is mid multi kill move then turn doesnt change and only those multi kill moves are allowed next.
    * 
    * @return ArrayList<Move> arraylist containing all possible and legal moves for current player.
    */
    public ArrayList<Move> getAllMoves(){
        
        PieceColor col = this.currentTurn;
        ArrayList<Move> killMoves = new ArrayList<Move>();//where possible kill moves are stored
        ArrayList<Move> normalMoves = new ArrayList<Move>();
        for(Node p: pieces.getChildren()){//iterates through all node pieces.
            Piece piece = (Piece)p;//castes the node to a piece.
            //checks if piece matches current turn and if the game is midway through a multi kill move.
            if(col == piece.getColor() && (killerPiece == null ||piece == killerPiece)){
                //this if statement handles white moves or kings moving backwards.
                if(piece.type() == PieceType.KING || piece.getColor() == PieceColor.WHITE){
                    ArrayList<Move> tempKill = new ArrayList<Move>();//only stores kill moves for 1 piece.
                    ArrayList<Move> tempNorm = new ArrayList<Move>();//only stores normal moves for 1 piece.
                    //the below 4 if statements only adds the moves if they are within bounds of the board.
                    if((piece.getX()-1) >= 0 && (piece.getY()+1) < 8 && killerPiece == null){
                        tempNorm.add( new Move(MoveType.NORMAL, piece, getTile(piece.getX()-1, piece.getY()+1)));
                    }
                    if((piece.getX()+1) < 8 && (piece.getY()+1) < 8 && killerPiece == null){
                        tempNorm.add( new Move(MoveType.NORMAL, piece, getTile(piece.getX()+1, piece.getY()+1)));
                    }
                    if((piece.getX()-2) >= 0 && (piece.getY()+2) < 8){
                        tempKill.add( new Move(MoveType.KILL, piece, getTile(piece.getX()-2, piece.getY()+2), getTile(piece.getX()-1, piece.getY()+1)));
                    }
                    if((piece.getX()+2) < 8 && (piece.getY()+2) < 8){
                        tempKill.add( new Move(MoveType.KILL, piece, getTile(piece.getX()+2, piece.getY()+2), getTile(piece.getX()+1, piece.getY()+1)));
                    }
                    //checks each move is legal before adding them to the master lists.
                    for(Move m: tempKill){
                        if(m.isLegal(this.currentTurn)){
                            killMoves.add(m);
                        }
                    }
                    for(Move m: tempNorm){
                        if(m.isLegal(this.currentTurn)){
                            normalMoves.add(m);
                        }
                    }
                }
                //this if statement handes red moves or kings moving forward. performs the same logic as above but different directional moves.
                if(piece.type() == PieceType.KING || piece.getColor() == PieceColor.RED){
                    ArrayList<Move> tempKill = new ArrayList<Move>();
                    ArrayList<Move> tempNorm = new ArrayList<Move>();
                    if((piece.getX()-1) >= 0 && (piece.getY()-1) >= 0 && killerPiece == null){
                        tempNorm.add( new Move(MoveType.NORMAL, piece, getTile(piece.getX()-1, piece.getY()-1)));
                    }
                    if((piece.getX()+1) < 8 && (piece.getY()-1) >=0 && killerPiece == null){
                        tempNorm.add( new Move(MoveType.NORMAL, piece, getTile(piece.getX()+1, piece.getY()-1)));
                    }
                    if((piece.getX()-2) >= 0 && (piece.getY()-2) >= 0){
                        tempKill.add( new Move(MoveType.KILL, piece, getTile(piece.getX()-2, piece.getY()-2), getTile(piece.getX()-1, piece.getY()-1)));
                    }
                    if((piece.getX()+2) < 8 && (piece.getY()-2) >= 0){
                        tempKill.add( new Move(MoveType.KILL, piece, getTile(piece.getX()+2, piece.getY()-2), getTile(piece.getX()+1, piece.getY()-1)));
                    }
                    for(Move m: tempKill){
                        if(m.isLegal(this.currentTurn)){killMoves.add(m);}
                    }
                    for(Move m: tempNorm){
                        
                        if(m.isLegal(this.currentTurn)){normalMoves.add(m);}
                    }
                }
            }
        }
        //this if statement implements kill moves having to be taken by only returning kill moves if atleast 1 is possible.
        if(killMoves.size() > 0){
            return killMoves;
        }
        //if no legal kill moves are available then return legal normal moves
        else{
            //System.out.println(normalMoves.size() +" normal moves");
            return normalMoves;
        }
    }
    /**
    * This method performs the logic for the board recieving a mouse click and creates an appropriate error message if the click is to move illegaly.
    * 
    * 
    * @param x board co-ordinate of the click.
    * @param y board -co-ordinate of the click.
    */
    public void recieveMouseClick(int x, int y){
        //Thread.sleep(300);
        Tile tileClicked = board[x][y];
        //if no moves are highlighted and no piece to move on tile
        if(tileClicked.getPiece() == null && availableMoves.size() < 1){
            new Alert(Alert.AlertType.ERROR, "No piece to move on this tile.").showAndWait();//informs player.
        }
        //player clicks on empty tile after clicking on own piece to move to tile.
        else if(tileClicked.getPiece() == null && availableMoves.size() > 0){
            boolean found = false;
            //checks available moves to see if move is legal.
            for(Move m: availableMoves){
                Tile tile = m.getTo();
                if(tileClicked == tile){
                    try{
                        movePiece(m,false);
                    }catch(Exception e){}
                    unHighlightAvailable();//unhighlights available moves
                    found = true;
                }
            }
            //move is legal so available moves are cleared.
            if(found){
                availableMoves.clear();
            }
            //move isnt legal so player is informed.
            else{
                new Alert(Alert.AlertType.ERROR, "Cannot move piece to that tile, select highlighted tile or a new piece to move.").showAndWait();
            }
        }
        //if no available moves shown and user clicks on enemy piece.
        else if(tileClicked.getPiece().getColor() != PieceColor.RED && availableMoves.size() < 1){
            new Alert(Alert.AlertType.ERROR, "This is not your piece, click on a red piece during your turn to see moves.").showAndWait();
        }
        //clicks on enemy piece after highlighting available moves.
        else if(tileClicked.getPiece().getColor() != PieceColor.RED && availableMoves.size() > 0){
            new Alert(Alert.AlertType.ERROR, "Invalid move location, click on one of the highlighted tiles to move or select a new piece.").showAndWait();
        }
        //player clicks on own piece, available moves are highlighted for that piece.
        else if(tileClicked.getPiece().getColor() == PieceColor.RED ){
            unHighlightAvailable();
            availableMoves.clear();
            getAvailableMoves(tileClicked.getPiece());
            
        }
        
    }
    /**
    * This method un highlights all the destination tiles in available moves for a piece when the piece is either moved or a new piece is selected.
    */
    private void unHighlightAvailable(){
        for(Move m:availableMoves){
            m.getTo().unHighlight();
        }
    }
    /**
    * This method iterates through the pieces and if each player has atleast one piece, the returns false otherwise if a player has no pieces returns true.
    * 
    * @return boolean true if a player has won otherwise false.
    */
    public boolean winCheck(){
        boolean whiteWin = true;
        boolean redWin = true;
        for(Node p: pieces.getChildren()){
            if(((Piece)p).getColor() == PieceColor.WHITE){
                redWin = false;
            }
            if(((Piece)p).getColor() == PieceColor.RED){
                whiteWin = false;
            }
        }
        if(whiteWin || redWin){
            return true;
        }
        else{
            return false;
        }
    }
    /**
    * Highlights all pieces with available moves.
    */
    public void highlightMovablePieces(){
        ArrayList<Move> allMoves = getAllMoves();
        for(Move m: allMoves){
            m.getPiece().highlight();
        }
    }
    /**
    * Unhighlights all pieces with available moves.
    */
    public void unHighlightPieces(){
       ArrayList<Move> allMoves = getAllMoves();
        for(Move m: allMoves){
            m.getPiece().unHighlight();
        } 
    }
    
}
