package me.valour.bowls;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LineItemAdapter extends ArrayAdapter<LineItem> {

	  public LineItemAdapter(Context context, int resource, List<LineItem> items) {
		  super(context, resource, items);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
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
		  return null;
	  }
	
}
