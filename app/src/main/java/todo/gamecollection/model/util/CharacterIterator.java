package todo.gamecollection.model.util;

import java.util.Iterator;

public class CharacterIterator implements Iterator<Character> {

    private final String str;
    private int pos = 0;
    private boolean stopAtWhitespaces = false;

    public CharacterIterator(String str) {
        this.str = str;
    }

    public CharacterIterator(String str, boolean stopAtWhitespaces) {
        this(str);
        this.stopAtWhitespaces = stopAtWhitespaces;
    }

    @Override
    public boolean hasNext() {
        char c = str.charAt(pos);
        boolean whitespaceCondition = !(stopAtWhitespaces && Character.isWhitespace(c));
        return pos < str.length() && whitespaceCondition;
    }

    @Override
    public Character next() {
        return str.charAt(pos++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
