package project.v_trainning;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.Vector;

//import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

public class TrainningActivity extends Activity implements LocationListener{
	/***
	 * @param mode, MYPREFS_SETTINGS
	 * Variables estaticas del modo en que se guardar�n las preferencias
	 * */
	  final int mode = Activity.MODE_PRIVATE;
	  final String MYPREFS_SETTINGS = "MyPreferencesSettings";
	  final String MYPREFS_TRAINING = "MyPreferencesTrainning";
	  SharedPreferences myPreferences, myPreferencesRecover,myPreferencesRecoverTrainning;
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
	public Boolean isStopTimer=false;
	
	
	/***
	 * 
	 * GPS
	 * 
	 * */
	
	/***
	 * @param E
	 *            array de strings con posibles estados del gps
	 */
	private static final String[] E = { "fuera de servicio",
			"temporalmente no disponible ", "disponible" };

	/***
	 * @param manejador
	 *            manejador del gps
	 */
	private LocationManager manejador;

	/***
	 * @param salida
	 *            para mostrar la salida en el textview
	 */
	//private TextView salida;

	/***
	 * @param pointsCuantity
	 *            para llevar orden de obtencion de las coordenadas gps
	 */
	private int pointsCuantity;

	/***
	 * @param velocity
	 *            almacena la velocidad en metros/segundo en cada punto de
	 *            coordenadas obtenido
	 */
	private float velocity;

	/***
	 * @param latitude
	 *            almacena la latitud en grados de cada punto de coordenadas
	 *            obtenido
	 */
	private double latitude;

	/***
	 * @param longitude
	 *            almacena la longitud en grados de cada punto de coordenadas
	 *            obtenido
	 */
	private double longitude;

	/***
	 * @param time
	 *            almacena el tiempo UTC en milisegundos desde el 1 de enero de
	 *            1970 en cada punto de coordenadas obtenido
	 */
	private long time;

	/***
	 * @param altitude
	 *            almacena la altura en metros desde el nivel del mar en cada
	 *            punto de coordenadas obtenido
	 */
	private double altitude;

	/***
	 * @param distance
	 *            almacena la distancia en metros entre el punto actual y el
	 *            anterior
	 */
	private float distance;

	/***
	 * @param lastLoc
	 *            almacena las coordenadas del punto anterior para poder
	 *            calcular la distancia
	 */
	private Location lastLoc;

	/***
	 * @param minTime
	 *            marca el tiempo minimo en milisegundos entre obtencion de
	 *            coordenadas
	 */
	private int minTime;

