package todo.spielesammlungprototyp;

public class GameCardView {

    private int gameIconId;
    private String gameTitle, gameDetails;

    public GameCardView(int gameIconId, String gameTitle, String gameDetails) {
        this.gameIconId = gameIconId;
        this.gameTitle = gameTitle;
        this.gameDetails = gameDetails;
    }

    int getGameIconId() {
        return gameIconId;
    }

    void setGameIconId(int gameIconId) {
        this.gameIconId = gameIconId;
    }

    String getGameTitle() {
        return gameTitle;
    }

    void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    String getGameDetails() {
        return gameDetails;
    }

    void setGameDetails(String gameDetails) {
        this.gameDetails = gameDetails;
    }
}
