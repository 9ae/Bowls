package me.valour.bowls;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PresetFragment extends PreferenceFragment {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

}
