package todo.spielesammlungprototyp.model.util;

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
import todo.spielesammlungprototyp.view.Game;

public final class Games {

    private final static Games instance = new Games();
    private static final String TAG = instance.getClass().getSimpleName();
    public final Map<String, List<Game>> games = new HashMap<>();
    private final String[] XML_ATTRIBUTES = {"icon", "title", "description", "rules", "activity"};

    private Games() {
        loadGamesFromXml();
        sortGameList();
    }

    public synchronized static Map<String, List<Game>> getGameList() {
        return instance.games;
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
                            attributes[i] = dereferenceString(attributeValue);
                        }
                        int icon = getResourceIdFromString(attributes[0]);
                        Game game = new Game(icon, attributes[1], attributes[2], attributes[3], attributes[4]);
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

    /**
     * Converts a resource from string-form to integer-form
     *
     * @param resourceStr The resource as string in the form of '@mimap/ic_launcher'
     * @return The resource in the form of R.mipmap.ic_launcher
     */
    private int getResourceIdFromString(String resourceStr) {
        Context mContext = App.getContext();
        if (resourceStr.startsWith("@")) resourceStr = resourceStr.substring(1);
        String[] splitString = resourceStr.split("/");
        return mContext.getResources().getIdentifier(splitString[1], splitString[0], mContext.getPackageName());
    }

    private String dereferenceString(String resourceStr) {
        Context mContext = App.getContext();
        if (!resourceStr.startsWith("@string/")) return resourceStr;
        int identifier = getResourceIdFromString(resourceStr);
        return mContext.getString(identifier);
    }
}
