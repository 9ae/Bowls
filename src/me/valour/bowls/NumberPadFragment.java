package me.valour.bowls;

import me.valour.bowls.R.color;
import me.valour.bowls.enums.InputFormat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NumberPadFragment extends Fragment {
	
	private NumberPadListener ourButtonListener;
	private CloseNumpadListener closeListener;
	private TextView numberValue;
	private Button dotButton;
	private TextView dollarSign;
	private TextView percentSign;
	private LinearLayout fieldBox;
	private InputFormat numberMode = InputFormat.DOLLAR;
	
	private Button enterButton;
	
	public static NumberPadFragment newInstance() {
		NumberPadFragment fragment = new NumberPadFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public NumberPadFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ourButtonListener = new NumberPadListener();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_number_pad, container, false);
		
		((Button)view.findViewById(R.id.no0)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no1)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no2)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no3)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no4)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no5)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no6)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no7)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no8)).setOnClickListener(ourButtonListener);
		((Button)view.findViewById(R.id.no9)).setOnClickListener(ourButtonListener);
		((ImageButton)view.findViewById(R.id.nodel)).setOnClickListener(ourButtonListener);
		dotButton = (Button)view.findViewById(R.id.nodot);
		dotButton.setOnClickListener(ourButtonListener);
		
		dollarSign = (TextView) view.findViewById(R.id.dollar_sign);
		percentSign = (TextView) view.findViewById(R.id.percent_sign);
		enterButton = (Button) view.findViewById(R.id.enter);
		
		enterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				closeListener.numPadClose();
			}
		});
		
		if(numberMode==InputFormat.DOLLAR){
			percentSign.setVisibility(View.INVISIBLE);
		} else {
			dollarSign.setVisibility(View.INVISIBLE);
		}
		
		numberValue = (TextView) view.findViewById(R.id.numberValue);
		fieldBox = (LinearLayout) view.findViewById(R.id.enter_number_layout);
		
		/*view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					Log.d("vars", "touched down");
				}
				return false;
			}
		}); */
		
		return view;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			closeListener = (CloseNumpadListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
	
	public void deleteLastChar(){
		CharSequence txt = numberValue.getText();
		int len = txt.length();
		if(len==0){
			return;
		}
		CharSequence newtxt = txt.subSequence(0,len-1);
		numberValue.setText(newtxt);
		if(txt.charAt(len-1)=='.'){
			dotButton.setEnabled(true);
		}
	}
	
	public void appendChar(CharSequence c){
		CharSequence txt = numberValue.getText();
		String newtxt = txt.toString().concat(c.toString());
		numberValue.setText(newtxt);
		if(c.equals(".")){
			dotButton.setEnabled(false);
		}
	}
	
	public void setAsDollarMode(){
		dollarSign.setVisibility(View.VISIBLE);
		percentSign.setVisibility(View.INVISIBLE);
	}
	
	public void setAsPercentMode(){
		dollarSign.setVisibility(View.INVISIBLE);
		percentSign.setVisibility(View.VISIBLE);
	}
	
	public String getStringValue(){
		return numberValue.getText().toString();
	}
	
	public double getNumberValue(){
		String v = numberValue.getText().toString();
		if(v.isEmpty()){
			return 0.0;
		} else {
			return Double.parseDouble(v);
		}
	}
	
	public void clearField(){
		numberValue.setText("");
		dotButton.setEnabled(true);
	}
	
	public void highlightTextField(boolean highlight){
		if(highlight){
			fieldBox.setBackgroundColor(Color.GREEN);
		} else {
			fieldBox.setBackgroundColor(Color.TRANSPARENT);
		}
	}

	public class NumberPadListener  implements View.OnClickListener {
		
		public NumberPadListener(){
			
		}
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.nodel){
				deleteLastChar();
			} else {
				Button b = (Button)v;
				appendChar(b.getText());
			}
		}
	}
	
	public interface CloseNumpadListener{
		public void numPadClose();
	}
}
