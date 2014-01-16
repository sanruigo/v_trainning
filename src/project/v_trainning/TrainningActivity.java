package project.v_trainning;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	TextView txtVelocidad;
	Button btnTrainActStart,btnTrainActPause;
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
	Double velocidadfinal;
	Double velocidadLap;
	
	Float distancia=(float) 0;
	Float distanciaKM = (float) 0;
	Float distanciaLap = (float) 0;
	Float calReto;
	DecimalFormat df = new DecimalFormat("0.00"); 
	SimpleDateFormat dfo = new SimpleDateFormat("00:00");
	Integer altitud;
	Bundle recuperacionpeso; //recuperaci�n param
	Integer peso; //recuperaci�n param
	Integer kg=90; //variable peso en kg si no se introduce por parte de usuario
	Float calorias =(float) 0;
	Float caloriastotal = (float) 0;
	Long memoCrono;
	Long elapsedMillis;
	Long elapsedMillisLapInicial=(long) 0;
	Long elapsedMillisLapFinal=(long) 0;
	Long diferenciatime;
	long mitime;
	Double elapsedMillis2;
	Long elapsedMillis3;
	Double elapsedMillis4;
	Integer km=1;
	Integer distareto;
	
	TextView distance;
	TextView speed;
	TextView alt;
	TextView ritmoact;
	TextView cal;
	TextView usuario;
	//EditText etxtPulso;
	
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
	              isTrainingActive=true;

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
				isTrainingActive=false;
				crono.stop();
				btnTrainActStart.setAlpha(1);
				btnTrainActStart.setEnabled(true);
				btnTrainActPause.setAlpha((float) 0.5);
				btnTrainActPause.setEnabled(false);
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
	
		myEditor.commit();	
		
	}


	private void showSavedPreferencesSettings() {
		// TODO Auto-generated method stub
		String temp;

		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		System.out.println(myPreferencesRecover.getString("nombre_actividad", "").toString());
		txtActividad.setText(myPreferencesRecover.getString("nombre_actividad", "").toString());
		temp=myPreferencesRecover.getString("completo", "").toString();
		isActivatedAjustes=Boolean.valueOf(temp);
		//actividad.setText(myPreferencesRecover.getString("nombre_actividad", ""));
		myPreferencesRecoverTrainning=getSharedPreferences(MYPREFS_TRAINING,mode);
		
		isTrainingActive=myPreferencesRecoverTrainning.getBoolean("estadoTimer", false);

	}
	
	
