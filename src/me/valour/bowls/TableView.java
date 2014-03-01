package me.valour.bowls;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	int bowlsIdCounter = 1;

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
			bowl.setId(bowlsIdCounter);
			bowls.add(bowl);
			bowlsIdCounter++;
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
			setBowlRadius();
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
	
	public void setBowlRadius(){
		double q = ((double)tableRadius*2.0*Math.PI)/(double)Kitchen.maxBowls;
		bowlRadius = (int)(q/2.0);
		tableRadius -= bowlRadius;
		Log.d("vars",String.format("bowl radius=%d",bowlRadius));
		Log.d("vars",String.format("table radius=%d",tableRadius));
	}
	
	public BowlView addBowl(){
		int i = bowls.size()+1;
		BowlView bowl = new BowlView(this.getContext());
		bowl.setId(bowlsIdCounter);
		bowl.init(Kitchen.assignColor(i), bowlRadius);
		bowls.add(bowl);
		bowl.setX(centerX);
		bowl.setY(centerY);
		this.invalidate();
		bowlsIdCounter++;
		return bowl;
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
			double px = Math.cos(angle)*topX - Math.sin(angle)*topY + centerX;
			double py = Math.sin(angle)*topX - Math.cos(angle)*topY + centerY;
			canvas.translate((float)px, (float)py);
			Log.d("vars",String.format("x=%f \t y=%f",px, py));
			bowl.draw(canvas);
			i++;
			canvas.restore();
		 }

	 }
	 
	 public void refreshBowls(){
		/* for(BowlView bv: bowls){
			// bv.formatText();
			 String p = String.format("\\$ %.2f", bv.user.getTotal());
			 bv.setText(p);
		 } */
		 this.invalidate();
	 }
	 
	 public List<Integer> getBowlViewIds(){
		 ArrayList<Integer> ids = new ArrayList<Integer>();
		 for(BowlView bw: bowls){
			 ids.add(bw.getId());
		 }
		 return ids;
	 }
	 
	 public List<User> getBowlUsers(){
		 ArrayList<User> users = new ArrayList<User>();
		 for(BowlView bw: bowls){
			 users.add(bw.user);
		 }
		 return users;
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
