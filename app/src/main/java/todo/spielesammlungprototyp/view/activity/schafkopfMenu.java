package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.games.schafkopf.schafkopf;

public class schafkopfMenu extends GameActivity {

    @Override
    protected void onLoadGame(Bundle savegame) {

    }

    @Override
    protected void onSaveGame(Bundle savegame) {

    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_cardgame_menu;
    }

    public void gotoGame(View view) {
        Intent gotoActivity = new Intent(this, schafkopf.class);
        startActivity(gotoActivity);
    }
}
