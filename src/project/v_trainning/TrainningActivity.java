package project.v_trainning;

import android.location.LocationManager;

import java.util.Vector;

//import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import project.gps.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

public class TrainningActivity extends Activity {
	/***
	 * @param mode, MYPREFS_SETTINGS
	 * Variables estaticas del modo en que se guardar�n las preferencias
	 * */
	  final int mode = Activity.MODE_PRIVATE;
	  final String MYPREFS_SETTINGS = "MyPreferencesSettings";
	  SharedPreferences myPreferences, myPreferencesRecover;
	boolean isActivatedAjustes=false;
	TextView txtActividad;
	TextView txtDistancia;
	Button btnTrainActStart;
	
	
	GPS gps;
	
	
	
/*
 * Variables para almacenar lo que devuelve el GPS
 */
	public Vector<Long> Tiempo=new Vector();
	public Vector<Float> Velocidad=new Vector();
	public Vector<Float> Distancia=new Vector();
	public int index=0;
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
	
	
	/**
	 * Timer
	 * 
	 * 
	 * */
	private long startTime = 0L;
	
	private Handler customHandler = new Handler();
	
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	private TextView txtTiempo;
	private Runnable updateTimerThread;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainning);

		System.out.println("Creando Trainning");
		gps = new GPS(this);
		timerCore();
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
		
		txtActividad=(TextView)findViewById(R.id.tViewTrainActAct);
		txtDistancia=(TextView)findViewById(R.id.tViewTrainActDist);
		txtTiempo=(TextView)findViewById(R.id.txtTiempo);
		btnTrainActStart=(Button)findViewById(R.id.btnTrainActStart);
		
		btnTrainActStart.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				startTraining();
				
				
				if(!isTrainingActive){
					System.out.println(isTrainingActive);
					startTime();
					isTrainingActive=true;
				}else{
					stopTime();
					isTrainingActive=false;
					
				}
				
			}
		});

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
		txtActividad.setText(myPreferencesRecover.getString("nombre_actividad", "").toString());
		TM=new TrainingMonitor((String) txtActividad.getText());
		//actividad.setText(myPreferencesRecover.getString("nombre_actividad", ""));
				

	}
	
	/**
	 * startTraining inicia o termina la funci�n de entrenmiento
	 * @param view
	 */
	public void startTraining(){
		System.out.println("trainingactive="+isTrainingActive);
		if (!isTrainingActive){
			System.out.println("ActivedAjustes="+isActivatedAjustes);
			if (isActivatedAjustes){
				System.out.println("Verificando si GPS esta activo");
				if (gps.isGPSactive()){
					isTrainingActive=true;
					TM.run();
					
					//startTime();
					
				}else{
					Toast.makeText(getApplicationContext(), R.string.msgNoGPS, Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), R.string.msgNoAjustes, Toast.LENGTH_LONG).show();
			}
		}
		else{
			isTrainingActive=false;
			//stopTime();
			TM.showResults();
		}
		
	}
	
	/**
	 *
	 * @author MNM
	 * clase encargada del monitoreo de la actividad
	 */
	public class TrainingMonitor extends Thread{
		
		private String Actividad;
		private boolean isActive;
		
		TrainingMonitor(String activ){
			Actividad=activ;
			isActive=false;
			Tiempo.clear();
			Velocidad.clear();
			Distancia.clear();
			Tiempo.add(Long.parseLong("0"));
			Velocidad.add(Float.parseFloat("0"));
			Distancia.add(Float.parseFloat("0"));
			index=0;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			startTrainingMonitoring();
		}

		@Override
		public synchronized void start() {
			// TODO Auto-generated method stub
			super.start();
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			super.interrupt();
			isActive=false;
		}

		/**
	 	* startTrainingMonitoring ejecuta el monitoreo secuencial del recorrido
	 	*/
		public void startTrainingMonitoring(){
			isActive=true;
			while (isActive){
				try {
					sleep(5000);
					index=index+1;
					Tiempo.add((long) (index*5));
					gps.localizador();
					Velocidad.add(gps.getLastSpeed());
					Distancia.add(gps.getLastDistance());
					//txtTiempo.setText(String.valueOf(Tiempo.get(index)));
					txtDistancia.setText(String.valueOf(Distancia.get(index)));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	
		/**
	 	* showResults muestra los resultados al volver a pulsar el boton play
	 	*/
		public void showResults(){
			this.interrupt();
		
			
		}


	
		
	}
	
	
	
	/**
	 * Timer
	 * */
	
	
	private void startTime(){
		
		startTime = SystemClock.uptimeMillis();
		customHandler.postDelayed(updateTimerThread, 0);
		
	}
	
	private void stopTime(){
		timeSwapBuff += timeInMilliseconds;
		customHandler.removeCallbacks(updateTimerThread);
	}

	
	private void timerCore(){
		updateTimerThread = new Runnable() {

			public void run() {
				
				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
				
				updatedTime = timeSwapBuff + timeInMilliseconds;

				int secs = (int) (updatedTime / 1000);
				int mins = secs / 60;
				secs = secs % 60;
				int milliseconds = (int) (updatedTime % 1000);
				System.out.println("" + mins + ":"
						+ String.format("%02d", secs) + ":"
						+ String.format("%03d", milliseconds));
				
				txtTiempo.setText("" + mins + ":"
						+ String.format("%02d", secs) + ":"
						+ String.format("%03d", milliseconds));
				customHandler.postDelayed(this, 0);
			}

		};
		
		
	}
	
	
	
}
