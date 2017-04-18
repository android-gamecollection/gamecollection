package todo.spielesammlungprototyp.Chess;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import chesspresso.position.*;
import chesspresso.move.*;
import chesspresso.Chess;



public class Chesstest {

    private Position position;

    public void startPosition() {
        position = Position.createInitialPosition();
    }

    public String getBoard() {
        return position.toString();
    }

    public void testmove() throws Exception {
        short move = Move.getRegularMove(Chess.E2, Chess.E4, false);
        position.doMove(move);
    }

    public void move(String from, String to) throws IllegalMoveException{
        short move = Move.getRegularMove(Chess.strToSqi(from),Chess.strToSqi(to),false);
        position.doMove(move);

    }
}
