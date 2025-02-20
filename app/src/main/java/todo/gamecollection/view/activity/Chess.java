package todo.gamecollection.view.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import todo.gamecollection.R;
import todo.gamecollection.model.games.chess.ChessHistoryAdapter;
import todo.gamecollection.model.games.chess.ChessWrapper;
import todo.gamecollection.model.games.chess.Doublemove;
import todo.gamecollection.model.games.chess.MoveTranslator;
import todo.gamecollection.model.util.AndroidResources;
import todo.gamecollection.model.util.AnimationEndListener;
import todo.gamecollection.model.util.AnimatorEndListener;
import todo.gamecollection.model.util.CharacterIterator;
import todo.gamecollection.model.util.MapBuilder;
import todo.gamecollection.model.util.Tuple;
import todo.gamecollection.view.view.CheckeredGameboardView;

public class Chess extends GameActivity {

    private static final String KEY_HISTORY = "HISTORY";
    private static final String KEY_MOVES = "MOVES";
    private static final int translationZ = 8;
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
    private Tuple<Tuple<Integer, Integer>, Tuple<Integer, Integer>> whitemove = null;
    private int whiteid;
    private ChessWrapper board;
    private int gridSize;
    private LinearLayoutManager recyclerManager;
    private ChessHistoryAdapter recyclerAdapter;
    private boolean aiGame, stateAllowClick = true;
    private FrameLayout chessBoardFrame;
    private RecyclerView recyclerHistory;
    private String startValue;
    private AiMoveTask mAiMoveTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chessBoardFrame = (FrameLayout) findViewById(R.id.frame_layout);
        chessboardView = (CheckeredGameboardView) findViewById(R.id.boardgameview_chess);
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
                if (!stateAllowClick) return false;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Tuple<Integer, Integer> tuple = chessboardView.getSquareFromTouch((int) event.getX(), (int) event.getY());
                    if (tuple != null) {
                        onSquareClicked(tuple);
                    }
                }
                return true;
            }
        });
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerHistory = (RecyclerView) findViewById(R.id.recyclerview_history);
        recyclerManager = new LinearLayoutManager(this);
        recyclerHistory.setLayoutManager(recyclerManager);
        if (recyclerAdapter.getItemCount() > 0) {
            setRecyclerVisibility(true);
            chessBoardFrame.animate().translationZ(translationZ);
        }
        recyclerHistory.setAdapter(recyclerAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ChessHistoryCallback());
        itemTouchHelper.attachToRecyclerView(recyclerHistory);
    }

    private void addItem(final Doublemove doublemove) {
        final Runnable addItem = new Runnable() {
            @Override
            public void run() {
                recyclerAdapter.addItem(doublemove);
                recyclerManager.scrollToPosition(0);
            }
        };
        if (chessBoardFrame.getZ() <= 0) {
            setRecyclerVisibility(true);
            chessBoardFrame.animate().translationZ(translationZ).withEndAction(addItem);
        } else {
            addItem.run();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        chessboardView.setColors(
                prefs.getInt(context.getString(R.string.settings_chess_color_light_key), 0),
                prefs.getInt(context.getString(R.string.settings_chess_color_dark_key), 0),
                prefs.getInt(context.getString(R.string.settings_chess_color_border_key), 0),
                prefs.getInt(context.getString(R.string.settings_chess_color_suggestion_key), 0),
                prefs.getInt(context.getString(R.string.settings_chess_color_highlight_key), 0)
        );
        boolean noAiMoveTask = mAiMoveTask == null || mAiMoveTask.getStatus() != AsyncTask.Status.RUNNING;
        if (aiGame && !board.isWhitesTurn() && noAiMoveTask) {
            aimove();
        }
    }

    @Override
    protected void onLoadGame(@Nullable Bundle savegame) {
        board = new ChessWrapper(game.isTaggedWith("chess960"));
        aiGame = game.isTaggedWith("aiGame");
        if (savegame != null) {
            board.doMoves(savegame.getString(KEY_MOVES));
            List<String> stringHistory = savegame.getStringArrayList(KEY_HISTORY);
            ArrayList<Doublemove> history = new ArrayList<>();
            assert stringHistory != null;
            for (String move : stringHistory) {
                history.add(Doublemove.fromString(move));
            }
            recyclerAdapter = new ChessHistoryAdapter(history);
        } else {
            recyclerAdapter = new ChessHistoryAdapter();
        }
        startValue = board.getBoard();
    }

    @Override
    protected void onSaveGame(Bundle savegame) {
        if (mAiMoveTask != null) {
            if (mAiMoveTask.getStatus() == AsyncTask.Status.RUNNING) {
                board.setPosition(mAiMoveTask.fen);
            }
            mAiMoveTask.cancel(true);
        }
        if (!startValue.equals(board.getBoard())) {
            List<Doublemove> history = recyclerAdapter.getAll();
            ArrayList<String> stringHistory = new ArrayList<>();
            for (Doublemove move : history) {
                stringHistory.add(move.toString());
            }
            savegame.putStringArrayList(KEY_HISTORY, stringHistory);
            savegame.putString(KEY_MOVES, board.getMoves());
        }
    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_chess;
    }

    private void setRecyclerVisibility(final boolean visible) {
        int colorStart = AndroidResources.getColor(R.color.background);
        int colorEnd = AndroidResources.getColor(R.color.backgroundDarker);
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(colorStart, colorEnd);
        colorAnimator.setDuration(ANIMATION_SPEED / 2);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                recyclerHistory.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
        if (visible) {
            recyclerHistory.setVisibility(View.VISIBLE);
            colorAnimator.start();
        } else {
            colorAnimator.addListener(new AnimatorEndListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    recyclerHistory.setVisibility(View.GONE);
                }
            });
            colorAnimator.reverse();
        }
    }

    private void onSquareClicked(Tuple<Integer, Integer> tuple) {
        if (board.isEndgame() != 0) return;
        boolean hasPiece = figuren[tuple.first][tuple.last] != null;
        boolean wTurn = board.isWhitesTurn();
        boolean wPiece = board.isWhitePiece(tuple);
        boolean allowSelect = hasPiece && (!aiGame && !wTurn && !wPiece || wTurn && wPiece);
        if (allowSelect) {
            chessboardView.clearColors();
            if (tuple.equals(logged)) {
                logged = null;
            } else {
                logged = tuple;
                if (chessboardView.isHighlightEnabled()) {
                    chessboardView.addHighlightColor(tuple);
                    if (chessboardView.areSuggestionsEnabled()) {
                        chessboardView.addSuggestionColor(getPossibleMoves(tuple));
                    }
                }
            }
        } else if (logged != null) {
            String from = MoveTranslator.numToString(logged);
            String to = MoveTranslator.numToString(tuple);
            boolean valid = false;
            if (board.isPromotion(from, to)) {
                valid = true;
                promotionDialog(logged, tuple);
                logged = null;
            } else if (board.move(from, to)) {
                valid = true;
                domove(logged, tuple);
                logged = null;
                chessboardView.clearColors();
            }
            if (!aiGame && valid) {
                stateAllowClick = false;
            }
        }
    }
    private void domove(Tuple<Integer,Integer>from,Tuple<Integer,Integer>to )
    {
        Tuple<Tuple<Integer,Integer>,Tuple<Integer,Integer>> thismove =  new Tuple(from,to);
        int id = (int)figuren[from.first][from.last].getTag();
        if(whitemove == null) {
            whitemove = thismove;
            whiteid = id;
        }
        else {
            addItem(new Doublemove(whitemove,thismove,whiteid,id));
            whitemove = null;
        }
        animatefigure(from,to);
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
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCustomTitle(title)
                .setCancelable(false)
                .create();
        alertDialog.show();
        ImageView queen = (ImageView) alertDialog.findViewById(R.id.promotion_queen);
        queen.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'q'));
        ImageView knight = (ImageView) alertDialog.findViewById(R.id.promotion_knight);
        knight.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'k'));
        ImageView rook = (ImageView) alertDialog.findViewById(R.id.promotion_rook);
        rook.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'r'));
        ImageView bishop = (ImageView) alertDialog.findViewById(R.id.promotion_bishop);
        bishop.setOnClickListener(new PromotionClickListener(alertDialog, from, to, 'b'));
        boolean isBlack = !board.isWhitePiece(from);
        if (isBlack) {
            queen.setImageDrawable(getDrawable(R.drawable.game_chess_queen_b));
            knight.setImageDrawable(getDrawable(R.drawable.game_chess_knight_b));
            rook.setImageDrawable(getDrawable(R.drawable.game_chess_rook_b));
            bishop.setImageDrawable(getDrawable(R.drawable.game_chess_bishop_b));
        }
    }

    private void promotionmove(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to, char c) {
        board.promotionmove(MoveTranslator.numToString(from), MoveTranslator.numToString(to), c);
        chessboardView.clearColors();
        domove(from, to);
        aimove();
    }

    private Tuple<Integer, Integer>[] getPossibleMoves(Tuple<Integer, Integer> position) {
        return board.getPossibleMoves(position, board.getBoard());
    }

    private void aimove() {
        if (!aiGame || board.isEndgame() != 0) return;
        mAiMoveTask = new AiMoveTask();
        mAiMoveTask.execute();
    }

    private void update() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.chess_coordinatorlayout);
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
            Snackbar.make(coordinatorLayout, snackbarText, Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(AndroidResources.getColor(R.color.snackbarActionColor))
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        } else if (board.isCheck()) {
            Snackbar.make(coordinatorLayout, getString(R.string.game_chess_mate), Snackbar.LENGTH_SHORT)
                    .show();
        }
        setFieldFromFEN(board.getBoard());
        addImages();
    }

    private boolean animatefigure(final Tuple<Integer, Integer> from, final Tuple<Integer, Integer> to) {
        return animatefigure(from, to, ANIMATION_SPEED);
    }

    private boolean animatefigure(final Tuple<Integer, Integer> from, final Tuple<Integer, Integer> to, int speed) {
        if (figuren[from.first][from.last] != null) {
            Point rectFrom = chessboardView.getRectangleCoordinates(from);
            Point rectTo = chessboardView.getRectangleCoordinates(to);
            int deltaX = rectTo.x - rectFrom.x;
            int deltaY = rectTo.y - rectFrom.y;
            TranslateAnimation translateAnimation = new TranslateAnimation(0, deltaX, 0, deltaY);
            translateAnimation.setDuration(speed);
            translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            translateAnimation.setAnimationListener(new AnimationEndListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    update();
                    if (aiGame && !board.isWhitesTurn()) {
                        aimove();
                    } else {
                        stateAllowClick = true;
                    }
                }
            });
            figuren[from.first][from.last].startAnimation(translateAnimation);
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
                    if (!aiGame) {
                        if (!startValue.equals(board.getBoard())) {
                            int pivotX = point.x + imageSize / 2;
                            int pivotY = point.y + imageSize / 2;
                            RotateAnimation rotateAnimation = new RotateAnimation(180, 0, pivotX, pivotY);
                            rotateAnimation.setDuration(ANIMATION_SPEED / 2);
                            rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                            rotateAnimation.setAnimationListener(new AnimationEndListener() {
                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    stateAllowClick = true;
                                }
                            });
                            figuren[i][j].startAnimation(rotateAnimation);
                        }
                        if (board.isWhitesTurn()) {
                            figuren[i][j].setRotation(0);
                        } else {
                            figuren[i][j].setRotation(180);
                        }
                    }
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
                figuren[x][y].setTag(chessDrawables.get(c));
                x += 1;
            }
        }
    }

    private void undoMoves(int howmany){
        for (int i = 0; i <howmany; i++) {
            for(int  j = 0; j< 2; j++)
            {
                String lastmove = board.getLastMove();
                String from = lastmove.substring(2,4);
                String to = lastmove.substring(0,2);
                Log.d("undo",to);
                Log.d("undo",from);
                animatefigure(MoveTranslator.stringToNum(from),MoveTranslator.stringToNum(to));
                board.undoMove();
            }

        }
    }

    private class AiMoveTask extends AsyncTask<Void, Void, String> {

        String fen;
        private long millis;
        private ProgressBar progressBar;

        @Override
        protected String doInBackground(Void... params) {
            return board.aimove();
        }

        @Override
        protected void onPreExecute() {
            millis = System.currentTimeMillis();
            fen = board.getBoard();
            progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            stateAllowClick = false;
        }

        @Override
        protected void onPostExecute(final String move) {
            long ellapsedMillis = System.currentTimeMillis() - millis;
            long delay = ellapsedMillis > ANIMATION_SPEED ? 0 : ANIMATION_SPEED - ellapsedMillis;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    domove(MoveTranslator.stringToNum(move.substring(0, 2)), MoveTranslator.stringToNum(move.substring(2, 4)));
                }
            }, delay);
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
            int before = recyclerAdapter.getItemCount();
            recyclerAdapter.removeItem(viewHolder.getAdapterPosition());
            if (recyclerAdapter.getItemCount() <= 0) {
                setRecyclerVisibility(false);
                chessBoardFrame.animate().translationZ(0);
            }
            undoMoves(before - recyclerManager.getItemCount());
        }
    }
}