package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TableFragment extends Fragment {

	private OkListener okButtonSpy;
	private NoListener noButtonSpy;
	private TipListener tipSpy;
	private TaxListener taxSpy;
	private PresetListener presetSpy;
	
	public BowlsGroup bowlsGroup;
	public TextView tvQuestion;
	public Button btnOk;
	public Button btnNo;
	
	
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
		add.setVisibility(View.GONE);
	/*	add.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addBowlSpy.OnAddBowlListener();
			}}); 
		*/
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
		bowlsGroup.attachBowlAgents(getActivity());
		
		tvQuestion = (TextView)view.findViewById(R.id.question);
		btnOk = (Button)view.findViewById(R.id.btn_ok);
		
		btnOk.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				okButtonSpy.OnOkButtonPress();
			}
		});
		
		btnNo = (Button)view.findViewById(R.id.btn_no);
		
		btnNo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				noButtonSpy.OnNoButtonPress();
			}
		});
		
		final Button pref = (Button)view.findViewById(R.id.btn_presets);
		pref.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				presetSpy.onPresetButtonPress(v);
				
			}
		});
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			okButtonSpy = (OkListener)activity;
			noButtonSpy = (NoListener)activity;
			tipSpy = (TipListener)activity;
			taxSpy = (TaxListener)activity;
			presetSpy = (PresetListener)activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement OnNewItemAddedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}
	
	public void askToAppy(String type, double value){
		Log.d("vars",value+"");
		double wholeNumber = value*100.0;
		Log.d("vars",wholeNumber+"");
		String question = String.format("Do you want to apply %.2f%% %s?", wholeNumber, type);
		tvQuestion.setText(question);
		tvQuestion.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);
		btnNo.setVisibility(View.VISIBLE);
	}
	
	public interface OkListener{
		public void OnOkButtonPress();
	}
	
	public interface NoListener{
		public void OnNoButtonPress();
	}
	
	public interface TipListener{
		public void onTipButtonPress(View v);
	}
	
	public interface TaxListener{
		public void onTaxButtonPress(View v);
	}
	
	public interface PresetListener{
		public void onPresetButtonPress(View v);
	}

}
