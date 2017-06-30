package todo.spielesammlungprototyp.model.games.chess;

import android.text.TextUtils;

import todo.spielesammlungprototyp.model.util.Tuple;

public class Doublemove {

    private static final String delimiter = " ";
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

    public static Doublemove fromString(String str) {
        String[] split = str.split(delimiter);
        Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> whitemove = new Tuple<>(
                MoveTranslator.stringToNum(split[0]), MoveTranslator.stringToNum(split[1])
        );
        Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> blackmove = new Tuple<>(
                MoveTranslator.stringToNum(split[2]), MoveTranslator.stringToNum(split[3])
        );
        int whiteID = Integer.parseInt(split[4]);
        int blackID = Integer.parseInt(split[5]);
        return new Doublemove(whitemove, blackmove, whiteID, blackID);
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

    @Override
    public String toString() {
        return TextUtils.join(delimiter, new String[]{
                MoveTranslator.numToString(whitemove.first),
                MoveTranslator.numToString(whitemove.last),
                MoveTranslator.numToString(blackmove.first),
                MoveTranslator.numToString(blackmove.last),
                String.valueOf(whiteID),
                String.valueOf(blackID)
        });
    }
}
