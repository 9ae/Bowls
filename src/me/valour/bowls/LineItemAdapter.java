package me.valour.bowls;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class LineItemAdapter extends ArrayAdapter<LineItem> {
	
	private LineItemAgent agent;
	
	  public LineItemAdapter(Context context, List<LineItem> items, BillFragment bf) {
		  super(context, R.layout.line_item, items);
		  agent = (LineItemAgent) bf;
	  }


	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
		  final LineItem li = this.getItem(position);
		  
		  View returnView;
		  
		  if(convertView==null){
			  LayoutInflater  inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			  returnView = inflater.inflate(R.layout.line_item, parent, false);
		  } else {
			  returnView = convertView;
		  }  

		  TextView tvPrice = (TextView) returnView.findViewById(R.id.lineItemPrice);
		  ImageButton delButton = (ImageButton) returnView.findViewById(R.id.lineDelete);
		  ImageButton editButton = (ImageButton) returnView.findViewById(R.id.lineEdit);
		 
		  final int index = position;
		  
		  tvPrice.setText(li.toString());
		  delButton.setEnabled(false);
		  delButton.setOnClickListener( new View.OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				agent.deleteLI(index);
			}
		}); 
		  
		  editButton.setEnabled(false);
		  editButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				agent.editLI(li);
			}
		});
		 
		  return returnView;
	  }
	  
	  public interface LineItemAgent{
		  public void deleteLI(int position);
		  public void editLI(LineItem li);
	  }
	
}
