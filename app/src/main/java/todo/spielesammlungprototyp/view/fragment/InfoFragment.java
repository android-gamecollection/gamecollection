package todo.spielesammlungprototyp.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;

import todo.spielesammlungprototyp.R;

public class InfoFragment extends PreferenceFragment {

    private final String carballoPref = "info_libraries_carballo";
    private final String jdroidlibPref = "info_libraries_jdroidlib";

    public static AlertDialog createAlertDialog(Context context, int titleID, int messageID) {
        return new AlertDialog.Builder(context)
                .setTitle(titleID)
                .setMessage(messageID)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, null)
                .create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.info);
        onClickDialog();
    }

    private void onClickDialog() {
        Preference carballoPreference = findPreference(carballoPref);
        Preference jDroidLibPreference = findPreference(jdroidlibPref);

        carballoPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(getActivity(), R.string.library_carballo_title, R.string.library_carballo_text).show();
                return true;
            }
        });

        jDroidLibPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference pref) {
                createAlertDialog(getActivity(), R.string.library_jdroidlib_title, R.string.library_jdroidlib_text).show();
                return true;
            }
        });
    }

}
