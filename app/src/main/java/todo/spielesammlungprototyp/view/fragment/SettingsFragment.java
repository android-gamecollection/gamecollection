package todo.spielesammlungprototyp.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.Serializable;

import todo.spielesammlungprototyp.R;
import todo.spielesammlungprototyp.model.util.AndroidResources;

public class SettingsFragment extends PreferenceFragment implements Serializable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reloadPreferences();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings_app_bar_items, menu);
        AndroidResources.colorMenuItems(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_restore:
                restorePreferences();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restorePreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        reloadPreferences();
    }

    private void reloadPreferences() {
        setPreferenceScreen(null);
        addPreferencesFromResource(R.xml.settings);
    }
}