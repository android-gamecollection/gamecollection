package todo.spielesammlungprototyp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.view.fragment.InfoFragment;
import todo.spielesammlungprototyp.view.fragment.SettingsFragment;

public class SettingsAndInfo extends AppCompatActivity {

    public int xmlToLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Intent intent = getIntent();
        xmlToLoad = intent.getIntExtra("xmlToLoad", R.xml.settings);
        PreferenceFragment fragmentToLoad;
        if (xmlToLoad == R.xml.settings) {
            fragmentToLoad = new SettingsFragment();
        } else {
            fragmentToLoad = new InfoFragment();
        }
        getFragmentManager().beginTransaction().replace(R.id.settings_frame_layout, fragmentToLoad).commit();
        setupActionBar();
    }

    private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (xmlToLoad != R.xml.settings) {
                actionBar.setTitle(R.string.nav_item_info);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
    }

}
