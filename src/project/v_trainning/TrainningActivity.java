package project.v_trainning;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

//import android.os.AsyncTask;
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
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
//import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import project.database.DataBase_vTrainning;
import project.gps.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.location.Criteria;



import com.google.android.gms.internal.s;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
<<<<<<< HEAD
	Button btnTrainActStart,btnTrainActPause,btnTrainActStop;
=======
	TextView txtVelocidad;
	Button btnTrainActStart,btnTrainActPause;
>>>>>>> 0d2fed84aafbe7cc0829f7e9ed53d8f6bb422fc5
	Chronometer crono;
	
	
	
	
	
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
	

	private OnLocationChangedListener mListener;
	private LocationManager locationManager;
	private LocationManager lm;
	private LocationProvider locationProvider;
	Marker marker;
	List<Location> list = new ArrayList<Location>();
	Iterator iterador;
	Iterator it;
	Location locationAnt;
	Location loc;
	Location locationPost;
	Location locationInicial;
	Location locationAux;
	LocationListener myLocationListener;
	private GoogleMap mMap;
	

	Boolean havelocation=false;
    Boolean flag=true;
    Boolean flag2 = true;
    Boolean gpsIsEnabled;
   
    
	Double velocidad;
	Double velocidadKM = 0.0;
	
	Double ritmomedioactual;
	Double ritmomediofinal;
	Double velocidadFinal=0.0;
	Double velocidadesAux=0.0;
	Vector<Double> velocidades;
	Float distancia=(float) 0;
	double distanciaKM =  0.000;
	Float distanciaLap = (float) 0;
	Float calReto;
	DecimalFormat df = new DecimalFormat("0.000"); 
	SimpleDateFormat dfo = new SimpleDateFormat("00:00");
	Integer edad;
	String actividad;
	Bundle recuperacionpeso; //recuperaci�n param
	Double peso; //recuperaci�n param
	Double kg=90.0; //variable peso en kg si no se introduce por parte de usuario
	Double calorias =0.0;
	Double caloriastotal = 0.0;
	Long memoCrono;
	Long elapsedMillis;
	Long elapsedMillisLapInicial=(long) 0;
	Long elapsedMillisLapFinal=(long) 0;
	Long diferenciatime;
	long minutos;
	Double elapsedMillis2;
	Long elapsedMillis3;
	Double elapsedMillis4;
	Integer km=1;
	Integer distareto;
	String nombre;
	TextView distance;
	TextView speed;
	TextView alt;
	TextView ritmoact;
	TextView cal;
	TextView usuario;
	//EditText etxtPulso;
<<<<<<< HEAD
	
	double frecMaxCad=0.0;
	int frecBasal=120;
	double frecOptima=0.0;
