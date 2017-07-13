package todo.gamecollection.view.fragment;

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

import todo.gamecollection.R;
import todo.gamecollection.model.gamemanager.Game;
import todo.gamecollection.model.gamemanager.GameAdapter;
import todo.gamecollection.model.gamemanager.GameCategory;
import todo.gamecollection.model.gamemanager.Games;

public class GameSelection extends Fragment {

    private static final String EXTRA_CATEGORY = "category";
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

        GameAdapter gameAdapter = new GameAdapter(games);
        recyclerView.setAdapter(gameAdapter);
    }
}
