package todo.gamecollection.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.io.Serializable;

import todo.gamecollection.R;
import todo.gamecollection.model.util.TextUtils;

public class InfoFragment extends PreferenceFragment implements Serializable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.info);

        addOnClickListeners();
    }

    private void addOnClickListeners() {
        Preference carballoPreference = findPreference("info_libraries_carballo");
        carballoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(R.string.library_carballo_title, R.string.library_carballo_text);
                return true;
            }
        });

        Preference jDroidLibPreference = findPreference("info_libraries_jdroidlib");
        jDroidLibPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(R.string.library_jdroidlib_title, R.string.library_jdroidlib_text);
                return true;
            }
        });

        Preference oliver = findPreference("info_authors_oliver");
        oliver.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(R.string.info_authors_oliver_title, R.string.info_authors_oliver_text);
                return true;
            }
        });

        Preference philipp = findPreference("info_authors_philipp");
        philipp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(R.string.info_authors_philipp_title, R.string.info_authors_philipp_text);
                return true;
            }
        });

        Preference leonard = findPreference("info_authors_leonard");
        leonard.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(R.string.info_authors_leonard_title, R.string.info_authors_leonard_text);
                return true;
            }
        });

        Preference frank = findPreference("info_authors_frank");
        frank.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(R.string.info_authors_frank_title, R.string.info_authors_frank_text);
                return true;
            }
        });
    }

    private void createAlertDialog(int titleID, int messageID) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(titleID)
                .setMessage(messageID)
                .setPositiveButton(R.string.ok, null)
                .create();
        alertDialog.show();
        TextUtils.setClickableLinks(alertDialog);
    }
}
