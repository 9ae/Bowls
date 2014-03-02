package me.valour.bowls;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BowlsGroup extends FrameLayout {

	int mMeasuredWidth;
	int mMeasuredHeight;
	int centerX;
	int centerY;
	int tableRadius;
	int bowlRadius;
	boolean measuredScreen = false;
	FrameLayout.LayoutParams defaultParams;

	ArrayList<BowlView> bowls;
	int bowlsIdCounter = 1;

	public BowlsGroup(Context context) {
		super(context);
		init();
	}

	public BowlsGroup(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		init();
	}

	public BowlsGroup(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	
	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom){
		measureView();	
		
		double angleDelta = Math.PI*2.0/bowls.size();
		double topX = 0;
		double topY = -1.0*tableRadius;
		
		int i= 0;
		 for(BowlView bowl: bowls){
			bowl.setRadius(bowlRadius); 
			bowl.bringToFront();
			double angle = angleDelta*i;
			double px = Math.cos(angle)*topX - Math.sin(angle)*topY + centerX;
			double py = Math.sin(angle)*topX - Math.cos(angle)*topY + centerY;
			bowl.setX((float)px);
			bowl.setY((float)py);
			Log.d("vars",String.format("x=%f \t y=%f",px, py));
			i++;
		 }
		 super.onLayout(changed, left, top, right, bottom);
	}

	private void init() {
		/* LayoutInflater inflater = (LayoutInflater)
			       this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    inflater.inflate(R.layout.bowlsgroup, this, true); */
		
		defaultParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setClickable(true);
		setFocusable(true);

		bowls = new ArrayList<BowlView>();
		for (int i = 1; i <= Kitchen.minBowls; i++) {
			BowlView bowl = new BowlView(this.getContext());
			bowl.setId(bowlsIdCounter);
			bowl.setColors(Kitchen.assignColor(i+1));
			bowls.add(bowl);
			bowlsIdCounter++;
			this.addView(bowl, defaultParams);
		}
		
	/*	TextView tv = new TextView(this.getContext());
		tv.setText("HERE!");
		addView(tv, params); 
		
		this.setWillNotDraw(false); */
	}
	
	
	public void measureView(){
		if(measuredScreen){
			return;
		}
			mMeasuredWidth = getMeasuredWidth();
			mMeasuredHeight = getMeasuredHeight();		
			centerX = mMeasuredWidth / 2;
			centerY = mMeasuredHeight / 2;		
			tableRadius = Math.min(centerX, centerY);
			
			double q = ((double) tableRadius * 2.0 * Math.PI)
					/ (double) Kitchen.maxBowls;
			bowlRadius = (int) (q / 2.0);
			tableRadius -= bowlRadius;
			Log.d("vars", String.format("bowl radius=%d", bowlRadius));
			Log.d("vars", String.format("table radius=%d", tableRadius));
			
			measuredScreen = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		int d = Math.min(measuredWidth, measuredHeight);
		setMeasuredDimension(d, d);

	}

	private int measure(int measureSpec) {
		int result = 0;

		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		return result;
	}
	/*
	 @Override
	  public void onDraw(Canvas canvas) {
		 measureView();	
		 
		 super.onDraw(canvas);
		 
		 
		 double angleDelta = Math.PI*2.0/bowls.size();
		double topX = 0;
		double topY = -1.0*tableRadius;
		
		int i= 0;
		 for(BowlView bowl: bowls){
			 canvas.save();
			 
			bowl.init(Kitchen.assignColor(i+1), bowlRadius); 
			bowl.bringToFront();
			double angle = angleDelta*i;
			double px = Math.cos(angle)*topX - Math.sin(angle)*topY + centerX;
			double py = Math.sin(angle)*topX - Math.cos(angle)*topY + centerY;
			canvas.translate((float)px, (float)py);
			Log.d("vars",String.format("x=%f \t y=%f",px, py));
			bowl.draw(canvas);
			i++;
			canvas.restore();
		 }

	 }
*/

	public BowlView addBowl() {
		int i = bowls.size() + 1;
		BowlView bowl = new BowlView(this.getContext());
		bowl.setId(bowlsIdCounter);
		bowl.setColors(Kitchen.assignColor(i));
		bowl.setX(centerX);
		bowl.setY(centerY);
		bowls.add(bowl);
		addView(bowl, defaultParams);
		bowlsIdCounter++;
		return bowl;
	}

	public void refreshBowls() {

		 for(BowlView bv: bowls){ 
			bv.formatText(); 
			 bv.invalidate();
		 }
		 
	}

	public List<Integer> getBowlViewIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (BowlView bw : bowls) {
			ids.add(bw.getId());
		}
		return ids;
	}

	public List<User> getBowlUsers() {
		ArrayList<User> users = new ArrayList<User>();
		for (BowlView bw : bowls) {
			users.add(bw.user);
		}
		return users;
	}

	public void bowlsFocus(boolean unfade) {
		for (BowlView bv : bowls) {
			if (unfade) {
				bv.unfade();
			} else {
				bv.fade();
			}
		}
	}

}
