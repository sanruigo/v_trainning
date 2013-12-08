package project.v_trainning;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class AjustesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajustes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajustes, menu);
		return true;
	}
	
	public void launchTranningActivity(View view){
		startActivity(new Intent(AjustesActivity.this,TrainningActivity.class));
		
	}
	

	public void launchResumeActivity(View view){
		startActivity(new Intent(AjustesActivity.this,ResumenActivity.class));
		
	}

}