//	
//	/**
//	 * startTraining inicia o termina la funci�n de entrenmiento
//	 * @param view
//	 */
//	public void startTraining(){
//		System.out.println("trainingactive="+isTrainingActive);
//		if (!isTrainingActive){
//			System.out.println("ActivedAjustes="+isActivatedAjustes);
//			if (isActivatedAjustes){
//				System.out.println("Verificando si GPS esta activo");
//				if (gps.isGPSactive()){
//					isTrainingActive=true;
//					TM.run();
//				
//					
//					
//				}else{
//					Toast.makeText(getApplicationContext(), R.string.msgNoGPS, Toast.LENGTH_LONG).show();
//				}
//			}else{
//				Toast.makeText(getApplicationContext(), R.string.msgNoAjustes, Toast.LENGTH_LONG).show();
//			}
//		}
//		else{
//			isTrainingActive=false;
//			TM.showResults();
//		}
//		
//		
//	}
//	
//	/**
//	 *
//	 * @author MNM
//	 * clase encargada del monitoreo de la actividad
//	 */
//	public class TrainingMonitor extends Thread{
//		
//		private String Actividad;
//		private boolean isActive;
//		
//		TrainingMonitor(String activ){
//			Actividad=activ;
//			isActive=false;
//			Tiempo.clear();
//			Velocidad.clear();
//			Distancia.clear();
//			Tiempo.add(Long.parseLong("0"));
//			Velocidad.add(Float.parseFloat("0"));
//			Distancia.add(Float.parseFloat("0"));
//			index=0;
//		}
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			super.run();
//			startTrainingMonitoring();
//			//startTime();
//		}
//
//		@Override
//		public synchronized void start() {
//			// TODO Auto-generated method stub
//			super.start();
//		}
//
//		@Override
//		public void interrupt() {
//			// TODO Auto-generated method stub
//			super.interrupt();
//			isActive=false;
//		}
//
//		/**
//	 	* startTrainingMonitoring ejecuta el monitoreo secuencial del recorrido
//	 	*/
//		public void startTrainingMonitoring(){
//			isActive=true;
//			while (isActive){
//				try {
//					sleep(5000);
//					index=index+1;
//					Tiempo.add((long) (index*5));
//					gps.localizador();
//					Velocidad.add(gps.getLastSpeed());
//					Distancia.add(gps.getLastDistance());
//					//txtTiempo.setText(String.valueOf(Tiempo.get(index)));
//					txtDistancia.setText(String.valueOf(Distancia.get(index)));
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			
//		}
//	
//		/**
//	 	* showResults muestra los resultados al volver a pulsar el boton play
//	 	*/
//		public void showResults(){
//			this.interrupt();
//		
//			
//		}
//
//
//	
//		
//	}
//	
//	
//	
//
//
//	public void startGPS(){
//		
//		
//		pointsCuantity = 0;
//		minTime = 30000;
//		minDist = 1;
//
//		
//
//		manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//		log("Esperando recepcion GPS");
//
//		
//		//Esta localizacion esta cacheada, no hay que mostrarla, es solo para comenzar mas rapido la recepcion GPS
//		Location lastKnownLocation = manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//		muestraLocaliz(lastKnownLocation);
//
//		manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,	minDist, this);
//	}
//	
//	
//	@Override
//	public void onLocationChanged(Location location) {
//		// TODO Auto-generated method stub
//		log("Nueva localizaciÛn: ");
//
//		if (pointsCuantity == 1) {
//			
//			lastLoc = new Location(location);
//			
//		}
//
//		muestraLocaliz(location);
//	}
//
//	@Override
//	public void onProviderDisabled(String proveedor) {
//		// TODO Auto-generated method stub
//		log("Proveedor deshabilitado: " + proveedor + "\n");
//
//	}
//
//	@Override
//	public void onProviderEnabled(String proveedor) {
//		// TODO Auto-generated method stub
//		log("Proveedor habilitado: " + proveedor + "\n");
//
//	}
//
//	@Override
//	public void onStatusChanged(String proveedor, int estado, Bundle extras) {
// 
//		// TODO Auto-generated method stub
//		log("Cambia estado proveedor: " + proveedor + ", estado="
//				+ E[Math.max(0, estado)] + "\n");
//	}
//	
//	
//	// MÈtodos para mostrar informaciÛn
//
//		private void log(String cadena) {
//
//			//salida.append(cadena + "\n");
//
//		}
//
//		private void muestraLocaliz(Location localizacion) {
//
//			if (localizacion == null)
//
//				log("LocalizaciÛn desconocida\n");
//
//			else
//
//			if (pointsCuantity <= 1) {
//				distance = 0;
//
//				if (pointsCuantity == 1)
//					log("FirstLocation");
//
//				if (pointsCuantity == 0)
//					log("LastKnownLocation");
//			}
//
//			else {
//				distance = localizacion.distanceTo(lastLoc);
//				lastLoc = new Location(localizacion);
//			}
//
//			
//			
//			//los datos a guardar son los sigientes, mas la distance (0 para el primer punto y la diferencia entre los anteriores para el resto
//			//solo se necesita guardar a partir de pointsCuantity = 1, la que el 0 es el LastKnownLocation que es la cacheada
//			latitude = localizacion.getLatitude();
//			altitude = localizacion.getAltitude();
//			longitude = localizacion.getLongitude();
//			velocity = localizacion.getSpeed();
//			time = localizacion.getTime();
//
//			log("Localizacion[ " + "Orden=" + pointsCuantity + ", Latitud(grados)="
//					+ latitude +
//
//					", Longitud(grados)=" + longitude +
//
//					", Altitud(metros)=" + altitude +
//
//					", Velocidad(m/s)=" + velocity +
//
//					", Distancia(metros)=" + velocity +
//
//					", Tiempo(UTC millisegundos)=" + time + " ]\n");
//			
//			pointsCuantity++;
//
//		}
//	
//		public float getLastDistance(){
//			return distance;
//		}
//		public float getLastSpeed(){
//			return velocity;
//		}





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

			            //llamamos a la funci�n que nos calcula la distancia
			            distancefunc(location);
			           	            
			            //ALTITUD 
			            //altitud = (int) location.getAltitude();//RETORNA UN DOUBLE
			           
			           
			            //llamamos a la tarea as�ncrona
			            task task = new task();
					    task.execute();
		  
					    
//			            //C�LCULO DE CALOR�AS Y RECUPERACI�N DEL PASO DE PAR�METROS DESDE AJUSTES DE USUARIO
//					    misPreferencias = getSharedPreferences(MYPREFS,mode);			    
//					    peso = misPreferencias.getInt("peso", 90);	   			   			
//					    calReto = misPreferencias.getFloat("calorias", 0);
//					   			    
//					    if(peso == 90){	 //no hay preferencias de usuario--no introdujo sus datos   	
//					    	if(velocidadKM <= 5) calorias =  (float) ((0.1 * kg)/60); 	            
//		                	else if(velocidadKM > 5.6 && velocidadKM <= 7.5) calorias = (float) ((0.2 * kg)/60);
//		                	else if(velocidadKM > 7.6 ) calorias = (float) ((0.3 * kg)/60);
//		                
//					    }else{		   //usuario introdujo sus datos  	
//					    	
//					    	if(velocidadKM <= 5) calorias =  (float) ((0.1 * peso)/60); 	            
//					    	else if(velocidadKM > 5.6 && velocidadKM <= 7.5) calorias = (float) ((0.2 * peso)/60);
//					    	else if(velocidadKM > 7.6 ) calorias = (float) ((0.3 * peso)/60);
//					    }			    	
//					    	caloriastotal = caloriastotal + calorias; //c�lculo del valor de calor�as consumidas
//					    	
//					    	//comprobaci�n si consigue reto de calor�as
//					    	if(repeat == 1 && calReto != 0  ){ //tenemos reto	
//		                			if(caloriastotal > calReto){              				
//		                				avisoRetoCompletado();
//		                				repeat = 0;
//		                				
//		                			}			    		
//					    	}
					    	
					    	
					   //calculo de reto tiempo
					   //comprobaci�n si consigue reto del tiempo
