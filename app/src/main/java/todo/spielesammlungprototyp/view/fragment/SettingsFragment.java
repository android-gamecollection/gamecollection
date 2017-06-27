package todo.spielesammlungprototyp.view.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import java.io.Serializable;

import todo.spielesammlungprototyp.R;

public class SettingsFragment extends PreferenceFragment implements Serializable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}