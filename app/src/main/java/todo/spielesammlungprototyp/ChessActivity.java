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
            R.drawable.chess_black_rook,
            R.drawable.chess_black_knight,
            R.drawable.chess_black_bishop,
            R.drawable.chess_black_queen,
            R.drawable.chess_black_king,
            R.drawable.chess_black_bishop,
            R.drawable.chess_black_knight,
            R.drawable.chess_black_rook,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_black_pawn,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_plate,
            R.drawable.chess_cross,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_pawn,
            R.drawable.chess_white_rook,
            R.drawable.chess_white_knight,
            R.drawable.chess_white_bishop,
            R.drawable.chess_white_queen,
            R.drawable.chess_white_king,
            R.drawable.chess_white_bishop,
            R.drawable.chess_white_knight,
            R.drawable.chess_white_rook,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_grid);

        ChessGrid adapter = new ChessGrid(ChessActivity.this, imageId);
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