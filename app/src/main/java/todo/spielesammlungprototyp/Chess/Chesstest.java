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

    public void move(String from, String to) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalMoveException {
        Class<?> c = Class.forName("chesspresso.Chess");
        Method methodfrom = c.getDeclaredMethod(from);
        Method methodto = c.getDeclaredMethod(to);
        int f = 0;
        int t = 0;
        methodfrom.invoke(f);
        methodto.invoke(t);
        boolean capturing = false;
        if(!position.isSquareEmpty(t))
        {
            capturing = true;
        }
        short move = Move.getRegularMove(f,t,capturing);

        position.doMove(move);
    }
}
