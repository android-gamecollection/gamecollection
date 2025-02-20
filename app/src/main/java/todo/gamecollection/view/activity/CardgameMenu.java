package todo.gamecollection.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import todo.gamecollection.R;

public class CardgameMenu extends GameActivity {

    private static final String GAME_PACKAGE = ".model.games.";

    @Override
    protected void onLoadGame(@Nullable Bundle savegame) {

    }

    @Override
    protected void onSaveGame(Bundle savegame) {

    }

    @Override
    protected int onLayoutRequest() {
        return R.layout.activity_cardgame_menu;
    }

    public void gotoGame(View view) {
        Context context = getApplicationContext();
        String classname = context.getPackageName() + GAME_PACKAGE + game.getTag();
        Intent intent = new Intent();
        intent.setClassName(context, classname);
        startActivity(intent);
    }
}
