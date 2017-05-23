package todo.spielesammlungprototyp.view.customViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.print.PrintAttributes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import todo.spielesammlungprototyp.tools.Tuple;


/**
 * Created by Oliver on 06.05.2017.
 */

public class Chessboard extends View {
    public static final int ANZAHL_FELDER_VERTICAL = 8;
    public static final int ANZAHL_FELDER_HORIZONTAL = 8;
    public Rect[][] feld;
    int width;
    int height;
    int upperline;
    int leftline;
    int thickness;
    private List<Tuple<Integer, Integer>> greenspots;
    private List<Tuple<Integer, Integer>> yellowspots;
    private Paint darkfield;
    private Paint brightfield;
    private Paint greenfield;
    private Paint yellowfield;

    public Chessboard(Context context) {
        super(context);
        init(null, 0);
    }

    public Chessboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(null, 0);
    }

    public Chessboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        greenspots = new ArrayList<>();
        yellowspots = new ArrayList<>();
        darkfield = new Paint();
        darkfield.setColor(Color.GRAY);
        darkfield.setAntiAlias(true);

        brightfield = new Paint();
        brightfield.setColor(Color.WHITE);
        brightfield.setAntiAlias(true);

        greenfield = new Paint();
        greenfield.setColor(Color.GREEN);
        greenfield.setAntiAlias(true);

        yellowfield = new Paint();
        yellowfield.setColor(Color.YELLOW);
        yellowfield.setAntiAlias(true);

        feld = new Rect[ANZAHL_FELDER_HORIZONTAL][ANZAHL_FELDER_VERTICAL];
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < ANZAHL_FELDER_VERTICAL; i++) {
            for (int j = 0; j < ANZAHL_FELDER_HORIZONTAL; j++) {
                if ((i + j) % 2 == 0) {
                    canvas.drawRect(feld[i][j], brightfield);
                } else {
                    canvas.drawRect(feld[i][j], darkfield);
                }

            }
        }
        for (Tuple<Integer, Integer> t : greenspots) {
            canvas.drawRect(feld[t.first][t.last], greenfield);
        }
        for (Tuple<Integer, Integer> t : yellowspots) {
            canvas.drawRect(feld[t.first][t.last], yellowfield);
        }
    }

    public void addgreen(Tuple<Integer, Integer>... tuple) {
        for (Tuple<Integer, Integer> t : tuple) {
            greenspots.add(t);
        }
        invalidate();
    }

    public void removegreen() {
        greenspots.clear();
        invalidate();
    }

    public void addyellow(Tuple<Integer, Integer>... tuple) {
        for (Tuple<Integer, Integer> t : tuple) {
            yellowspots.add(t);
        }
        invalidate();
    }

    public void removeyellow() {
        yellowspots.clear();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (width < height) {
            thickness = width / 8;
        } else {
            thickness = height / 8;
        }
        upperline = (height - (8 * thickness)) / 2;
        leftline = (width - (8 * thickness)) / 2;
        for (int i = 0; i < ANZAHL_FELDER_VERTICAL; i++) {
            for (int j = 0; j < ANZAHL_FELDER_HORIZONTAL; j++) {
                feld[i][j] = new Rect();
                feld[i][j].left = leftline + (i * thickness);
                feld[i][j].top = upperline + (j * thickness);
                feld[i][j].right = leftline + ((i + 1) * thickness);
                feld[i][j].bottom = upperline + ((j + 1) * thickness);

            }
        }
    }

    public Tuple<Integer, Integer> getfieldfromtouch(int x, int y) {
        for (int i = 0; i < ANZAHL_FELDER_VERTICAL; i++) {
            for (int j = 0; j < ANZAHL_FELDER_HORIZONTAL; j++) {
                if (feld[i][j].contains(x, y)) {
                    return new Tuple<>(i, j);
                }
            }
        }
        return null;
    }

    public PrintAttributes.Margins getMarginfromfield(Tuple<Integer, Integer> tuple) {
        int left = feld[tuple.first][tuple.last].left;
        int top = feld[tuple.first][tuple.last].top;
        int right = feld[tuple.first][tuple.last].right;
        int bottom = feld[tuple.first][tuple.last].bottom;
        return new PrintAttributes.Margins(left, top, right, bottom);
    }
}