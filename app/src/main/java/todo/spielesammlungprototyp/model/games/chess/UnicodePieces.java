package todo.spielesammlungprototyp.model.games.chess;

import java.util.Map;

import todo.spielesammlungprototyp.tools.MapBuilder;

public abstract class UnicodePieces {

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

    public static final Map<Character, String> pieces = new MapBuilder<Character, String>().build(
            'b', BISHOP_B,
            'k', KING_B,
            'n', KNIGHT_B,
            'p', PAWN_B,
            'r', ROOK_B,
            'q', QUEEN_B,
            'B', BISHOP_W,
            'K', KING_W,
            'N', KNIGHT_W,
            'P', PAWN_W,
            'R', ROOK_W,
            'Q', QUEEN_W,
            '_', NONE
    );
}