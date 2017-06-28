package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.maumau.MauMau;

/**
 * Created by phil2 on 28.06.2017.
 */

public class schafkopfMenu extends GameActivity{

    @Override
    protected void onLoadGame(Bundle savegame) {

    }

    @Override
    protected void onSaveGame(Bundle savegame) {

    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_schafkopfmenu;
    }

    public void gotoschafkopfGame(View view) {
        Intent gotoActivity = new Intent(this, todo.spielesammlungprototyp.model.games.schafkopf.schafkopf.class);
        startActivity(gotoActivity);
    }
}
