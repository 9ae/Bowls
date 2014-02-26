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
		
		bowlsCount = Kitchen.minBowls;
		
		setContentView(R.layout.activity_table);
		
		fm = getFragmentManager();
		tableFragment = (TableFragment) fm.findFragmentById(R.id.tableFragment);
		numFragment = (NumberPadFragment) fm.findFragmentById(R.id.numpadFragment);
		
		if(splitEqually){
			initSplitEqually();
		} else {
			initSplitLineItems();
		}
		tableFragment.tvQuestion.bringToFront();
	}
	
	private void initSplitEqually(){
		tableFragment.tvQuestion.setText(R.string.q_enter_subtotal);
		Log.d("vars", getString(R.string.q_enter_subtotal));
	}
	
	private void initSplitLineItems(){
		tableFragment.tvQuestion.setText(R.string.q_enter_first_li);
		Log.d("vars", getString(R.string.q_enter_first_li));
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
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

}
