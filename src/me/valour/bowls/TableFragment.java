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

	private ButtonAgent buttonAgent;
	
	public BowlsGroup bowlsGroup;
	private TextView tvQuestion;
	private Button btnOk;
	private Button btnNo;
	
	
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

		final Button tip = (Button)view.findViewById(R.id.btn_tip);
		tip.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				buttonAgent.onTipButtonPress(v);
			}});
		
		final Button tax = (Button)view.findViewById(R.id.btn_tax);
		tax.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				buttonAgent.onTaxButtonPress(v);
			}});
	
		bowlsGroup = (BowlsGroup)view.findViewById(R.id.bowlsGroup);
		bowlsGroup.attachBowlAgents(getActivity());
		
		tvQuestion = (TextView)view.findViewById(R.id.question);
		btnOk = (Button)view.findViewById(R.id.btn_ok);
		
		btnOk.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				buttonAgent.OnOkButtonPress();
			}
		});
		
		btnNo = (Button)view.findViewById(R.id.btn_no);
		
		btnNo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buttonAgent.OnNoButtonPress();
			}
		});
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			buttonAgent = (ButtonAgent)activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement OnNewItemAddedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}
	
	public void setQuestionText(String s){
		if(s==null){
			tvQuestion.setVisibility(View.INVISIBLE);
		} else {
			tvQuestion.setText(s);
			tvQuestion.setVisibility(View.VISIBLE);
		}
	}
	
	public void setQuestionText(int s){
		tvQuestion.setText(s);
		tvQuestion.setVisibility(View.VISIBLE);
	}
	
	public void setQuestionText(int s, Object ...objects){
		tvQuestion.setText(getString(s, objects));
		tvQuestion.setVisibility(View.VISIBLE);
	}
	
	public void showNoButton(boolean show){
		if(show){
			btnNo.setVisibility(View.VISIBLE);
		} else {
			btnNo.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setNoButtonText(int s){
			btnNo.setText(s);
			btnNo.setVisibility(View.VISIBLE);
	}
	
	public void showOkButton(boolean show){
		if(show){
			btnOk.setVisibility(View.VISIBLE);
		} else {
			btnOk.setVisibility(View.INVISIBLE);
		}
	}
	
	public void askToAppy(String type, double value){
		Log.d("vars",value+"");
		double wholeNumber = value*100.0;
		Log.d("vars",wholeNumber+"");
		tvQuestion.setText(getString(R.string.q_ask_percent, wholeNumber, type));
		tvQuestion.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);
		btnNo.setVisibility(View.VISIBLE);
	}
	
	public interface ButtonAgent{
		public void OnOkButtonPress();

		public void OnNoButtonPress();

		public void onTipButtonPress(View v);

		public void onTaxButtonPress(View v);
	}

}
