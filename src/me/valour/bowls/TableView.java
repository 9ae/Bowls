package me.valour.bowls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

public class TableView extends ViewGroup {
	
	int mMeasuredWidth;
	int mMeasuredHeight;
	int centerX;
	int centerY;	
	int tableRadius;
	int bowlRadius;
	boolean measuredScreen=false;
	boolean baseBowlsInitialized = false;
	
//	private int bowlsCount=2;
	
//	private Paint bowlsPaint;

	public TableView(Context context) {
		super(context);
		init();
	}

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		for(int i=1; i<=Kitchen.minBowls; i++){
			BowlView bowl = new BowlView(this.getContext());
			bowl.setId(i);
			this.addView(bowl);
		}
	//	setWillNotDraw(false);
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
			setBowlRadius(Kitchen.maxBowls);
			measuredScreen = true;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		int d = Math.min(measuredWidth, measuredHeight);
		setMeasuredDimension(d,d);
		
		int children = getChildCount();
		for(int i=0; i<children; i++){
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
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
	
	public void setBowlRadius(int max){
		Log.d("vars",String.format("table radius=%d",tableRadius));
		double q = ((double)tableRadius*2.0*Math.PI)/(double)max;
		if(max>6){
			bowlRadius = (int)q;
		} else {
			bowlRadius = (int)(q/2.0);
		}
		Log.d("vars",String.format("bowl radius=%d",bowlRadius));
	}
	
/*	public void setBowlsCount(int count){
		  bowlsCount = count;
		  Log.d("vars",String.format("bowls=%d",bowlsCount));
		  bowlsPaint.setColor(Kitchen.assignColor(bowlsCount));
		  invalidate();
	} */
	
	public void addBowl(int i){
		BowlView bowl = new BowlView(this.getContext());
		bowl.setId(i);
		bowl.init(Kitchen.assignColor(i), bowlRadius);
		this.addView(bowl, bowlRadius, bowlRadius);
		bowl.setX(centerX);
		bowl.setY(centerY);

	}
	
	private void initBaseBowls(){
		if(baseBowlsInitialized){ return; }
		double angleDelta = Math.PI*2.0/Kitchen.minBowls;
		double topX = 0;
		double topY = -1.0*tableRadius;
		for(int i=0; i<Kitchen.minBowls; i++){
			BowlView bowl = (BowlView)getChildAt(i);
			bowl.init(Kitchen.assignColor(i+1), bowlRadius);
			double angle = angleDelta*i;
			double px = Math.cos(angle)*topX - Math.sin(angle)*topY + tableRadius;
			double py = Math.sin(angle)*topX - Math.cos(angle)*topY + tableRadius;
			bowl.setX((float)px);
			bowl.setY((float)py);
		}
		baseBowlsInitialized = true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		measureView();
		initBaseBowls();
		int children = getChildCount();
		for(int i=0; i<children; i++){
			BowlView bowl = (BowlView)getChildAt(i);
			if(bowl.getVisibility()!=GONE){
				bowl.layout(l, t, r, b);
			}
		}
	}
}
