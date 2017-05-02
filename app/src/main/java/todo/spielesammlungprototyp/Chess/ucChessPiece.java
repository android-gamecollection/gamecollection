package todo.spielesammlungprototyp.Chess;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ucChessPiece {
    public static final String BISHOP_B = "\u265D";
    public static final String KING_B = "\u265A";
    public static final String KNIGHT_B = "\u265E";
    public static final String PAWN_B = "\u265F";
    public static final String ROOK_B = "\u265C";
    public static final String QUEEN_B = "\u265B";

    public static final String BISHOP_W = "\u2657";
    public static final String KING_W = "\u2654";
    public static final String KNIGHT_W = "\u2658";
    public static final String PAWN_W = "\u2659";
    public static final String ROOK_W = "\u2656";
    public static final String QUEEN_W = "\u2655";

    public static final String NONE = "";

    public static final Map<Character, String> pieces;

    static {
        Map<Character, String> map = new HashMap<>();
        map.put('b', BISHOP_B);
        map.put('k', KING_B);
        map.put('n', KNIGHT_B);
        map.put('p', PAWN_B);
        map.put('r', ROOK_B);
        map.put('q', QUEEN_B);
        map.put('B', BISHOP_W);
        map.put('K', KING_W);
        map.put('N', KNIGHT_W);
        map.put('P', PAWN_W);
        map.put('R', ROOK_W);
        map.put('Q', QUEEN_W);
        map.put('_', NONE);
        pieces = Collections.unmodifiableMap(map);
    }
}