package todo.gamecollection.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import todo.gamecollection.App;
import todo.gamecollection.R;
import todo.gamecollection.model.gamemanager.Games;
import todo.gamecollection.model.savegamestorage.Savegame;
import todo.gamecollection.model.savegamestorage.SavegameAdapter;
import todo.gamecollection.model.savegamestorage.SavegameStorage;
import todo.gamecollection.model.util.AndroidResources;
import todo.gamecollection.model.util.AnimationEndListener;

public class Hub extends Fragment {

    private SavegameAdapter savegameAdapter;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fabNewGame, fabNewGameC, fabNewGameB;
    private Animation fabOpenUpper, fabCloseUpper, fabCloseLower, fabOpenLower, fabRotateClockwise, fabRotateAnticlockwise;
    private boolean isFabOpen = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView emptyText;
    private AlphaAnimation alphaOut, alphaIn;

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
        emptyText = (TextView) rootView.findViewById(R.id.fragment_hub_empty_textview);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinator_layout);
        setupAlphaAnimation();
        setupRecyclerView();
        checkSavegameAdapterCount();
        setupFabs(rootView);
    }

    private void setupFabs(View rootView) {
        Context context = App.getContext();
        fabNewGame = (FloatingActionButton) rootView.findViewById(R.id.fab_new_game);
        fabNewGameC = (FloatingActionButton) rootView.findViewById(R.id.fab_new_cardgame);
        fabNewGameB = (FloatingActionButton) rootView.findViewById(R.id.fab_new_boardgame);
        fabOpenUpper = AnimationUtils.loadAnimation(context, R.anim.fab_open_upper);
        fabCloseUpper = AnimationUtils.loadAnimation(context, R.anim.fab_close_upper);
        fabOpenLower = AnimationUtils.loadAnimation(context, R.anim.fab_open_lower);
        fabCloseLower = AnimationUtils.loadAnimation(context, R.anim.fab_close_lower);
        fabRotateClockwise = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_clockwise);
        fabRotateAnticlockwise = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_anticlockwise);

        fabNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fabNewGameB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((todo.gamecollection.view.activity.Hub) getActivity()).switchFragment('2');
            }
        });

        fabNewGameC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((todo.gamecollection.view.activity.Hub) getActivity()).switchFragment('1');
            }
        });
    }

    private void setupAlphaAnimation() {
        int animationDuration = 250;
        alphaOut = new AlphaAnimation(1, 0);
        alphaIn = new AlphaAnimation(0, 1);
        alphaOut.setDuration(animationDuration);
        alphaIn.setDuration(animationDuration);
    }

    private void animateFab() {
        if (isFabOpen) {
            fabNewGameB.startAnimation(fabCloseLower);
            fabNewGameC.startAnimation(fabCloseUpper);
            fabNewGame.startAnimation(fabRotateAnticlockwise);
            fabNewGameB.setClickable(false);
            fabNewGameC.setClickable(false);
            this.isFabOpen = false;
        } else {
            fabNewGameB.startAnimation(fabOpenLower);
            fabNewGameC.startAnimation(fabOpenUpper);
            fabNewGame.startAnimation(fabRotateClockwise);
            fabNewGameB.setClickable(true);
            fabNewGameC.setClickable(true);
            this.isFabOpen = true;
        }
    }

    private void tintFabs() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        int colorStr = prefs.getInt(
                getString(R.string.settings_general_accent_color_key),
                AndroidResources.getColor(R.color.colorAccent)
        );
        ColorStateList csl = ColorStateList.valueOf(colorStr);
        fabNewGame.setBackgroundTintList(csl);
        fabNewGameC.setBackgroundTintList(csl);
        fabNewGameB.setBackgroundTintList(csl);
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
                    fabNewGame.hide();
                } else {
                    fabNewGame.show();
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alphaOut.setAnimationListener(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setupAdapter();
                        recyclerView.startAnimation(alphaIn);
                        refreshComplete();
                    }
                });
                recyclerView.startAnimation(alphaOut);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        tintFabs();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setupAdapter() {
        savegameAdapter = new SavegameAdapter();
        savegameAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                checkSavegameAdapterCount();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                checkSavegameAdapterCount();
            }
        });
        recyclerView.setAdapter(savegameAdapter);
    }

    private void refreshComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void checkSavegameAdapterCount() {
        if (savegameAdapter.getItemCount() == 0) {
            emptyText.startAnimation(alphaIn);
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.startAnimation(alphaOut);
            emptyText.setVisibility(View.GONE);
        }
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
            snackbar.setAction(R.string.undo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savegameAdapter.addItem(savegame);
                }
            });
            snackbar.setActionTextColor(AndroidResources.getColor(R.color.snackbarActionColor));
            snackbar.show();
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                Paint rectPaint = new Paint();
                int rgb = getRgbValue(dX, itemView.getWidth());
                rectPaint.setARGB(255, rgb, rgb, rgb);
                if (dX > 0) {
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), rectPaint);
                } else {
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), rectPaint);
                }
                float alpha = (1 - getFactor(dX, itemView.getWidth()));
                itemView.setAlpha(alpha);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        private int getRgbValue(float dX, int itemWidth) {
            int minRgb = 200;
            int maxRgb = 255;
            return (int) (minRgb + (maxRgb - minRgb) * getFactor(dX, itemWidth));
        }

        private float getFactor(float dX, int itemWidth) {
            return Math.abs(dX) / itemWidth;
        }
    }
}
