package me.valour.bowls;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BowlsGroup extends FrameLayout {

	int mMeasuredWidth;
	int mMeasuredHeight;
	float centerX=0;
	float centerY=0;
	int tableRadius;
	int bowlRadius;
	boolean measuredScreen;
	boolean selectReady = false;
	
	FrameLayout.LayoutParams defaultParams;
	BowlSelectListener bowlSelect;
	NewBowlListener newBowlSpy;
	AddBowlListener addBowlAgent;
	RemoveBowlListener rmBowlAgent;

	LinkedList<BowlView> bowls;
	BowlView newBowl;
	int bowlsIdCounter = 1;

	public BowlsGroup(Context context) {
		super(context);
		init();
	}

	public BowlsGroup(Context context, AttributeSet ats, int ds) {
		super(context, ats, ds);
		init();
	}

	public BowlsGroup(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	
	@Override
	protected void onLayout (boolean changed, int left, int top, int right, int bottom){
		measureView();	
		
		double angleDelta = Math.PI*2.0/bowls.size();
		double topX = 0;
		double topY = -1.0*tableRadius;
		
		int i= 0;
		 for(BowlView bowl: bowls){
			bowl.bringToFront();
			double angle = angleDelta*i;
			double px = Math.cos(angle)*topX - Math.sin(angle)*topY + centerX;
			double py = Math.sin(angle)*topX - Math.cos(angle)*topY + centerY;
			bowl.move((float)px, (float)py);
			i++;
		 }
		 
		 super.onLayout(changed, left, top, right, bottom);
	}

	private void init() {
		bowlSelect = new BowlSelectListener();
		newBowlSpy = new NewBowlListener();
		measuredScreen = false;
		defaultParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setClickable(true);
		setFocusable(true);

		bowls = new LinkedList<BowlView>();
		for (int i = 1; i <= Kitchen.minBowls; i++) {
			BowlView bowl = new BowlView(this.getContext());
			bowl.setId(bowlsIdCounter);
			bowl.setColors(Kitchen.assignColor(i+1));
			bowls.add(bowl);
			bowlsIdCounter++;
			this.addView(bowl, defaultParams);
			bowl.setOnTouchListener(bowlSelect);
		}
		
		newBowl = getNewBowl();
	}
	
	private BowlView getNewBowl(){
		int i = bowls.size()+1;
		BowlView bowl = new BowlView(this.getContext());
		bowl.setColors(Kitchen.assignColor(i));
		if(measuredScreen){
			bowl.setRadius(bowlRadius);
		}
		this.addView(bowl, defaultParams);
		bowl.setOnTouchListener(newBowlSpy);
		bowl.setOnDragListener(newBowlSpy);
		bowl.bringToFront();
		return bowl;
	}
	
	
	public void measureView(){
		if(measuredScreen){
			return;
		} else {
			mMeasuredWidth = getMeasuredWidth();
			mMeasuredHeight = getMeasuredHeight();		
			int cx = mMeasuredWidth / 2;
			int cy = mMeasuredHeight / 2;		
			tableRadius = Math.min(cy, cy);
			centerX = (float)cx;
			centerY = (float)cy;
			
			double q = ((double) tableRadius * 2.0 * Math.PI)
					/ (double) Kitchen.maxBowls;
			bowlRadius = (int) (q / 2.0);
			tableRadius -= bowlRadius;
			Log.d("vars", String.format("bowl radius=%d", bowlRadius));
			Log.d("vars", String.format("table radius=%d", tableRadius));
			Log.d("vars", String.format("cx=%f \t cy=%f", centerX, centerY));
			newBowl.setRadius(bowlRadius);
			for(BowlView bv: bowls){
				bv.setRadius(bowlRadius);
			}
			measuredScreen = true;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);
		int d = Math.min(measuredWidth, measuredHeight);
		setMeasuredDimension(d, d);

	}

	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			result = 500;
		} else {
			result = specSize;
		}
		return result;
	}
	
	public void addBowlAt(int index){
		newBowl.setId(bowlsIdCounter);
		bowls.add(index, newBowl);
		bowlsIdCounter++;
		newBowl.setOnDragListener(null);
		newBowl.setOnTouchListener(null);
		newBowl.setOnTouchListener(bowlSelect);
		addBowlAgent.addUser(newBowl.user);
		newBowl = getNewBowl();
	}
	
	public void removeBowl(final BowlView bowl){
		ViewPropertyAnimator ani = bowl.animate();
		ani.alpha(0).setDuration(2000);
		ani.withEndAction(new Runnable(){
			@Override
			public void run() {
				bowls.remove(bowl);
				refreshBowls();
			}	
		});
		ani.start();
	}

	public void refreshBowls() {
		 for(BowlView bv: bowls){ 
			bv.formatText(); 
			 bv.invalidate();
		 }
	}

	public List<Integer> getBowlViewIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (BowlView bw : bowls) {
			ids.add(bw.getId());
		}
		return ids;
	}

	public List<User> getBowlUsers() {
		ArrayList<User> users = new ArrayList<User>();
		for (BowlView bw : bowls) {
			users.add(bw.user);
		}
		return users;
	}

	public void bowlsFocus(boolean unfade) {
		for (BowlView bv : bowls) {
			if (unfade) {
				bv.unfade();
			} else {
				bv.fade();
			}
		}
	}
	
	
	public void clearSelection(){
		bowlSelect.selected.clear();
		for(BowlView bv: bowls){
			bv.setSelected(false);
		}
	}
	
	public void readyBowlSelect(){
		selectReady = true;
		clearSelection();
		bowlsFocus(false);
	}
	
	public void stopBowlSelect(){
		selectReady = false;
		clearSelection();
		bowlsFocus(true);
	}
	
	public List<User> getSelectedUsers(){
		return bowlSelect.selected;
	}

	
	public void attachBowlAgents(Activity activity){
		addBowlAgent = (AddBowlListener)activity;
		rmBowlAgent = (RemoveBowlListener)activity;
	}
	
	private class BowlSelectListener implements OnTouchListener{

		public List<User> selected;
		private float prevX;
		private float prevY;
		private float dx;
		private float dy;
		private boolean bowlMoved=false;
		
		public BowlSelectListener(){
			selected = new ArrayList<User>();
			dx = 0;
			dy = 0;
		}
		
		public boolean deleteBowl(BowlView bowl){
			if(rmBowlAgent.removeUserConfirm(bowl)){
				removeBowl(bowl);
				Log.d("vars","delete this bowl");
			}
			return true;
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent move) {
			BowlView bv = (BowlView)v;
			int action = move.getAction();
			if(selectReady){
				if(action==MotionEvent.ACTION_DOWN){
					if(bv.toggleSelected()){
						selected.remove(bv.user);
					} else {
						selected.add(bv.user);
					}
				} return false;
			} else if (bowls.size()>2) {
				switch(action){
				case MotionEvent.ACTION_DOWN:
					prevX = bv.getX() + (float)bv.getRadius();
					prevY = bv.getY() +  (float)bv.getRadius(); 
					bowlMoved = false;
					break;
				case MotionEvent.ACTION_MOVE:
					bowlMoved = true;
					dx = move.getX();
					dy = move.getY();
					bv.setX(prevX+dx);
					bv.setY(prevY+dy);
					break;
				case MotionEvent.ACTION_UP:
					if(bowlMoved && dx>10 && dy>10){
						float x = prevX+dx;
						float y = prevY+dy;
						double angle = Kitchen.angleBetween(centerX, centerY, prevX, prevY, x, y);
						Log.d("vars", String.format("a:(%f, %f) \t b:(%f, %f) \t c:(%f, %f)", centerX, centerY, prevX, prevY, x, y));
						Log.d("vars","angle="+Math.toDegrees(angle));
						if(Math.abs(angle)>(Math.PI/2.0)){
							deleteBowl(bv);
						} else {
							Log.d("vars","keep this bowl");
							bv.resetPosition();
						}
					}  else {
						bv.resetPosition();
					}
					break;
				}
				return true;
			}
			else {
				return false;
			}
		}
		
	}
	
	public class NewBowlListener implements OnTouchListener, OnDragListener{
		private float prevX;
		private float prevY;
		
		public boolean testAdd(float x, float y){
			float center_x = (float)centerX;
			float center_y = (float)centerY;
			float radius = (float)(tableRadius);
			return Math.pow(x - center_x,2) + Math.pow(y - center_y,2) <= (radius*radius);
		}
		
		public double findAngle(float x, float y){
			double angle = 0;
			float px = bowls.getFirst().getX();
			float py = bowls.getFirst().getY();
			float cx = (float)centerX;
			float cy = (float)centerY;
			angle = (Math.atan2(x - cx,y - cy)- Math.atan2(px- cx,py- cy));
			if(angle<0){
				angle = Math.PI*2 - Math.abs(angle);
			}
			Log.d("vars","angle="+angle);
			return angle;
		}

		@Override
		public boolean onDrag(View v, DragEvent event) {
			BowlView bv = (BowlView)event.getLocalState();
			float x = event.getX();
			float y = event.getY();
	    	if(testAdd(x,y)){
	    		Log.d("vars","in bounds");
	    	} else {
	    		Log.d("vars","out bounds");
	    	}
			switch (event.getAction()) {
		    case DragEvent.ACTION_DRAG_STARTED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_ENTERED:
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DRAG_LOCATION:
		    	break;
		    case DragEvent.ACTION_DRAG_EXITED:        
		        //no action necessary
		        break;
		    case DragEvent.ACTION_DROP:

		        break;
		    case DragEvent.ACTION_DRAG_ENDED:

		        break;
		    default:
		        break;
		} 
			
	   /* 	if(testAdd(x,y)){
				double angle = findAngle(x,y);
				double delta = Math.PI*2.0/bowls.size();
				int index = (int)Math.round(angle/delta);
				addBowlAt(index);
			} else {
				bv.setX(0);
				bv.setY(0);
			} */
		return true;
		}

		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
			/*	ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0); */
				prevX = event.getX();
				prevY = event.getY(); 
			    return true;
			} else if(event.getAction() == MotionEvent.ACTION_MOVE){
				float x = event.getX();
				float y = event.getY();
				if(x<0 || x>mMeasuredWidth){
					return false;
				}
				if(y<0 || y>mMeasuredHeight){
					return false;
				}
			/*	ViewPropertyAnimator ani = view.animate();
				ani.x(x);
				ani.y(y);
				ani.setDuration(100);
				ani.start(); */
				BowlView bv = (BowlView)view;
				bv.setX(x);
				bv.setY(y);
				
				prevX = x;
				prevY = y;
				
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP){
				float x = event.getX();
				float y = event.getY();
				if(testAdd(x,y)){
				//	view.animate().cancel();
					double angle = findAngle(x,y);
					double delta = Math.PI*2.0/bowls.size();
					int index = (int)Math.round(angle/delta);
					addBowlAt(index);
				} else {
					ViewPropertyAnimator ani = view.animate();
					ani.x(0);
					ani.y(0);
					ani.start();
				}
				return true;
			} 
			else {
			    return false;
			}
		}
		}

	public interface AddBowlListener{
		public void addUser(User user);
	}
	
	public interface RemoveBowlListener{
		public boolean removeUserConfirm(BowlView bv);
		
		public void removeUserDo(User user);
	}

}
