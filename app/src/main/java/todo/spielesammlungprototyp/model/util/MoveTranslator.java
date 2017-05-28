package todo.spielesammlungprototyp.model.util;

import java.util.Map;

public class MoveTranslator {
    private static MoveTranslator instance;
    private Map<Integer, String> horizontalmap;
    private Map<Integer, String> verticalmap;

    private MoveTranslator() {
        horizontalmap = new MapBuilder<Integer, String>().build(
                0, "A",
                1, "B",
                2, "C",
                3, "D",
                4, "E",
                5, "F",
                6, "G",
                7, "H"
        );
        verticalmap = new MapBuilder<Integer, String>().build(
                0, "8",
                1, "7",
                2, "6",
                3, "5",
                4, "4",
                5, "3",
                6, "2",
                7, "1"
        );
    }

    public static MoveTranslator getInstance() {
        if (instance == null) {
            instance = new MoveTranslator();
        }
        return instance;
    }

    public String getRow(int i) {
        return horizontalmap.get(i);
    }

    public String getLine(int i) {
        return verticalmap.get(i);
    }

    public String numToString(Tuple<Integer, Integer> tuple) {
        return getRow(tuple.first) + getLine(tuple.last);
    }

    public Tuple<Integer, Integer> stringToNum(String s) {
        int hoizont = 0;
        for (Map.Entry e : horizontalmap.entrySet()) {
            if (e.getValue().equals(s.substring(0, 1).toUpperCase())) {
                hoizont = (int) e.getKey();
            }
        }

        int vertical = 0;
        for (Map.Entry e : verticalmap.entrySet()) {
            if (e.getValue().equals(s.substring(1, 2))) {
                vertical = (int) e.getKey();
            }
        }
        return new Tuple<Integer, Integer>(hoizont, vertical);
    }
}