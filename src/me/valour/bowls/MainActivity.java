package me.valour.bowls;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		((Button)findViewById(R.id.btn_split_line)).setEnabled(true);
		((Button)findViewById(R.id.btn_split_equally)).setEnabled(true);
	}
	
	private void launchTableActivity(boolean splitEqually){
		Intent intent = new Intent(this, TableActivity.class);
		intent.putExtra("splitEqually", splitEqually);
		startActivity(intent);
	}
	
	public void onSplitEqually(View view){
		Button btnOther = (Button)findViewById(R.id.btn_split_line);
		btnOther.setEnabled(false);
		launchTableActivity(true);
	}
	
	public void onSplitByItem(View view){
		Button btnOther = (Button)findViewById(R.id.btn_split_equally);
		btnOther.setEnabled(false);
		launchTableActivity(false);
	}

}