//						   misPreferencias = getSharedPreferences(MYPREFS,mode);		    
//						   timereto = misPreferencias.getLong("duracion", 0);
//						   elapsedMillis = (SystemClock.elapsedRealtime() - crono.getBase()) / 1000;
//						   elapsedMillis3 = elapsedMillis / 60; // me quedo con los minutos
//						   				  
//					    	if(repeat == 1 && timereto != 0  ){ //tenemos reto	
//				       			if(elapsedMillis3 >= timereto){              				
//				       				avisoRetoCompletado();
//				       				repeat = 0;
//					       				
//					       		}			    		
//						   	}
						    	

			            //C�LCULO DEL RITMO MEDIO ACTUAL			   			    
					     elapsedMillis = (SystemClock.elapsedRealtime() - crono.getBase())/1000;
					     elapsedMillisLapFinal = elapsedMillis;
					     diferenciatime = elapsedMillisLapFinal - elapsedMillisLapInicial;
				
			            if(estado == "activo"){
			            	ritmomedioactual = (double) ((diferenciatime  / distanciaLap));
			            	//conversi�n para mostrar minutos y segundos que llevamos en la vuelta actual por km
			            	ritmomedioactual = (ritmomedioactual / 60)*1000;
			            }            	          	                      
			            String chronoText2 = String.valueOf(ritmomedioactual);  
			            //LE PASAMOS A STRING EL VALOR DEL RITMOMEDIOACTUAL QUE LLEVAMOS	            
			            String[] array = chronoText2.split("\\.");   //SEPARAMOS LAS CIFRAS POR EL "." PARA SACAR MINUTOS Y SEGUNDOS       	            
			              if (array.length == 2) {           	  
			            	  //acotar y transformar decimales para no pasar de 60 seg y sacar minutos           	  
			            	  array[1]=array[1].substring(0, 3); //ACOTA A 3 DECIMALES
			            	  //convertimos a double para poder realizar y sacar el valor correcto tipo :04 segs
			            	  Double segundos = (double) Integer.parseInt(array[1]);
			            	  segundos = segundos*(double)6/(double)10000;
			            	  String seeg = String.valueOf(segundos);  
			            	  String[] array2 = seeg.split("\\."); 
			            	  Integer minutos = Integer.parseInt(array[0]); 
			            	  //HAY QUE PASARLOS A STRINGS PARA HACER LA CONCATENACI�N DE VALORES Y MOSTRAR EN TEXTVIEW
			            	  String min = String.valueOf(minutos);  
			            	 
			            	  if(array2[1].length()>2) array2[1] = array2[1].substring(0,2);            	  	
			            	 
			            	  //CONCATENACI�N DE LOS MINUTOS Y SEGUNDOS PARA LUEGO MOSTRAR RESULTADO EN TEXTVIEW
			            	  ritmo = min.concat(":").concat(array2[1]);	 
			               }
			              
			              //guardar los laps en la bd
			              if(distanciaLap >= 10){ //en metros
				           	   velocidadLap = (double) (distanciaLap / diferenciatime);
				    		   velocidadLap = (velocidadLap / 1000) * 3600;
				               //data.guardarLaps(id, km, ritmo, velocidadLap);
				    		   km = km + 1;
				    		   distanciaLap = (float) 0;
				               elapsedMillisLapInicial = elapsedMillisLapFinal;
				          }	              
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
		        locationAux = locationDis;
		        
		        //comprobaci�n si consigue reto de distancia
//		    	if(repeat == 1 && distareto != 0  ){ //tenemos reto	
//		    			if(distancia > distareto){              				
//		    				avisoRetoCompletado();
//		    				repeat = 0;
//		    				
//		    			}			    		
//		    	}
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
				   
				   distance.setText(String.valueOf(df.format(distanciaKM)));
				   System.out.println(distanciaKM);
				   //speed.setText(String.valueOf(df.format(velocidadKM)));  			  
				   //alt.setText(String.valueOf(altitud));			  
				   //ritmoact.setText(ritmo);	
				   //int calo = (int) (Math.round(caloriastotal));
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
			 
						 
}
