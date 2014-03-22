package me.valour.bowls;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class LineItemsFragment extends ListFragment {
	
	private Bill bill;
	
	// TODO: Rename and change types of parameters
	public static LineItemsFragment newInstance() {
		LineItemsFragment fragment = new LineItemsFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public LineItemsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
	}


	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_lineitems,container,false);
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setListAdapter(new LineItemAdapter(getActivity(), bill.lineItems));
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			bill = ((TableActivity)activity).getBill();
			
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

}
