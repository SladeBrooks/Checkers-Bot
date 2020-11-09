import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
    
/**
* Used to represent a tile on the board, extends rectangle to create a grid system.
* 
* @author      Slade Brooks
* @version     1
*/
public class Tile extends Rectangle
{
    private Piece piece;//Piece stored in tile
    public Color c;//Colour of tile
    final int TILE_SIZE = Board.TILE_SIZE;
        
    /**
    * Constructor used when creating a deep copy.
    *
    * @param  tile the tile param provided is deep copied to create this tile.
    */
    public Tile(Tile tile){
        this(tile.getXInt(),tile.getYInt(),tile.getColor());
    }
    /**
    * Constructor used for creating a tile.
    *
    * @param  x used to set the x value of the rectangle and the xInt value of the Tile.
    * @param  y used to set the y value of the rectangle and the yInt value of the Tile.
    * @param  c of type Color, used to fill the rectangles colour.
    */
    public Tile(int x, int y,Color c)
    {   
        piece = null;//tiles are created with no pieces.
        this.setX(x);//sets x co ordinate of tile on board.
        this.setY(y);//sets y co ordinate of tile on board.
        this.c = c;
        
        setWidth(TILE_SIZE);//sets width of tile to 100.
        setHeight(TILE_SIZE);//sets height of tile to 100.
        
        //tile is set to the position of its x and y multiplied by tile size.
        relocate(this.getX() * TILE_SIZE, this.getY() * TILE_SIZE);
        setFill(c);//fills the colour of the tile on board.
    }
    
    /**
    * Method is used to determine if the tiles contains a piece or if its Piece field is set to null.
    * 
    * @return True if Tile contains piece, otherwise false.
    */
    public boolean hasPiece()
    {
        return piece != null;
    }
    /**
    * Get method for the tiles stored Piece.
    * 
    * @return Piece returns the Tiles associated piece field.
    */
    public Piece getPiece(){
        return piece;
    }
    /**
    * A set method for the tiles Piece field.
    * 
    * @param p sets the Piece field of tile to equal p.
    */
    public void addPiece(Piece p){
        this.piece = p;
    }
    /**
    * Sets the Piece field of Tile to equal null.
    */
    public void removePiece(){
        piece = null;
    }
    /**
    * Method is used to highlight the tile as green in the GUI.
    */
    public void highlight(){
        setFill(Color.GREEN);
    }
    /**
    * Method is used to remove GUI highlight and set tile back to origional colour.
    */
    public void unHighlight(){
        setFill(this.c);
    }
    /**
    * The getX() method for rectangle is final and cannot be overwritten, this is used as an alternative that returns an int, used instead of casting.
    * 
    * return x int representation of the tiles x position.
    */
    public int getXInt(){
        return (int)this.getX();
    }
    /**
    * The getY() method for rectangle is final and cannot be overwritten, this is used as an alternative that returns an int, used instead of casting.
    * 
    * return y int representation of the tiles y position.
    */
    public int getYInt(){
        return (int)this.getY();
    }
    /**
    * Get method for the colour of the tile.
    * 
    * return c returns the default colour of the tile in type Color.
    */
    public Color getColor(){
        return this.c;
    }
}
