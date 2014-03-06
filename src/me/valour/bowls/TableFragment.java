package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TableFragment extends Fragment {

	private AddBowlListener addBowlSpy;
	private OkListener okButtonSpy;
	private NoListener noButtonSpy;
	private TipListener tipSpy;
	private TaxListener taxSpy;
	private PresetListener presetSpy;
	private NewBowlListener newBowlSpy;
	
	public BowlsGroup bowlsGroup;
	public TextView tvQuestion;
	public Button btnOk;
	public Button btnNo;
	
	private BowlView newBowl;
	
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
		
		newBowl = (BowlView) view.findViewById(R.id.newBowl);
		newBowl.setColors(Kitchen.assignColor(Kitchen.minBowls+1));
		newBowl.setRadius(Kitchen.minRadius);
		newBowl.setOnTouchListener(newBowlSpy);
		newBowl.setOnDragListener(newBowlSpy);
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			addBowlSpy = (AddBowlListener)activity;
			okButtonSpy = (OkListener)activity;
			noButtonSpy = (NoListener)activity;
			tipSpy = (TipListener)activity;
			taxSpy = (TaxListener)activity;
			presetSpy = (PresetListener)activity;
			newBowlSpy = new NewBowlListener();
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
	
	public interface AddBowlListener{
		public void OnAddBowlListener();
	}
	
	public interface SubBowlListener{
		public void OnSubBowlListener();
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
	
	public class NewBowlListener implements OnTouchListener, OnDragListener{
	/*	
		public boolean testAdd(float x, float y){
			float center_x = (float)centerX;
			float center_y = (float)centerY;
			float radius = (float)(tableRadius-bowlRadius);
			return Math.pow(x - center_x,2) + Math.pow(y - center_y,2) <= (radius*radius);
		}
		
		public double findAngle(float x, float y){
			double angle = 0;
			float px = bowls.getFirst().getX();
			float py = bowls.getFirst().getY();
			float cx = (float)centerX;
			float cy = (float)centerY;
			angle = (Math.atan2(x - cx,y - cy)- Math.atan2(px- cx,py- cy));
			if(angle<0){
				angle = Math.PI*2 - Math.abs(angle);
			}
			Log.d("vars","angle="+angle);
			return angle;
		}
*/
		@Override
		public boolean onDrag(View v, DragEvent event) {
			BowlView bv = (BowlView)event.getLocalState();
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		    	if(v.getId()==R.id.bowlsGroup){
		    		Log.d("vars", "entered circle");
		    	}
		        break;
		    case DragEvent.ACTION_DRAG_EXITED:        
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DROP:

		        break;
		    case DragEvent.ACTION_DRAG_ENDED:

		        break;
		    default:
		        break;
		} 
		/*	
	    	if(testAdd(x,y)){
				double angle = findAngle(x,y);
				double delta = Math.PI*2.0/bowls.size();
				int index = (int)Math.round(angle/delta);
				addBowlAt(index);
			} else {
				bv.setX(0);
				bv.setY(0);
			}
		return true; */
			return true;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
			    return true;
			} 
			else {
			    return false;
			}
		}
		}

}
