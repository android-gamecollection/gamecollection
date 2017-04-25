package todo.spielesammlungprototyp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alonsoruibal.chess.Board;
import todo.spielesammlungprototyp.Chess.ChessGrid;
import todo.spielesammlungprototyp.Chess.ucChessPiece;

public class ChessActivity extends Activity {

    GridView gridView;
    ArrayAdapter<String> adapter;

    // Changing this array will modify the GUI
    // call 'adapter.notifyDataSetChanged();' after
    String[] chessFigure = new String[64];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_grid);

        setFromFen(Board.FEN_START_POSITION);
        adapter = new ChessGrid(this, chessFigure);
        gridView = (GridView) findViewById(R.id.gridview_Chess);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You Clicked at " + position, Toast.LENGTH_SHORT).show();
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