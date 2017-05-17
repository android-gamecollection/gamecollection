package todo.spielesammlungprototyp.view.activity;

import android.os.Bundle;

import todo.spielesammlungprototyp.R;

public class Bauernkrieg extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_bauernkrieg);
        super.onCreate(savedInstanceState);
    }
}
