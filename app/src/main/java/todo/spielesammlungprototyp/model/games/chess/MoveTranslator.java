package todo.spielesammlungprototyp.model.games.chess;

import todo.spielesammlungprototyp.model.util.Tuple;

public final class MoveTranslator {

    private MoveTranslator() {
    }

    private static char colToChar(int i) {
        if ((i >= 0) && (i <= 7)) {
            return (char) (i + 'a');
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static char rowToChar(int i) {
        if ((i >= 0) && (i <= 7)) {
            return (char) ((8 - i) + '0');
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static int charToCol(char c) {
        c = Character.toLowerCase(c);
        if ((c >= 'a') && (c <= 'h')) {
            return c - 'a';
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static int charToRow(char c) {
        if ((c >= '1') && (c <= '8')) {
            return 8 - (c - '0');
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String numToString(Tuple<Integer, Integer> tuple) {
        return "" + colToChar(tuple.first) + rowToChar(tuple.last);
    }

    public static Tuple<Integer, Integer> stringToNum(String s) {
        int horizontal = charToCol(s.charAt(0));
        int vertical = charToRow(s.charAt(1));
        return new Tuple<>(horizontal, vertical);
    }
}