package me.valour.bowls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class TableView extends View {
	
	int mMeasuredWidth;
	int mMeasuredHeight;
	int centerX;
	int centerY;	
	int bigRadius;
	boolean measuredScreen=false;
	
	private int bowlsCount=2;
	
	private Paint bowlsPaint;

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
		bowlsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bowlsPaint.setColor(Color.BLUE);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(!measuredScreen){
			mMeasuredWidth = getMeasuredWidth();
			mMeasuredHeight = getMeasuredHeight();		
			centerX = mMeasuredWidth / 2;
			centerY = mMeasuredHeight / 2;		
			bigRadius = Math.min(centerX, centerY);
			measuredScreen = true;
		}
		canvas.drawCircle(centerX, centerY, bigRadius, bowlsPaint);
	//	super.onDraw(canvas);
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
	
	public void setBowlsCount(int count){
		  bowlsCount = count;
		  Log.d("vars",String.format("bowls=%d",bowlsCount));
		  bowlsPaint.setColor(Kitchen.assignColor(bowlsCount));
		  invalidate();
	}
}
