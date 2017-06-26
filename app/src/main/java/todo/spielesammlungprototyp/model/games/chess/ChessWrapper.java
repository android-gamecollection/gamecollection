package todo.spielesammlungprototyp.model.games.chess;


import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.bitboard.BitboardUtils;
import com.alonsoruibal.chess.search.SearchEngine;
import com.alonsoruibal.chess.search.SearchParameters;

import java.util.ArrayList;

import todo.spielesammlungprototyp.model.util.MoveTranslator;
import todo.spielesammlungprototyp.model.util.Tuple;

public class ChessWrapper {

    private final Config config;
    private final SearchEngine searchEngine;
    private final Board board;
    private final SearchParameters searchParameters;

    public ChessWrapper() {
        this(false);
    }
    public ChessWrapper(boolean isChess960) {
        config = new Config();
        config.setUciChess960(isChess960);
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

    public String getMoves() {
        return board.getMoves();
    }

    public void doMoves(String moves) {
        board.doMoves(moves);
    }

    public void undoMove() {
        board.undoMove();
    }

    public boolean promotionmove(String from, String to, char figure) {
        int move = Move.getFromString(board, from + " " + to, true);
        int fromIndex = Move.getFromIndex(move);
        int toIndex = Move.getToIndex(move);
        int pieceMoved = Move.getPieceMoved(move);
        boolean capture = Move.isCapture(move);
        boolean check = Move.isCheck(move);
        int movetype;
        switch (figure) {
            case 'b':
                movetype = Move.TYPE_PROMOTION_BISHOP;
                break;
            case 'k':
                movetype = Move.TYPE_PROMOTION_KNIGHT;
                break;
            case 'r':
                movetype = Move.TYPE_PROMOTION_ROOK;
                break;
            case 'q':
            default:
                movetype = Move.TYPE_PROMOTION_QUEEN;
                break;

        }
        int promotionmove = Move.genMove(fromIndex, toIndex, pieceMoved, capture, check, movetype);
        return board.doMove(promotionmove);
    }

    public boolean isPromotion(String from, String to) {
        int move = Move.getFromString(board, from + " " + to, true);
        return Move.isPromotion(move);
    }

    public String getBestMove() {
        searchEngine.run();
        int move = searchEngine.getBestMove();
        return Move.toString(move);
    }

    public int getMoveNumber() {
        return board.getMoveNumber();
    }

    public String aimove() {
        searchEngine.run();
        int move = searchEngine.getBestMove();
        String smove = Move.toString(move);
        board.doMove(move);
        return smove;

    }

    public boolean isWhitesTurn() {
        return board.getTurn();
    }

    public boolean isWhitePiece(Tuple<Integer, Integer> pos) {
        String s2 = MoveTranslator.numToString(pos);
        long square = BitboardUtils.algebraic2Square(s2);
        char pieceAt = board.getPieceAt(square);
        return Character.isUpperCase(pieceAt);
    }

    public int isEndgame() {
        return board.isEndGame();
    }

    public Tuple<Integer, Integer>[] getPossibleMoves(Tuple<Integer, Integer> from, String FEN) {
        ArrayList<Tuple<Integer, Integer>> liste = new ArrayList<>();
        Board testboard = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (testboard == null) {
                    testboard = new Board();
                    testboard.setFen(FEN);
                }
                Tuple<Integer, Integer> to = new Tuple<>(i, j);
                String smove = MoveTranslator.numToString(from) + " " + MoveTranslator.numToString(to);
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
