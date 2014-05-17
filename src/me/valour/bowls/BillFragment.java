package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BillFragment extends Fragment implements 
	Bill.BillChangesAgent, 
	LineItemAdapter.LineItemAgent{

	private Bill bill;
	private TextView amountSubtotal;
	private ImageButton editSubtotal;
	private TextView percentTax;
	private TextView amountTax;
	private ImageButton editTax;
	private TextView percentTip;
	private TextView amountTip;
	private ImageButton editTip;
	private TextView amountTotal;
	private TextView newLineItem;
	
	private ListView listView;
	private LineItemAdapter adapter;
	
	private BillFragmentAgent agent; 
	
	public BillFragment(){}
	
	private int selectedLI = -1;
	private View prevView = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_bill, container, false);
		listView = (ListView) view.findViewById(R.id.lineItemsList);
		adapter = new LineItemAdapter(getActivity(), bill.lineItems, this);
		adapter.setNotifyOnChange(true);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				if(selectedLI==position){
					Log.d("vars","click again");
					deselectLineItem();
				} else {
					if(hasSelectedLineItem()){
						deselectLineItem();
					}
					selectLineItem(view, position);
				}
			}
			
		});
		
		amountSubtotal = (TextView) view.findViewById(R.id.subtotal_amount);
		amountTotal = (TextView) view.findViewById(R.id.total_amount);
		
		percentTax = (TextView) view.findViewById(R.id.tax_percent);
		amountTax = (TextView) view.findViewById(R.id.tax_amount);
		
		percentTip = (TextView) view.findViewById(R.id.tip_percent);
		amountTip = (TextView) view.findViewById(R.id.tip_amount);
		
		editSubtotal = (ImageButton) view.findViewById(R.id.edit_subtotal);
		editTax = (ImageButton) view.findViewById(R.id.edit_tax);
		editTip = (ImageButton) view.findViewById(R.id.edit_tip);
		
		newLineItem = (TextView) view.findViewById(R.id.newLineItem);
		newLineItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(hasSelectedLineItem()){
					deselectLineItem();
				}
				agent.onNewLineItem();
			}
		});
		
		editTax.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				agent.editTax();
				
			}
		});
		
		editTip.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				agent.editTip();
				
			}
		});
		
		return view;
    }

	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			bill = ((TableActivity)activity).getBill();
			agent = (BillFragmentAgent) activity;
			bill.attachAgent(this);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}
	
	public void selectLineItem(View view, int position){
		prevView = view;
		selectedLI = position;
		agent.selectLineItem(position);
		
		 ImageButton delButton = (ImageButton) view.findViewById(R.id.lineDelete);
		 delButton.setEnabled(true);
		 delButton.setVisibility(View.VISIBLE);
		 ImageButton editButton = (ImageButton) view.findViewById(R.id.lineEdit);
		 editButton.setEnabled(true);
		 editButton.setVisibility(View.VISIBLE);
		 
		 view.setBackgroundResource(R.drawable.li_gradient_active);
		 ((TextView)view.findViewById(R.id.lineItemPrice)).setTextColor(getResources().getColor(R.color.lemon));
	}
	
	public void deselectLineItem(){
		if(prevView!=null){
			 ImageButton delButton = (ImageButton) prevView.findViewById(R.id.lineDelete);
			 delButton.setEnabled(false);
			 delButton.setVisibility(View.INVISIBLE);
			 ImageButton editButton = (ImageButton) prevView.findViewById(R.id.lineEdit);
			 editButton.setEnabled(false);
			 editButton.setVisibility(View.INVISIBLE);
			 prevView.setBackgroundResource(R.drawable.li_gradient);
			 ((TextView)prevView.findViewById(R.id.lineItemPrice)).setTextColor(getResources().getColor(R.color.apple));
		}
		agent.deselectLineItem();
		prevView = null;
		selectedLI = -1;
	}
	
	public boolean hasSelectedLineItem(){
		return prevView!=null && selectedLI>-1;
	}
	
	private void setSubtotal(double t){
		amountSubtotal.setText(getString(R.string.x_dollars, t));
	}
	
	private void setTotal(double t){
		amountTotal.setText(getString(R.string.x_dollars, t));
	}
	
	private void setTaxPercent(double t){
		percentTax.setText(getString(R.string.x_percent, (t*100)));
	}
	
	private void setTipPercent(double t){
		percentTip.setText(getString(R.string.x_percent, (t*100)));
	}
	
	private void setTaxAmount(double t){
		amountTax.setText(getString(R.string.x_dollars, t));
	}
	
	private void setTipAmount(double t){
		amountTip.setText(getString(R.string.x_dollars, t));
	}
	
	public void clearSummary(){
		setSubtotal(0.0);
		setTotal(0.0);
		setTaxPercent(bill.getTax());
		setTaxAmount(0.0);
		setTipPercent(bill.getTip());
		setTipAmount(0.0);
	}
	
	public void adjustForSplitEqually(){
		listView.setVisibility(View.GONE);
		newLineItem.setVisibility(View.GONE);
		editSubtotal.setVisibility(View.VISIBLE);
		editSubtotal.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				agent.editSubtotal();
			}
		});
		
	}
	
	public void updatedList(){
		adapter.notifyDataSetChanged();
	}
	
	public interface BillFragmentAgent{
		public void onNewLineItem();
		public void selectLineItem(int position);
		public void deselectLineItem();
		public void editLineItem();
		public void updateBowlsPrice();
		public void editSubtotal();
		public void editTax();
		public void editTip();
	}

	/*
	 * BillChangesAgent methods begin
	 */
	
	@Override
	public void subtotalChanged() {
		setSubtotal(bill.getSubtotal());
		updateTotal();
	}

	@Override
	public void taxChanged(boolean rateChanged) {
		if(rateChanged){
			setTaxPercent(bill.getTax());
		}
		setTaxAmount(bill.getTaxAmount());
		updateTotal();
	}

	@Override
	public void tipChanged(boolean rateChanged) {
		if(rateChanged){
			setTipPercent(bill.getTip());
		}
		setTipAmount(bill.calculateTip());
		updateTotal();
	}

	@Override
	public void updateTotal() {
		double total = bill.getSubtotal() + bill.getTaxAmount() + bill.calculateTip();
		setTotal(total);
	}
	
	@Override
	public void removeLineItemFromBill() {
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void toggleEditTaxButton(){
		if(bill.taxApplied() && bill.getSubtotal()>0.0){
			editTax.setVisibility(View.VISIBLE);
		} else {
			editTax.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void toggleEditTipButton(){
		if(bill.tipApplied()){
			editTip.setVisibility(View.VISIBLE);
		} else {
			editTip.setVisibility(View.INVISIBLE);
		}
	}
	
	/*
	 * BillChangesAgent methods end
	 */

	/*
	 * LineItemAgent methods
	 */
	
	@Override
	public void deleteLI(int position) {
		deselectLineItem();
		bill.itemRemove(position);
		agent.updateBowlsPrice();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void editLI(LineItem li) {
		agent.editLineItem();
	}

}
