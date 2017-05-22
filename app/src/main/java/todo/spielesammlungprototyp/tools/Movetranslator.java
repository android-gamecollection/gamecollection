package todo.spielesammlungprototyp.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oliver on 09.05.2017.
 */

public class Movetranslator {
    private static Movetranslator instance;
    private Map<Integer, String> horizontalmap;
    private Map<Integer, String> verticalmap;

    private Movetranslator() {
        horizontalmap = new HashMap<>();
        verticalmap = new HashMap<>();

        horizontalmap.put(0, "A");
        horizontalmap.put(1, "B");
        horizontalmap.put(2, "C");
        horizontalmap.put(3, "D");
        horizontalmap.put(4, "E");
        horizontalmap.put(5, "F");
        horizontalmap.put(6, "G");
        horizontalmap.put(7, "H");

        verticalmap.put(0, "8");
        verticalmap.put(1, "7");
        verticalmap.put(2, "6");
        verticalmap.put(3, "5");
        verticalmap.put(4, "4");
        verticalmap.put(5, "3");
        verticalmap.put(6, "2");
        verticalmap.put(7, "1");
    }

    public static Movetranslator getInstance() {
        if (instance == null) {
            instance = new Movetranslator();
        }
        return instance;
    }

    public String getRow(int i) {
        return horizontalmap.get(i);
    }

    public String getLine(int i) {
        return verticalmap.get(i);
    }

    public String numToString(Tupel<Integer, Integer> tupel) {
        return getRow(tupel.first) + getLine(tupel.second);
    }

    public Tupel<Integer, Integer> stringToNum(String s) {
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
        return new Tupel<Integer, Integer>(hoizont, vertical);
    }
}