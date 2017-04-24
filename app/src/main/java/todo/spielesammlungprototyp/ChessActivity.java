package todo.spielesammlungprototyp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import todo.spielesammlungprototyp.Chess.ChessGrid;
import todo.spielesammlungprototyp.Chess.ucChessPiece;

public class ChessActivity extends Activity {
    GridView grid;

    int[] imageId = new int[64];

    String[] chessFigure = {
            ucChessPiece.ROOK_B,
            ucChessPiece.KNIGHT_B,
            ucChessPiece.BISHOP_B,
            ucChessPiece.QUEEN_B,
            ucChessPiece.KING_B,
            ucChessPiece.BISHOP_B,
            ucChessPiece.KNIGHT_B,
            ucChessPiece.ROOK_B,

            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,
            ucChessPiece.PAWN_B,

            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,

            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,

            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,

            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,
            ucChessPiece.NONE,

            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,
            ucChessPiece.PAWN_W,

            ucChessPiece.ROOK_W,
            ucChessPiece.KNIGHT_W,
            ucChessPiece.BISHOP_W,
            ucChessPiece.QUEEN_W,
            ucChessPiece.KING_W,
            ucChessPiece.BISHOP_W,
            ucChessPiece.KNIGHT_W,
            ucChessPiece.ROOK_W,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_grid);

        setGridColors();
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

    private void setGridColors() {
        for (int i = 0; i < 64; i++) {
            boolean testable = (i + i/8 % 2) % 2 == 0;
            imageId[i] = testable ? R.drawable.chess_white_plate : R.drawable.chess_black_plate;
        }
    }
}