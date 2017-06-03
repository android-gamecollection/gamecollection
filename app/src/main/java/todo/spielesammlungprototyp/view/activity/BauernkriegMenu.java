package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.bauernkrieg.Bauernkrieg;

/**
 * Created by phil2 on 19.04.2017.
 */

public class BauernkriegMenu extends GameActivity {

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_bauernkriegmenu;
    }

    public void gotoBauernkrieggame(View view) {
        Intent gotoActivity = new Intent(this, Bauernkrieg.class);
        startActivity(gotoActivity);
    }
}
