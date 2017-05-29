package todo.spielesammlungprototyp.view.activity;

import android.os.Bundle;

import com.alonsoruibal.chess.Board;

import todo.spielesammlungprototyp.R;

public class Chess extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_chess);
        super.onCreate(savedInstanceState);

        setFromFen(Board.FEN_START_POSITION);
    }

    public void setFromFen(String fen) {

    }
}