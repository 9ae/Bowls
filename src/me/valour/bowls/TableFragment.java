package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TableFragment extends Fragment {

	private AddBowlListener addBowlSpy;
	private SubBowlListener subBowlSpy;
	public TableView tableView;
	
	public TableFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_table, container, false);
		
		final Button add = (Button)view.findViewById(R.id.btn_addBowl);
		add.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				addBowlSpy.OnAddBowlListener();
			}});
		
		final Button sub = (Button)view.findViewById(R.id.btn_subBowl);
		sub.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				subBowlSpy.OnSubBowlListener();
				
			}});
		
		tableView = (TableView)view.findViewById(R.id.tableView);
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			addBowlSpy = (AddBowlListener)activity;
			subBowlSpy = (SubBowlListener)activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement OnNewItemAddedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}
	
	public interface AddBowlListener{
		public void OnAddBowlListener();
	}
	
	public interface SubBowlListener{
		public void OnSubBowlListener();
	}
}
