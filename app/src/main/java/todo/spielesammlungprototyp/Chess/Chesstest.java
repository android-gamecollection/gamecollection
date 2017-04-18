package todo.spielesammlungprototyp.Chess;

import chesspresso.position.*;
import chesspresso.move.*;
import chesspresso.Chess;



class Chesstest {

    private Position position;

    void startPosition() {
        position = Position.createInitialPosition();
    }

    String getBoard() {
        return position.toString();
    }

    void move(String from, String to) throws IllegalMoveException{
        short move = Move.getRegularMove(Chess.strToSqi(from),Chess.strToSqi(to),false);
        position.doMove(move);

    }
}
