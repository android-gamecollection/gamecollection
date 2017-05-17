package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.bauernkrieg.Bauernkrieg;

/**
 * Created by phil2 on 19.04.2017.
 */

public class BauernkriegMenu extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_bauernkriegmenu);
        super.onCreate(savedInstanceState);
    }

    public void gotoBauernkrieggame(View view) {
        Intent gotoActivity = new Intent(this, Bauernkrieg.class);
        startActivity(gotoActivity);
    }
}
