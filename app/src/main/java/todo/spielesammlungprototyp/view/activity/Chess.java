package todo.spielesammlungprototyp.view.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.ChessWrapper;
import todo.spielesammlungprototyp.model.util.MapBuilder;
import todo.spielesammlungprototyp.model.util.MoveTranslator;
import todo.spielesammlungprototyp.model.util.Tuple;
import todo.spielesammlungprototyp.view.view.ChessboardView;

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
    private ChessboardView chessboardView;
    private ImageView[][] figuren;
    private Tuple<Integer, Integer> logged;
    private ChessWrapper board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_chess);
        super.onCreate(savedInstanceState);

        board = new ChessWrapper();
        board.setStartPosition();
        chessboardView = (ChessboardView) findViewById(R.id.gridview_chess);
        figuren = new ImageView[ChessboardView.ANZAHL_FELDER_HORIZONTAL][ChessboardView.ANZAHL_FELDER_VERTICAL];
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
        if (logged == null) {
            if (figuren[tuple.first][tuple.last] != null) {
                logged = tuple;
                chessboardView.addYellow(tuple);
                chessboardView.addGreen(getPossibleMoves(tuple));
            }
        } else if (logged.equals(tuple)) {
            logged = null;
            chessboardView.removeGreen();
            chessboardView.removeYellow();
        } else {
            MoveTranslator mt = MoveTranslator.getInstance();
            if (board.move(mt.numToString(logged).toLowerCase(), mt.numToString(tuple).toLowerCase())) {
                animatefigure(logged, tuple);
                logged = null;
                chessboardView.removeGreen();
                chessboardView.removeYellow();
                aimove();
            }
        }
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
        if (board.isDraw()) {
            Toast.makeText(this, "Patt", Toast.LENGTH_SHORT).show();
        }
        if (board.isMate()) {
            Toast.makeText(this, "Schachmatt", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < ChessboardView.ANZAHL_FELDER_HORIZONTAL; i++) {
            for (int j = 0; j < ChessboardView.ANZAHL_FELDER_VERTICAL; j++) {
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
                removeFigure(new Tuple<Integer, Integer>(i, j));
            }
        }
    }

    public void setFieldFromFEN(String FEN) {
        int x = 0;
        int y = 0;
        String text = FEN;
        Log.d("Fen :", FEN);
        removeAllFigures();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case 'w':
                case ' ':
                    return;
                case '/':
                    x = 0;
                    y++;
                    break;
                default:
                    if (Character.isDigit(c)) {
                        int number = Integer.parseInt(text.substring(i, i + 1));
                        x = x + number;
                    } else {
                        figuren[x][y] = new ImageView(this);
                        figuren[x][y].setImageResource(chessDrawables.get(c));
                        x++;
                    }
                    break;
            }
        }
    }
}