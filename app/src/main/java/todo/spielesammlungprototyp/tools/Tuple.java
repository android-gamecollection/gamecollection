package todo.spielesammlungprototyp.tools;

/**
 * Created by Oliver on 20.05.2017.
 */

public class Tuple<T, F> {

    public T first;    //X
    public F last;   //Y

    public Tuple(T first, F last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Tuple<?, ?>
                && first.equals(((Tuple) other).first)
                && last.equals(((Tuple) other).last);
    }
}
