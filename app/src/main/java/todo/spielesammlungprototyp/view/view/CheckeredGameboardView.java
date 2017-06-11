package todo.spielesammlungprototyp.view.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.Tuple;

public class CheckeredGameboardView extends View {
    public static final int VERTICAL_SQUARES_COUNT = 8;
    public static final int HORIZONTAL_SQUARES_COUNT = 8;
    private final int COLOR_LIGHT = ContextCompat.getColor(getContext(), R.color.chessSquareLight);
    private final int COLOR_DARK = ContextCompat.getColor(getContext(), R.color.chessSquareDark);
    private final int COLOR_HIGHLIGHT = ContextCompat.getColor(getContext(), R.color.chessSquareHighlight);
    private final int COLOR_SUGGESTION = ContextCompat.getColor(getContext(), R.color.chessSquareSuggestion);
    private final int STROKE_WIDTH = 10;
    private Rect[][] boardQuares;
    private int thickness;
    private List<Tuple<Integer, Integer>> highlightSquares;
    private List<Tuple<Integer, Integer>> suggestionSquares;
    private Paint paintDark;
    private Paint paintLight;
    private Paint paintSuggestion;
    private Paint paintHightlight;

    public CheckeredGameboardView(Context context) {
        super(context);
        init(null, 0);
    }

    public CheckeredGameboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null, 0);
    }

    public CheckeredGameboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        highlightSquares = new ArrayList<>();
        suggestionSquares = new ArrayList<>();

        int flags = Paint.ANTI_ALIAS_FLAG;

        paintDark = new Paint(flags);
        paintDark.setColor(COLOR_DARK);

        paintLight = new Paint(flags);
        paintLight.setColor(COLOR_LIGHT);

        paintSuggestion = new Paint(flags);
        paintSuggestion.setColor(COLOR_SUGGESTION);
        paintSuggestion.setStyle(Paint.Style.STROKE);
        paintSuggestion.setStrokeWidth(STROKE_WIDTH);

        paintHightlight = new Paint(flags);
        paintHightlight.setColor(COLOR_HIGHLIGHT);
        paintHightlight.setStyle(Paint.Style.STROKE);
        paintHightlight.setStrokeWidth(STROKE_WIDTH);

        boardQuares = new Rect[HORIZONTAL_SQUARES_COUNT][VERTICAL_SQUARES_COUNT];
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < VERTICAL_SQUARES_COUNT; i++) {
            for (int j = 0; j < HORIZONTAL_SQUARES_COUNT; j++) {
                Paint paintSquare = ((i + j) % 2 == 0) ? paintLight : paintDark;
                canvas.drawRect(boardQuares[i][j], paintSquare);
            }
        }
        paintSquares(canvas, suggestionSquares, paintSuggestion);
        paintSquares(canvas, highlightSquares, paintHightlight);
    }

    private void paintSquares(Canvas canvas, List<Tuple<Integer, Integer>> squares, Paint paint) {
        int offset = STROKE_WIDTH / 2;
        for (Tuple<Integer, Integer> t : squares) {
            Rect rect = boardQuares[t.first][t.last];
            canvas.drawRect(rect.left + offset, rect.top + offset, rect.right - offset, rect.bottom - offset, paint);
        }
    }

    @SafeVarargs
    public final void addHighlightColor(Tuple<Integer, Integer>... tuple) {
        Collections.addAll(highlightSquares, tuple);
        invalidate();
    }

    @SafeVarargs
    public final void addSuggestionColor(Tuple<Integer, Integer>... tuple) {
        Collections.addAll(suggestionSquares, tuple);
        invalidate();
    }

    public final void clearColors() {
        highlightSquares.clear();
        suggestionSquares.clear();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        for (int i = 0; i < VERTICAL_SQUARES_COUNT; i++) {
            for (int j = 0; j < HORIZONTAL_SQUARES_COUNT; j++) {
                boardQuares[i][j] = new Rect();
                boardQuares[i][j].left = i * thickness;
                boardQuares[i][j].top = j * thickness;
                boardQuares[i][j].right = (i + 1) * thickness;
                boardQuares[i][j].bottom = (j + 1) * thickness;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width < height ? width : height;
        thickness = size / 8;
        setMeasuredDimension(size, size);
    }

   public int getThickness() {
        return thickness;
    }

    public Tuple<Integer, Integer> getFieldFromTouch(int x, int y) {
        for (int i = 0; i < VERTICAL_SQUARES_COUNT; i++) {
            for (int j = 0; j < HORIZONTAL_SQUARES_COUNT; j++) {
                if (boardQuares[i][j].contains(x, y)) {
                    return new Tuple<>(i, j);
                }
            }
        }
        return null;
    }

    public Point getRectangleCoordinates(Tuple<Integer, Integer> tuple) {
        Rect rect = boardQuares[tuple.first][tuple.last];
        int x = this.getLeft() + rect.left;
        int y = this.getTop() + rect.top;
        return new Point(x, y);
    }
}