package me.valour.bowls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

public class BowlView extends TextView{
	
	private Paint primaryPaint;
	private Paint blackPaint;
	private int radius;
	public User user;
	private boolean selected = false;
	private float originalX;
	private float originalY;
	private float offsetX;
	private float offsetY;
	
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
		 primaryPaint = null;
		 blackPaint =  new Paint(Paint.ANTI_ALIAS_FLAG);
		 blackPaint.setColor(Color.BLACK);
		 
		 originalX = 0;
		 originalY = 0;
		 
		 offsetX = 0;
		 offsetY = 0;
		 
		 this.setGravity(Gravity.CENTER);
	 }
	 
	 public void move(float x, float y){
		 if(originalX==0 && originalY==0){
			 originalX = x;
			 originalY = y;
			 setX(x);
			 setY(y);
		 } else {
			 ViewPropertyAnimator anim = animate();
			 anim.x(x - (float)radius);
			 anim.y(y - (float)radius);
			 anim.start();
			 originalX = x;
			 originalY = y;
		 }
	 }
	 
	 public void setAngle(double a){
		 
		 offsetY = -1*(float)(Math.sin(a)*4.0);
		 offsetX = (float)(Math.cos(a)*4.0);
		 
		 Log.d("vars", offsetX+" "+offsetY);
	 }
	 
	 public void resetPosition(){
		 setX(originalX);
		 setY(originalY);
	 }
	 
	 @Override
	 public void setId(int id){
		 super.setId(id);
		 user = new User(this);
	 }
	 
	 public void setColors(int color){
		 primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		 primaryPaint.setColor(color);
		 setTextColor(Kitchen.calculateTextColor(color));
	 }
	 
	 public int getColor(){
		 return primaryPaint.getColor();
	 }
	 
	 public void setRadius(int radius){
		 this.radius = radius;
		 
		 this.setMinimumHeight(2*radius);
		 this.setMinimumWidth(2*radius);
	 }
	 
	 public int getRadius(){
		 return radius;
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
		  double total = user.getTotal();
		  if(total>0.0){
			  String p = String.format("$ %.2f", total);
			  setText(p);
		  } else {
			  setText("");
		  }
		 } else {
			 setText("");
		 }
	 }
	 
	 @Override
	  public void onDraw(Canvas canvas) {
		 canvas.drawCircle(radius, radius, radius, blackPaint);
		 canvas.drawCircle(radius+offsetX, radius+offsetY, radius-4, primaryPaint);
		 super.onDraw(canvas);
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
		blackPaint.setColor(Color.WHITE);
		primaryPaint.setAlpha(50);
		this.invalidate();
	}
	
	public void unfade(){
		blackPaint.setColor(Color.BLACK);
		primaryPaint.setAlpha(255);
		this.invalidate();
	}
	
	public Paint getPrimaryPaint() {
		return primaryPaint;
	}

}
