package project.v_trainning;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TrainningActivity extends Activity {
	
	  final int mode = Activity.MODE_PRIVATE;
	  final String MYPREFS_SETTINGS = "MyPreferencesSettings";
	  SharedPreferences myPreferences, myPreferencesRecover;
	boolean isActivatedAjustes=false;
	TextView actividad;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainning);

		System.out.println("Creando Trainning");
		
		createWidget();
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(isActivatedAjustes);
		if(isActivatedAjustes){
			showSavedPreferencesSettings();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trainning, menu);
		return true;
	}

	private void createWidget(){
		
		actividad=(TextView)findViewById(R.id.tViewTrainActAct);
	}
	

	/**
	 * Method to launch Settings
	 * */
	public void launchSettingActivity(View view){
		isActivatedAjustes=true;
		startActivity(new Intent(TrainningActivity.this,AjustesActivity.class));
		//isActivatedAjustes=true;
	}
	
	/**
	 * Method to launch Resume
	 * */
	public void launchResumeActivity(View view){
		startActivity(new Intent(TrainningActivity.this,ResumenActivity.class));
		
	}

	private void showSavedPreferencesSettings() {
		// TODO Auto-generated method stub
		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		System.out.println(myPreferencesRecover.getString("nombre_actividad", "").toString());
		actividad.setText(myPreferencesRecover.getString("nombre_actividad", "").toString());
		//actividad.setText(myPreferencesRecover.getString("nombre_actividad", ""));
				

	}
	

	
}
