package todo.spielesammlungprototyp.view;

import android.support.annotation.NonNull;

public class Game implements Comparable<Game> {

    private int gameIconId;
    private String gameTitle, gameDescription, gameRules, activity, uuid;

    private Game() {

    }

    public Game(int gameIconId, String gameTitle, String gameDescription, String gameRules, String activity) {
        this.gameIconId = gameIconId;
        this.gameTitle = gameTitle;
        this.gameDescription = gameDescription;
        this.gameRules = gameRules;
        this.activity = activity;
        uuid = String.valueOf(hash());
    }

    public int getGameIconId() {
        return gameIconId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public String getGameRules() {
        return gameRules;
    }

    public String getActivity() {
        return activity;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", gameIconId, gameTitle, gameDescription, activity);
    }

    @Override
    public int compareTo(@NonNull Game other) {
        return this.getGameTitle().compareTo(other.getGameTitle());
    }

    private long hash() {
        String[] strList = {gameTitle, activity};
        // Fowler–Noll–Vo-1a 64 bit hash function
        long hash = 0xCBF29CE484222325L;
        for (String str : strList) {
            hash ^= str.hashCode();
            hash *= 0x100000001B3L;
        }
        return hash;
    }
}
