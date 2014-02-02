package me.valour.bowls;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

public class TableActivity extends Activity implements TableFragment.AddBowlListener, TableFragment.SubBowlListener {

	private int bowlsCount;
	private FragmentManager fm;
	
	private TableFragment tableFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		bowlsCount = Kitchen.minBowls;
		
		fm = getFragmentManager();
		tableFragment = (TableFragment) fm.findFragmentById(R.id.tableFragment);

	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.table, menu);
		return true;
	} */

	@Override
	public void OnAddBowlListener() {
		if(bowlsCount==Kitchen.maxBowls){
			
		} else {
			bowlsCount++;
			tableFragment.tableView.addBowl(bowlsCount);
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

	@Override
	public void OnSubBowlListener() {
		if(bowlsCount==Kitchen.minBowls){
			
		} else {
			bowlsCount--;
		//	tableFragment.tableView.setBowlsCount(bowlsCount);
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

}
