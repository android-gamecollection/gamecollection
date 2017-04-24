package todo.spielesammlungprototyp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import todo.spielesammlungprototyp.Chess.ChessGrid;

public class ChessActivity extends Activity {
    GridView grid;

    int[] imageId = {
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,

            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,

            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,

            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,

            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,

            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,

            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,

            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
            R.drawable.chess_white_plate,
            R.drawable.chess_black_plate,
    };

    String[] chessFigure = {
            "\u265C",
            "\u265E",
            "\u265D",
            "\u265A",
            "\u265B",
            "\u265D",
            "\u265E",
            "\u265C",

            "\u265F",
            "\u265F",
            "\u265F",
            "\u265F",
            "\u265F",
            "\u265F",
            "\u265F",
            "\u265F",

            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",

            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",

            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",

            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",

            "\u2659",
            "\u2659",
            "\u2659",
            "\u2659",
            "\u2659",
            "\u2659",
            "\u2659",
            "\u2659",

            "\u2656",
            "\u2658",
            "\u2657",
            "\u2654",
            "\u2655",
            "\u2657",
            "\u2658",
            "\u2656",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_grid);

        ChessGrid adapter = new ChessGrid(ChessActivity.this, imageId, chessFigure);
        grid=(GridView)findViewById(R.id.gridview_Chess);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChessActivity.this, "You Clicked at "+imageId[position], Toast.LENGTH_SHORT).show();

            }
        });

    }

}