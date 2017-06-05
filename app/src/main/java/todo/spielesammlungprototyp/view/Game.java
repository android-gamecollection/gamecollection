package todo.spielesammlungprototyp.view;

import android.support.annotation.NonNull;

public class Game implements Comparable<Game> {

    private int gameIconId;
    private String gameTitle, gameDescription, gameRules, activity, uuid;

    public Game(int gameIconId, String gameTitle, String gameDescription, String gameRules, String activity) {
        this.gameIconId = gameIconId;
        this.gameTitle = gameTitle;
        this.gameDescription = gameDescription;
        this.gameRules = gameRules;
        this.activity = activity;
    }

    public Game(int gameIconId, String gameTitle, String gameDescription, String gameRules, String activity, String uuid) {
        this(gameIconId, gameTitle, gameDescription, gameRules, activity);
        this.uuid = uuid;
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
}
