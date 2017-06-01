package todo.spielesammlungprototyp.view.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Map;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.ChessWrapper;
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
    private CheckeredGameboardView chessboardView;
    private ImageView[][] figuren;
    private Tuple<Integer, Integer> logged;
    private ChessWrapper board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_chess);
        super.onCreate(savedInstanceState);

        board = new ChessWrapper();
        board.setStartPosition();
        board.setPosition("pppqkppp/PPPPPPPP/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        chessboardView = (CheckeredGameboardView) findViewById(R.id.gridview_chess);
        figuren = new ImageView[CheckeredGameboardView.HORIZONTAL_SQUARES_COUNT][CheckeredGameboardView.VERTICAL_SQUARES_COUNT];
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
    }

    public void trymove(Tuple<Integer, Integer> tuple) {
        MoveTranslator mt = MoveTranslator.getInstance();
        if (logged == null) {
            if (figuren[tuple.first][tuple.last] != null) {
                logged = tuple;
                chessboardView.addSuggestionColor(tuple);
                chessboardView.addHighlightColor(getPossibleMoves(tuple));
            }
        } else if (logged.equals(tuple)) {
            logged = null;
            chessboardView.clearColors();
        } else if (board.isPromotion(mt.numToString(logged).toLowerCase(), mt.numToString(tuple).toLowerCase())) {
            promotionDialog(logged, tuple);
            logged = null;
        } else {
            if (board.move(mt.numToString(logged).toLowerCase(), mt.numToString(tuple).toLowerCase())) {
                animatefigure(logged, tuple);
                logged = null;
                chessboardView.clearColors();
                aimove();
            }
        }
    }

    public void promotionDialog(final Tuple<Integer, Integer> from, final Tuple<Integer, Integer> to) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_chess_promotion_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view)
                .setTitle("Select a figure")
                .setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView queen = (ImageView) alertDialog.findViewById(R.id.promotion_queen);
        queen.setOnClickListener(new PromotionClickListener(alertDialog,from,to,'q'));
        ImageView knight = (ImageView) alertDialog.findViewById(R.id.promotion_knight);
        knight.setOnClickListener(new PromotionClickListener(alertDialog,from,to,'k'));
        ImageView rook = (ImageView) alertDialog.findViewById(R.id.promotion_rook);
        rook.setOnClickListener(new PromotionClickListener(alertDialog,from,to,'r'));
        ImageView bishop = (ImageView) alertDialog.findViewById(R.id.promotion_bishop);
        bishop.setOnClickListener(new PromotionClickListener(alertDialog,from,to,'b'));
    }

    public void promotionmove(Tuple<Integer, Integer> from, Tuple<Integer, Integer> to, char c) {
        MoveTranslator mt = MoveTranslator.getInstance();
        board.promotionmove(mt.numToString(from).toLowerCase(), mt.numToString(to).toLowerCase(), c);
        chessboardView.clearColors();
        animatefigure(from,to);
        aimove();
    }

    public Tuple<Integer, Integer>[] getPossibleMoves(Tuple<Integer, Integer> position) {
        return board.getPossibleMoves(position, board.getBoard());
    }

    public void aimove() {
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MoveTranslator mt = MoveTranslator.getInstance();
                String move = board.aimove();
                animatefigure(mt.stringToNum(move.substring(0, 2)), mt.stringToNum(move.substring(2, 4)));
            }
        }, 1000);
    }

    public void update() {
        int endgame = board.isEndgame();
        if(endgame != 0) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.chess_ralativelayout);
            if (endgame == 1) {
                Snackbar bar = Snackbar.make(relativeLayout, "Schachmatt, Weiß hat gewonnen", Snackbar.LENGTH_LONG);
                bar.show();
            }
            if (endgame == -1) {
                Snackbar bar = Snackbar.make(relativeLayout, "Schachmatt, Schwarz hat gewonnen", Snackbar.LENGTH_LONG);
                bar.show();
            }
            if (endgame == 99) {
                Snackbar bar = Snackbar.make(relativeLayout, "Patt", Snackbar.LENGTH_LONG);
                bar.show();
            }
        }
        setFieldFromFEN(board.getBoard());
        addImages();
    }

    public boolean animatefigure(final Tuple<Integer, Integer> from, final Tuple<Integer, Integer> to) {

        if (figuren[from.first][from.last] != null) {
            Point rectFrom = chessboardView.getRectangleCoordinates(from);
            Point rectTo = chessboardView.getRectangleCoordinates(to);

            ObjectAnimator animX = ObjectAnimator.ofFloat(figuren[from.first][from.last], "x", rectFrom.x, rectTo.x);
            ObjectAnimator animY = ObjectAnimator.ofFloat(figuren[from.first][from.last], "y", rectFrom.y, rectTo.y);
            AnimatorSet animset = new AnimatorSet();
            animset.playTogether(animX, animY);
            animset.setDuration(500);
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
        for (int i = 0; i < CheckeredGameboardView.HORIZONTAL_SQUARES_COUNT; i++) {
            for (int j = 0; j < CheckeredGameboardView.VERTICAL_SQUARES_COUNT; j++) {
                if (figuren[i][j] != null) {
                    Point rect = chessboardView.getRectangleCoordinates(new Tuple<>(i, j));
                    figuren[i][j].setX(rect.x);
                    figuren[i][j].setY(rect.y);
                    addContentView(figuren[i][j], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

    public void setFieldFromFEN(String fen) {
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
    private class PromotionClickListener implements View.OnClickListener
    {
        private AlertDialog alertDialog;
        private Tuple<Integer,Integer> from;
        private Tuple<Integer,Integer> to;
        private char figur;

        public PromotionClickListener(AlertDialog alertDialog, Tuple<Integer, Integer> from, Tuple<Integer, Integer> to, char figur) {
            this.alertDialog = alertDialog;
            this.from = from;
            this.to = to;
            this.figur = figur;
        }

        public void onClick(View v) {
            alertDialog.cancel();
            promotionmove(from, to, figur);
        }
    }
}