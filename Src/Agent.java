import java.lang.Math;
import java.util.ArrayList;
/**
 * Write a description of class Agent here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Agent
{   
    Move bestMove= null;
    int agentDepth;
    
    int pieceX;
    int pieceY;
    int toX;
    int toY;
    MoveType type;
    public Agent()
    {
        this.agentDepth = 1;
    }
    public void setDepth(int depth){
        this.agentDepth = depth;
    }
    
    public Move successor(Board board){
        bestMove = null;
        int alpha = -10000;
        int beta = 10000;
        int bestScore = minimax(new Board(board), this.agentDepth, alpha, beta, true);
        
        return new Move(bestMove, board);
    }
    public int minimax(Board board, int depth, int alpha, int beta, Boolean isWhite){
        
        ArrayList<Move> legalMoves = board.getAllMoves();
        
        if(depth <= 0 || legalMoves.size() < 1){
            return board.getHeuristic();
        }
        else if(isWhite){
            int v = -10000;
            for(Move m: legalMoves){
                Move move = new Move(m,new Board(board));
                
                int max = minimax(board.getMoveResults(m),depth-1,alpha,beta,!isWhite);
                if(max > v && this.agentDepth == depth){
                    
                    bestMove = move;
                    
                }
                v = Math.max(v,max);
                alpha = Math.max(alpha, v);
                if(alpha >= beta){
                    break;
                }
            }
            return v; 
        }
        else{
            int v = 10000;
            for(Move m: legalMoves){
                int min = minimax(board.getMoveResults(m),depth-1,alpha,beta,!isWhite);
                v = Math.min(v,min);
                beta = Math.min(beta, v);
                if(alpha >= beta){
                    break;
                }
            }
            return v;
        }
    }
}
