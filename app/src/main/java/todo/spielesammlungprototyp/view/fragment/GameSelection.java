package todo.spielesammlungprototyp.view.fragment;

import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import todo.spielesammlungprototyp.R;

public class GameSelection extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private Map<String, List<Map<String, String>>> games = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadGamesFromXml();
        logGameList();
    }

    private void loadGamesFromXml() {
        try (XmlResourceParser xmlGames = getResources().getXml(R.xml.games)) {
            String gameCategory = null;

            int eventType = xmlGames.getEventType();
            for (; eventType != XmlPullParser.END_DOCUMENT; eventType = xmlGames.next()) {
                if (eventType != XmlPullParser.START_TAG) continue;

                switch (xmlGames.getDepth()) {
                    case 2:
                        gameCategory = xmlGames.getName();
                        games.put(gameCategory, new ArrayList<Map<String, String>>());
                        break;
                    case 3:
                        Map<String, String> attributes = new HashMap<>();
                        for (int i = 0; i < xmlGames.getAttributeCount(); i++) {
                            String name = xmlGames.getAttributeName(i);
                            String value = xmlGames.getAttributeValue(i);
                            attributes.put(name, value);
                        }
                        games.get(gameCategory).add(attributes);
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "loadGamesFromXml():", e);
        }
    }

    private void logGameList() {
        for (Map.Entry<String, List<Map<String, String>>> entry : games.entrySet()) {
            Log.d(TAG, entry.getKey() + ":");

            for (Map<String, String> game : entry.getValue()) {
                String output = TextUtils.join(", ", game.values());
                Log.d(TAG, output);
            }
        }
    }
}
