package me.valour.bowls;

import android.graphics.Color;
import android.util.Log;

public class Kitchen {
	
	public final static int minBowls=2;
	public final static int maxBowls=20;
	public final static int minRadius = 50;
	
	public static int assignColor(int i){
		float hue = 60*((float)(i%6+1));
		float sat = 1;
		float bright = 1;
		if(i<=6){
			sat = 1;
			bright = 1;
		}
		else if(i<=12){
			hue -= 30; 
		} else if( i<=18){
			sat= (float)0.3;
		} else {
			hue -= 30;
			sat = (float)0.3;
		}
		float[] hsv = {hue,sat,bright};
		return Color.HSVToColor(hsv);
	}
	
	public static float angleBetween(float x1, float y1, float cx, float cy, float x2, float y2){
		double v1x = (double)(x1-cx);
		double v1y = (double)(y1-cy);
		double v2x = (double)(x2-cx);
		double v2y = (double)(y2-cx);
		double dot = v1x*v2x + v1y*v2y;
		double scalar = Math.sqrt( (v1x*v1x + v1y*v1y)*(v2x*v2x + v2y*v2y) );
		return (float) Math.acos(dot/scalar);
	}
	
	private static int colorDifference(int r0, int g0, int b0, int r1, int g1, int b1){
		  return (Math.max(r0,r1) - Math.min(r0,r1)) +
		  (Math.max(g0,g1) - Math.min(g0,g1)) +
		  (Math.max(b0,b1) - Math.min(b0,b1));
		}
		 
	public static int calculateTextColor(int color){
		int r = Color.red(color);
		  int g = Color.green(color);
		  int b = Color.blue(color);
		  
		 double brightness  =  Math.sqrt( .299*Math.pow(r, 2)+ .587*Math.pow(g, 2) + .114*Math.pow(b, 2));
				  
		  if(brightness<180){
		    return Color.WHITE;
		  } else {
		    return Color.BLACK;
		  }
	}
	
	public static double roundSigFig(double input, int f){
		double a = input*Math.pow(10, f);
		return Math.round(a)*Math.pow(10, -1*f);
	}
	
}
