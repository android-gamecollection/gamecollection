package todo.spielesammlungprototyp.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.alonsoruibal.chess.Board;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.chess.ChessAdapter;
import todo.spielesammlungprototyp.model.games.chess.UnicodePieces;

public class Chess extends GameActivity {

    // Changing this array will modify the GUI
    // call 'adapter.notifyDataSetChanged();' after
    private final String[] chessFigures = new String[64];
    private GridView gridView;
    private ArrayAdapter<String> adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_chess);
        super.onCreate(savedInstanceState);

        setFromFen(Board.FEN_START_POSITION);
        adapter = new ChessAdapter(this, chessFigures);
        gridView = (GridView) findViewById(R.id.gridview_chess);
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
        fen = replaceNumbersBySpaces(fen);
        fen = fen.replace("/", "");

        for (int i = 0; i < 64; i++)
            chessFigures[i] = UnicodePieces.pieces.get(fen.charAt(i));
    }

    private String replaceNumbersBySpaces(String str) {
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