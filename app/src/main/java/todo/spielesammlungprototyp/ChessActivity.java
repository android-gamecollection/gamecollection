package todo.spielesammlungprototyp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alonsoruibal.chess.Board;
import todo.spielesammlungprototyp.Chess.ChessGrid;
import todo.spielesammlungprototyp.Chess.ucChessPiece;

public class ChessActivity extends Activity {

    GridView grid;

    int[] imageId = new int[64];
    String[] chessFigure = new String[64];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_grid);

        setGridColors();
        setFromFen(Board.FEN_START_POSITION);

        ChessGrid adapter = new ChessGrid(ChessActivity.this, imageId, chessFigure);
        grid = (GridView)findViewById(R.id.gridview_Chess);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChessActivity.this, "You Clicked at " + imageId[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setFromFen(String fen) {
        fen = fen.split("\\s+")[0];
        fen = replaceNumbers(fen);
        fen = fen.replace("/", "");

        for (int i = 0; i < 64; i++)
            chessFigure[i] = ucChessPiece.pieces.get(fen.charAt(i));
    }

    private void setGridColors() {
        for (int i = 0; i < 64; i++) {
            boolean testable = (i + i/8 % 2) % 2 == 0;
            imageId[i] = testable ? R.drawable.chess_white_plate : R.drawable.chess_black_plate;
        }
    }

    private String replaceNumbers(String str) {
        Pattern pattern = Pattern.compile("(\\d)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer s = new StringBuffer();
        while (matcher.find())
            matcher.appendReplacement(s, genSpace(Integer.parseInt(matcher.group(1))));
        matcher.appendTail(s);
        return s.toString();
    }

    private String genSpace(int length) {
        return new String(new char[length]).replace("\0", "_");
    }
}