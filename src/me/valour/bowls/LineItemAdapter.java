package me.valour.bowls;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LineItemAdapter extends ArrayAdapter<LineItem> {

//	private static LayoutInflater inflater=null;
	
	  public LineItemAdapter(Context context, List<LineItem> items) {
		  super(context, R.layout.line_item, items);
		//  inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
		  LineItem li = this.getItem(position);
		  
		  View returnView;
		  
		  if(convertView==null){
			  LayoutInflater  inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  returnView = inflater.inflate(R.layout.line_item, parent, false);
		  } else {
			  returnView = convertView;
		  }
		  

		  TextView tvPrice = (TextView) returnView.findViewById(R.id.lineItemPrice);
	//	  LineItemColorsView vColors = (LineItemColorsView) returnView.findViewById(R.id.lineItemColors);
		 
		  tvPrice.setText(li.toString());
		//  vColors.addColors(li.listUsers());
		 
		  return returnView;
	  }
	
}
