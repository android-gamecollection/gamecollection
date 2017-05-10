package todo.spielesammlungprototyp.games.chess;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import todo.spielesammlungprototyp.R;

public class ChessGrid extends ArrayAdapter<String> {

    public ChessGrid(Context context, String[] objects) {
        super(context, R.layout.chess_grid_single, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String chessPiece = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chess_grid_single, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.chess_figure);
        textView.setText(chessPiece);
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), getColorFromInd(position)));

        return convertView;
    }

    private int getColorFromInd(int i) {
        boolean testable = (i + i / 8 % 2) % 2 == 0;
        return testable ? R.color.chessWhite : R.color.chessBlack;
    }
}