	/***
	 * @param minDist
	 *            marca la distancia minima en metros para la obtencion de
	 *            coordenadas
	 */
	private int minDist;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainning);

		System.out.println("Creando Trainning");
		gps = new GPS(this);
		

		
		createWidget();
		
		
		
	}

	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		savePreferences();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//System.out.println(isActivatedAjustes);
		//if(isActivatedAjustes){
			showSavedPreferencesSettings();
		//}
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
				//startTraining();
				System.out.println("click start");
				
				if(!isTrainingActive){
					System.out.println(isTrainingActive);
					
					isTrainingActive=true;
					timerCore();
					startTime();
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
	
	
	private void savePreferences() {
		// TODO Auto-generated method stub
		myPreferences = getSharedPreferences(MYPREFS_TRAINING, mode);
		Editor myEditor = myPreferences.edit();
		//myEditor.putString("estadoTimer", isActivadoTimer);
		myEditor.putBoolean("estadoTimer", isTrainingActive);
		myEditor.putBoolean("estadoStopTimer", isStopTimer);
		myEditor.commit();	
		stopTime();
	}
	private void savePreferencesTime(long time){
		myPreferences = getSharedPreferences(MYPREFS_TRAINING, mode);
		Editor myEditor = myPreferences.edit();
		//myEditor.putString("estadoTimer", isActivadoTimer);
		myEditor.putLong("Timer", time);
		System.out.println("save:"+time);
		myEditor.commit();	
	}
	private void savePreferencesTime(String time){
		myPreferences = getSharedPreferences(MYPREFS_TRAINING, mode);
		Editor myEditor = myPreferences.edit();
		//myEditor.putString("estadoTimer", isActivadoTimer);
		myEditor.putString("TimerString", time);
		//System.out.println("save:"+time);
		myEditor.commit();	
	}

	private void showSavedPreferencesSettings() {
		// TODO Auto-generated method stub
		
		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		System.out.println(myPreferencesRecover.getString("nombre_actividad", "").toString());
		txtActividad.setText(myPreferencesRecover.getString("nombre_actividad", "").toString());
		TM=new TrainingMonitor((String) txtActividad.getText());
		//actividad.setText(myPreferencesRecover.getString("nombre_actividad", ""));
		myPreferencesRecoverTrainning=getSharedPreferences(MYPREFS_TRAINING,mode);
		
		isTrainingActive=myPreferencesRecoverTrainning.getBoolean("estadoTimer", false);
		//isStopTimer=myPreferencesRecoverTrainning.getBoolean("estadoStopTimer", false);
		//timeSwapBuff=myPreferencesRecoverTrainning.getLong("Timer", 0);
		System.out.println("//////////"+isTrainingActive);
		System.out.println("----"+isStopTimer);
		if(isTrainingActive){
			//stopTime();
			timeSwapBuff=myPreferencesRecoverTrainning.getLong("Timer", 0);
			
//			customHandler.removeCallbacks(updateTimerThread);
			timerCore();
			startTime();
		}
		else{ 
			if(isStopTimer){
				timeSwapBuff=myPreferencesRecoverTrainning.getLong("Timer", 0);
				txtTiempo.setText(myPreferencesRecoverTrainning.getString("TimerString", "10:00:00"));	
			}
			
			
		}
		
		
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
//					startTime();
					TM.run();
				
					
					
				}else{
					Toast.makeText(getApplicationContext(), R.string.msgNoGPS, Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), R.string.msgNoAjustes, Toast.LENGTH_LONG).show();
			}
		}
		else{
			isTrainingActive=false;
			stopTime();
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
			//startTime();
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
		isStopTimer=false;
	}
	
	private void stopTime(){
		timeSwapBuff += timeInMilliseconds;
		System.out.println("....."+timeSwapBuff+"   "+timeInMilliseconds);
		customHandler.removeCallbacks(updateTimerThread);
		isStopTimer=true;
	}

	String timer="00:00:00";
	private void timerCore(){
		updateTimerThread = new Runnable() {
			
			boolean primerMultiplo=true;
			
			public void run() {
				
				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
				
				updatedTime = timeSwapBuff + timeInMilliseconds;
				savePreferencesTime(updatedTime);
				//System.out.println(".,,,,."+updatedTime);
				int secs = (int) (updatedTime / 1000);
				int mins = secs / 60;
				secs = secs % 60;
				int milliseconds = (int) (updatedTime % 1000);
//				System.out.println("" + mins + ":"
//						+ String.format("%02d", secs) + ":"
//						+ String.format("%03d", milliseconds));
				timer=("" + mins + ":"
						+ String.format("%02d", secs) + ":"
						+ String.format("%03d", milliseconds));
				System.out.println(timer);
				
				savePreferencesTime(timer);
				if(GPS5Seg(secs, 5)){
					if(primerMultiplo){
						System.out.println("GPSSSSSS");
						
						//startGPS();
						//txtDistancia.setText(String.valueOf(getLastDistance()));
						primerMultiplo=false;
					}					
					}else{
					primerMultiplo=true;
				}	
				
				
				
				txtTiempo.setText("" + mins + ":"
						+ String.format("%02d", secs) + ":"
						+ String.format("%03d", milliseconds));
				customHandler.postDelayed(this, 0);
				
			
			}
			
			public boolean GPS5Seg(int num1,int num2){
				int resto=0;
				resto=num1%num2;
				if(resto==0){
					return true;
				}
				return false;
			}

		};
		
		
	}

	public void startGPS(){
		
		
		pointsCuantity = 0;
		minTime = 30000;
		minDist = 1;

		

		manejador = (LocationManager) getSystemService(LOCATION_SERVICE);

		log("Esperando recepcion GPS");

		
		//Esta localizacion esta cacheada, no hay que mostrarla, es solo para comenzar mas rapido la recepcion GPS
		Location lastKnownLocation = manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		muestraLocaliz(lastKnownLocation);

		manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,	minDist, this);
	}
	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		log("Nueva localizaciÛn: ");

		if (pointsCuantity == 1) {
			
			lastLoc = new Location(location);
			
		}

		muestraLocaliz(location);
	}

	@Override
	public void onProviderDisabled(String proveedor) {
		// TODO Auto-generated method stub
		log("Proveedor deshabilitado: " + proveedor + "\n");

	}

	@Override
	public void onProviderEnabled(String proveedor) {
		// TODO Auto-generated method stub
		log("Proveedor habilitado: " + proveedor + "\n");

	}

	@Override
	public void onStatusChanged(String proveedor, int estado, Bundle extras) {
 
		// TODO Auto-generated method stub
		log("Cambia estado proveedor: " + proveedor + ", estado="
				+ E[Math.max(0, estado)] + "\n");
	}
	
	
	// MÈtodos para mostrar informaciÛn

		private void log(String cadena) {

			//salida.append(cadena + "\n");

		}

		private void muestraLocaliz(Location localizacion) {

			if (localizacion == null)

				log("LocalizaciÛn desconocida\n");

			else

			if (pointsCuantity <= 1) {
				distance = 0;

				if (pointsCuantity == 1)
					log("FirstLocation");

				if (pointsCuantity == 0)
					log("LastKnownLocation");
			}

			else {
				distance = localizacion.distanceTo(lastLoc);
				lastLoc = new Location(localizacion);
			}

			
			
			//los datos a guardar son los sigientes, mas la distance (0 para el primer punto y la diferencia entre los anteriores para el resto
			//solo se necesita guardar a partir de pointsCuantity = 1, la que el 0 es el LastKnownLocation que es la cacheada
			latitude = localizacion.getLatitude();
			altitude = localizacion.getAltitude();
			longitude = localizacion.getLongitude();
			velocity = localizacion.getSpeed();
			time = localizacion.getTime();

			log("Localizacion[ " + "Orden=" + pointsCuantity + ", Latitud(grados)="
					+ latitude +

					", Longitud(grados)=" + longitude +

					", Altitud(metros)=" + altitude +

					", Velocidad(m/s)=" + velocity +

					", Distancia(metros)=" + velocity +

					", Tiempo(UTC millisegundos)=" + time + " ]\n");
			
			pointsCuantity++;

		}
	
		public float getLastDistance(){
			return distance;
		}
		public float getLastSpeed(){
			return velocity;
		}
}
