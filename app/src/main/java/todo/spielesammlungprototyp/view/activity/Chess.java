package todo.spielesammlungprototyp.view.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.chess.ChessHistoryAdapter;
import todo.spielesammlungprototyp.model.games.chess.ChessWrapper;
import todo.spielesammlungprototyp.model.util.CharacterIterator;
import todo.spielesammlungprototyp.model.util.MapBuilder;
import todo.spielesammlungprototyp.model.util.MoveTranslator;
import todo.spielesammlungprototyp.model.util.Tuple;
import todo.spielesammlungprototyp.view.view.CheckeredGameboardView;

public class Chess extends GameActivity {

    private static Map<Character, Integer> chessDrawables = new MapBuilder<Character, Integer>().build(
            'r', R.drawable.game_chess_rook_b,
            'R', R.drawable.game_chess_rook_w,
            'n', R.drawable.game_chess_knight_b,
            'N', R.drawable.game_chess_knight_w,
            'b', R.drawable.game_chess_bishop_b,
            'B', R.drawable.game_chess_bishop_w,
            'q', R.drawable.game_chess_queen_b,
            'Q', R.drawable.game_chess_queen_w,
            'k', R.drawable.game_chess_king_b,
            'K', R.drawable.game_chess_king_w,
            'p', R.drawable.game_chess_pawn_b,
            'P', R.drawable.game_chess_pawn_w
    );
    private final int ANIMATION_SPEED = 1000;
    private CheckeredGameboardView chessboardView;
    private ImageView[][] figuren;
    private Tuple<Integer, Integer> logged;
    private ChessWrapper board;
    private int gridSize;
    private LinearLayoutManager recyclerManager;
    private ChessHistoryAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        board = new ChessWrapper();
        board.setStartPosition();
        chessboardView = (CheckeredGameboardView) findViewById(R.id.gridview_chess);
        gridSize = chessboardView.getGridSize();
        figuren = new ImageView[gridSize][gridSize];
        setFieldFromFEN(board.getBoard());
        final ViewTreeObserver vto = chessboardView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addImages();
                chessboardView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        chessboardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Tuple<Integer, Integer> tuple = chessboardView.getFieldFromTouch((int) event.getX(), (int) event.getY());
                    if (tuple != null) {
                        trymove(tuple);
                    }
                }
                return true;
            }
        });
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerHistory = (RecyclerView) findViewById(R.id.recyclerview_history);
        recyclerManager = new LinearLayoutManager(this);
        recyclerHistory.setLayoutManager(recyclerManager);
        recyclerAdapter = new ChessHistoryAdapter();
        recyclerHistory.setAdapter(recyclerAdapter);
        DividerItemDecoration recyclerDecoration = new DividerItemDecoration(recyclerHistory.getContext(), recyclerManager.getOrientation());
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.shape_recyclerview_divider);
        recyclerDecoration.setDrawable(drawable);
        recyclerHistory.addItemDecoration(recyclerDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ChessHistoryCallback());
        itemTouchHelper.attachToRecyclerView(recyclerHistory);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                testAddItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void testAddItem() {
        recyclerAdapter.addItem();
        recyclerManager.scrollToPosition(0);
    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_chess;
    }

    private void trymove(Tuple<Integer, Integer> tuple) {
        if (logged == null) {
            if (figuren[tuple.first][tuple.last] != null) {
                logged = tuple;
                if (chessboardView.isHighlightEnabled()) {
                    chessboardView.addHighlightColor(tuple);
                    if (chessboardView.areSuggestionsEnabled()) {
                        chessboardView.addSuggestionColor(getPossibleMoves(tuple));
                    }
                }
            }
        } else if (logged.equals(tuple)) {
            logged = null;
            chessboardView.clearColors();
        } else if (board.isPromotion(MoveTranslator.numToString(logged), MoveTranslator.numToString(tuple))) {
            promotionDialog(logged, tuple);
            logged = null;
        } else if (board.move(MoveTranslator.numToString(logged), MoveTranslator.numToString(tuple))) {
            animatefigure(logged, tuple);
            logged = null;
            chessboardView.clearColors();
            aimove();
        }
    }

    private void promotionDialog(final Tuple<Integer, Integer> from, final Tuple<Integer, Integer> to) {
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_chess_promotion_dialog, null);
        TextView title = new TextView(this);
        title.setText(R.string.game_chess_promotion_dialog_title);
        title.setBackgroundColor(Color.DKGRAY);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setCustomTitle(title)
                .setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView queen = (ImageView) alertDialog.findViewById(R.id.promotion_queen);
        queen.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'q'));
        ImageView knight = (ImageView) alertDialog.findViewById(R.id.promotion_knight);
        knight.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'k'));
        ImageView rook = (ImageView) alertDialog.findViewById(R.id.promotion_rook);
        rook.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'r'));
        ImageView bishop = (ImageView) alertDialog.findViewById(R.id.promotion_bishop);
        bishop.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'b'));
    }

    private void promotionmove(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to, char c) {
        board.promotionmove(MoveTranslator.numToString(from), MoveTranslator.numToString(to), c);
        chessboardView.clearColors();
        animatefigure(from, to);
        aimove();
    }

    private Tuple<Integer, Integer>[] getPossibleMoves(Tuple<Integer, Integer> position) {
        return board.getPossibleMoves(position, board.getBoard());
    }

    private void aimove() {
        if (board.isEndgame() != 0) return;
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String move = board.aimove();
                animatefigure(MoveTranslator.stringToNum(move.substring(0, 2)), MoveTranslator.stringToNum(move.substring(2, 4)));
            }
        }, ANIMATION_SPEED + 1000);
    }

    private void update() {
        Log.d("Chess.java", "update()");
        int endgame = board.isEndgame();
        if (endgame != 0) {
            int gameoverReason;
            switch (endgame) {
                case 1:
                    gameoverReason = R.string.game_chess_gameover_checkmate_white;
                    break;
                case -1:
                    gameoverReason = R.string.game_chess_gameover_checkmate_black;
                    break;
                case 99:
                    gameoverReason = R.string.game_chess_gameover_stalemate;
                    break;
                default:
                    gameoverReason = R.string.game_chess_gameover_noreason;
            }
            String snackbarText = getString(R.string.game_chess_snackbar_gameover) + getString(gameoverReason);
            CoordinatorLayout chessLayout = (CoordinatorLayout) findViewById(R.id.chess_coordinatorlayout);
            final Snackbar snackbar = Snackbar.make(chessLayout, snackbarText, Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
            snackbar.setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            snackbar.show();
        }
        setFieldFromFEN(board.getBoard());
        addImages();
    }

    private boolean animatefigure(final Tuple<Integer, Integer> from, final Tuple<Integer, Integer> to) {
        if (figuren[from.first][from.last] != null) {
            Point rectFrom = chessboardView.getRectangleCoordinates(from);
            Point rectTo = chessboardView.getRectangleCoordinates(to);

            ObjectAnimator animX = ObjectAnimator.ofFloat(figuren[from.first][from.last], "x", rectFrom.x, rectTo.x);
            ObjectAnimator animY = ObjectAnimator.ofFloat(figuren[from.first][from.last], "y", rectFrom.y, rectTo.y);
            AnimatorSet animset = new AnimatorSet();
            animset.playTogether(animX, animY);
            animset.setDuration(ANIMATION_SPEED);
            animset.setInterpolator(new AccelerateDecelerateInterpolator());
            animset.start();
            animset.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    removeFigure(to);
                    figuren[to.first][to.last] = figuren[from.first][from.last];
                    figuren[from.first][from.last] = null;
                    update();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            return true;
        }
        return false;
    }

    private void addImages() {
        int imageSize = chessboardView.getThickness();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (figuren[i][j] != null) {
                    Point point = chessboardView.getRectangleCoordinates(new Tuple<>(i, j));
                    figuren[i][j].setX(point.x);
                    figuren[i][j].setY(point.y);
                    addContentView(figuren[i][j], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    figuren[i][j].getLayoutParams().width = imageSize;
                    figuren[i][j].getLayoutParams().height = imageSize;
                }
            }
        }
    }

    private void removeFigure(Tuple<Integer, Integer> tupel) {
        if (figuren[tupel.first][tupel.last] != null) {
            ((ViewGroup) figuren[tupel.first][tupel.last].getParent()).removeView(figuren[tupel.first][tupel.last]);
            figuren[tupel.first][tupel.last] = null;
        }
    }

    private void removeAllFigures() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                removeFigure(new Tuple<>(i, j));
            }
        }
    }

    private void setFieldFromFEN(String fen) {
        int x = 0;
        int y = 0;
        Log.d("Fen :", fen);
        removeAllFigures();

        CharacterIterator iterator = new CharacterIterator(fen, true);
        while (iterator.hasNext()) {
            Character c = iterator.next();
            if (c.equals('/')) {
                x = 0;
                y += 1;
            } else if (Character.isDigit(c)) {
                x += Character.getNumericValue(c);
            } else {
                if (figuren[x][y] == null)
                    figuren[x][y] = new ImageView(this);
                figuren[x][y].setImageResource(chessDrawables.get(c));
                x += 1;
            }
        }
    }

    private class PromotionClickListener implements View.OnClickListener {
        private AlertDialog alertDialog;
        private Tuple<Integer, Integer> from;
        private Tuple<Integer, Integer> to;
        private char figur;

        PromotionClickListener(AlertDialog alertDialog, Tuple<Integer, Integer> from, Tuple<Integer, Integer> to, char figur) {
            this.alertDialog = alertDialog;
            this.from = from;
            this.to = to;
            this.figur = figur;
        }

        public void onClick(View v) {
            alertDialog.dismiss();
            promotionmove(from, to, figur);
        }
    }

    private class ChessHistoryCallback extends ItemTouchHelper.SimpleCallback {

        ChessHistoryCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            recyclerAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }
}