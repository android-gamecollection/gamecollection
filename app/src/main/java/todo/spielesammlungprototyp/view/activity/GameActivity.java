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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.Savegame;
import todo.spielesammlungprototyp.model.util.SavegameStorage;

import static todo.spielesammlungprototyp.model.util.SavegameStorage.getInstance;


public abstract class GameActivity extends AppCompatActivity {

    public final static String KEY_RULES = "rules";
    public final static String KEY_TITLE = "title";
    public Savegame currentSaveGame;
    public String uuid;
    public boolean isSaved;
    private SavegameStorage savegameStorage;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setContentView(onLayoutRequest());
        setTitle(extras.getString(KEY_TITLE));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        //Load Savegame if "Intent" has Extra
        savegameStorage = getInstance(this);
        savegameLoadChecker(savedInstanceState);
        isSaved = false;

        onLoadGame();
    }

    private void savegameLoadChecker(Bundle savedInstanceState) {
        if (uuidInspector(savedInstanceState)) {
            ArrayList<Savegame> listOfGames = savegameStorage.getSavegameList();
            if (!listOfGames.isEmpty()) {
                currentSaveGame = savegameStorage.findByUUID(uuid);
            }
        }
    }

    private boolean uuidInspector(Bundle savedInstanceState) {
        boolean erg;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                erg = false;
            } else {
                uuid = extras.getString("UUID");
                erg = uuid != null;
            }
        } else {
            uuid = savedInstanceState.getString("UUID");
            erg = uuid != null;

        }
        return erg;
    }

    public void saveGame(String value) {
        if(!TextUtils.isEmpty(value)){
            if (!isSaved) {
                Class<? extends GameActivity> clazz = this.getClass();
                if (currentSaveGame == null) {
                    currentSaveGame = new Savegame(value, clazz);
                    savegameStorage.addSavegame(currentSaveGame);
                } else {
                    if (!currentSaveGame.value.equals(value)) {
                        currentSaveGame.value = value;
                        savegameStorage.updateSavegame(currentSaveGame);
                    }
                }
            }
            isSaved = true;
        }
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
        saveGame(onSaveGame());
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        saveGame(onSaveGame());
        if(isSaved) {
            savedInstanceState.putString("UUID", currentSaveGame.uuid);
        }
    }

    protected abstract void onLoadGame();

    protected abstract String onSaveGame();

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
                break;
            case R.id.action_info:
                showRulesDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        alertDialog.setMessage(getIntent().getExtras().getString(KEY_RULES, "empty"));
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
