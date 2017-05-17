package todo.spielesammlungprototyp.view.activity;

/**
 * Created by phil2 on 23.04.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.maumau.MauMau;

public class MauMauMenu extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getIntent().putExtra(GameActivity.KEY_LAYOUT, R.layout.activity_maumaumenu);
        super.onCreate(savedInstanceState);
    }

    public void gotoMauMauGame(View view) {
        Intent gotoActivity = new Intent(this, MauMau.class);
        startActivity(gotoActivity);
    }
}