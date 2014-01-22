package project.v_trainning;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.util.Locale;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import project.database.DataBase_vTrainning;

import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Looper;
import android.location.Criteria;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.MarkerOptions;

/**
 * This class performs all the tasks about the training, and displays all training data for example distance, duration,
 * type of training, and the route on a map 
 * @author Various
 * @version 1.0
 */
public class TrainningActivity extends Activity implements LocationSource{
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
	TextView txtVelocidad;
	Button btnTrainActStart,btnTrainActPause,btnTrainActStop;
	Chronometer crono;
	
	
	
/*
 * Variables para almacenar lo que devuelve el GPS
 */
	public Vector<Long> Tiempo;
	public Vector<Double> Velocidades;
	public Vector<Double> Distancia;
	public int index=0;
	/***
	 * It indicates if the training mode is active.
	 * */ 
	boolean isTrainingActive=false; //Agregado por Marlon
	

	private OnLocationChangedListener mListener;
	private LocationManager locationManager;
	private LocationManager lm;
	private LocationProvider locationProvider;
	private Marker marker;
	private List<Location> list = new ArrayList<Location>();
	private Iterator iterador;
	private Iterator it;
	private Location locationAnt;
	private Location loc;
	Location locationPost;
	Location locationInicial;
	Location locationAux;
	LocationListener myLocationListener;
	private GoogleMap mMap;
	

	private Boolean havelocation=false;
	private Boolean flag=true;
	private Boolean flag2 = true;
	private Boolean gpsIsEnabled;
   
    
	private Double velocidad;
	private Double velocidadKM = 0.0;
	
	private Double velocidadFinal;
	private Double velocidadesAux;
	
	private Float distancia=(float) 0;
	private Float distanciaKM = (float) 0;
	private Float distanciaLap = (float) 0;
	private DecimalFormat df = new DecimalFormat("0.00"); 
	//private SimpleDateFormat dfo = new SimpleDateFormat("00:00");
	private Integer edad;
	private String actividad;
	//private Bundle recuperacionpeso; //recuperaci�n param
	private Double peso; //recuperaci�n param
	//private Double kg=90.0; //variable peso en kg si no se introduce por parte de usuario
	private Double calorias =0.0;
	
	private long minutos;
	private String nombre;
	
	private double frecMaxCad=0.0;
	private int frecBasal=120;
	private double frecOptima=0.0;
    
