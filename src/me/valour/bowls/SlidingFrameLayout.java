package me.valour.bowls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class SlidingFrameLayout extends FrameLayout {

	public SlidingFrameLayout(Context context) {
		super(context);
	}
	
	public SlidingFrameLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public SlidingFrameLayout(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
	}
	
	public float getXFraction(){
		int w = this.getWidth();
		if(w>0){
			return getX() / w;
		} else {
			return 1;
		}
	}
	
	public void setXFraction(float xFraction) {
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }
	
	public float getYFraction(){
		int h = this.getHeight();
		if(h>0){
			return getY() / h;
		} else {
			return 1;
		}
	}
	
	public void setYFraction(float yFraction) {
        final int h = this.getHeight();
        setY((h > 0) ? (yFraction * h) : -9999);
    }
	
}