=======
>>>>>>> 0d2fed84aafbe7cc0829f7e9ed53d8f6bb422fc5
	
	private String proveedor;
	private Resources res = null;
    String locationContext = Context.LOCATION_SERVICE;
    String ritmo;
    String ritmoresumen;
    String estado ="inactivo";
    String duracel;
    String chronoText4;
    
    Integer repeat = 1; 

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainning);
		velocidades=new Vector<Double>();
		//Crea el objeto que gestiona las localizaciones
		 lm = (LocationManager) getSystemService(locationContext);
		 myLocationListener = new MyLocationListener();
		 lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		 //Location myLocation = lm.getLastKnownLocation("network"); //using "gps" returned NULL
		 Location myLocation = lm.getLastKnownLocation(lm.GPS_PROVIDER);
		
		 
		 
		System.out.println("Creando Trainning");
		
		createWidget();
		setUpMapIfNeeded();
		//avisoNoConexionGPS();
		 
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

			showSavedPreferencesSettings();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trainning, menu);
		return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menuOpAjustes:
	        	System.out.println("menuAjustes");
	        	launchSettingActivity(findViewById(R.id.btnResumActSetting));
	            return true;
	        case R.id.menuOpResumen:
	        	System.out.println("menuResumen");
	        	launchResumeActivity(findViewById(R.id.btnAjustActResum));
	            return true;
	        case R.id.menuOpCreditos:
	            
	            return true;
	        case R.id.menuOpSalir:
	            questionMessage(getResources().getString(R.string.title_activity_trainning),getResources().getString(R.string.msgSalir),
	            		getResources().getString(R.string.btnOk),getResources().getString(R.string.btnCancel),false);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	private void createWidget(){
		
		txtActividad=(TextView)findViewById(R.id.tViewTrainActAct);
		txtDistancia=(TextView)findViewById(R.id.tViewTrainActDist);
<<<<<<< HEAD
//		etxtPulso=(EditText)findViewById(R.id.eTextPulso);
		crono = (Chronometer)findViewById(R.id.chronometer1);
		btnTrainActStart=(Button)findViewById(R.id.btnTrainActStart);
		btnTrainActPause=(Button)findViewById(R.id.btnTrainActPause);
		btnTrainActStop=(Button)findViewById(R.id.btnTrainActStop);
=======
		txtVelocidad=(TextView)findViewById(R.id.tViewTrainActVeloc);
		//etxtPulso=(EditText)findViewById(R.id.eTextPulso);
		crono = (Chronometer)findViewById(R.id.chronometer1);
		btnTrainActStart=(Button)findViewById(R.id.btnTrainActStart);
		btnTrainActPause=(Button)findViewById(R.id.btnTrainActPause);
		
		if (isTrainingActive){
			btnTrainActStart.setAlpha(1);
			btnTrainActStart.setEnabled(true);
			btnTrainActPause.setAlpha((float) 0.5);
			btnTrainActPause.setEnabled(false);
		}else{
			btnTrainActStart.setAlpha((float) 0.5);
			btnTrainActStart.setEnabled(false);
			btnTrainActPause.setAlpha(1);
			btnTrainActPause.setEnabled(true);
			
		}
>>>>>>> 0d2fed84aafbe7cc0829f7e9ed53d8f6bb422fc5
		btnTrainActStart.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				
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
<<<<<<< HEAD
=======
	              isTrainingActive=true;

>>>>>>> 0d2fed84aafbe7cc0829f7e9ed53d8f6bb422fc5
	              crono.start(); //inicia el cron�metro    
	              btnTrainActStart.setAlpha((float) 0.5);
	              btnTrainActStart.setEnabled(false);
	              btnTrainActPause.setAlpha(1);
	              btnTrainActPause.setEnabled(true);
				
			}
		});
		
		btnTrainActPause.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				System.out.println("click pause");
				//etxtPulso.setActivated(true);
<<<<<<< HEAD
=======
				isTrainingActive=false;
>>>>>>> 0d2fed84aafbe7cc0829f7e9ed53d8f6bb422fc5
				crono.stop();
				btnTrainActStart.setAlpha(1);
				btnTrainActStart.setEnabled(true);
				btnTrainActPause.setAlpha((float) 0.5);
				btnTrainActPause.setEnabled(false);
			}
		});
		
		btnTrainActStop.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				//c�lculo de los par�metros finales de resumen
            	calculofinal();      	
            	//acceso a bd para guardar el entrenamiento realizado         	
            	//SQLiteDatabase db = data.getWritableDatabase();              	
            	//CAMBIOS EN FECHA AHORA SIENDO UN STRING MOSTRANDO BIEN EL FORMATO
            	//mitime = fechaActusql.getTime();          	            	          	
            	String mitime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            	//guardamos entrenamiento en la BD
            	//data.guardarEntrenamiento(id, duracel, distanciaKM, velocidadfinal, ritmoresumen, caloriastotal, mitime, elapsedMillis);    
            	
                //db.close();
                 
                
              
            			 
            		crono.setBase(SystemClock.elapsedRealtime());
                    estado = "inactivo"; //el estado pasa a estar inactivo y paramos el tiempo
                    crono.stop();
                    
                    //reset de preferencias al guardar la sesi�n
