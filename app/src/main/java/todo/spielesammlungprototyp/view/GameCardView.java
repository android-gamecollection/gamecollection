package todo.spielesammlungprototyp.view;

public class GameCardView {

    private int gameIconId;
    private String gameTitle, gameDetails, activity;

    public GameCardView(int gameIconId, String gameTitle, String gameDetails, String activity) {
        this.gameIconId = gameIconId;
        this.gameTitle = gameTitle;
        this.gameDetails = gameDetails;
        this.activity = activity;
    }

    public int getGameIconId() {
        return gameIconId;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public String getGameDetails() {
        return gameDetails;
    }

    public String getActivity() {
        return activity;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", gameIconId, gameTitle, gameDetails, activity);
    }
}
