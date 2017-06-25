package todo.spielesammlungprototyp.model.gamemanager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Game implements Comparable<Game> {

    private int gameIconId;
    private String gameTitle, gameDescription, gameRules, activity, uuid, tag;

    private Game() {

    }

    public Game(int gameIconId, String gameTitle, String gameDescription, String gameRules, String activity, @Nullable String tag) {
        this.gameIconId = gameIconId;
        this.gameTitle = gameTitle;
        this.gameDescription = gameDescription;
        this.gameRules = gameRules;
        this.activity = activity;
        this.tag = tag;
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

    public boolean isTaggedWith(String tag) {
        if (this.tag != null && tag != null) {
            String[] splitTag = this.tag.split("\\|");
            for (String t : splitTag) {
                if (t.equals(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", gameIconId, gameTitle, gameDescription, activity);
    }

    @Override
    public int compareTo(@NonNull Game other) {
        int result = this.gameTitle.compareTo(other.getGameTitle());
        if (result == 0) {
            result = this.gameDescription.compareTo(other.getGameDescription());
        }
        return result;
    }

    private long hash() {
        String[] strList = {activity, tag};
        // Fowler–Noll–Vo-1a 64 bit hash function
        long hash = 0xCBF29CE484222325L;
        for (String str : strList) {
            hash ^= str == null ? "null".hashCode() : str.hashCode();
            hash *= 0x100000001B3L;
        }
        return hash;
    }
}
