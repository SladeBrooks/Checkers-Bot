/**
 * Move is a type that contains all the required information to perform a move. It gets saved as a type to make checking legality easier as well
 * as grouping all neccessary information for easier retrieval.
 *
 * @author Slade Brooks
 * @version 1
 */
public class Move
{
    private Piece piece;//the piece to be moved.
    private Tile to;//destination of move.
    private Tile killTile;//If move is of type kill then the tile of the kill is saved
    private MoveType type;// Can be either NORMAL or KILL

    /**
    * Constructor used when creating a deep copy from another Move, the associated co-ords from the old board are used to generate a depp copy
    * of a move with co-ords relating to a new board.
    *
    * @param  oldMove the old move to be copied.
    * @param newBoard the new board for the new objects to be linked to.
    */
    public Move(Move oldMove, Board newBoard){
        this(oldMove.getType(), //type
            newBoard.getPiece(oldMove.getPiece().getX(),oldMove.getPiece().getY()), //piece 
            newBoard.getTile(oldMove.getTo().getXInt(),oldMove.getTo().getYInt()),  //to
            (oldMove.getKill() == null ? null : newBoard.getTile(oldMove.getKill().getXInt(),oldMove.getKill().getYInt())) //killTile
        );
    }
    /**
    * Constructor used for creating a Move of type KILL.
    *
    * @param  type used to represent if the move is of type KILL or type NORMAL.
    * @param  piece links to a Piece object to be moved.
    * @param  to links to a Tile object as the destination of the piece.
    * @param  killTile links to a Tile for kill move.
    */
    public Move(MoveType type, Piece piece, Tile to, Tile killTile)
    {
        this(type,piece,to);
        this.killTile = killTile;
    }
    /**
    * Constructor used for creating a Move without a kill Tile.
    *
    * @param  type used to represent if the move is of type KILL or type NORMAL.
    * @param  piece links to a Piece object to be moved.
    * @param  to links to a Tile object as the destination of the piece.
    */
    public Move(MoveType type, Piece piece, Tile to)
    {
        this.piece = piece;
        this.to = to;
        this.type = type;
    }
    /**
    * Get method for Piece to be moved.
    * 
    * @return piece to be moved.
    */
    public Piece getPiece(){
        return piece;
    }
    /**
    * Get method for the destination Tile.
    * 
    * @return destination Tile.
    */
    public Tile getTo(){
        return to;
    }
    /**
    * Get method for the kill Tile.
    * 
    * @return kill Tile.
    */
    public Tile getKill(){
        return killTile;
    }
    /**
    * Get method for the MoveType e.g. KILL, NORMAL.
    * 
    * @return MoveType of Move.
    */
    public MoveType getType(){
        return type;
    }
    /**
    * Checks legality of move using the internals stored in Move.
    * 
    * @return boolean true if legal, otherwise false.
    */
    public boolean isLegal(PieceColor currentTurn){
        //todo handleillegal moves
        if(currentTurn!= piece.getColor()){
            return false;
        }
        else if(to.hasPiece() == true){
            return false;
        }
        if(type == MoveType.KILL && killTile.hasPiece() == false){
            return false;
        }
        else if(type == MoveType.KILL && killTile.getPiece().getColor() == currentTurn){
            return false;
        }
        return true;    
    }
}
