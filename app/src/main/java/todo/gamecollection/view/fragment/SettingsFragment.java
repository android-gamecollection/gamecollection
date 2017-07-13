package todo.gamecollection.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.Serializable;

import todo.gamecollection.R;
import todo.gamecollection.model.util.AndroidResources;

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
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.settings_restore_preferences_title)
                .setMessage(R.string.settings_restore_preferences_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        restorePreferences();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create().show();
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