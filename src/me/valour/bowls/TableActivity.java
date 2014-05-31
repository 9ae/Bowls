package me.valour.bowls;

import java.util.List;

import me.valour.bowls.enums.Action;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

public class TableActivity extends Activity implements
		BowlsGroup.BowlsGroupAgent, 
		TableFragment.ButtonAgent,
		BillFragment.BillFragmentAgent, 
		NumberPadFragment.CloseNumpadListener {

	private int bowlsCount;
	private Bill bill;
	private FragmentManager fm;

	private TableFragment tableFragment;
	private NumberPadFragment numFragment;
	private BillFragment billFragment;
	
	public boolean splitEqually;
	public boolean leftHanded;
	private boolean isEdit;
	private Action action;
	
	private SharedPreferences sp;
	
	private LineItem selectedLineItem = null;
	
	private double taxEstimate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		splitEqually = intent.getBooleanExtra("splitEqually", true);
		
		sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		bowlsCount = Kitchen.minBowls;
		action = Action.ITEM_PRICE;
		bill = new Bill(getTax(), getTip(), splitEqually);
		
		leftHanded = !sp.getBoolean("left_right", true);

		if(leftHanded){
			setContentView(R.layout.left_activity_table);
		} else {
			setContentView(R.layout.activity_table);
		}

		fm = getFragmentManager();
		tableFragment = (TableFragment) fm.findFragmentById(R.id.tableFragment);
		if(leftHanded){
			tableFragment.alignForLeftHanded();
		}
		numFragment = new NumberPadFragment();
		
		bill.usersAddBatch(tableFragment.bowlsGroup.getBowlUsers());
		
		billFragment = (BillFragment) fm.findFragmentById(R.id.billFragment);
		billFragment.clearSummary();
		
		if (splitEqually) {
			initSplitEqually();
		} else {
			initSplitLineItems();
		} 
		isEdit = false;
	}
	
	@Override
    public void onBackPressed()
    {
		if(!numFragment.isVisible()){
			super.onBackPressed();
		}
    }

	public void applyTax() {
		bill.calculateTax();
		bill.applyTax();
		updateBowlsPrice();
	}

	public double getTax() {
		if (sp == null) {
			return 0.0;
		} else {

			String stringTax = sp.getString("default_tax", getString(R.string.tax_percent));
			return Double.parseDouble(stringTax) / 100.0;
		}
	}

	public void setTax(String tax, boolean save) {
		double amount;
		try{
			amount = Double.parseDouble(tax);
		} catch(NumberFormatException e){
			amount = 0.0;
		}

			bill.setTaxAmount(amount);
			if (sp != null && save) {
				sp.edit().putString("default_tax", Double.toString(bill.getTax()*100)).commit();
			}
		 if(amount==0.0){
			onTaxButtonPress(tableFragment.getTaxButton());
		 } else {
			 applyTax();
		 }
		
	}

	public void applyTip() {
		bill.applyTip();
		updateBowlsPrice();
	}

	public double getTip() {
		if (sp == null) {
			return 0.0;
		} else {
			String stringTip = sp.getString("default_tip", getString(R.string.tip_percent));
			return Double.parseDouble(stringTip) / 100.0;
		}
	}

	public void setTip(String tip, boolean save) {
		double tipWhole;
		try{
			tipWhole = Double.parseDouble(tip);
		} catch(NumberFormatException e) {
			tipWhole = 0.0;
		}

			double percent = tipWhole / 100.0;
			bill.setTip(percent);
	
			if (sp != null && save) {
				sp.edit().putString("default_tip", tip).commit();
			}
		if(percent==0.0) {
			onTipButtonPress(tableFragment.getTipButton());
		} else {
			applyTip();
		}
	}

	private void initSplitEqually() {
		tableFragment.setQuestionText(null);
		
		billFragment.adjustForSplitEqually();
		action = Action.ENTER_SUBTOTAL;
		onNewLineItem();
	}

	private void initSplitLineItems() {
		tableFragment.setQuestionText(null);
	}

	private void clearCenter() {
		action = Action.NONE;
		tableFragment.setQuestionText(null);
		tableFragment.showOkButton(false);
	}

	private void completePercentChange() {
		numFragment.clearField();
		numFragment.setAsDollarMode();
		clearCenter();
	}

	private void registerItemPrice() {
		double price = numFragment.getNumberValue();
		LineItem li = bill.itemAdd(price);
		if (splitEqually) {
			bill.divideEqually();
			clearCenter();
		} else {
			prepareForSelectingBowls(li);
			billFragment.updatedList();
		}
		updateBowlsPrice();
	}
	
	private void updateItemPrice(){
		double price = numFragment.getNumberValue();
		bill.itemUpdate(selectedLineItem, price);
		if(splitEqually){
			clearCenter();
		} else {
			billFragment.updatedList();
		}
		updateBowlsPrice();
	} 

	private void prepareForSelectingBowls(LineItem li) {
		tableFragment.setQuestionText(R.string.q_select_bowls);
		tableFragment.showOkButton(true);
		if(selectedLineItem!=li){
			selectedLineItem = li;
		}
		tableFragment.bowlsGroup.readyBowlSelect();
		action = Action.SELECT_BOWLS;
	}

	private void handleSelectedBowls() {
		if (selectedLineItem != null) {
			List<User> consumers = tableFragment.bowlsGroup.getSelectedUsers();
			if(consumers.isEmpty()){
				tableFragment.setQuestionText(R.string.q_min_select);
				return;
			}
			if(isEdit){
				bill.itemUpdate(selectedLineItem, consumers);
			} else {
				bill.divideAmongst(selectedLineItem, consumers);
			}
				tableFragment.bowlsGroup.stopBowlSelect();

				// move to being ready for next Item
				tableFragment.setQuestionText(null);
				tableFragment.showOkButton(false);
				action = Action.ITEM_PRICE;
				selectedLineItem = null;
				if(isEdit){
					billFragment.deselectLineItem();
				}
		}
		updateBowlsPrice();
		if(!billFragment.isActionsEnabled()){
			billFragment.enableActions(true);
		}
		tableFragment.enableTaxTip(true);
	}

	@Override
	public void OnOkButtonPress() {
		switch (action) {
		case ITEM_PRICE:
			registerItemPrice();
			break;
		case SELECT_BOWLS:
			handleSelectedBowls();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTipButtonPress(View v) {
		Button btn = (Button) v;

		if (!bill.tipApplied()) {
			applyTip();
			if(leftHanded){
				btn.setBackgroundResource(R.drawable.ic_tbtn_top_left_sub);
			} else {
				btn.setBackgroundResource(R.drawable.ic_tbtn_top_right_sub);
			}
	
		} else {
			bill.clearTip();
			updateBowlsPrice();
			if(leftHanded){
				btn.setBackgroundResource(R.drawable.ic_tbtn_top_left_add);
			} else {
				btn.setBackgroundResource(R.drawable.ic_tbtn_top_right_add);
			}
		}

	}

	@Override
	public void onTaxButtonPress(View v) {
		Button btn = (Button) v;

		if (!bill.taxApplied()) {
			taxEstimate = bill.calculateTax();
			applyTax();
			if(leftHanded){
				btn.setBackgroundResource(R.drawable.ic_tbtn_bot_left_sub);
			} else {
				btn.setBackgroundResource(R.drawable.ic_tbtn_bot_right_sub);
			}
		} else {
			bill.clearTax();
			updateBowlsPrice();
			if(leftHanded){
				btn.setBackgroundResource(R.drawable.ic_tbtn_bot_left_add);
			} else {
				btn.setBackgroundResource(R.drawable.ic_tbtn_bot_right_add);
			}
		}
		
	}

	@Override
	public void addUser(User user) {
		if (bowlsCount != Kitchen.maxBowls) {
			bowlsCount++;
			bill.userAdd(user);
			if (splitEqually) {
				bill.divideEqually();
				bill.reapplyTax();
				bill.reapplyTip();
				updateBowlsPrice();
			}
		}	
	}

	@Override
	public boolean removeUserConfirm(BowlView bv) {
		User user = bv.user;
		removeUserDo(user);
		return true;
	}

	@Override
	public void removeUserDo(User user) {
		bill.userRemove(user);
		bill.reapplyTax();
		bill.reapplyTip();
		updateBowlsPrice();
		bowlsCount--;
	}
	
	public Bill getBill(){
		return bill;
	}
	
	/*
	 * BillFragmentAgent methods BEGIN
	 */

	@Override
	public void onNewLineItem() {
		Bundle bundle = new Bundle();
		bundle.putBoolean("percentMode", false);
		if(splitEqually){
			bundle.putString("hint", getString(R.string.q_enter_subtotal));
		}
		numFragment.setArguments(bundle);
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.to_nw, R.animator.to_se);
		ft.replace(R.id.rightContainer, numFragment);
		ft.addToBackStack("bill");
		ft.commit();
		tableFragment.disableActions();
		isEdit = false;
	}
	

	@Override
	public void selectLineItem(int position) {
		isEdit = true;
		selectedLineItem = bill.lineItems.get(position);
		prepareForSelectingBowls(selectedLineItem);
		tableFragment.bowlsGroup.manualSelect(bill.usersOfItem(selectedLineItem));
	}
	
	@Override
	public void deselectLineItem(){
		tableFragment.bowlsGroup.stopBowlSelect();

		tableFragment.setQuestionText(null);
		tableFragment.showOkButton(false);
		action = Action.ITEM_PRICE;
		selectedLineItem = null;
	}
	
	@Override
	public void editLineItem() {
		if(selectedLineItem==null){
			return;
		}
		double price = selectedLineItem.getPrice();
		
		Bundle bundle = new Bundle();
		bundle.putDouble("numberValue", price);
		bundle.putBoolean("percentMode", false);
		numFragment.setArguments(bundle);
		
		if(billFragment.isActionsEnabled()){
			billFragment.enableActions(false);
		}
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.to_nw, R.animator.to_se);
		ft.replace(R.id.rightContainer, numFragment);
		ft.addToBackStack(null);
		ft.commit();
		tableFragment.disableActions();
		tableFragment.bowlsGroup.stopBowlSelect();
		tableFragment.bowlsGroup.clearCenter();
	}
	
	@Override
	public void updateBowlsPrice() {
		tableFragment.bowlsGroup.refreshBowls();
	}

	@Override
	public void editSubtotal() {
		action = Action.EDIT_SUBTOTAL;
		selectedLineItem = bill.lineItems.get(0); 
		editLineItem();
	}
	
	@Override
	public void editTax(){
		action = Action.SET_TAX;
		openNumberPadForAmountChange(taxEstimate);
	}
	
	@Override
	public void editTip(){
		action = Action.SET_TIP;
		openNumberPadForPercentChange(bill.getTip());
	}
	
	/*
	 * BillFragmentAgent methods END
	 */

	
	public void openNumberPadForPercentChange(double percent){
		Bundle bundle = new Bundle();
		bundle.putDouble("numberValue", percent*100);
		bundle.putBoolean("percentMode",true);
		
		if(action==Action.SET_TIP){
			bundle.putString("hint", getString(R.string.q_enter_tip_percent));
			bundle.putBoolean("allowZero", true);
		}
		
		numFragment.setArguments(bundle);
		
		if(billFragment.isActionsEnabled()){
			billFragment.enableActions(false);
		}
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.to_nw, R.animator.to_se);
		ft.replace(R.id.rightContainer, numFragment);
		ft.addToBackStack(null);
		ft.commit();
		tableFragment.disableActions();
	
	}
	
	public void openNumberPadForAmountChange(double amount){
		Bundle bundle = new Bundle();
		bundle.putDouble("numberValue", amount);
		bundle.putBoolean("percentMode",false);
		
		
		if(action==Action.SET_TAX){
			bundle.putString("hint", getString(R.string.q_enter_tax_dollars));
			bundle.putBoolean("allowZero", true);
		}
		
		numFragment.setArguments(bundle);

		if(billFragment.isActionsEnabled()){
			billFragment.enableActions(false);
		}
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.to_nw, R.animator.to_se);
		ft.replace(R.id.rightContainer, numFragment);
		ft.addToBackStack(null);
		ft.commit();
		tableFragment.disableActions();
	}

	@Override
	public void numPadClose(boolean isEditMode) {
		if(isEditMode){
			switch(action){
			case SET_TAX:
				setTax(numFragment.getStringValue(), false);
				clearCenter();
				tableFragment.bowlsGroup.addRemoveIcons(true);
				break;
			case SET_TIP:
				setTip(numFragment.getStringValue(), false);
				clearCenter();
				tableFragment.bowlsGroup.addRemoveIcons(true);
				break;
			case EDIT_SUBTOTAL:
				updateItemPrice();
				tableFragment.bowlsGroup.addRemoveIcons(true);
				break;
			default:
				updateItemPrice();
				prepareForSelectingBowls(selectedLineItem);
				tableFragment.bowlsGroup.manualSelect(bill.usersOfItem(selectedLineItem));
				break;
			}
		} else {
			registerItemPrice();
			if(!splitEqually){
				billFragment.enableActions(false);
			}
		}
		fm.popBackStack();
		tableFragment.enableActions(isEditMode || splitEqually);
		billFragment.enableActions(true);
	}

}
