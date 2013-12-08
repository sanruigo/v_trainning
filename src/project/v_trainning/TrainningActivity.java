package project.v_trainning;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class TrainningActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainning);

		System.out.println("Creando Trainning");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trainning, menu);
		return true;
	}


	/**
	 * Method to launch Settings
	 * */
	public void launchSettingActivity(View view){
		startActivity(new Intent(TrainningActivity.this,AjustesActivity.class));
		
	}
	

	public void launchResumeActivity(View view){
		startActivity(new Intent(TrainningActivity.this,ResumenActivity.class));
		
	}

	
}
