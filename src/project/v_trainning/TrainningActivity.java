package project.v_trainning;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import project.gps.*;

public class TrainningActivity extends Activity {
	/***
	 * @param mode, MYPREFS_SETTINGS
	 * Variables estaticas del modo en que se guardarán las preferencias
	 * */
	  final int mode = Activity.MODE_PRIVATE;
	  final String MYPREFS_SETTINGS = "MyPreferencesSettings";
	  SharedPreferences myPreferences, myPreferencesRecover;
	boolean isActivatedAjustes=false;
	TextView actividad;
	/***
	 * @param isTrainingActive 
	 * Variable que indica si esta activo el modo entrenamiento 
	 * */ 
	boolean isTrainingActive=false; //Agregado por Marlon
	/**
	 * @param TM
	 * Se utiliza para el monitoreo de la actividad
	 */ 
	TrainingMonitor TM; //Agregado por Marlon  
	
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
		TM=new TrainingMonitor((String) actividad.getText());
		//actividad.setText(myPreferencesRecover.getString("nombre_actividad", ""));
				

	}
	
	/**
	 * startTraining inicia o termina la función de entrenmiento
	 * @param view
	 */
	private void startTraining(View view){
		if (!isTrainingActive && isActivatedAjustes){
			isTrainingActive=true;
			TM.startTrainingMonitoring();
		}
		else{
			isTrainingActive=false;
			TM.showResults();
		}
		
	}
	
	/**
	 *
	 * @author MNM
	 * clase encargada del monitoreo de la actividad
	 */
	public class TrainingMonitor extends AsyncTask<Boolean,Integer,Boolean>{
		
		private String Actividad;
		private GPS gps;
		private boolean isActive;
		
		TrainingMonitor(String activ){
			Actividad=activ;
			gps = new GPS();
			isActive=false;
		}
		
		/**
	 	* startTrainingMonitoring ejecuta el monitoreo secuencial del recorrido
	 	*/
		public void startTrainingMonitoring(){
			isActive=true;
			while (isActive){
				
			}
		}
	
		/**
	 	* showResults muestra los resultados al volver a pulsar el boton play
	 	*/
		public void showResults(){
		
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			// TODO Auto-generated method stub
			return null;
		}	
	
	}
}
