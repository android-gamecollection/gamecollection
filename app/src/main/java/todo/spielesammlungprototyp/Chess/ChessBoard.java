package todo.spielesammlungprototyp.Chess;

import com.alonsoruibal.chess.*;

class ChessBoard {

    private Board board;
    public ChessBoard()
    {
        board = new Board();
    }

    void startPosition() {
        board.startPosition();
    }

    String getBoard() {
        return board.getFen();
    }

    String getOverview() {
        String str = board.toString();
        str = str.substring(0, 144); // 144 = amount of chars on a normal field
        str = str + "\n" + "a b c d e f g h";
        return str;
    }

    boolean move(String from, String to){
        int move = Move.getFromString(board,from + " " + to,true);
        return board.doMove(move);
    }
}