	private String proveedor;
    String locationContext = Context.LOCATION_SERVICE;
    String ritmo;
    String ritmoresumen;
    String estado ="inactivo";
    String duracel;
    String chronoText4;
    Integer repeat = 1; 

    
    /////TextToSpeech
	private TextToSpeech tts;
	private static int TTS_DATA_CHECK = 1;
	private boolean isTTSInitialized = false;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainning);
		 Velocidades=new Vector<Double>();
		 Distancia=new Vector<Double>();
		 Tiempo=new Vector<Long>();
		//Crea el objeto que gestiona las localizaciones
		 lm = (LocationManager) getSystemService(locationContext);
		 myLocationListener = new MyLocationListener();
		 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		 //Location myLocation = lm.getLastKnownLocation("network"); //using "gps" returned NULL
		 Location myLocation = lm.getLastKnownLocation(lm.GPS_PROVIDER);
		
		//System.out.println("Creando Trainning");
		
		createWidget();
		setUpMapIfNeeded();
		//avisoNoConexionGPS();
		speakBeggin();
		confirmTTSData();

        if(locationManager != null)
        {         
            
            gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(gpsIsEnabled) //actualizo cada 2 segundos
            {
            	
            	startListener(); 
            	
            }            
             else
            {
                //Show an error dialog that GPS is disabled...
            	Toast.makeText(this, getResources().getString(R.string.msgNoGPS), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
        	//Toast.makeText(this, "fallo location manager", Toast.LENGTH_LONG).show();
        }	
        
//		speakUserLocale(getResources().getString(R.string.msgWelcome));
//		confirmTTSData();
	}

	



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		savePreferences();
	}

	@Override
	protected void onResume() {
		// Auto-generated method stub
		super.onResume();

			showSavedPreferencesSettings();
			Toast.makeText(getApplicationContext(), R.string.msgInicio, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trainning, menu);
		return true;
	}

	/**
	 * Ask the user for a confirmation of the exit action. 
	 */
	@Override
	public void onBackPressed() {
		// Auto-generated method stub
        questionMessage(getResources().getString(R.string.title_activity_trainning),getResources().getString(R.string.msgSalir),
        		getResources().getString(R.string.btnOk),getResources().getString(R.string.btnCancel),false);
	}


	/**
	 * Define the menu actions for this activity.
	 * @param item
	 * The menu selected item.
	 * 
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menuOpAjustes:
	        	//System.out.println("menuAjustes");
	        	launchSettingActivity(findViewById(R.id.btnTrainActAjust));
	            return true;
	        case R.id.menuOpResumen:
	        	//System.out.println("menuResumen");
	        	launchResultsActivity(findViewById(R.id.btnTrainActResum));
	            return true;
	        case R.id.menuOpCreditos:
	        	launchAboutActivity(findViewById(R.id.btnTrainActTrain));
	            return true;
	        case R.id.menuOpSalir:
	            questionMessage(getResources().getString(R.string.title_activity_trainning),getResources().getString(R.string.msgSalir),
	            		getResources().getString(R.string.btnOk),getResources().getString(R.string.btnCancel),false);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * This method initialize the widgets and implements the start button, stop button and pause button
	 */
	private void createWidget(){
		
		
		txtActividad=(TextView)findViewById(R.id.tViewTrainActAct);
		txtDistancia=(TextView)findViewById(R.id.tViewTrainActDist);
		txtVelocidad=(TextView)findViewById(R.id.tViewTrainActVeloc);
		//etxtPulso=(EditText)findViewById(R.id.eTextPulso);
		crono = (Chronometer)findViewById(R.id.chronometer1);
		btnTrainActStart=(Button)findViewById(R.id.btnTrainActStart);
		btnTrainActPause=(Button)findViewById(R.id.btnTrainActPause);
		btnTrainActStop=(Button)findViewById(R.id.btnTrainActStop);
		
		if (!isTrainingActive){
			btnTrainActStart.setAlpha(1);
			btnTrainActStart.setEnabled(true);
			btnTrainActPause.setAlpha((float) 0.5);
			btnTrainActPause.setEnabled(false);
			btnTrainActStop.setAlpha((float) 0.5);
			btnTrainActStop.setEnabled(false);
		}else{
			btnTrainActStart.setAlpha((float) 0.5);
			btnTrainActStart.setEnabled(false);
			btnTrainActPause.setAlpha(1);
			btnTrainActPause.setEnabled(true);
			btnTrainActStop.setAlpha(1);
			btnTrainActStop.setEnabled(true);
			
		}
		btnTrainActStart.setOnClickListener(new View.OnClickListener() {
			/**
			 * Start the training measurements.
			 */
			public void onClick(View view) {
				speakUserLocale(getResources().getString(R.string.msgStart));
				confirmTTSData();
				if (isActivatedAjustes){
					int stoppedMilliseconds = 0;
					String chronoText = crono.getText().toString();
					String array[] = chronoText.split(":"); //SEPARA CIFRAS POR EL :
					if (array.length == 2) {
	            	  //pasa a minutos
						stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
						+ Integer.parseInt(array[1]) * 1000;
					} else if (array.length == 3) {
	            	  //a horas
						stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 
	                    + Integer.parseInt(array[1]) * 60 * 1000
	                    + Integer.parseInt(array[2]) * 1000;
					}
					crono.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
					//elapsedMillisLapInicial = (SystemClock.elapsedRealtime() - crono.getBase())/1000;
	            
	            
					float tiempo =  (SystemClock.elapsedRealtime() - stoppedMilliseconds);
	              
					estado = "activo"; //al darle a bot�n inicio el estado pasa a estar activo
	              //etxtPulso.setActivated(false);
					isTrainingActive=true;
					//speakUserLocale();
					crono.start(); //inicia el cron�metro    
					btnTrainActStart.setAlpha((float) 0.5);
					btnTrainActStart.setEnabled(false);
					btnTrainActPause.setAlpha(1);
					btnTrainActPause.setEnabled(true);
					btnTrainActStop.setAlpha(1);
					btnTrainActStop.setEnabled(true);
				}else{
					Toast.makeText(getApplicationContext(), R.string.msgNoAjustes, Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		btnTrainActPause.setOnClickListener(new View.OnClickListener() {
			/**
			 * Pause the training measurements.
			 */
			public void onClick(View view) {
				//System.out.println("click pause");
				speakUserLocale(getResources().getString(R.string.msgPause));
				confirmTTSData();
				//etxtPulso.setActivated(true);
				isTrainingActive=false;
				crono.stop();
				btnTrainActStart.setAlpha(1);
				btnTrainActStart.setEnabled(true);
				btnTrainActPause.setAlpha((float) 0.5);
				btnTrainActPause.setEnabled(false);
				btnTrainActStop.setAlpha((float) 0.5);
				btnTrainActStop.setEnabled(false);

			}
		});

        btnTrainActStop.setOnClickListener(new View.OnClickListener() {
			/**
			 * Stop the training measurements and make the results computations.
			 */
            public void onClick(View view) {
                    //c�lculo de los par�metros finales de resumen
            	speakUserLocale(getResources().getString(R.string.msgStop));
				confirmTTSData();
            	calculofinal();         
            	//acceso a bd para guardar el entrenamiento realizado         
            	//SQLiteDatabase db = data.getWritableDatabase();         
            	//CAMBIOS EN FECHA AHORA SIENDO UN STRING MOSTRANDO BIEN EL FORMATO
            	//mitime = fechaActusql.getTime();                           
            	//String mitime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            	//guardamos entrenamiento en la BD
            	//data.guardarEntrenamiento(id, duracel, distanciaKM, velocidadfinal, ritmoresumen, caloriastotal, mitime, elapsedMillis);
        
            	//db.close();
    			btnTrainActStart.setAlpha(1);
    			btnTrainActStart.setEnabled(true);
    			btnTrainActPause.setAlpha((float) 0.5);
    			btnTrainActPause.setEnabled(false);
    			btnTrainActStop.setAlpha((float) 0.5);
    			btnTrainActStop.setEnabled(false);
     
            	crono.setBase(SystemClock.elapsedRealtime());
            	estado = "inactivo"; //el estado pasa a estar inactivo y paramos el tiempo
            	crono.stop();
            	//crono.setBase(0);
            	txtDistancia.setText("");
            	txtVelocidad.setText("");
            	showResults();
            	//reset de preferencias al guardar la sesi�n
            	//misPreferencias = getSharedPreferences(MYPREFS, mode);
            	//miEditor = misPreferencias.edit();
            	//miEditor.putFloat("calorias", 0);
            	//miEditor.putInt("distancia", 0);
            	//miEditor.putLong("duracion", 0);
            	//miEditor.commit();
            	//	
            	//tarea tat = new tarea(MainActivity.this);
            	//     tat.execute();
            }
        });		
	}

	
	

	/**
	 * Method to launch Settings Activity.
	 * @param view
	 * */
	public void launchSettingActivity(View view){
		startActivity(new Intent(TrainningActivity.this,AjustesActivity.class));

	}
	
	/**
	 * Method to launch Resumen Activity.
	 * @param view
	 * */
	public void launchResumeActivity(View view){
		startActivity(new Intent(TrainningActivity.this,ResumenActivity.class));
		
	}
	
	/**
	 * Method to launch Results Activity.
	 * @param view
	 * */
	public void launchResultsActivity(View view){
		startActivity(new Intent(TrainningActivity.this,ResultsActivity.class));
		
	}
	
	/**
	 * Method to launch About Activity.
	 * @param view
	 * */	
	public void launchAboutActivity(View view){
		startActivity(new Intent(TrainningActivity.this,AboutActivity.class));
		
	}

	
	/**
	 * Method to launch Results Activity with the training results.
	 * */
	private void showResults(){
		startActivity(new Intent(TrainningActivity.this,ResultsActivity.class));
	}
	
	
	/**
	 * Save the user data in the app preferences.
	 */
	private void savePreferences() {
		// Auto-generated method stub
		myPreferences = getSharedPreferences(MYPREFS_TRAINING, mode);
		Editor myEditor = myPreferences.edit();
	
		myEditor.putBoolean("estadoTimer", isTrainingActive);
	
		myEditor.commit();	
		
	}


	/**
	 * Recover the saved data from preferences
	 */
	private void showSavedPreferencesSettings() {
		// Auto-generated method stub
		String temp;

		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		//System.out.println(myPreferencesRecover.getString("nombre_actividad", "").toString());
		nombre=myPreferencesRecover.getString("nombre", "").toString();
		//System.out.println(nombre);
		txtActividad.setText(myPreferencesRecover.getString("nombre_actividad", "").toString());
		temp=myPreferencesRecover.getString("completo", "").toString();
		isActivatedAjustes=Boolean.valueOf(temp);
		temp=myPreferencesRecover.getString("peso", "60.0").toString();
		if (isPosDouble(temp)){
			peso=Double.valueOf(temp);
		}else peso=0.0;
        temp=myPreferencesRecover.getString("edad", "20").toString();
        if (isPosInteger(temp))
        	edad=Integer.valueOf(temp);
        else edad=0;
        temp=myPreferencesRecover.getString("frecuencia_basal", "120");
        if (isPosInteger(temp))
        	frecBasal=Integer.valueOf(temp);
        else frecBasal=120;
		myPreferencesRecoverTrainning=getSharedPreferences(MYPREFS_TRAINING,mode);
		isTrainingActive=myPreferencesRecoverTrainning.getBoolean("estadoTimer", false);
        velocidadFinal=Double.valueOf(myPreferencesRecoverTrainning.getString("velocidadFinal", "0.0"));

	}


		@Override
		public void activate(OnLocationChangedListener listener) {
			// Auto-generated method stub
			mListener = listener;
		}





		@Override
		public void deactivate() {
			// Auto-generated method stub
			mListener = null;
		}
		//SE HACE UN PROPIO LOCATION LISTENER
		 private class MyLocationListener implements LocationListener 
		    {     
			 @Override
			    public void onLocationChanged(Location location) //cambio de lugar de localizaci�n
			    {   	  		 		 
			        if( mListener != null )
			        {
			           mListener.onLocationChanged(location);
			           mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
			           //enter.setEnabled(true); //activamos el bot�n de inicio al tener ya conexi�n gps para realizar la actividad
			           if(estado == "activo")
			             //enter.setEnabled(false);
			           
			            if(estado == "activo"){	            	
			              if(flag){
			            	locationInicial = location;
			            	flag = false;
			            	
			            }	        
			            list.add(location);
			            
			            //nos centrar� la posici�n en el mapa 
			            CameraPosition newCameraPosition = new CameraPosition.Builder()
			            .target(new LatLng(location.getLatitude(), location.getLongitude()))     
			            .zoom(15)                  
			            .bearing(0)       
			            .tilt(0)                 
			            .build(); 
			           	 
			            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition));
			            //end
			            
			            
			            //calculo de la velocidad actual  
			            velocidad = (double) location.getSpeed(); 
			            velocidadKM = (velocidad / 1000) * 3600;
			            txtVelocidad.setText(String.valueOf((Math.round(velocidadKM*10)/10)));
			            velocidadesAux=0.0;
			            Velocidades.add(velocidadKM);
			            //System.out.println("Velocidad Size:"+Velocidades.size());
			            

			            
			            //System.out.println("q===="+multiplo(Long.valueOf((crono.getText().toString().substring(0,crono.getText().toString().indexOf(":")))), 5));
			            
			            //long t=Long.valueOf((crono.getText().toString().substring(0,crono.getText().toString().indexOf(":"))));
			            if(Long.valueOf((crono.getText().toString().substring(0,crono.getText().toString().indexOf(":"))))>0){
			            	if(multiplo(Long.valueOf((crono.getText().toString().substring(0,crono.getText().toString().indexOf(":")))), 2)){
				            	
			            		if(unaVez){
			            			System.out.println("siiii");
					            	speakUserLocale("Animo!!! vamos!!!");
									confirmTTSData();
									unaVez=false;
			            		}else{
			            			
			            		}
			            		
			            	}else{
			            		unaVez=true;
			            	}
			            }
			            
			            
                        for(int i=0;i<Velocidades.size();i++){
                        	//System.out.println("....l."+i+":"+Velocidades.get(i));
                            velocidadesAux+=Velocidades.get(i);
                            
                        }
                   
                        velocidadFinal=velocidadesAux/(Velocidades.size());
			            
                        myPreferences = getSharedPreferences(MYPREFS_TRAINING, mode);
                        Editor myEditor = myPreferences.edit();
                        myEditor.putString("velocidadFinal", velocidadFinal.toString());
                        myEditor.commit();        

			            //llamamos a la funci�n que nos calcula la distancia
			            distancefunc(location);
			           	            
			            //ALTITUD 
			            //altitud = (int) location.getAltitude();//RETORNA UN DOUBLE
			           
			            //llamamos a la tarea as�ncrona
			            task task = new task();
					    task.execute();
		  
					    

			            }                
			        }else{
			        	Toast.makeText(TrainningActivity.this, R.string.msgWarning, Toast.LENGTH_LONG).show();
			        }	       
			    }
			 
			 
			//forzar usuario a que encienda gps antes de iniciar app
			/**
		|	* If GPS is disabled force the user to activate it.
			* @param arg0
			*/
			 @Override
			public void onProviderDisabled(String arg0) {
				// Auto-generated method stub
				Toast.makeText(getApplicationContext(), R.string.msgNoGPS, Toast.LENGTH_LONG).show();
				Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    startActivity(intent);
			}

			@Override
			public void onProviderEnabled(String arg0) {
				// Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String proveedor, int status, Bundle extras) {
				// Auto-generated method stub
					
			}	
		  }
		 
		 /**
		  * Calculate the distance between the actual and last locations.
		  * @param locationDis
		  * The actual location.
		  * @author Daniel Aparicio
		  */
		 public void distancefunc (Location locationDis){
			 
//				misPreferencias = getSharedPreferences(MYPREFS,mode);
//				distareto = misPreferencias.getInt("distancia", 0);
				    		    
		        if(distancia == 0) locationAux = locationInicial ;
		           
		         //c�lculo de la distancia 
		        if(estado == "activo"){
		            float distanciaAux = locationAux.distanceTo(locationDis);
		            distancia = distancia + distanciaAux;
		            distanciaLap = distanciaLap + distanciaAux;
		        }
		        distanciaKM = (distancia / 1000);    
		        locationAux = locationDis;
		        
		    }
		 
		 /**
		  * This class provides the asyncronus tasks fot updates the distance textView.
		  * @author Jorge Zambrano
		  *
		  */
		  private class task extends AsyncTask<Void, Void, Void>{
			   @Override
				protected Void doInBackground(Void... arg0) {
				   return null;
			   }
			   
			   protected void onPostExecute(Void result) {	 
				 
				   //speed = (TextView) findViewById(R.id.textView6);
				   //alt = (TextView) findViewById(R.id.textView7);
				   //ritmoact = (TextView) findViewById(R.id.textView10);
				   //txtDistancia = (TextView) findViewById(R.id.tViewTrainActDist);			  
				   //cal = (TextView) findViewById(R.id.textView11);
				   
				   txtDistancia.setText(String.valueOf(df.format(distanciaKM)));
				   
				   System.out.println(distanciaKM);
				   //speed.setText(String.valueOf(df.format(velocidadKM)));  			  
				   //alt.setText(String.valueOf(altitud));			  
				   //ritmoact.setText(ritmo);	
				   //int calo = (int) (Math.round(caloriastotal));
				   //cal.setText(String.valueOf((calo)));
				   
			   }
			   
		  }

		  	/**
		  	 * Initialize the map.
		  	 */
		    private void setUpMap()
		    {
		        mMap.setMyLocationEnabled(true); //nos localiza
		        mMap.getUiSettings().setMyLocationButtonEnabled(false); //quita el bot�n de mi opsici�n
		    }
		     
		    /**
		     * Setup the map if the map isn't initializated.
		     */
			 @SuppressLint("NewApi")
			private void setUpMapIfNeeded() {
			        // todav�a sin inicializar el mapa
			        if (mMap == null)
			        {
			            // Tratamos de obtener el mapa
			            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragment1)).getMap();
			          
			            //inicializamos el mapa
			            if (mMap != null)
			            {
			                setUpMap();
			            }
			 
			            //se registra el LocationSource del mapa
			            mMap.setLocationSource(this);
			        }
			    }

			 /**
			  * Start the listener for GPS.
			  */
			 private void startListener()  
			  {  
			    // Create a new Thread and start it  
			    new Thread()  
			    {  
			      /** Set the location listener */  
			      public void run()  
			      {  
			        // Init a message looper, required for Android  
			        Looper.prepare();  
			        //se establece la precisi�n
			        Criteria c = new Criteria();
		            c.setAccuracy(Criteria.ACCURACY_FINE); 
			        //obtiene el mejor proveedor en funci�n del criterio asignado
		            //(la mejor precisi�n posible)
		            proveedor = locationManager.getBestProvider(c, true);
		        	lm.requestLocationUpdates(proveedor, 0, 10F, myLocationListener);
		        	locationProvider = lm.getProvider(proveedor);
		        	
		        	
		        	
		        	//ya sabemos que GPS ON
		        	Toast.makeText(TrainningActivity.this, getResources().getString(R.string.msgGPSok), Toast.LENGTH_LONG).show();
		        	
		        	//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 3F, myLocationListener);
			        
			         // 20 meters  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 3F, this);
			      //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 3F, myLocationListener);  
			      }  
			    }
			    .start();  
			  } 

			 /**
			  * Show an alert if GPS isn't enable or doesn't work suitably.
			  */
			 public void avisoNoConexionGPS(){
			    	
			    	AlertDialog.Builder builder4 = new AlertDialog.Builder(TrainningActivity.this);
			    	
			    	builder4.setTitle(getResources().getString(R.string.msgWarning));
			    	builder4.setMessage(getResources().getString(R.string.msgNoGPS))
				       .setCancelable(false)
				       
				       .setPositiveButton(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
					       		//dialog.cancel();
				        	   //saludo();
				           }
				       });
			    	AlertDialog alert4 = builder4.create();
			    	alert4.show();
			    }

			 /**
			  * Finish the Activity and the app.
			  */
			 public void salir(){
				 finish();
				 System.exit(0);
			 }
				
				
		private void questionMessage(String title,String text, String nameButton1, String nameButton2, boolean cancelable){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(text)
				    .setTitle(title)
					.setCancelable(cancelable)
					.setPositiveButton(nameButton1,
			                new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int id) {
			                    	salir();
					                //finish();
					                }
					            })
					.setNegativeButton(nameButton2,
					        	new DialogInterface.OnClickListener() {
					        		public void onClick(DialogInterface dialog, int id) {
					        	 }
			                });
			AlertDialog alert = builder.create();
			alert.show();

		}
			 
        /**
         * Calculates the training Results.
         * @author Sandra Ruiz
         */
		public void calculofinal(){

        	Toast.makeText(TrainningActivity.this, getResources().getString(R.string.msgCalculo), Toast.LENGTH_LONG).show();
            double basal=frecBasal;
            actividad=txtActividad.getText().toString();
            frecMaxCad=208-(0.7*edad);
            frecOptima=((frecMaxCad-frecBasal)/2)+80;
            
            myPreferencesRecoverTrainning=getSharedPreferences(MYPREFS_TRAINING,mode);
            
            velocidadFinal=Double.valueOf(myPreferencesRecoverTrainning.getString("velocidadFinal", "0.0"));

            minutos=Long.valueOf((crono.getText().toString().substring(0,crono.getText().toString().indexOf(":"))));
            //System.out.println("..............."+minutos);
          //Actividad ciclismo
            if(actividad.equalsIgnoreCase(getResources().getString(R.string.ActivCycling))){ 
                    
                    if(velocidadFinal>=0 && velocidadFinal<=16){
                            calorias=0.049*peso*2.2*minutos;
                    }else {
                            calorias=0.071*peso*2.2*minutos;
                    }        
                    
            		}else if(actividad.equalsIgnoreCase(getResources().getString(R.string.ActivRunning))){
//                    
//            			System.out.println("peso:"+peso);
//            			System.out.println("distancia:"+String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+4));
                    
            			calorias=1.03*peso*Double.valueOf(String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+4));
            		}
            		String mitime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            		double distanciafinal=Double.valueOf(String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+4));
            		savedDataBaseSesion(nombre,mitime,basal, 60, calorias, velocidadFinal, distanciafinal, minutos+"");
            
            		it=list.listIterator();
            		while(it.hasNext()){
            			loc = new Location((Location) it.next());
  
            			//guardamos puntos longitud-latitud en BD
            			// data.guardarPuntos(id, loc.getLongitude(), loc.getLatitude());
            			savedDataBaseGPS(nombre,loc.getLatitude(),loc.getLongitude(),loc.getAltitude(),0.0,0.0,0.0);
  
            		}
            		calculaVelocidades();
            		
        	}

		/**
		 * Save the training session results in the database.
		 * @param name
		 * User name
		 * @param date
		 * The training date
		 * @param basal_sistolica
		 * Sistolic heart rate
		 * @param basal_diastolica
		 * Distolic heart rate
		 * @param calories
		 * Burned calories
		 * @param average_speed
		 * Training average speed
		 * @param total_distance
		 * Total training distance
		 * @param total_time
		 * Total training time
		 */
        private void savedDataBaseSesion(String name,String date,double basal_sistolica, double basal_diastolica,double calories, double average_speed,double total_distance, String total_time){
            
            DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
            //Inserta Usuarios
            boolean bbb= db.setSesion(name, date, basal_sistolica, basal_diastolica, calories, average_speed, total_distance, total_time);
//            System.out.println(name);
//            System.out.println(date);
//            System.out.println(basal_sistolica);
//            System.out.println(calories);
//            System.out.println(total_time);
//            System.out.println("Guardado:"+bbb);
            db.closeDataBase();
        }
    
        /**
         * Save the GPS data for future use.
         * @param name
         * @param latitude
         * @param longitude
         * @param GPS_height
         * @param partial_speed
         * @param partial_distance
         * @param partial_time
         */
        private void savedDataBaseGPS(String name,double latitude,double longitude,double GPS_height,double partial_speed,double partial_distance,double partial_time){
            
            DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
            //Inserta Usuarios
            db.setGPS(name, latitude, longitude, GPS_height, partial_speed, partial_distance, partial_time);
            db.closeDataBase();
    }   
        
        /**
         * Calculates the average speeds over time, to show them in a graphic.
         */
        private void calculaVelocidades(){
        	int i,j,k,l;
        	double velProm;
        	Double [] promedios= new Double[9];
        	i=Velocidades.size();
        	Double [] veloc=new Double[i];
        	for (j=0; j<i; j++)
        		veloc[j]= Velocidades.get(j);
        	k=Math.round(i/16);
        	if (k==0) k=1;
        	promedios[0]=veloc[0];
        	for (i=1;i<9;i++){
        		velProm=0;
        		l=i*2*k;
        		for (j=(l-k);j<=(l+k);j++){
        			if (j<Velocidades.size() && j>=0)
        			velProm=velProm+veloc[j];
        			
        		}
        		if (j<=Velocidades.size()){
        			promedios[i]=velProm/(2*k);
        		}else promedios[i]=velProm/(k);
        	}
            myPreferences = getSharedPreferences(MYPREFS_TRAINING, mode);
            Editor myEditor = myPreferences.edit();
            myEditor.putString("velocidadPromedio0", String.valueOf(promedios[0]));
            myEditor.putString("velocidadPromedio1", String.valueOf(promedios[1]));
            myEditor.putString("velocidadPromedio2", String.valueOf(promedios[2]));
            myEditor.putString("velocidadPromedio3", String.valueOf(promedios[3]));
            myEditor.putString("velocidadPromedio4", String.valueOf(promedios[4]));
            myEditor.putString("velocidadPromedio5", String.valueOf(promedios[5]));
            myEditor.putString("velocidadPromedio6", String.valueOf(promedios[6]));
            myEditor.putString("velocidadPromedio7", String.valueOf(promedios[7]));
            myEditor.putString("velocidadPromedio8", String.valueOf(promedios[8]));
            myEditor.commit();        
        	
        	
        }

    	/**
    	 * Verify if an input string is a positive integer.
    	 * @param cadena
    	 * @return true if cadena is a integer number greater than zero; false if not.
    	 */
    	private boolean isPosInteger(String cadena){
    		try{
    			int dd=Integer.parseInt(cadena);
    			if (dd>0){
    				return true;
    			}else return false;
    		}catch (NumberFormatException e){
    			return false;
    		}
    	}

    	/**
    	 * Verify if an input string is a positive double.
    	 * @param cadena
    	 * @return true if cadena is a double number greater than zero; false if not.
    	 */
    	private boolean isPosDouble(String cadena){
    		try{
    			double dd=Double.parseDouble(cadena);
    			if (dd>0){
    				return true;
    			}else return false;
    		}catch (NumberFormatException e){
    			return false;
    		}
    	}
    	
    	
    	
    	
    	
    	private void confirmTTSData()  {
	    	Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA);
	    	startActivityForResult(intent, TTS_DATA_CHECK);
	    }

	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	if (requestCode == TTS_DATA_CHECK) {
	    		if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
	    			//Voice data exists		
	    			initializeTTS();
	    		}
	    		else {
	    			Intent installIntent = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
	    			startActivity(installIntent);
	    		}
	    	}
	    }
	    
	    private void initializeTTS() {
	    	
	    	tts = new TextToSpeech(this, new OnInitListener() {
	    		public void onInit(int status) {
	    			if (status == TextToSpeech.SUCCESS) {
	    				isTTSInitialized = true;
	    			}
	    			else {
	    				//Handle initialization error here
	    				isTTSInitialized = false;
	    			}
	    		}
	    	});
	    }
	    
	    private void speakUserLocale(String message) {
	    	if(isTTSInitialized) {
	    		//Determine User's Locale
	    		Locale locale = this.getResources().getConfiguration().locale;
	    		
//	    		System.out.println("locale="+locale);
	    		if (tts.isLanguageAvailable(locale) >= 0) 
	    			tts.setLanguage(locale);
	    		
	    		tts.setPitch(0.8f);
	    		tts.setSpeechRate(1.1f);
	    		
	    		tts.speak(message, TextToSpeech.QUEUE_ADD, null);
	    	}
	    }
	    
	    private void speakBeggin() {
	    	if(isTTSInitialized) {
	    		//Determine User's Locale
	    		Locale locale = this.getResources().getConfiguration().locale;
	    		
	    		//System.out.println("locale="+locale);
	    		if (tts.isLanguageAvailable(locale) >= 0) 
	    			tts.setLanguage(locale);
	    		
	    		tts.setPitch(0.8f);
	    		tts.setSpeechRate(1.1f);
	    		
	    		tts.speak("", TextToSpeech.QUEUE_ADD, null);
	    	}
	    }

	    @Override
	    public void onDestroy() {
	    	if (tts != null) {
	    		
	    		tts.stop();
	    		tts.shutdown();
	    	}
	    	super.onDestroy();
	    }
	    
	    boolean unaVez=false;
	    public boolean multiplo(long num, int multiplo){
	    	
	    	long mul=num%multiplo;
	    	
	    	if(mul==0){
	    		
	    		return true;
	    	
	    	}
	    	else{
	    		
	    		return false;
	    	}
	    	
	    }

}
