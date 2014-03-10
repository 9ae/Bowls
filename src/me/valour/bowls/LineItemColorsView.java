package me.valour.bowls;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Paint;

public class LineItemColorsView extends View {
	
	ArrayList<Paint> palette;
	Paint black;

	public LineItemColorsView(Context context, AttributeSet ats, int ds){
		super(context, ats, ds);
		init();
	}
	
	public LineItemColorsView (Context context) {
	    super(context);
	    init();
	  }

	 public LineItemColorsView(Context context, AttributeSet attr) {
	    super(context, attr);
	    init();
	  }
	 
	 private void init(){
		 palette = new ArrayList<Paint>();
			black =  new Paint(Color.GRAY);
	 }

	@Override
	protected void onDraw(Canvas canvas) {
		int w = this.getWidth();
		float h = 10;
		if(palette.isEmpty()){
			canvas.drawRect(0, 0, w, 10,black);
		} else {
			float per = (float)w/(float)palette.size();
			float x = 0;
			for(Paint p : palette){
				canvas.drawRect(x, 0, x+per, h, p);
				x+= per;
			}
		}
	}
	 
}
