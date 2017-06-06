package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.bauernkrieg.Bauernkrieg;

public class BauernkriegMenu extends GameActivity {

    @Override
    protected void onLoadGame() {

    }

    @Override
    protected void onSaveGame() {

    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_bauernkriegmenu;
    }

    public void gotoBauernkrieggame(View view) {
        Intent gotoActivity = new Intent(this, Bauernkrieg.class);
        startActivity(gotoActivity);
    }
}
