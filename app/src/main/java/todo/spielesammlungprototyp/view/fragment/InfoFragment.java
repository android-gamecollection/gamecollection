package todo.spielesammlungprototyp.view.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import java.io.Serializable;

import todo.spielesammlungprototyp.R;

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
    }

    private void createAlertDialog(int titleID, int messageID) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(titleID)
                .setMessage(messageID)
                .setCancelable(true)
                .setPositiveButton(R.string.ok, null)
                .create();
        alertDialog.show();
        TextView message = (TextView) alertDialog.findViewById(android.R.id.message);
        assert message != null;
        // Clickable links in TextView
        message.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
