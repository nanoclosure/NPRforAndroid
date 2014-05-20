package edu.nanoracket.nprforandroid.ui.fragment;

import android.os.Bundle;
import edu.nanoracket.nprforandroid.R;

public class PrefsFragment extends PreferenceCompatFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}