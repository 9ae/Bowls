package me.valour.bowls;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * TODO: document your custom view class.
 */
public class CompassView extends View {
	
	private float bearing;
	private Paint markerPaint;
	private Paint textPaint;
	private Paint circlePaint;
	private String northString;
	private String eastString;
	private String southString;
	private String westString;
	private int textHeight;

	public CompassView(Context context) {
		super(context);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initCompassView();
	}

	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initCompassView();
	}

	private void initCompassView() {
		setFocusable(true);
		Resources r = this.getResources();

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(r.getColor(R.color.background_color));
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

		northString = r.getString(R.string.cardinal_north);
		eastString = r.getString(R.string.cardinal_east);
		southString = r.getString(R.string.cardinal_south);
		westString = r.getString(R.string.cardinal_west);

		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(r.getColor(R.color.text_color));

		textHeight = (int) textPaint.measureText("yY");

		markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		markerPaint.setColor(r.getColor(R.color.marker_color));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int mMeasuredWidth = getMeasuredWidth();
		int mMeasuredHeight = getMeasuredHeight();

		int px = mMeasuredWidth / 2;
		int py = mMeasuredHeight / 2;

		int radius = Math.min(px, py);
		
		canvas.drawCircle(px, py, radius, circlePaint);
		canvas.save();
		
		canvas.rotate(-bearing, px, py);
		
		int textWidth = (int) textPaint.measureText("W");
		int cardinalX = px - textWidth / 2;
		int cardinalY = py - radius + textHeight;

		// Draw the marker every 15 degrees and text every 45.
		for (int i = 0; i < 24; i++) {
			// Draw a marker.
			canvas.drawLine(px, py - radius, px, py - radius + 10, markerPaint);

			canvas.save();
			canvas.translate(0, textHeight);

			// Draw the cardinal points
			if (i % 6 == 0) {
				String dirString = "";
				switch (i) {
				case (0): {
					dirString = northString;
					int arrowY = 2 * textHeight;
					canvas.drawLine(px, arrowY, px - 5, 3 * textHeight,
							markerPaint);
					canvas.drawLine(px, arrowY, px + 5, 3 * textHeight,
							markerPaint);
					break;
				}
				case (6):
					dirString = eastString;
					break;
				case (12):
					dirString = southString;
					break;
				case (18):
					dirString = westString;
					break;
				}
				canvas.drawText(dirString, cardinalX, cardinalY, textPaint);
			}

			else if (i % 3 == 0) {
				// Draw the text every alternate 45deg
				String angle = String.valueOf(i * 15);
				float angleTextWidth = textPaint.measureText(angle);

				int angleTextX = (int) (px - angleTextWidth / 2);
				int angleTextY = py - radius + textHeight;
				canvas.drawText(angle, angleTextX, angleTextY, textPaint);
			}
			canvas.restore();

			canvas.rotate(15, px, py);
		}
		canvas.restore();

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
	
	public void setBearing(float _bearing){
		bearing = _bearing;
		
		sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
	}
	
	public float getBearing(){
		return bearing;
	}
	
	@Override
	public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent event) {
	  super.dispatchPopulateAccessibilityEvent(event);
	  if (isShown()) {
	    String bearingStr = String.valueOf(bearing);
	    if (bearingStr.length() > AccessibilityEvent.MAX_TEXT_LENGTH)
	      bearingStr = bearingStr.substring(0, AccessibilityEvent.MAX_TEXT_LENGTH);
	    
	    event.getText().add(bearingStr);
	    return true;
	  }
	  else
	    return false;
	}
}
