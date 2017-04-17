package todo.spielesammlungprototyp.Chess;

import chesspresso.position.*;
import chesspresso.move.*;
import chesspresso.Chess;


public class Chesstest {
    private Position position;

    public void startposition()
    {
        position = Position.createInitialPosition();
    }
    public String getBoard()
    {
        return position.toString();
    }
    public void testmove() throws Exception
    {
        short move = Move.getRegularMove(Chess.E2,Chess.E4,false);
        position.doMove(move);
    }
}
