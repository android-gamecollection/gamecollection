package todo.spielesammlungprototyp.model.games.chess;

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

public class ChessAdapter extends ArrayAdapter<String> {

    public ChessAdapter(Context context, String[] objects) {
        super(context, R.layout.game_chess_grid_cell, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String chessPiece = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_chess_grid_cell, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.text_chesspiece);
        textView.setText(chessPiece);
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), colorFromIdx(position)));

        return convertView;
    }

    private int colorFromIdx(int i) {
        boolean testable = (i + i / 8 % 2) % 2 == 0;
        return testable ? R.color.chessWhite : R.color.chessBlack;
    }
}