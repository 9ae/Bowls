package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TableFragment extends Fragment {

	private AddBowlListener addBowlSpy;
	private SubBowlListener subBowlSpy;
	private OkListener okButtonSpy;
	private TipListener tipSpy;
	private TaxListener taxSpy;
	
	public BowlsGroup bowlsGroup;
	public TextView tvQuestion;
	public Button btnOk;
	
	public TableFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_table, container, false);
		
		final Button add = (Button)view.findViewById(R.id.btn_addBowl);
		add.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addBowlSpy.OnAddBowlListener();
			}});
		
		final Button sub = (Button)view.findViewById(R.id.btn_subBowl);
		sub.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				subBowlSpy.OnSubBowlListener();
			}});
		
		final Button tip = (Button)view.findViewById(R.id.btn_tip);
		tip.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				tipSpy.onTipButtonPress(v);
			}});
		
		final Button tax = (Button)view.findViewById(R.id.btn_tax);
		tax.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				taxSpy.onTaxButtonPress(v);
			}});
		
		bowlsGroup = (BowlsGroup)view.findViewById(R.id.bowlsGroup);
		tvQuestion = (TextView)view.findViewById(R.id.question);
		btnOk = (Button)view.findViewById(R.id.okButton);
		
		btnOk.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				okButtonSpy.OnOkButtonPress();
			}
		});
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			addBowlSpy = (AddBowlListener)activity;
			subBowlSpy = (SubBowlListener)activity;
			okButtonSpy = (OkListener)activity;
			tipSpy = (TipListener)activity;
			taxSpy = (TaxListener)activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement OnNewItemAddedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}
	
	public interface AddBowlListener{
		public void OnAddBowlListener();
	}
	
	public interface SubBowlListener{
		public void OnSubBowlListener();
	}
	
	public interface OkListener{
		public void OnOkButtonPress();
	}
	
	public interface TipListener{
		public void onTipButtonPress(View v);
	}
	
	public interface TaxListener{
		public void onTaxButtonPress(View v);
	}

}
