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
		init();
	}
	
	public BowlView (Context context) {
	    super(context);
	    init();
	  }

	 public BowlView(Context context, AttributeSet attr) {
	    super(context, attr);
	    init();
	  }
	 
	 private void init(){
		 setClickable(true);
		 this.setFocusable(true);
		 setRadius(Kitchen.minRadius);
		 user = null;
		 
		 primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		 primaryPaint.setColor(Color.GREEN);
		 textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
		 textPaint.setColor(Color.BLACK);
		 textPaint.setTextSize((float)20.5);
		 
		 setOnTouchListener(this);
	 }
	 
	 @Override
	 public void setId(int id){
		 super.setId(id);
		 user = new User(id);
	 }
	 
	 public void setColors(int color){
		 primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		 primaryPaint.setColor(color);
		 textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
		 textPaint.setColor(Color.BLACK);
		 textPaint.setTextSize((float)20.5);
	 }
	 
	 public void setRadius(int radius){
		 this.radius = radius;
		 
		 this.setMinimumHeight(2*radius);
		 this.setMinimumWidth(2*radius);
	 }
	 
	 @Override
	 public void setX(float x){
		 float newX = x - (float)radius;
		 super.setX(newX);
	 }
	 
	 @Override
	 public void setY(float y){
		 float newY = y - (float)radius;
		 super.setY(newY);
	 }
	 
	 public void formatText(){
		 if(user!=null){
		  String p = String.format("$ %.2f", user.getTotal());
		  setText(p);
		 } else {
			 setText(R.string.zero_dollars);
		 }
	 }
	 
	 @Override
	  public void onDraw(Canvas canvas) {
		 canvas.drawCircle(radius, radius, radius, primaryPaint);
	/*	 String p = "0.00";
		 if(user!=null){
		  p = String.format("$ %.2f", user.getTotal());
		 }
		 canvas.drawText(p, 0, 0, textPaint); */
		 super.onDraw(canvas);
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
		this.invalidate();
	}
	
	public void unfade(){
		primaryPaint.setAlpha(255);
		this.invalidate();
	}
}
