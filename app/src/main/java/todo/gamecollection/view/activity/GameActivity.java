package todo.gamecollection.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import todo.gamecollection.R;
import todo.gamecollection.model.gamemanager.Game;
import todo.gamecollection.model.gamemanager.Games;
import todo.gamecollection.model.savegamestorage.Savegame;
import todo.gamecollection.model.savegamestorage.SavegameStorage;
import todo.gamecollection.model.util.AndroidResources;
import todo.gamecollection.model.util.TextUtils;

import static todo.gamecollection.model.savegamestorage.SavegameStorage.getInstance;


public abstract class GameActivity extends AppCompatActivity {

    public static final String KEY_GAME_UUID = "gameUuid";
    public static final String KEY_SAVEGAME_UUID = "UUID";
    protected Game game;
    private SavegameStorage savegameStorage;
    private String savegameUuid;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        savegameStorage = getInstance();

        onCreateExistingGame();

        setContentView(onLayoutRequest());
        setTitle(game.getGameTitle());
        setupActionBar();
    }

    private void onCreateExistingGame() { //TODO: "I don't like my name, please help me!" - sad Method 2017
        Bundle extras = getIntent().getExtras();
        savegameUuid = extras.getString(KEY_SAVEGAME_UUID);
        Savegame savegame = savegameStorage.getFromUuid(savegameUuid);
        String gameUuid;
        Bundle savegameBundle;
        if (savegame != null) {
            gameUuid = savegame.gameUuid;
            savegameBundle = savegame.bundle;
        } else {
            gameUuid = extras.getString(KEY_GAME_UUID);
            savegameBundle = null;
        }
        game = Games.getFromUuid(gameUuid);
        onLoadGame(savegameBundle);
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean saveGame() {
        Bundle bundle = new Bundle();
        onSaveGame(bundle);
        if (!bundle.isEmpty()) {
            Savegame savegame;
            if (savegameUuid == null) {
                savegame = new Savegame(game.getUuid(), bundle);
                savegameUuid = savegame.uuid;
            } else {
                savegame = savegameStorage.getFromUuid(savegameUuid);
                savegame.update(bundle);
            }
            savegameStorage.updateSavegame(savegame);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_items, menu);
        AndroidResources.colorMenuItems(menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveGame();
    }

    protected abstract void onLoadGame(@Nullable Bundle savegame);

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
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.action_gamerules))
                .setMessage(game.getGameRules())
                .setPositiveButton(R.string.ok, null)
                .create();
        alertDialog.show();
        TextUtils.setClickableLinks(alertDialog);
    }
}
