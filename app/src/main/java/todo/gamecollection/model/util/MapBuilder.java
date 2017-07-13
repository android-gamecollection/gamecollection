package todo.gamecollection.model.util;

import java.util.HashMap;

public final class MapBuilder<K, V> {

    public HashMap<K, V> build(K first, V second, Object... args) throws IllegalArgumentException {
        if (args.length % 2 != 0)
            throw new IllegalArgumentException("Incorrect number of arguments");

        HashMap<K, V> map = new HashMap<>();

        Class<?> KeyType = first.getClass();
        Class<?> ValueType = second.getClass();
        map.put(first, second);

        for (int i = 0; i < args.length; i += 2) {
            @SuppressWarnings("unchecked")
            K key = (K) args[i];
            @SuppressWarnings("unchecked")
            V value = (V) args[i + 1];
            if (!(KeyType.isInstance(key) && ValueType.isInstance(value)))
                throw new IllegalArgumentException("Incorrect type of arguments");
            map.put(key, value);
        }

        return map;
    }
}
