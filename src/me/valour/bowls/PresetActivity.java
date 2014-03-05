package me.valour.bowls;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class PresetActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                // Display the fragment as the main content.
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager
                                        .beginTransaction();
                PresetFragment mPrefsFragment = new PresetFragment();
                mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
                mFragmentTransaction.commit();
     
    }
}
