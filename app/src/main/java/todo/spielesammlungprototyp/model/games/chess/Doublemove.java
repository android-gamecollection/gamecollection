package todo.spielesammlungprototyp.model.games.chess;

import todo.spielesammlungprototyp.model.util.Tuple;

public class Doublemove {
    private Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> whitemove;
    private Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> blackmove;
    private int whiteID;
    private int blackID;

    public Doublemove(Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> whitemove, Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> blackmove, int whiteID, int blackID) {
        this.whitemove = whitemove;
        this.blackmove = blackmove;
        this.whiteID = whiteID;
        this.blackID = blackID;
    }

    public Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> getWhitemove() {
        return whitemove;
    }

    public Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> getBlackmove() {
        return blackmove;
    }

    public int getWhiteID() {
        return whiteID;
    }

    public int getBlackID() {
        return blackID;
    }
}
