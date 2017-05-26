package todo.spielesammlungprototyp.Chess;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.search.SearchEngine;

public class ChessWrapper {
    Board board;
    private Config config;
    private SearchEngine searchengine;

    public ChessWrapper() {
        config = new Config();
    }

    public void setDifficlty(int difficlty) {
        switch (difficlty) {
            case 3:
                config.setTranspositionTableSize(100);
                break;
            case 2:
                config.setTranspositionTableSize(50);
                break;
            case 1:
            default:
                config.setTranspositionTableSize(10);
        }
    }

    private void initiate() {
        searchengine = new SearchEngine(config);
        board = searchengine.getBoard();
    }

    public Board getBoard() {
        return board;
    }

    public boolean doMove(String move) {
        int intmove = Move.getFromString(board, move, true);
        if (board.doMove(intmove)) {
            animation(move);
            return true;
        } else {
            return false;
        }
    }

    public String getBestMove() {
        try {
            searchengine.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String string = Move.toString(searchengine.getBestMove());
        return string;
    }

    public void aimove() {
        doMove(getBestMove());
    }

    public void restart() {
        initiate();
        board.startPosition();
    }

    public String getFen() {
        return board.getFen();
    }

    public String getOverview() {
        String str = board.toString();
        str = str.substring(0, 144); // 144 = amount of chars on a normal field
        str = str + "\n" + "a b c d e f g h";
        return str;
    }

    public int isEndgame() {
        return board.isEndGame();
    }

    private void animation(String move) {

    }
}
