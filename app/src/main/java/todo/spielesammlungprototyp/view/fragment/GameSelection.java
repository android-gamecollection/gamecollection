package todo.spielesammlungprototyp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.ClickListener;
import todo.spielesammlungprototyp.view.GameCardView;
import todo.spielesammlungprototyp.view.GameCardViewAdapter;
import todo.spielesammlungprototyp.view.activity.GameActivity;

public class GameSelection extends Fragment implements ClickListener {

    private final static String EXTRA_CATEGORY = "category";
    private static final String[] XML_ATTRIBUTES = {"icon", "title", "description", "activity"};
    private final String ACTIVITY_PACKAGE = ".view.activity.";
    private final String TAG = getClass().getSimpleName();
    private Map<String, List<GameCardView>> games = new HashMap<>();
    private String gameCategory;

    public static GameSelection newInstance(String category) {
        Bundle args = new Bundle();
        args.putString(EXTRA_CATEGORY, category);
        GameSelection fragment = new GameSelection();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameCategory = getArguments().getString(EXTRA_CATEGORY);
        loadGamesFromXml();
        sortGameList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_game_selection);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        GameCardViewAdapter gcvAdapter = new GameCardViewAdapter(games.get(gameCategory));
        gcvAdapter.setClickListener(this);
        recyclerView.setAdapter(gcvAdapter);
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent();
        Context context = view.getContext();
        GameCardView gameCardView = games.get(gameCategory).get(position);
        String str = context.getPackageName() + ACTIVITY_PACKAGE + gameCardView.getActivity();
        intent.setClassName(context, str);
        intent.putExtra(GameActivity.KEY_TITLE, gameCardView.getGameTitle());
        context.startActivity(intent);
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
                        games.put(gameCategory, new ArrayList<GameCardView>());
                        break;
                    case 3:
                        String[] attributes = XML_ATTRIBUTES.clone();
                        for (int i = 0; i < attributes.length; i++) {
                            attributes[i] = xmlGames.getAttributeValue(null, attributes[i]);
                        }
                        int icon = getResourceIdFromString(attributes[0]);
                        GameCardView gameCardView = new GameCardView(icon, attributes[1], attributes[2], attributes[3]);
                        games.get(gameCategory).add(gameCardView);
                        break;
                }
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "loadGamesFromXml():", e);
        }
    }

    private void logGameList() {
        for (Map.Entry<String, List<GameCardView>> entry : games.entrySet()) {
            Log.d(TAG, entry.getKey() + ":");

            for (GameCardView game : entry.getValue()) {
                Log.d(TAG, game.toString());
            }
        }
    }

    private void sortGameList() {
        for (Map.Entry<String, List<GameCardView>> entry : games.entrySet()) {
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
        if (resourceStr.startsWith("@")) resourceStr = resourceStr.substring(1);
        String[] splitString = resourceStr.split("/");
        return getResources().getIdentifier(splitString[1], splitString[0], getContext().getPackageName());
    }
}
