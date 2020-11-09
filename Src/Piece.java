import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.ArrayList;

/**
*The piece class extends StackPane so that multiple ellipses can be stacked ontop of eachother to represent a King piece.
*
* @author      Slade Brooks
* @version     1
*/
public class Piece extends StackPane
{   
    final int TILE_SIZE = Board.TILE_SIZE;
    private PieceType type;
    private PieceColor color;
    private int x, y;
    
    
    /**
    * Constructor used when creating a deep copy from another Piece.
    *
    * @param  piece the piece param provided is deep copied to create this Piece.
    */
    public Piece(Piece piece){
        this(piece.type(),piece.getColor(),piece.getX(),piece.getY());
    }
    /**
    * Constructor used for creating a Piece.
    *
    * @param  type used to set the x value of the rectangle and the xInt value of the Tile.
    * @param  color used to set the y value of the rectangle and the yInt value of the Tile.
    * @param  x position of piece in terms of board tiles.
    * @param  y of type Color, used to fill the rectangles colour.
    */
    public Piece(PieceType type,PieceColor color, int x, int y)
    {
        this.type = type;//type of piece, either PIECE type or KING type
        this.x = x;//x co ordinate on the board.
        this.y = y;//y co ordinate on the board.
        this.color = color;//colour  of piece, either RED or WHITE
        
        this.move(x, y);//sets the piece over the appropriate tile

        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3, TILE_SIZE * 0.3);//This ellipse represents the bottom piece itself.
        ellipse.setId("ellipse");//used to lookup the bottom piece(kings crown doesnt need to be looked up).
        ellipse.setRadiusX(30);
        ellipse.setRadiusY(30);
        ellipse.setFill(color == PieceColor.WHITE ? Color.WHITE : Color.RED);//fils the ellipse to match the appropriate PieceColor

        getChildren().addAll(ellipse);//adds the ellipse to the StackPane
        
        //Implements king type if type is set as king
        if(type == PieceType.KING){
            this.setKing();
        }
    }
    /**
    * Sets the piece to type KING and creates a golden ellipse to be placed on the top to distinguish it.
    */
    public void setKing(){
        //implements king if not already a king.
        if(this.type != PieceType.KING){
            this.type = PieceType.KING;//new PieceType.
            Ellipse crown = new Ellipse(20 * 0.3, 20 * 0.3);//creates crown ellipse.
            crown.setRadiusX(10);//crown is smaller than origional piece ellipse.
            crown.setRadiusY(10);
            crown.setFill(Color.GOLD);//colours crown gold.
            getChildren().addAll(crown);//adds crown to the Piece stackpane.
        }
    }
    /**
    * Relocates the stackpane to x,y co-ords on the board.
    */
    public void move(int x, int y) {
        relocate(x * TILE_SIZE + 25, y * TILE_SIZE + 25);
    }
    /**
    * Get method for PieceType enum stored in Piece.
    * 
    * @return returns the PieceType of the piece, e.g. PIECE, KING.
    */
    public PieceType type(){
        return type;
    }
    /**
    * Set method for the x and y co-ords of the piece.
    * 
    * @param x sets pieces x to this.
    * @param y sets pieces y to this.
    */
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }
    /**
    * Get method for x co-ord of piece on board.
    * 
    * @return x int co-ord of piece.
    */
    public int getX(){
        return x;
    }
    /**
    * Get method for y co-ord of piece on board.
    * 
    * @return y int co-ord of piece.
    */
    public int getY(){
        return y;
    }
    /**
    * Get method for PieceColor of the Piece e.g. RED, WHITE.
    * 
    * @return PieceColor field.
    */
    public PieceColor getColor(){
        return this.color;
    }
    /**
    * Looks up the bottom ellipse and colours it green to represent highlighting.
    */
    public void highlight(){
        Ellipse ellipse = (Ellipse)lookup("#ellipse");
        ellipse.setFill(Color.GREEN);
    }
    /**
    * Used to remove highlighting from a highlighted piece.
    */
    public void unHighlight(){
        Ellipse ellipse = (Ellipse)lookup("#ellipse");
        ellipse.setFill((color == PieceColor.WHITE ? Color.WHITE : Color.RED));
    }
}
