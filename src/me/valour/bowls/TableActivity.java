package me.valour.bowls;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class TableActivity extends Activity 
	implements TableFragment.AddBowlListener, 
	TableFragment.SubBowlListener {

	private int bowlsCount;
	private FragmentManager fm;
	
	private TableFragment tableFragment;
	private NumberPadFragment numFragment;
	public boolean splitEqually;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		splitEqually = intent.getBooleanExtra("splitEqually", true);
		
		setContentView(R.layout.activity_table);
		bowlsCount = Kitchen.minBowls;
		
		fm = getFragmentManager();
		tableFragment = (TableFragment) fm.findFragmentById(R.id.tableFragment);
		numFragment = (NumberPadFragment) fm.findFragmentById(R.id.numpadFragment);
		
		if(splitEqually){
			Log.i("vars", "eq");
		} else {
			Log.i("vars", "li");
		}
	}

	@Override
	public void OnAddBowlListener() {
		if(bowlsCount==Kitchen.maxBowls){
			
		} else {
			bowlsCount++;
			tableFragment.tableView.addBowl();
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

	@Override
	public void OnSubBowlListener() {
		if(bowlsCount==Kitchen.minBowls){
			
		} else {
			bowlsCount--;
			tableFragment.tableView.subBowl();
		//	tableFragment.tableView.setBowlsCount(bowlsCount);
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

}
