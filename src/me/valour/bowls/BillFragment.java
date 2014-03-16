package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class BillFragment extends Fragment {

	private Bill bill;
	private TextView amountSubtotal;
	private TextView percentTax;
	private TextView amountTax;
	private TextView percentTip;
	private TextView amountTip;
	private TextView amountTotal;
	private TextView newLineItem;
	
	private ListView listView;
	
	private NewLineItemListener newLineItemSpy; 
	
	public BillFragment(){}
	
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
		listView.setAdapter(new LineItemAdapter(getActivity(), bill.getLineItems()));
		
		amountSubtotal = (TextView) view.findViewById(R.id.subtotal_amount);
		amountTotal = (TextView) view.findViewById(R.id.total_amount);
		percentTax = (TextView) view.findViewById(R.id.tax_percent);
		amountTax = (TextView) view.findViewById(R.id.tax_amount);
		percentTip = (TextView) view.findViewById(R.id.tip_percent);
		amountTip = (TextView) view.findViewById(R.id.tip_amount);
		
		newLineItem = (TextView) view.findViewById(R.id.newLineItem);
		newLineItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				newLineItemSpy.OnNewLineItem();
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
			newLineItemSpy = (NewLineItemListener) activity;
			
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
	
	public void setSubtotal(double t){
		amountSubtotal.setText(getString(R.string.x_dollars, t));
	}
	
	public void setTotal(double t){
		amountTotal.setText(getString(R.string.x_dollars, t));
	}
	
	public void setTaxPercent(double t){
		percentTax.setText(getString(R.string.x_percent, t));
	}
	
	public void setTipPercent(double t){
		percentTip.setText(getString(R.string.x_percent, t));
	}
	
	public void setTaxAmount(double t){
		amountTax.setText(getString(R.string.x_dollars, t));
	}
	
	public void setTipAmount(double t){
		amountTip.setText(getString(R.string.x_dollars, t));
	}
	
	public void clearSummary(){
		setSubtotal(0.0);
		setTotal(0.0);
		setTaxPercent(0.0);
		setTaxAmount(0.0);
		setTipPercent(0.0);
		setTipAmount(0.0);
	}
	
	public void adjustForSplitEqually(){
		listView.setVisibility(View.GONE);
		newLineItem.setVisibility(View.GONE);
	}
	
	public interface NewLineItemListener{
		public void OnNewLineItem();
	}
}
