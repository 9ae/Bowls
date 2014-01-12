package me.valour.bowls;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View.MeasureSpec;

public class TableSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder holder;
	private ArtistThread thread;
	private boolean hasSurface;
	
	private Paint circlePaint;
	private Paint textPaint;
	private int bowlsCount=2;
	
	public TableSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public TableSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public TableSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		holder = getHolder();
	    holder.addCallback(this);
	    hasSurface = false;
	    
	    Resources r = this.getResources();
	    circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(r.getColor(R.color.background_color));
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(r.getColor(R.color.text_color));
		textPaint.setTextSize(72);
		
		thread = new ArtistThread();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	/*	if (thread != null)
		      mySurfaceViewThread.onWindowResize(w, h); */
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		hasSurface = true;
	    if (thread != null){
	     thread.start();	
	    }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	    pause();
	}
	
	public void resume() {
	    // Create and start the graphics update thread.
	    if (thread == null) {
	     thread= new ArtistThread();
	      if (hasSurface == true)
	        thread.start();
	    }
	  }

	  public void pause() {
	    // Kill the graphics update thread
	    if (thread != null) {
	      thread.requestExitAndWait();
	      thread = null;
	    }
	  }
	  
	  public void setBowlsCount(int count){
		  bowlsCount = count;
		  circlePaint.setColor(Kitchen.assignColor(bowlsCount));
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
			    result = 500;
			  } else {
			    // As you want to fill the available space
			    // always return the full available bounds.
			    result = specSize;
			  }
			  return result;
		}

	class ArtistThread extends Thread{
		private boolean done;
		
		ArtistThread(){
			super();
			done = false;
		}
		
		@Override
		public void run(){
			Log.d("fun","started thread");
			SurfaceHolder surfaceHolder = holder;
			while(!done){
				Canvas canvas = surfaceHolder.lockCanvas();
				initDraw(canvas);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
		
		public void initDraw(Canvas canvas){
			canvas.drawPaint(circlePaint);
			int mMeasuredWidth = getMeasuredWidth();
			int mMeasuredHeight = getMeasuredHeight();

			int px = mMeasuredWidth / 2;
			int py = mMeasuredHeight / 2;
			canvas.drawText(bowlsCount+"", px, py, textPaint);
			int radius = Math.min(px, py);
			
			//canvas.drawCircle(px, py, radius, circlePaint);
			//canvas.save();
		}
		
		public void requestExitAndWait() {
		      // Mark this thread as complete and combine into
		      // the main application thread.
		      done = true;
		      try {
		        join();
		      } catch (InterruptedException ex) { }
		    }
		
	}
}
