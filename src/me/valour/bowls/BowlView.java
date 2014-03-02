package me.valour.bowls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

public class BowlView extends TextView implements OnTouchListener{
	
	private Paint primaryPaint;
	private Paint textPaint;
	private int radius;
	public User user;
	private boolean selected = false;
	
	public BowlView(Context context, AttributeSet ats, int ds){
		super(context, ats, ds);
		this.setClickable(true);
	}
	
	public BowlView (Context context) {
	    super(context);
	    this.setClickable(true);
	  }

	 public BowlView(Context context, AttributeSet attr) {
	    super(context, attr);
	    this.setClickable(true);
	  }
	 
	 @Override
	 public void setId(int id){
		 super.setId(id);
		 user = new User(id);
	 }
	 
	 public void init(int color, int radius){
		 primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		 primaryPaint.setColor(color);
		 textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
		 textPaint.setColor(Color.BLACK);
		 textPaint.setTextSize((float)20.5);
		 this.radius = radius;
	 }
	 
	 @Override
	  public void onDraw(Canvas canvas) {
		 canvas.drawCircle(0, 0, radius, primaryPaint);
		 String p =String.format("$ %.2f", user.getTotal());
		 canvas.drawText(p, -1*radius, 0, textPaint);
		// super.onDraw(canvas);
	 }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		BowlView bv = (BowlView)v;
		bv.toggleSelected();
		Log.d("vars", bv.getId()+" clicked");
		return true;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean toggleSelected(){
		if(selected){
			selected = false;
			fade();
			return true;
		} else {
			selected = true;
			unfade();
			return false;
		}
	}
	 
	
	public void fade(){
		primaryPaint.setAlpha(100);
	}
	
	public void unfade(){
		primaryPaint.setAlpha(255);
	}
}
