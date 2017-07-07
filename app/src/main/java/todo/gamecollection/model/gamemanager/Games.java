package todo.gamecollection.model.gamemanager;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import todo.gamecollection.App;
import todo.gamecollection.R;
import todo.gamecollection.model.util.AndroidResources;

public final class Games {

    private static final Games instance = new Games();
    private static final String TAG = instance.getClass().getSimpleName();
    private final String[] CATEGORY_ATTRIBUTES = {"icon", "id", "title"};
    private final String[] GAME_ATTRIBUTES = {"icon", "title", "description", "rules", "activity", "tag"};
    private final Map<String, List<Game>> games = new HashMap<>();
    private final List<GameCategory> categories = new ArrayList<>();

    private Games() {
        loadGamesFromXml();
        sortGameList();
    }

    public static synchronized Map<String, List<Game>> getGameList() {
        return Collections.unmodifiableMap(instance.games);
    }

    public static synchronized List<Game> getGameList(String category) {
        return Collections.unmodifiableList(instance.games.get(category));
    }

    public static synchronized List<GameCategory> getCategoryList() {
        return Collections.unmodifiableList(instance.categories);
    }

    public static Game getFromUuid(String uuid) {
        for (Map.Entry<String, List<Game>> entry : getGameList().entrySet()) {
            for (Game game : entry.getValue()) {
                if (game.getUuid().equals(uuid)) {
                    return game;
                }
            }
        }
        return null;
    }

    public static void logGameList() {
        for (Map.Entry<String, List<Game>> entry : getGameList().entrySet()) {
            Log.d(TAG, entry.getKey() + ":");

            for (Game game : entry.getValue()) {
                Log.d(TAG, game.toString());
            }
        }
    }

    private void loadGamesFromXml() {
        Context mContext = App.getContext();
        try (XmlResourceParser xmlParser = mContext.getResources().getXml(R.xml.games)) {
            String stateGameCategory = null;

            int eventType = xmlParser.getEventType();
            for (; eventType != XmlPullParser.END_DOCUMENT; eventType = xmlParser.next()) {
                if (eventType != XmlPullParser.START_TAG) continue;

                switch (xmlParser.getDepth()) {
                    case 2:
                        // Tiefe 2: Kategorie
                        String[] categoryAttributes = getAttributes(xmlParser, CATEGORY_ATTRIBUTES);
                        int categoryIcon = AndroidResources.getResourceIDFromString(categoryAttributes[0]);
                        GameCategory category = new GameCategory(categoryIcon, categoryAttributes[1], categoryAttributes[2]);
                        stateGameCategory = category.getCategoryId();
                        categories.add(category);
                        games.put(stateGameCategory, new ArrayList<Game>());
                        break;
                    case 3:
                        // Tiefe 3: Spiel
                        String[] gameAttributes = getAttributes(xmlParser, GAME_ATTRIBUTES);
                        int gameIcon = AndroidResources.getResourceIDFromString(gameAttributes[0]);
                        Game game = new Game(gameIcon, gameAttributes[1], gameAttributes[2], gameAttributes[3], gameAttributes[4], gameAttributes[5]);
                        games.get(stateGameCategory).add(game);
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "loadGamesFromXml():", e);
        }
    }

    private String[] getAttributes(XmlResourceParser xmlParser, String[] attributesList) {
        int length = attributesList.length;
        String[] attributes = new String[length];
        for (int i = 0; i < length; i++) {
            String attributeValue = xmlParser.getAttributeValue(null, attributesList[i]);
            attributes[i] = AndroidResources.getResourceString(attributeValue);
        }
        return attributes;
    }

    private void sortGameList() {
        for (Map.Entry<String, List<Game>> entry : games.entrySet()) {
            Collections.sort(entry.getValue());
        }
        Collections.sort(categories);
    }
}
