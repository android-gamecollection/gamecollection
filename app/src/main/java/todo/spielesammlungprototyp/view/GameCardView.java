package todo.spielesammlungprototyp.view;

import android.support.annotation.NonNull;

public class GameCardView implements Comparable<GameCardView> {

    private int gameIconId;
    private String gameTitle, gameDescription, activity;

    public GameCardView(int gameIconId, String gameTitle, String gameDescription, String activity) {
        this.gameIconId = gameIconId;
        this.gameTitle = gameTitle;
        this.gameDescription = gameDescription;
        this.activity = activity;
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

    public String getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", gameIconId, gameTitle, gameDescription, activity);
    }

    @Override
    public int compareTo(@NonNull GameCardView other) {
        return this.getGameTitle().compareTo(other.getGameTitle());
    }
}
