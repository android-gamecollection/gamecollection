package todo.spielesammlungprototyp.games.consolechess;

import android.util.Log;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.search.SearchEngine;

class ChessBoard {
    private final String TAG = this.getClass().getSimpleName();

    private final Config config;
    private final SearchEngine searchEngine;
    private final Board board;

    ChessBoard() {
        config = new Config();
        searchEngine = new SearchEngine(config);

        board = searchEngine.getBoard();
    }

    void setStartPosition() {
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

    boolean move(String from, String to) {
        int move = Move.getFromString(board, from + " " + to, true);
        return board.doMove(move);
    }

    boolean aimove() {
        try {
            searchEngine.run();
        } catch (NullPointerException e) {
            Log.w(TAG, "aimove():", e);
        }

        return board.doMove(searchEngine.getBestMove());
    }
}
