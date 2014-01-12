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
	
	private final int minBowls=2;
	private final int maxBowls=24;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		bowlsCount = minBowls;
		
		//TODO: How do get values through attr file? Can we?
	//	minBowls = R.attr.minBowls;
	//	maxBowls = R.attr.maxBowls;
		
		Log.d("vars",String.format("min bowls=%d",minBowls));
		Log.d("vars",String.format("max bowls=%d",maxBowls));
		
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
		if(bowlsCount==maxBowls){
			
		} else {
			bowlsCount++;
			tableFragment.tableView.setBowlsCount(bowlsCount);
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

	@Override
	public void OnSubBowlListener() {
		if(bowlsCount==minBowls){
			
		} else {
			bowlsCount--;
			tableFragment.tableView.setBowlsCount(bowlsCount);
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

}
