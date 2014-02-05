package me.valour.bowls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class BowlView extends TextView {
	
	private Paint primaryPaint;
	private int radius;
	
	public BowlView(Context context, AttributeSet ats, int ds){
		super(context, ats, ds);
	}
	
	public BowlView (Context context) {
	    super(context);

	  }

	 public BowlView(Context context, AttributeSet attr) {
	    super(context, attr);
	  }
	 
	 public void init(int color, int radius){
		 primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		 primaryPaint.setColor(color);
		 this.setTextColor(Color.WHITE);
		 this.radius = radius;
		 this.setMaxWidth(radius);
		 this.setMaxHeight(radius);
		 this.setMinWidth(radius);
		 this.setMinHeight(radius);
	//	 this.setText(R.string.zero_dollars);
	 }
	 
	 @Override
	  public void onDraw(Canvas canvas) {
		 canvas.drawCircle(0, 0, radius, primaryPaint);
		 super.onDraw(canvas);
	 }

}
