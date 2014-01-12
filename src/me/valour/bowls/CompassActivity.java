package me.valour.bowls;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.view.Menu;

public class CompassActivity extends Activity {

	private FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		fm = getFragmentManager();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compass, menu);
		return true;
	}

}
