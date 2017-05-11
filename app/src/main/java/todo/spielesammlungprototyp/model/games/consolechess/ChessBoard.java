package todo.spielesammlungprototyp.model.games.consolechess;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.search.SearchEngine;
import com.alonsoruibal.chess.search.SearchParameters;

class ChessBoard {

    private final Config config;
    private final SearchEngine searchEngine;
    private final Board board;
    private final SearchParameters searchParameters;

    ChessBoard() {
        config = new Config();
        searchParameters = new SearchParameters();
        searchEngine = new SearchEngine(config);
        searchEngine.setInitialSearchParameters(searchParameters);
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
        str += "\n" + "a b c d e f g h";
        return str;
    }

    boolean move(String from, String to) {
        int move = Move.getFromString(board, from + " " + to, true);
        return board.doMove(move);
    }

    boolean aimove() {
        searchEngine.run();
        return board.doMove(searchEngine.getBestMove());
    }
}
