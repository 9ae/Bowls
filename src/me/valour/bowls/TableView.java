package me.valour.bowls;

import java.util.LinkedList;

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

public class TableView extends View {
	
	int mMeasuredWidth;
	int mMeasuredHeight;
	int centerX;
	int centerY;	
	int tableRadius;
	int bowlRadius;
	boolean measuredScreen=false;
	boolean baseBowlsInitialized = false;
	
	LinkedList<BowlView> bowls;
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
		
		bowls = new LinkedList<BowlView>();
		
		for(int i=1; i<=Kitchen.minBowls; i++){
			BowlView bowl = new BowlView(this.getContext());
			bowl.setId(i);
			bowls.add(bowl);
		}
	//	initBaseBowls();
	//	this.setBackgroundColor(Color.LTGRAY);
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
	
	public void addBowl(int i){
		BowlView bowl = new BowlView(this.getContext());
		bowl.setId(i);
		bowl.init(Kitchen.assignColor(i), bowlRadius);
		bowls.add(bowl);
		bowl.setX(centerX);
		bowl.setY(centerY);
		this.invalidate();
	}
	
	public void subBowl(){
		bowls.pop();
		this.invalidate();
	}
	
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
			double px = Math.cos(angle)*topX - Math.sin(angle)*topY + tableRadius;
			double py = Math.sin(angle)*topX - Math.cos(angle)*topY + tableRadius;
			canvas.translate((float)px, (float)py);
			Log.d("vars",String.format("x=%f \t y=%f",px, py));
			bowl.draw(canvas);
			i++;
			canvas.restore();
		 }

	 }

/*	@Override
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
		super.layout(0, 0, mMeasuredWidth, mMeasuredHeight);
	} */
}
