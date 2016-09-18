package imperiumnet.imperious.volumecontrol.fragments;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import imperiumnet.imperious.volumecontrol.R;

/**
 * Created by blaze on 9/17/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
        setRetainInstance(true);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preference_frag, container, false);
        EditTextPreference defaultPortPref = (EditTextPreference) getPreferenceManager().findPreference("default_port");
        return view;
    }

}
