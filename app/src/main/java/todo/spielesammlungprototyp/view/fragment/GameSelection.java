package todo.spielesammlungprototyp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.gamemanager.Game;
import todo.spielesammlungprototyp.model.gamemanager.GameCategory;
import todo.spielesammlungprototyp.model.gamemanager.Games;
import todo.spielesammlungprototyp.view.ClickListener;
import todo.spielesammlungprototyp.model.gamemanager.GameCardViewAdapter;
import todo.spielesammlungprototyp.view.activity.GameActivity;

public class GameSelection extends Fragment implements ClickListener {

    private static final String EXTRA_CATEGORY = "category";
    private static final String ACTIVITY_PACKAGE = ".view.activity.";
    private List<Game> games;

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

        String gameCategory = getArguments().getString(EXTRA_CATEGORY);
        games = Games.getGameList(gameCategory);
        List<GameCategory> categoryList = Games.getCategoryList();
        for (GameCategory category : categoryList) {
            Log.d("GameSelection", category.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = getView();
        if (rootView == null) {
            throw new NullPointerException("Root view is null");
        }
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_game_selection);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        GameCardViewAdapter gcvAdapter = new GameCardViewAdapter(games);
        gcvAdapter.setClickListener(this);
        recyclerView.setAdapter(gcvAdapter);
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent();
        Context context = view.getContext();
        Game game = games.get(position);
        String str = context.getPackageName() + ACTIVITY_PACKAGE + game.getActivity();
        intent.setClassName(context, str);
        intent.putExtra(GameActivity.KEY_GAME_UUID, game.getUuid());
        context.startActivity(intent);
    }
}
