package me.valour.bowls;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PreludeFragment extends Fragment {
	
	View view;
	
	public PreludeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view =  inflater.inflate(R.layout.fragment_prelude, container, false);
		return view;
	}
	
	
	public void enable(boolean enableViews){
		view.findViewById(R.id.btn_presets).setEnabled(enableViews);
		view.findViewById(R.id.btn_split_equally).setEnabled(enableViews);
		view.findViewById(R.id.btn_split_line).setEnabled(enableViews);
	}

}
