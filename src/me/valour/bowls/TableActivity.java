package me.valour.bowls;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TableActivity extends Activity implements
		BowlsGroup.AddBowlListener, TableFragment.OkListener,
		TableFragment.NoListener, TableFragment.TaxListener,
		TableFragment.TipListener, TableFragment.PresetListener {

	private int bowlsCount;
	private Bill bill;
	private FragmentManager fm;

	private TableFragment tableFragment;
	private NumberPadFragment numFragment;
	public boolean splitEqually;
	private Action action;
	private LineItem selectedLineItem = null;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		splitEqually = intent.getBooleanExtra("splitEqually", true);

		bowlsCount = Kitchen.minBowls;
		action = Action.ITEM_PRICE;

		setContentView(R.layout.activity_table);

		fm = getFragmentManager();
		tableFragment = (TableFragment) fm.findFragmentById(R.id.tableFragment);
		numFragment = (NumberPadFragment) fm
				.findFragmentById(R.id.numpadFragment);

		sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		bill = new Bill(splitEqually, getTax(), getTip());
		if (splitEqually) {
			initSplitEqually();
		} else {
			initSplitLineItems();
		}
		tableFragment.tvQuestion.bringToFront();
		bill.addUniqueUsers(tableFragment.bowlsGroup.getBowlUsers());
	}

	public void applyTax() {
		bill.calculateTax();
		tableFragment.bowlsGroup.refreshBowls();
	}

	public double getTax() {
		if (sp == null) {
			return 0.0;
		} else {
			String stringTax = sp.getString("default_tax",
					Double.toString(Kitchen.tax));
			return Double.parseDouble(stringTax) / 100.0;
		}
	}

	public void setTax(String tax, boolean save) {
		double percent = Double.parseDouble(tax) / 100;
		bill.setTax(percent);
		if (sp != null && save) {
			sp.edit().putString("default_tax", tax).commit();
		}
	}

	public void applyTip() {
		bill.calculateTip();
		tableFragment.bowlsGroup.refreshBowls();
	}

	public double getTip() {
		if (sp == null) {
			return 0.0;
		} else {
			String stringTip = sp.getString("default_tip",
					Double.toString(Kitchen.tip));
			return Double.parseDouble(stringTip) / 100.0;
		}
	}

	public void setTip(String tip, boolean save) {
		double percent = Double.parseDouble(tip) / 100;
		bill.setTip(percent);

		if (sp != null && save) {
			sp.edit().putString("default_tip", tip).commit();
		}
	}

	private void initSplitEqually() {
		tableFragment.tvQuestion.setText(R.string.q_enter_subtotal);
	}

	private void initSplitLineItems() {
		tableFragment.tvQuestion.setText(R.string.q_enter_first_li);
	}

	private void clearCenter() {
		action = Action.NONE;
		tableFragment.btnOk.setVisibility(View.INVISIBLE);
		tableFragment.tvQuestion.setText("");
		tableFragment.tvQuestion.setVisibility(View.INVISIBLE);
		tableFragment.btnNo.setVisibility(View.INVISIBLE);
	}

	private void completePercentChange() {
		numFragment.clearField();
		numFragment.setAsDollarMode();
		numFragment.highlightTextField(false);
		clearCenter();
	}

	private void registerItemPrice() {
		double price = numFragment.getNumberValue();
		LineItem li = bill.addLineItem(price);
		if (splitEqually) {
			bill.divideEqually(li);
			clearCenter();
		} else {
			tableFragment.tvQuestion.setText(R.string.q_select_bowls);
			prepareForSelectingBowls(li);
		}
		tableFragment.bowlsGroup.refreshBowls();
	}

	private void prepareForSelectingBowls(LineItem li) {
		selectedLineItem = li;
		tableFragment.bowlsGroup.readyBowlSelect();
		action = Action.SELECT_BOWLS;
	}

	private void handleSelectedBowls() {
		if (selectedLineItem != null) {
			List<User> consumers = tableFragment.bowlsGroup.getSelectedUsers();
			if(consumers.isEmpty()){
				tableFragment.tvQuestion.setText(R.string.q_min_select);
				return;
			}
			bill.divideAmongst(selectedLineItem, consumers);

			tableFragment.bowlsGroup.stopBowlSelect();

			// move to being ready for next Item
			tableFragment.tvQuestion.setText(R.string.q_enter_next_li);
			numFragment.clearField();
			action = Action.ITEM_PRICE;
			selectedLineItem = null;
		}
		tableFragment.bowlsGroup.refreshBowls();
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
		case CONFIRM_TIP:
			/* apply default tip */
			applyTip();
			clearCenter();
			break;
		case SET_TIP:
			/* apply tip at new rate */
			setTip(numFragment.getStringValue(), false);
			applyTip();
			completePercentChange();
			break;
		case CONFIRM_TAX:
			applyTax();
			clearCenter();
			break;
		case SET_TAX:
			setTax(numFragment.getStringValue(), false);
			applyTax();
			completePercentChange();
		default:
			break;
		}
	}

	@Override
	public void OnNoButtonPress() {
		switch (action) {
		case CONFIRM_TAX:
			numFragment.clearField();
			numFragment.highlightTextField(true);
			numFragment.setAsPercentMode();
			tableFragment.tvQuestion.setText(R.string.q_enter_tax_percent);
			tableFragment.btnNo.setVisibility(View.INVISIBLE);
			action = Action.SET_TAX;
			break;

		case CONFIRM_TIP:
			numFragment.clearField();
			numFragment.highlightTextField(true);
			numFragment.setAsPercentMode();
			tableFragment.tvQuestion.setText(R.string.q_enter_tip_percent);
			tableFragment.btnNo.setVisibility(View.INVISIBLE);
			action = Action.SET_TIP;
			break;

		default:
			break;
		}

	}

	@Override
	public void onTipButtonPress(View v) {
		Button btn = (Button) v;
		String txt = btn.getText().toString();
		if (txt.contains("+")) {
			tableFragment.askToAppy("tip", bill.getTip());
			action = Action.CONFIRM_TIP;
			txt = txt.replaceFirst("\\+", "\\-");
		} else {
			bill.clearTip();
			tableFragment.bowlsGroup.refreshBowls();
			txt = txt.replaceFirst("\\-", "\\+");
		}
		btn.setText(txt);
	}

	@Override
	public void onTaxButtonPress(View v) {
		Button btn = (Button) v;
		String txt = btn.getText().toString();
		if (txt.contains("+")) {
			tableFragment.askToAppy("tax", bill.getTax());
			action = Action.CONFIRM_TAX;
			txt = txt.replaceFirst("\\+", "\\-");
		} else {
			bill.clearTax();
			tableFragment.bowlsGroup.refreshBowls();
			txt = txt.replaceFirst("\\-", "\\+");
		}
		btn.setText(txt);
	}

	@Override
	public void onPresetButtonPress(View v) {
		Intent intent = new Intent(this, PresetActivity.class);
		startActivity(intent);
	}

	@Override
	public void addUser(User user) {
		if (bowlsCount == Kitchen.maxBowls) {

		} else {
			bowlsCount++;
			bill.addUser(user);
			if (splitEqually) {
				bill.redivideEqually();
			}
		}
		Log.d("vars", String.format("bowls=%d", bowlsCount));
		
	}

}
