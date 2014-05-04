package me.valour.bowls;

import me.valour.bowls.R.color;
import me.valour.bowls.enums.InputFormat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.Toast;


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
	
	private boolean isEditMode = false;
	protected boolean allowZero;
	
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
		
		numberValue = (TextView) view.findViewById(R.id.numberValue);
	//	fieldBox = (LinearLayout) view.findViewById(R.id.enter_number_layout);
		
		Bundle bundle = this.getArguments();
		
		if(bundle.containsKey("hint")){
			String hint = bundle.getString("hint");
			numberValue.setHint(hint);
		}
		
		if(bundle.containsKey("numberValue")){
			double no = bundle.getDouble("numberValue");
			
			if(bundle.getBoolean("percentMode", false)){
				numberMode = InputFormat.PERCENT;
			} else {
				numberMode = InputFormat.DOLLAR;
			}
			setValue(no);
		} else {
			clearField();
		}
		
		allowZero = bundle.getBoolean("allowZero", false);
		Log.d("vars", "outside allow zero "+allowZero);
		
		if(numberMode==InputFormat.DOLLAR){
			percentSign.setVisibility(View.INVISIBLE);
			dollarSign.setVisibility(View.VISIBLE);
		} else {
			dollarSign.setVisibility(View.INVISIBLE);
			percentSign.setVisibility(View.VISIBLE);
		}
		
		final Context ctx = view.getContext();
		final String nonzero_msg = this.getString(R.string.non_zero);
		enterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("vars", "inside allow zero "+allowZero);
				if(allowZero){
					closeListener.numPadClose(isEditMode);
				} else {
					if(getNumberValue()==0.0){
						Toast toast = Toast.makeText(ctx, nonzero_msg, Toast.LENGTH_SHORT);
						toast.show();
					} else {
						closeListener.numPadClose(isEditMode);
					}
				}
			}
		});
		
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
		isEditMode = false;
	}
	
	public void setValue(double value){
		String strVal;
		if(numberMode==InputFormat.DOLLAR){
			strVal = String.format("%.2f", value);
		} else {
			strVal = Double.toString(value);
		}
		numberValue.setText(strVal);
		if(strVal.contains(".")){
			dotButton.setEnabled(false);
		} else {
			dotButton.setEnabled(true);
		}
		isEditMode = true;
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
		public void numPadClose(boolean isEditMode);
	}
}
