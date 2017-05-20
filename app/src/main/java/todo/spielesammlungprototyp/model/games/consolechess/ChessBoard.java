package todo.spielesammlungprototyp.model.games.consolechess;

import android.util.Log;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.search.SearchEngine;
import com.alonsoruibal.chess.search.SearchParameters;

public class ChessBoard {

    private final Config config;
    private final SearchEngine searchEngine;
    private final Board board;
    private final SearchParameters searchParameters;

    public ChessBoard() {
        config = new Config();
        searchParameters = new SearchParameters();
        searchEngine = new SearchEngine(config);
        searchEngine.setInitialSearchParameters(searchParameters);
        board = searchEngine.getBoard();
    }

    public void setPosition(String FEN)
    {
        board.setFen(FEN);
    }
    public void setStartPosition() {
        board.startPosition();
    }

    public String getBoard() {
        return board.getFen();
    }

    public String getOverview() {
        String str = board.toString();
        str = str.substring(0, 144); // 144 = amount of chars on a normal field
        str += "\n" + "a b c d e f g h";
        return str;
    }

    public boolean move(String from, String to) {
        int move = Move.getFromString(board, from + " " + to, true);
        return board.doMove(move);
    }

    public String aimove() {
        searchEngine.run();
        int move = searchEngine.getBestMove();
        String smove = Move.toString(move);
        board.doMove(move);
        Log.d("test",smove);
        return smove;

    }

}
