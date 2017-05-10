package todo.spielesammlungprototyp.games.consolechess;

import com.alonsoruibal.chess.*;
import com.alonsoruibal.chess.search.SearchEngine;

class ChessBoard {
    private Config config;
    private SearchEngine se;
    private Board board;
    public ChessBoard()
    {
        config = new Config();
        se = new SearchEngine(config);

        board = se.getBoard();
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
    boolean aimove()
    {
        try
        {
            se.run();
        }
        catch (Exception e)
        {

        }
        return board.doMove(se.getBestMove());
    }
}
