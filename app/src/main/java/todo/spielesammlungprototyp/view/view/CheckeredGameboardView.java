package todo.spielesammlungprototyp.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.Tuple;

public class CheckeredGameboardView extends View {
    private int gridSize, thickness, strokeWidth, borderStrokeWidth, highlightStrokeStyle, borderSize;
    private Rect[][] boardSquares;
    private List<Tuple<Integer, Integer>> highlightSquares, suggestionSquares;
    private Paint paintDark, paintLight, paintSuggestion, paintHightlight, paintBorder;
    private boolean disableSuggestions;

    public CheckeredGameboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckeredGameboardView, 0, 0));
    }

    private void init(TypedArray attrs) {
        highlightSquares = new ArrayList<>();
        suggestionSquares = new ArrayList<>();

        int flags = Paint.ANTI_ALIAS_FLAG;

        paintDark = new Paint(flags);
        paintLight = new Paint(flags);
        paintBorder = new Paint(flags);
        paintSuggestion = new Paint(flags);
        paintHightlight = new Paint(flags);

        paintBorder.setStyle(Paint.Style.STROKE);
        paintSuggestion.setStyle(Paint.Style.STROKE);
        paintHightlight.setStyle(Paint.Style.STROKE);

        paintLight.setColor(attrs.getColor(R.styleable.CheckeredGameboardView_colorLight, Color.WHITE));
        paintDark.setColor(attrs.getColor(R.styleable.CheckeredGameboardView_colorDark, Color.GRAY));
        paintBorder.setColor(attrs.getColor(R.styleable.CheckeredGameboardView_colorBorder, Color.GRAY));
        paintSuggestion.setColor(attrs.getColor(R.styleable.CheckeredGameboardView_colorSuggestion, Color.GREEN));
        paintHightlight.setColor(attrs.getColor(R.styleable.CheckeredGameboardView_colorHighlight, Color.YELLOW));

        highlightStrokeStyle = attrs.getInteger(R.styleable.CheckeredGameboardView_highlightStrokeStyle, 0);
        borderSize = attrs.getInteger(R.styleable.CheckeredGameboardView_borderSize, 0);

        disableSuggestions = attrs.getBoolean(R.styleable.CheckeredGameboardView_disableSuggestions, false);

        this.gridSize = attrs.getInteger(R.styleable.CheckeredGameboardView_gridSize, 8);
        boardSquares = new Rect[gridSize][gridSize];

        attrs.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Paint paintSquare = ((i + j) % 2 == 0) ? paintLight : paintDark;
                canvas.drawRect(boardSquares[i][j], paintSquare);
            }
        }
        Rect firstSquare = boardSquares[0][0];
        Rect[] lastRow = boardSquares[boardSquares.length - 1];
        Rect lastSquare = lastRow[lastRow.length - 1];
        if (borderStrokeWidth > 0) {
            canvas.drawRect(outerStroke(borderStrokeWidth, firstSquare.left, firstSquare.top, lastSquare.right, lastSquare.bottom), paintBorder);
        }
        if (strokeWidth > 0) {
            if (!disableSuggestions) {
                paintSquares(canvas, suggestionSquares, paintSuggestion);
            }
            paintSquares(canvas, highlightSquares, paintHightlight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width < height ? width : height;
        int factor;
        HighlightStrokeStyle highlightStrokeStyle = HighlightStrokeStyle.values()[this.highlightStrokeStyle];
        switch (highlightStrokeStyle) {
            case THIN:
                factor = 200;
                break;
            case FILL:
                factor = 10;
                break;
            case NONE:
                factor = 0;
                break;
            default:
                factor = 100;
        }
        strokeWidth = factor != 0 ? size / factor : 0;

        BorderSize borderSize = BorderSize.values()[this.borderSize];
        switch (borderSize) {
            case SMALL:
                factor = 200;
                break;
            case BIG:
                factor = 20;
                break;
            case NONE:
                factor = 0;
                break;
            default:
                factor = 100;
        }
        borderStrokeWidth = factor != 0 ? size / factor : 0;
        thickness = (size - borderStrokeWidth * 2) / gridSize;
        // Calculate size again because of rounding errors when dividing
        size = thickness * 8 + borderStrokeWidth * 2;
        applyStrokes();
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                boardSquares[i][j] = new Rect();
                boardSquares[i][j].left = i * thickness;
                boardSquares[i][j].top = j * thickness;
                boardSquares[i][j].right = (i + 1) * thickness;
                boardSquares[i][j].bottom = (j + 1) * thickness;
                boardSquares[i][j].offset(borderStrokeWidth, borderStrokeWidth);
            }
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

    public int getThickness() {
        return thickness;
    }

    public int getGridSize() {
        return gridSize;
    }

    public boolean isHighlightEnabled() {
        return HighlightStrokeStyle.values()[highlightStrokeStyle] != HighlightStrokeStyle.NONE;
    }

    public boolean areSuggestionsEnabled() {
        return !disableSuggestions;
    }

    public Tuple<Integer, Integer> getFieldFromTouch(int x, int y) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (boardSquares[i][j].contains(x, y)) {
                    return new Tuple<>(i, j);
                }
            }
        }
        return null;
    }

    public Point getRectangleCoordinates(Tuple<Integer, Integer> tuple) {
        Rect rect = boardSquares[tuple.first][tuple.last];
        int x = this.getLeft() + rect.left;
        int y = this.getTop() + rect.top;
        return new Point(x, y);
    }

    private void paintSquares(Canvas canvas, List<Tuple<Integer, Integer>> squares, Paint paint) {
        for (Tuple<Integer, Integer> t : squares) {
            canvas.drawRect(innerStroke(strokeWidth, boardSquares[t.first][t.last]), paint);
        }
    }

    private Rect innerStroke(int strokeWidth, Rect rect) {
        int offset = strokeWidth / 2;
        Rect newRect = new Rect(rect);
        newRect.inset(offset, offset);
        return newRect;
    }

    private Rect outerStroke(int strokeWidth, int left, int top, int right, int bottom) {
        int offset = strokeWidth / 2;
        Rect newRect = new Rect(left, top, right, bottom);
        newRect.inset(-offset, -offset);
        return newRect;
    }

    private void applyStrokes() {
        paintBorder.setStrokeWidth(borderStrokeWidth);
        paintSuggestion.setStrokeWidth(strokeWidth);
        paintHightlight.setStrokeWidth(strokeWidth);
    }

    private enum HighlightStrokeStyle {
        REGULAR, THIN, FILL, NONE
    }

    private enum BorderSize {
        NORMAL, SMALL, BIG, NONE
    }
}