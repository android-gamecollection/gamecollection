package todo.spielesammlungprototyp.model.gamemanager;

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

import todo.spielesammlungprototyp.App;
import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.AndroidResources;

public final class Games {

    private static final Games instance = new Games();
    private static final String TAG = instance.getClass().getSimpleName();
    public final Map<String, List<Game>> games = new HashMap<>();
    private final String[] XML_ATTRIBUTES = {"icon", "title", "description", "rules", "activity", "tag"};

    private Games() {
        loadGamesFromXml();
        sortGameList();
    }

    public synchronized static Map<String, List<Game>> getGameList() {
        return instance.games;
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
        try (XmlResourceParser xmlGames = mContext.getResources().getXml(R.xml.games)) {
            String gameCategory = null;

            int eventType = xmlGames.getEventType();
            for (; eventType != XmlPullParser.END_DOCUMENT; eventType = xmlGames.next()) {
                if (eventType != XmlPullParser.START_TAG) continue;

                switch (xmlGames.getDepth()) {
                    case 2:
                        gameCategory = xmlGames.getName();
                        games.put(gameCategory, new ArrayList<Game>());
                        break;
                    case 3:
                        String[] attributes = XML_ATTRIBUTES.clone();
                        for (int i = 0; i < attributes.length; i++) {
                            String attributeValue = xmlGames.getAttributeValue(null, attributes[i]);
                            attributes[i] = AndroidResources.getResourceString(attributeValue);
                        }
                        int icon = AndroidResources.getResourceIDFromString(attributes[0]);
                        Game game = new Game(icon, attributes[1], attributes[2], attributes[3], attributes[4], attributes[5]);
                        games.get(gameCategory).add(game);
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "loadGamesFromXml():", e);
        }
    }

    private void sortGameList() {
        for (Map.Entry<String, List<Game>> entry : games.entrySet()) {
            Collections.sort(entry.getValue());
        }
    }
}
