package todo.spielesammlungprototyp.Chess;

import com.alonsoruibal.chess.Board;
import com.alonsoruibal.chess.Config;
import com.alonsoruibal.chess.Move;
import com.alonsoruibal.chess.search.SearchEngine;

/**
 * Created by Oliver on 02.05.2017.
 */

public class ChessWrapper {
    private Config config;
    private SearchEngine searchengine;
    Board board;

    public ChessWrapper(){
        config = new Config();
    }
    public void setDifficlty(int difficlty)
    {
        switch (difficlty)
        {
            case 3:
                config.setTranspositionTableSize(5);
                break;
            case 2:
                config.setTranspositionTableSize(2);
                break;
            case 1:
            default:
                config.setTranspositionTableSize(1);
        }
    }
    private void initiate()
    {
        searchengine = new SearchEngine(config);
        board = searchengine.getBoard();
    }
    public Board getBoard()
    {
        return board;
    }
    public boolean doMove(String move)
    {
        int intmove = Move.getFromString(board,move,true);
        if(board.doMove(intmove)) {
            animation(move);
            return true;
        }
        else
        {
            return false;
        }
    }
    public String getBestMove()
    {
        try{
            searchengine.run();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String string = Move.toString(searchengine.getBestMove());
        return string;
    }
    public void restart()
    {
        initiate();
        board.startPosition();
    }
    public String getFen()
    {
        return board.getFen();
    }
    public String getOverview() {
        String str = board.toString();
        str = str.substring(0, 144); // 144 = amount of chars on a normal field
        str = str + "\n" + "a b c d e f g h";
        return str;
    }
    private void animation(String move)
    {

    }
}
