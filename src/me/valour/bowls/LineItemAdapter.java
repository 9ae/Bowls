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
import android.widget.LinearLayout;
import android.widget.TextView;

public class LineItemAdapter extends ArrayAdapter<LineItem> {

	private static LayoutInflater inflater=null;
	
	  public LineItemAdapter(Context context, int resource, List<LineItem> items) {
		  super(context, resource, items);
		  inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		  
		  LineItem li = this.getItem(position);
		  
		  View returnView;
		  
		  if(convertView==null){
			  returnView = inflater.inflate(R.layout.line_item, parent, false);
		  } else {
			  returnView = convertView;
		  }
		  
		  Button btnEdit = (Button) returnView.findViewById(R.id.editLineItem);
		  Button btnDel = (Button) returnView.findViewById(R.id.delLineItem);
		  TextView tvPrice = (TextView) returnView.findViewById(R.id.lineItemPrice);
		  LineItemColorsView vColors = (LineItemColorsView) returnView.findViewById(R.id.lineItemColors);
		 
		  tvPrice.setText(li.toString());
		  vColors.addColors(li.listUsers());
		  btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("vars","edit price");
			}
		});
		  
		  btnDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d("vars","delete line item");
			}
		});
		  
	/*    LinearLayout todoView;

	    TaskItem item = getItem(position);

	    String taskString = item.getTask();
	    Date createdDate = item.getCreated();
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	    String dateString = sdf.format(createdDate);

	    if (convertView == null) {
	      todoView = new LinearLayout(getContext());
	      String inflater = Context.LAYOUT_INFLATER_SERVICE;
	      LayoutInflater li;
	      li = (LayoutInflater)getContext().getSystemService(inflater);
	      li.inflate(resource, todoView, true);
	    } else {
	      todoView = (LinearLayout) convertView;
	    }

	    TextView dateView = (TextView)todoView.findViewById(R.id.rowDate);
	    TextView taskView = (TextView)todoView.findViewById(R.id.row);

	    dateView.setText(dateString);
	    taskView.setText(taskString);

	    return todoView; */
		  return returnView;
	  }
	
}
