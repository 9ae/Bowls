package me.valour.bowls;

import me.valour.bowls.TutorialFragment.TutorialCloseAgent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements TutorialCloseAgent {
	
	private SharedPreferences sp;
	FragmentManager fm;
	PreludeFragment prelude;
	TutorialFragment tutorial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fm = getFragmentManager();
		prelude = null;
		tutorial = null;
		
		sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		boolean showTutorial = sp.getBoolean("tutorial", true);
		Log.d("vars", "show tuts = "+showTutorial);
		if(showTutorial){
			launchTutorial();
		} else {
			launchPrelude();
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(prelude!=null){
			prelude.enable(true);
		}
	}
	
	private void launchTutorial(){
		FragmentTransaction ft = fm.beginTransaction();
		tutorial = new TutorialFragment();
		ft.add(R.id.fragment_main, tutorial);
		ft.commit();
	}
	
	private void completeTutorial(){
		if(tutorial==null) return;
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(tutorial);
		prelude = new PreludeFragment();
		ft.add(R.id.fragment_main, prelude);
		ft.commit();
			
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("tutorial", false);
		editor.commit();
	}
	
	private void launchPrelude(){
		FragmentTransaction ft = fm.beginTransaction();
		prelude = new PreludeFragment();
		ft.add(R.id.fragment_main, prelude);
		ft.commit();
	}
	
	public void launchTableActivity(boolean splitEqually){
		Intent intent = new Intent(this, TableActivity.class);
		intent.putExtra("splitEqually", splitEqually);
		startActivity(intent);
	}
	
	public void onSplitEqually(View view){
		view.setEnabled(false);
		launchTableActivity(true);
	}
	
	public void onSplitByItem(View view){
		view.setEnabled(false);
		launchTableActivity(false);
	}
	
	public void onOpenPresets(View view){
		Intent intent = new Intent(this, PresetActivity.class);
		startActivity(intent);
	}

	@Override
	public void closeTutorialFragment() {
		completeTutorial();
	}

}
