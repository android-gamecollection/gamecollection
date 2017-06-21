package todo.spielesammlungprototyp.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import todo.spielesammlungprototyp.App;
import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.AndroidResources;
import todo.spielesammlungprototyp.model.util.Games;
import todo.spielesammlungprototyp.model.util.Savegame;
import todo.spielesammlungprototyp.model.util.SavegameStorage;
import todo.spielesammlungprototyp.view.ClickListener;
import todo.spielesammlungprototyp.view.Game;
import todo.spielesammlungprototyp.view.SavegameAdapter;
import todo.spielesammlungprototyp.view.activity.GameActivity;

public class Hub extends Fragment implements ClickListener {

    private final String ACTIVITY_PACKAGE = ".view.activity.";
    private SavegameAdapter savegameAdapter;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hub_content, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View rootView = getView().getRootView();
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinator_layout);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_new_game);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerview_game_selection);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        setupAdapter();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new GameSelectionCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    floatingActionButton.hide();
                } else {
                    floatingActionButton.show();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final AlphaAnimation alphaOut = new AlphaAnimation(1, 0);
                final AlphaAnimation alphaIn = new AlphaAnimation(0, 1);
                final int duration = 250;
                alphaOut.setDuration(duration);
                alphaIn.setDuration(duration);
                alphaOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setupAdapter();
                        recyclerView.startAnimation(alphaIn);
                        refreshComplete();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                recyclerView.startAnimation(alphaOut);
            }
        });
    }

    private void setupAdapter() {
        savegameAdapter = new SavegameAdapter(SavegameStorage.getInstance().getSavegameList());
        savegameAdapter.setClickListener(this);
        recyclerView.setAdapter(savegameAdapter);
    }

    private void refreshComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent();
        Context context = view.getContext();
        Savegame savegame = savegameAdapter.get(position);
        Game game = Games.getFromUuid(savegame.gameUuid);
        if (game == null) {
            throw new NullPointerException();
        }
        String str = context.getPackageName() + ACTIVITY_PACKAGE + game.getActivity();
        intent.setClassName(context, str);
        intent.putExtra(GameActivity.KEY_SAVEGAME_UUID, savegame.uuid);
        context.startActivity(intent);
    }

    private class GameSelectionCallback extends ItemTouchHelper.SimpleCallback {

        GameSelectionCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            final Savegame savegame = savegameAdapter.get(position);
            savegameAdapter.removeItem(position);
            String deleteText = App.getContext().getString(R.string.savegame_deleted);
            String gameTitle = Games.getFromUuid(savegame.gameUuid).getGameTitle();
            String snackbarText = String.format("%s: %s - %s", deleteText, gameTitle, savegame.getDateString());
            Snackbar snackbar = Snackbar.make(coordinatorLayout, snackbarText, Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savegameAdapter.addItem(savegame);
                }
            });
            snackbar.setActionTextColor(AndroidResources.getColor(R.color.snackbarActionColor));
            snackbar.show();
        }
    }
}
