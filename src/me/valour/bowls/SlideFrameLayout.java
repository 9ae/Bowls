package me.valour.bowls;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;


public class SlideFrameLayout extends FrameLayout {

	private float xFraction;
	
	public SlideFrameLayout(Context context) {
		super(context);
	}
	
	public SlideFrameLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}
	
	public SlideFrameLayout(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
	}
	
/*	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
	}
*/

	public float getXFraction(){
		int w = this.getWidth();
		if(w>0){
			return getX() / w;
		} else {
			return 100;
		}
	}
	
	public void setXFraction(float xFraction) {
        final int width = getWidth();
        Log.d("vars","sfw="+width);
        xFraction = (width > 0) ? (xFraction * width) : -9999;
        setX(xFraction);
    }
	

}
