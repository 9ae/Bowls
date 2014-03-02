package me.valour.bowls;

import java.util.List;

import me.valour.bowls.enums.OkMode;

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
	TableFragment.SubBowlListener,
	TableFragment.OkListener{

	private int bowlsCount;
	private Bill bill;
	private FragmentManager fm;
	
	private TableFragment tableFragment;
	private NumberPadFragment numFragment;
	public boolean splitEqually;
	private OkMode okMode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		splitEqually = intent.getBooleanExtra("splitEqually", true);
		
		bowlsCount = Kitchen.minBowls;
		okMode = OkMode.ITEM_PRICE;
		
		setContentView(R.layout.activity_table);
		
		fm = getFragmentManager();
		tableFragment = (TableFragment) fm.findFragmentById(R.id.tableFragment);
		numFragment = (NumberPadFragment) fm.findFragmentById(R.id.numpadFragment);
		
		bill = new Bill(splitEqually);
		if(splitEqually){
			initSplitEqually();
		} else {
			initSplitLineItems();
		}
		tableFragment.tvQuestion.bringToFront();
		//bill.addUsers(tableFragment.tableView.getBowlViewIds());
		bill.addUniqueUsers(tableFragment.tableView.getBowlUsers());
	}
	
	private void initSplitEqually(){
		tableFragment.tvQuestion.setText(R.string.q_enter_subtotal);
	//	Log.d("vars", getString(R.string.q_enter_subtotal));
	}
	
	private void initSplitLineItems(){
		tableFragment.tvQuestion.setText(R.string.q_enter_first_li);
	//	Log.d("vars", getString(R.string.q_enter_first_li));
	}

	@Override
	public void OnAddBowlListener() {
		if(bowlsCount==Kitchen.maxBowls){
			
		} else {
			bowlsCount++;
			BowlView bowl = tableFragment.tableView.addBowl();
			bill.addUser(bowl.user);
			if(splitEqually){
				bill.redivideEqually();
				tableFragment.refresh();
			}
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
	}

	@Override
	public void OnSubBowlListener() {
	/*	deprecate
	 * if(bowlsCount==Kitchen.minBowls){
			
		} else {
			bowlsCount--;
			tableFragment.tableView.subBowl();
		}
		Log.d("vars",String.format("bowls=%d",bowlsCount));
		*/
	}

	@Override
	public void OnOkButtonPress() {
		    	switch(okMode){
				case ITEM_PRICE:
					registerItemPrice();
					break;
				default:
					break;
			}
		    if(splitEqually && okMode==OkMode.ITEM_PRICE){	
		    	okMode = OkMode.NONE; 
		    	tableFragment.btnOk.setVisibility(View.INVISIBLE);
		    	tableFragment.tvQuestion.setText("");
		    	tableFragment.tvQuestion.setVisibility(View.INVISIBLE);
		    }
	}
	
	private void registerItemPrice(){
		double price = numFragment.getNumberValue();
		LineItem li = bill.addLineItem(price);
		if(splitEqually){
			bill.divideEqually(li);
		} else {
			//TODO: change tvQ to ask to select people?
		}
		tableFragment.tableView.refreshBowls();
	}

}
