package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TableFragment extends Fragment {

	private ButtonAgent buttonAgent;
	
	public BowlsGroup bowlsGroup;
	private TextView tvQuestion;
	private Button btnOk;
	private Button tax;
	private Button tip;
	
	private boolean tvQuestionWasVisible = false;
	private boolean btnOkWasVisible = false;
	
	public TableFragment() {

	}
	
	public void enableActions(){
		tax.setEnabled(true);
		tax.setVisibility(View.VISIBLE);
		tip.setEnabled(true);
		tip.setVisibility(View.VISIBLE);
		tvQuestion.setVisibility(View.VISIBLE);
		if(tvQuestionWasVisible){
			tvQuestion.setVisibility(View.VISIBLE);
		}
		if(btnOkWasVisible){
			btnOk.setVisibility(View.VISIBLE);
		}

		bowlsGroup.enableActions();
		bowlsGroup.animate().alpha(1).start();
	}
	
	public void disableActions(){
		tax.setEnabled(false);
		tax.setVisibility(View.INVISIBLE);
		tip.setEnabled(false);
		tip.setVisibility(View.INVISIBLE);
		if(tvQuestion.getVisibility()==View.VISIBLE){
			tvQuestion.setVisibility(View.INVISIBLE);
			tvQuestionWasVisible = true;
		}
		if(btnOk.getVisibility()==View.VISIBLE){
			btnOk.setVisibility(View.INVISIBLE);
			btnOkWasVisible = true;
		}

		bowlsGroup.disableActions();
		bowlsGroup.animate().alpha(0).start();
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

		tip = (Button)view.findViewById(R.id.btn_tip);
		tip.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				buttonAgent.onTipButtonPress(v);
			}});
		
		tax = (Button)view.findViewById(R.id.btn_tax);
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
			tvQuestion.setText("");
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
	
	public void showOkButton(boolean show){
		if(show){
			btnOk.setVisibility(View.VISIBLE);
		} else {
			btnOk.setVisibility(View.INVISIBLE);
		}
	}
	
	public void askToAppy(String type, double value){
		if(type.equals("tax")){
			tvQuestion.setText(getString(R.string.q_ask_amount, value, type));
		} else if (type.equals("tip")){
			tvQuestion.setText(getString(R.string.q_ask_percent, value, type));
		}
		tvQuestion.setVisibility(View.VISIBLE);
		btnOk.setVisibility(View.VISIBLE);
		bowlsGroup.clearCenter();
	}
	
	public void alignForLeftHanded(){
		RelativeLayout.LayoutParams taxParams = (RelativeLayout.LayoutParams) tax.getLayoutParams();
		taxParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
		taxParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		tax.setLayoutParams(taxParams);
		tax.setBackgroundResource(R.drawable.ic_tbtn_bot_left_add);
		tax.setGravity(Gravity.TOP | Gravity.RIGHT);
		
		RelativeLayout.LayoutParams tipParams = (RelativeLayout.LayoutParams) tip.getLayoutParams();
		tipParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
		tipParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		tip.setLayoutParams(tipParams);
		tip.setBackgroundResource(R.drawable.ic_tbtn_top_left_add);
		tip.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
	}
	
	public interface ButtonAgent{
		public void OnOkButtonPress();

		public void onTipButtonPress(View v);

		public void onTaxButtonPress(View v);
	}

}
