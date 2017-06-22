package todo.spielesammlungprototyp.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.Games;
import todo.spielesammlungprototyp.model.util.Savegame;
import todo.spielesammlungprototyp.model.util.SavegameStorage;
import todo.spielesammlungprototyp.view.Game;

import static todo.spielesammlungprototyp.model.util.SavegameStorage.getInstance;


public abstract class GameActivity extends AppCompatActivity {

    public static final String KEY_GAME_UUID = "gameUuid";
    public static final String KEY_SAVEGAME_UUID = "UUID";
    private Game game;
    private SavegameStorage savegameStorage;
    private String savegameUuid;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savegameStorage = getInstance();
        Bundle extras = getIntent().getExtras();

        savegameUuid = extras.getString(KEY_SAVEGAME_UUID);
        Savegame savegame = savegameStorage.getFromUuid(savegameUuid);
        String gameUuid;
        if (savegame != null) {
            gameUuid = savegame.gameUuid;
            onLoadGame(savegame.bundle);
        } else {
            gameUuid = extras.getString(KEY_GAME_UUID);
            onLoadGame(null);
        }
        game = Games.getFromUuid(gameUuid);
        setContentView(onLayoutRequest());
        setTitle(game.getGameTitle());
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private boolean saveGame() {
        Bundle bundle = new Bundle();
        onSaveGame(bundle);
        if (!bundle.isEmpty()) {
            Savegame savegame;
            if (savegameUuid == null) {
                savegame = new Savegame(game.getUuid(), bundle);
            } else {
                savegame = savegameStorage.getFromUuid(savegameUuid);
                savegame.update(bundle);
            }
            savegameStorage.modifySavegame(savegame, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_items, menu);

        TypedArray typedArray = obtainStyledAttributes(R.style.AppTheme_AppBarOverlay, new int[]{android.R.attr.textColorPrimary});
        int themedColor = typedArray.getColor(0, Color.RED);

        // Change action bar icon color based on text color of current action bar theme
        for (int i = 0; i < menu.size(); i++) {
            Drawable icon = menu.getItem(i).getIcon();
            if (icon != null) {
                icon.mutate();
                icon.setColorFilter(themedColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        typedArray.recycle();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveGame();
    }

    protected abstract void onLoadGame(Bundle savegame);

    protected abstract void onSaveGame(Bundle savegame);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_info:
                showRulesDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Gets called when setting the content view of the activity
     * The returned layout has to include the layout '@layout/fragment_app_bar'
     *
     * @return The reference to the layout
     */
    protected abstract int onLayoutRequest();

    private void showRulesDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.action_gamerules));
        alertDialog.setMessage(game.getGameRules());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