//                    misPreferencias = getSharedPreferences(MYPREFS, mode);
//                	miEditor = misPreferencias.edit();
//                	miEditor.putFloat("calorias", 0);    
//                	miEditor.putInt("distancia", 0);
//                	miEditor.putLong("duracion", 0);
//                    miEditor.commit();
//                    
//                    tarea tat = new tarea(MainActivity.this);
//            		tat.execute();
            	}
		});
		
	}

	
	

	/**
	 * Method to launch Settings
	 * */
	public void launchSettingActivity(View view){
		//isActivatedAjustes=true;
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
	
		myEditor.putBoolean("estadoTimer", isTrainingActive);
		myEditor.putString("velocidadFinal", velocidadFinal.toString());
		myEditor.commit();	
		
	}


	private void showSavedPreferencesSettings() {
		// TODO Auto-generated method stub
		String temp;

		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		System.out.println(myPreferencesRecover.getString("nombre_actividad", "").toString());
		actividad=myPreferencesRecover.getString("nombre_actividad", "").toString();
		txtActividad.setText(actividad);
		temp=myPreferencesRecover.getString("completo", "").toString();
		isActivatedAjustes=Boolean.valueOf(temp);
		peso=Double.valueOf(myPreferencesRecover.getString("peso", "60.0").toString());
		edad=Integer.valueOf(myPreferencesRecover.getString("edad", "20").toString());
		frecBasal=myPreferencesRecover.getInt("frecuencia_basal", 120);
		
		myPreferencesRecoverTrainning=getSharedPreferences(MYPREFS_TRAINING,mode);
		isTrainingActive=myPreferencesRecoverTrainning.getBoolean("estadoTimer", false);
		velocidadFinal=Double.valueOf(myPreferencesRecoverTrainning.getString("velocidadFinal", "0.0"));

	}


		@Override
		public void activate(OnLocationChangedListener listener) {
			// TODO Auto-generated method stub
			mListener = listener;
		}





		@Override
		public void deactivate() {
			// TODO Auto-generated method stub
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
			            
			            
			            velocidades.add(velocidadKM);
			            System.out.println("Velocidad Size:"+velocidades.size());
			            
			            for(int i=0;i<velocidades.size();i++){
			            	velocidadesAux+=velocidades.get(i);
			            }
			            
			            velocidadFinal=velocidadesAux/velocidades.size();
			            
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
		  
					    
			            //C�LCULO DE CALOR�AS Y RECUPERACI�N DEL PASO DE PAR�METROS DESDE AJUSTES DE USUARIO
//					    myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
//						peso=Double.valueOf(myPreferencesRecover.getString("peso", "").toString());
//						edad=Integer.valueOf(myPreferencesRecover.getString("edad", "").toString());
//						actividad=myPreferencesRecover.getString("nombre_actividad", "").toString();		   
//						
//						
//						if(actividad.equalsIgnoreCase("Ciclismo")){
//							
//								
//							
//						}else if(actividad.equalsIgnoreCase("Correr")){
//							
//							
//							calorias=1.03*peso*distancia;
//						}
//						
						
						

					    	



//			              //guardar los laps en la bd
//			              if(distanciaLap >= 10){ //en metros
//				           	   velocidadLap = (double) (distanciaLap / diferenciatime);
//				    		   velocidadLap = (velocidadLap / 1000) * 3600;
//				               //data.guardarLaps(id, km, ritmo, velocidadLap);
//				    		   km = km + 1;
//				    		   distanciaLap = (float) 0;
//				               elapsedMillisLapInicial = elapsedMillisLapFinal;
//				          }	              
			            }                
			        }else{
			        	Toast.makeText(TrainningActivity.this, "no hay", Toast.LENGTH_LONG).show();
			        }	       
			    }
			 
			 

			//forzar usuario a que encienda gps antes de iniciar app
			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent( android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    startActivity(intent);
			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String proveedor, int status, Bundle extras) {
				// TODO Auto-generated method stub
					
//			    switch (status) {
//			    case LocationProvider.OUT_OF_SERVICE:
//			        Log.i("", "Status Changed: Out of Service");
//			        Toast.makeText(TrainningActivity.this, "Status Changed: Out of Service",
//			                Toast.LENGTH_SHORT).show();
//			        break;
//			    case LocationProvider.TEMPORARILY_UNAVAILABLE:
//			        Log.i("", "Status Changed: Temporarily Unavailable");
//			        Toast.makeText(TrainningActivity.this, "Status Changed: Temporarily Unavailable",
//			                Toast.LENGTH_SHORT).show();
//			        break;
//			    case LocationProvider.AVAILABLE:
//			        Log.i("", "Status Changed: Available");
//			        Toast.makeText(TrainningActivity.this, "Status Changed: Available",
//			                Toast.LENGTH_SHORT).show();
//			        break;
//				
//			    }
//			
			}	
		  }
		 
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
		        System.out.println("DistanciaKM==="+distanciaKM);
		        locationAux = locationDis;
		        

		    }
		 
		  private class task extends AsyncTask<Void, Void, Void>{
			   @Override
				protected Void doInBackground(Void... arg0) {
				   return null;
			   }
			   
			   protected void onPostExecute(Void result) {	 
				 
				   //speed = (TextView) findViewById(R.id.textView6);
				   //alt = (TextView) findViewById(R.id.textView7);
				   //ritmoact = (TextView) findViewById(R.id.textView10);
				   distance = (TextView) findViewById(R.id.tViewTrainActDist);			  
				   //cal = (TextView) findViewById(R.id.textView11);
				   
				   
				   
				   
				   //distance.setText(String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+2));
				   distance.setText(String.valueOf(df.format(distanciaKM)));
				   //System.out.println("DistanciaKM:"+distanciaKM);
				   //speed.setText(String.valueOf(df.format(velocidadKM)));  			  
				   //alt.setText(String.valueOf(altitud));			  
				   //ritmoact.setText(ritmo);	
				   int calo = (int) (Math.round(caloriastotal));
				   //cal.setText(String.valueOf((calo)));
				   
			   }
			   
		  }

		    private void setUpMap()
		    {
		        mMap.setMyLocationEnabled(true); //nos localiza
		        mMap.getUiSettings().setMyLocationButtonEnabled(false); //quita el bot�n de mi opsici�n
		    }
		     
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
			 
			
		
		
		public void calculofinal(){
			myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
			frecBasal=myPreferencesRecover.getInt("frecuencia_basal", 120);
			peso=Double.valueOf(myPreferencesRecover.getString("peso", "").toString());
			edad=Integer.valueOf(myPreferencesRecover.getString("edad", "").toString());
			actividad=myPreferencesRecover.getString("nombre_actividad", "").toString();
			nombre=myPreferencesRecover.getString("nombre", "");
			double basal=myPreferencesRecover.getInt("frecuencia_basal", 120);
			frecMaxCad=208-(0.7*edad);
			frecOptima=((frecMaxCad-frecBasal)/2)+80;
			
			myPreferencesRecoverTrainning=getSharedPreferences(MYPREFS_TRAINING,mode);
			
			velocidadFinal=Double.valueOf(myPreferencesRecoverTrainning.getString("velocidadFinal", "0.0"));
			

            
            
		
            minutos=Long.valueOf((crono.getText().toString().substring(0,crono.getText().toString().indexOf(":"))));
            System.out.println("..............."+minutos);
            
			if(actividad.equalsIgnoreCase("Ciclismo")){
				
				if(velocidadFinal>=0 && velocidadFinal<=16){
					calorias=0.049*peso*2.2*minutos;
				}else {
					calorias=0.071*peso*2.2*minutos;
				}	
				
				
				
			}else if(actividad.equalsIgnoreCase("Correr")){
				
				System.out.println("peso:"+peso);
				System.out.println("distancia:"+String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+4));
				
				calorias=1.03*peso*Double.valueOf(String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+4));
			}
			String mitime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			double distanciafinal=Double.valueOf(String.valueOf(distanciaKM).substring(0, String.valueOf(distanciaKM).indexOf(".")+4));
			savedDataBaseSesion(nombre,mitime,basal, 60, calorias,  velocidadFinal, distanciafinal, minutos+"");
			
			
  		it=list.listIterator();
          while(it.hasNext()){
              loc = new Location((Location) it.next());
              
              //guardamos puntos longitud-latitud en BD
             // data.guardarPuntos(id, loc.getLongitude(), loc.getLatitude());
              savedDataBaseGPS(nombre,loc.getLatitude(),loc.getLongitude(),loc.getAltitude(),0.0,0.0,0.0);
              
          }

			//System.out.println("..............."+calorias);
			
			   //velocidad promedio
//			   velocidadfinal = (double) (distancia / elapsedMillis);
//			   velocidadfinal = (velocidadfinal / 1000) * 3600;			   
		 }
		
		private void savedDataBaseSesion(String name,String date,double basal_sistolica, double basal_diastolica,double calories, double average_speed,double total_distance, String total_time){
			
			DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
			//Inserta Usuarios
			db.setSesion(name, date, basal_sistolica, basal_diastolica, calories, average_speed, total_distance, total_time);
			db.closeDataBase();
		}
		
		private void savedDataBaseGPS(String name,double latitude,double longitude,double GPS_height,double partial_speed,double partial_distance,double partial_time){
			
			DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
			//Inserta Usuarios
			db.setGPS(name, latitude, longitude, GPS_height, partial_speed, partial_distance, partial_time);
			db.closeDataBase();
		}
}
