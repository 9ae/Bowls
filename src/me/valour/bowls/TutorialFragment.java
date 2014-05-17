package me.valour.bowls;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TutorialFragment extends Fragment {

	private TutorialCloseAgent agent;
	private ImageView img;
	private int state = 0;
	
	public TutorialFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_tutorial, container, false);
		img = (ImageView) view.findViewById(R.id.img_tutorial);
		
		view.setOnTouchListener(new View.OnTouchListener() {	
			private float startX;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					startX = event.getX();
					return true;
				}
				else {
					if(event.getX()<startX){
						if(event.getAction()==MotionEvent.ACTION_UP){
							updateState();
						}
						return true;
					} else {
						return false;
					}
				}
			}
		});
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
	
	public void updateState(){
		switch(state){
		case 0:
			img.setImageResource(R.drawable.tutorial2);
			state = 1;
			toggleImages();
			break;
		default:
			agent.closeTutorialFragment();
			break;
		}
	}
	
	public void toggleImages(){
		img.postDelayed(new Runnable(){
			@Override
			public void run() {
				if(state==1){
					img.setImageResource(R.drawable.tutorial3);
					state = 2;
				}
			}
			
		}, 3000);
	}
	
	public interface TutorialCloseAgent{
		public void closeTutorialFragment();
	}

}
