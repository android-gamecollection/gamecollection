package todo.gamecollection.view.fragment;

import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import todo.gamecollection.App;
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
        Preference carballoPreference = findPreference(getString(R.string.library_carballo_key));
        Preference jDroidLibPreference = findPreference(getString(R.string.library_jdroidlib_key));
        Preference colorPickerPreference = findPreference(getString(R.string.library_colorpicker_key));
        Preference oliver = findPreference(getString(R.string.info_authors_oliver_key));
        Preference philipp = findPreference(getString(R.string.info_authors_philipp_key));
        Preference leonard = findPreference(getString(R.string.info_authors_leonard_key));
        Preference frank = findPreference(getString(R.string.info_authors_frank_key));

        carballoPreference.setOnPreferenceClickListener(new DialogClickListener(R.string.library_carballo_title, R.string.library_carballo_text));
        jDroidLibPreference.setOnPreferenceClickListener(new DialogClickListener(R.string.library_jdroidlib_title, R.string.library_jdroidlib_text));
        colorPickerPreference.setOnPreferenceClickListener(new DialogClickListener(R.string.library_colorpicker_title, R.string.library_colorpicker_text));
        oliver.setOnPreferenceClickListener(new MediaClickListener());
        philipp.setOnPreferenceClickListener(new MediaClickListener());
        leonard.setOnPreferenceClickListener(new MediaClickListener());
        frank.setOnPreferenceClickListener(new MediaClickListener());
    }

    private static class MediaClickListener implements Preference.OnPreferenceClickListener {

        private static WeakReference<MediaPlayer> mp;
        private static boolean toggle = true;

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (toggle) {
                mp = new WeakReference<>(MediaPlayer.create(App.getContext(), R.raw.rick));
                mp.get().start();
            } else {
                mp.get().pause();
                mp.get().seekTo(0);
            }
            toggle ^= true;
            return true;
        }
    }

    private class DialogClickListener implements Preference.OnPreferenceClickListener {

        private int titleID, messageID;

        DialogClickListener(int titleID, int messageID) {
            this.titleID = titleID;
            this.messageID = messageID;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            createAlertDialog();
            return true;
        }

        private void createAlertDialog() {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(titleID)
                    .setMessage(messageID)
                    .setPositiveButton(R.string.ok, null)
                    .create();
            alertDialog.show();
            TextUtils.setClickableLinks(alertDialog);
        }
    }
}
