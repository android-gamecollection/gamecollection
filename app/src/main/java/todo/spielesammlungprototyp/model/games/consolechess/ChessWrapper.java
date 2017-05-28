package todo.spielesammlungprototyp.model.games.consolechess;


import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.search.SearchEngine;
import com.alonsoruibal.chess.search.SearchParameters;
import java.util.ArrayList;
import todo.spielesammlungprototyp.util.MoveTranslator;
import todo.spielesammlungprototyp.util.Tuple;

public class ChessWrapper {

    private final Config config;
    private final SearchEngine searchEngine;
    private final Board board;
    private final SearchParameters searchParameters;

    public ChessWrapper() {
        config = new Config();
        config.setTranspositionTableSize(1);
        searchParameters = new SearchParameters();
        searchEngine = new SearchEngine(config);
        searchEngine.setInitialSearchParameters(searchParameters);
        board = searchEngine.getBoard();
    }

    public void setPosition(String FEN) {
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

    public String getBestMove()
    {
        searchEngine.run();
        int move = searchEngine.getBestMove();
        String smove = Move.toString(move);
        return smove;
    }
    public String aimove() {
        searchEngine.run();
        int move = searchEngine.getBestMove();
        String smove = Move.toString(move);
        board.doMove(move);
        return smove;

    }

    public boolean isMate() {
        return board.isMate();
    }

    public boolean isDraw() {
        return board.isDraw();
    }

    public Tuple<Integer, Integer>[] getPossibleMoves(Tuple<Integer, Integer> from, String FEN) {
        ArrayList<Tuple<Integer, Integer>> liste = new ArrayList<>();
        Board testboard = null;
        MoveTranslator mt = MoveTranslator.getInstance();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (testboard == null) {
                    testboard = new Board();
                    testboard.setFen(FEN);
                }
                Tuple<Integer, Integer> to = new Tuple<>(i, j);
                String smove = (mt.numToString(from) + " " + mt.numToString(to)).toLowerCase();
                int move = Move.getFromString(testboard, smove, true);
                if (testboard.doMove(move)) {
                    liste.add(to);
                    testboard = null;
                }
            }
        }
        Tuple<Integer, Integer>[] array = new Tuple[liste.size()];
        return liste.toArray(array);
    }
}
