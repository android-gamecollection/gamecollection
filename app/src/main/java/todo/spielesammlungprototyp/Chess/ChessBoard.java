package todo.spielesammlungprototyp.Chess;

import chesspresso.position.*;
import chesspresso.move.*;
import chesspresso.Chess;



class ChessBoard {

    private Position position;

    void startPosition() {
        position = Position.createInitialPosition();
    }

    String getBoard() {
        return position.toString();
    }

    String getOverview() {
        String str = "";
        for (int i = 0; i < 64; i++) {
            // 56, 57, .., 63, 48, 49, ..., 0, 1, 2, .., 7
            int positionNumber = i + 56 - (i / 8) * 16;
            int piece = position.getPiece(positionNumber);
            char pieceChar = piece != 0 ? Chess.pieceToChar(piece) : '-';
            // Newline every 8 fields
            str += pieceChar + ((i + 1) % 8 == 0 ? "\n" : " ");
        }
        return str;
    }

    void move(String from, String to) throws IllegalMoveException{
        short move = Move.getRegularMove(Chess.strToSqi(from),Chess.strToSqi(to),false);
        position.doMove(move);

    }
}
