package me.valour.bowls;

import android.graphics.Color;

public class Kitchen {
	
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
}
