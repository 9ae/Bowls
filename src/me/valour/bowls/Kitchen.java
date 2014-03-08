package me.valour.bowls;

import android.graphics.Color;

public class Kitchen {
	
	public final static int minBowls=2;
	public final static int maxBowls=19;
	public final static int minRadius = 50;
	
	public final static double tip = 15;
	public final static double tax = 8; 
	
	public static int assignColor(int i){
		float hue = (256/8)*((float)i%8);
		float sat = 255;
		if(i>=8){
		  int j = i/8;
		sat = 192-(256/4)*(float)j;
		}
		float[] hsv = {hue,sat,255};
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
	
}
