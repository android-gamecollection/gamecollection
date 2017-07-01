package todo.gamecollection.model.gamemanager;

import android.support.annotation.NonNull;

public class GameCategory implements Comparable<GameCategory> {

    private int categoryIconId;
    private String categoryId, categoryTitle;

    GameCategory(int categoryIconId, String categoryId, String categoryTitle) {
        this.categoryIconId = categoryIconId;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
    }

    public int getCategoryIconId() {
        return categoryIconId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", categoryTitle, categoryId);
    }

    @Override
    public int compareTo(@NonNull GameCategory o) {
        return categoryTitle.compareTo(o.getCategoryTitle());
    }
}
