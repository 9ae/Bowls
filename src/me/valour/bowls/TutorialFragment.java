package me.valour.bowls;

import me.valour.bowls.TableFragment.ButtonAgent;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TutorialFragment extends Fragment {

	TutorialCloseAgent agent;
	ImageView img;
	
	public TutorialFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_tutorial, container, false);
		img = (ImageView) view.findViewById(R.id.img_tutorial);
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			agent = (TutorialCloseAgent)activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString()+" must implement OnNewItemAddedListener");
		}
	}
	
	public interface TutorialCloseAgent{
		public void closeTutorialFragment();
	}
	
}
