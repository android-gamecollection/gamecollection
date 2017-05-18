package todo.spielesammlungprototyp.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import todo.spielesammlungprototyp.R;


public abstract class GameActivity extends AppCompatActivity {

    public final static String KEY_LAYOUT = "layout";
    public final static String KEY_RULES = "rules";
    public final static String KEY_TITLE = "title";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        setContentView(extras.getInt(KEY_LAYOUT));
        setTitle(extras.getString(KEY_TITLE));
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_items, menu);
        return true;
    }

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
