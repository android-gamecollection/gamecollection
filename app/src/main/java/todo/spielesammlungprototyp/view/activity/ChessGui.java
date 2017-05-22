package todo.spielesammlungprototyp.view.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.consolechess.ChessBoard;
import todo.spielesammlungprototyp.tools.Movetranslator;
import todo.spielesammlungprototyp.tools.Tupel;
import todo.spielesammlungprototyp.view.customViews.Chessboard;

/**
 * Created by Oliver on 19.05.2017.
 */
public class ChessGui extends Activity {

    Chessboard chessboard;
    ImageView[][] figuren;
    Tupel<Integer, Integer> logged;
    ChessBoard board;

    public ChessGui(String FEN) {
        this();
        board.setPosition(FEN);
    }

    public ChessGui() {
        board = new ChessBoard();
        board.setStartPosition();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chessboard = new Chessboard(this);
        addContentView(chessboard, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        figuren = new ImageView[Chessboard.ANZAHL_FELDER_HORIZONTAL][Chessboard.ANZAHL_FELDER_VERTICAL];
        setFieldFromFEN(board.getBoard());
        final ViewTreeObserver vto = chessboard.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addImages();
                chessboard.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }


        });
        chessboard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    Tupel<Integer, Integer> tupel = chessboard.getfieldfromtouch((int) event.getX(), (int) event.getY());
                    if (tupel != null) {
                        trymove(tupel);
                    }

                }
                return true;
            }
        });

    }

    public void trymove(Tupel<Integer, Integer> tupel) {

        if (logged == null) {
            if (figuren[tupel.first][tupel.second] != null) {
                logged = tupel;
                chessboard.addgreen(tupel);

                return;
            }
            if (figuren[tupel.first][tupel.second] == null) {
                return;
            }
        }

        if (logged.first == tupel.first && logged.second == tupel.second) {
            logged = null;
            chessboard.removegreen();
            return;
        }

        Movetranslator mt = Movetranslator.getInstance();
        String move = mt.numToString(logged) + mt.numToString(tupel);
        if (board.move(mt.numToString(logged).toLowerCase(), mt.numToString(tupel).toLowerCase())) {
            animatefigure(logged, tupel);
            logged = null;
            chessboard.removegreen();
            aimove();
        }

    }

    public void aimove() {
        Movetranslator mt = Movetranslator.getInstance();
        String move = board.aimove();
        animatefigure(mt.stringToNum(move.substring(0, 2)), mt.stringToNum(move.substring(2, 4)));
    }

    public void update() {
        if (board.isDraw()) {
            Toast t = Toast.makeText(this, "Patt", Toast.LENGTH_SHORT);
            t.show();
        }
        if (board.isMate()) {
            Toast t = Toast.makeText(this, "Schachmatt", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public boolean animatefigure(final Tupel<Integer, Integer> from, final Tupel<Integer, Integer> to) {

        if (figuren[from.first][from.second] != null) {

            float fromx = chessboard.feld[from.first][from.second].left;
            float tox = chessboard.feld[to.first][to.second].left;
            float fromy = chessboard.feld[from.first][from.second].top;
            float toy = chessboard.feld[to.first][to.second].top;

            ObjectAnimator animX = ObjectAnimator.ofFloat(figuren[from.first][from.second], "x", fromx, tox);
            ObjectAnimator animY = ObjectAnimator.ofFloat(figuren[from.first][from.second], "y", fromy, toy);
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
                    if (figuren[to.first][to.second] != null) {
                        ((ViewGroup) figuren[to.first][to.second].getParent()).removeView(figuren[to.first][to.second]);
                    }

                    figuren[to.first][to.second] = figuren[from.first][from.second];
                    figuren[from.first][from.second] = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            update();
            return true;
        }
        return false;
    }

    private void addImages() {
        for (int i = 0; i < Chessboard.ANZAHL_FELDER_HORIZONTAL; i++) {
            for (int j = 0; j < Chessboard.ANZAHL_FELDER_VERTICAL; j++) {
                if (figuren[i][j] != null) {
                    int top = chessboard.feld[i][j].top;
                    int left = chessboard.feld[i][j].left;
                    figuren[i][j].setX(left);
                    figuren[i][j].setY(top);
                    addContentView(figuren[i][j], new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            }
        }
    }

    public void setFieldFromFEN(String FEN) {
        int x = 0;
        int y = 0;
        String text = FEN;
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
                        figuren[x][y].setImageResource(getVcsIDfromChar(c));
                        x++;
                    }
                    break;
            }
        }
    }

    private int getVcsIDfromChar(char c) {
        switch (c) {
            case 'r':
                return R.drawable.game_chess_rook_b;
            case 'R':
                return R.drawable.game_chess_rook_w;
            case 'n':
                return R.drawable.game_chess_knight_b;
            case 'N':
                return R.drawable.game_chess_knight_w;
            case 'b':
                return R.drawable.game_chess_bishop_b;
            case 'B':
                return R.drawable.game_chess_bishop_w;
            case 'q':
                return R.drawable.game_chess_queen_b;
            case 'Q':
                return R.drawable.game_chess_queen_w;
            case 'k':
                return R.drawable.game_chess_king_b;
            case 'K':
                return R.drawable.game_chess_king_w;
            case 'p':
                return R.drawable.game_chess_pawn_b;
            case 'P':
                return R.drawable.game_chess_pawn_w;
            default:
                return R.drawable.game_chess_king_b;
        }
    }
}