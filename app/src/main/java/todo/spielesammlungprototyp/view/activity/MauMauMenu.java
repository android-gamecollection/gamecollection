package todo.spielesammlungprototyp.view.activity;

/**
 * Created by phil2 on 23.04.2017.
 */

import android.content.Intent;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.maumau.MauMau;

public class MauMauMenu extends GameActivity {

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_maumaumenu;
    }

    public void gotoMauMauGame(View view) {
        Intent gotoActivity = new Intent(this, MauMau.class);
        startActivity(gotoActivity);
    }
}