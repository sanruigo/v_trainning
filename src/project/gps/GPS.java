package project.gps;



import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

/**
 * This class implements GPS methods.
 * @author Daniel Aparicio
 *
 */
public class GPS {
	/***
	 * @param E
	 *            array de strings con posibles estados del gps
	 */
	private static  String[] E = { "fuera de servicio",
			"temporalmente no disponible ", "disponible" };

	private static  String LOCATION_SERVICE = null;

	/***
	 * @param manejador, listener
	 *            manejador y listener del gps
	 */
	private Activity act;
	private LocationManager manejador;
	
	
	/*ATENCION
	 * El LocationListener es el que hay que programar para que se obtengan los cambios de 
	 * posicionamiento y localizacion a traves del GPS 
	 * (Marlon)
	 */
	private LocationListener listener;/* = new LocationListener();{
		public void onLocationChanged(Location location) {

			log("Nueva localizacion: ");

			if (pointsCuantity == 1) {
				
				lastLoc = new Location(location);
				
			}

			guardaLozaliz(location);

		}

		public void onProviderDisabled(String proveedor) {

			log("Proveedor deshabilitado: " + proveedor + "\n");

		}

		public void onProviderEnabled(String proveedor) {

			log("Proveedor habilitado: " + proveedor + "\n");

		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}

	  };*/
	
	
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

	public GPS(Activity activ){
		act=activ;
		manejador = (LocationManager) act.getSystemService(LOCATION_SERVICE);
	}
	
	
	/**
	 * @author MNM
	 * verifica que el gps este activo y funcionando
	 */
	public boolean isGPSactive(){
		boolean rtn=false;
			if (manejador==null){
				manejador=(LocationManager)act.getSystemService(Context.LOCATION_SERVICE);
				System.out.println("manejador era null");
			}
			if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				rtn=true;
				System.out.println("gps activo");
					
			}
		return rtn;
	}
	
	public void localizador(){
		
		pointsCuantity = 0;
		minTime = 30000;
		minDist = 1;

		//salida = (TextView) findViewById(R.id.salida);

		

		log("Esperando recepcion GPS");

		
		//Esta localizacion esta cacheada, no hay que mostrarla, es solo para comenzar mas rapido la recepcion GPS
		Location lastKnownLocation = manejador.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		guardaLozaliz(lastKnownLocation);

		//manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,	minDist, this);
	}
	
	
	public void onLocationChanged(Location location) {

		log("Nueva localizacion: ");

		if (pointsCuantity == 1) {
			
			lastLoc = new Location(location);
			
		}

		guardaLozaliz(location);

	}

	public void onProviderDisabled(String proveedor) {

		log("Proveedor deshabilitado: " + proveedor + "\n");

	}

	public void onProviderEnabled(String proveedor) {

		log("Proveedor habilitado: " + proveedor + "\n");

	}

//	public void onStatusChanged(String proveedor, int estado, Bundle extras) {
//
//		log("Cambia estado proveedor: " + proveedor + ", estado="
//				+ E[Math.max(0, estado)] + "\n");
//
//	}
//	
	// M�todos para mostrar informaci�n

		private void log(String cadena) {

			//salida.append(cadena + "\n");

		}

		private void guardaLozaliz(Location localizacion) {
			System.out.println("Localizacion:"+localizacion);
			if (localizacion == null)

				log("Localizaci�n desconocida\n");

			else{

				if (pointsCuantity <= 1) {
					distance = 0;

					if (pointsCuantity == 1)
						log("FirstLocation");

					if (pointsCuantity == 0)
						log("LastKnownLocation");

					latitude = localizacion.getLatitude();
					altitude = localizacion.getAltitude();
					longitude = localizacion.getLongitude();
					velocity = localizacion.getSpeed();
					time = localizacion.getTime();


				}else {
					distance = localizacion.distanceTo(lastLoc);
					lastLoc = new Location(localizacion);
				}
			}	
			
			//los datos a guardar son los sigientes, mas la distance (0 para el primer punto y la diferencia entre los anteriores para el resto
			//solo se necesita guardar a partir de pointsCuantity = 1, la que el 0 es el LastKnownLocation que es la cacheada
			
			//Comentario de Daniel para la base de datos, en este punto almacenamos las variables en la base de datos, no podemos acceder a ellas desde 
			//otra clase ya que son privadas y volatiles, no tiene sentido usar setters y getters ya que nunca se llamaran desde otra clase, desde otra clase solo
			//se podra acceder a las variables que hemos almacenado en la base de datos, ya que estas solo viven 30 segundos para almacenarse 
			//y despues mueren, si quieres obtener los datos de estas variables desde otra clase tendrias que sincronizarlas para que
			//obtengan los valores dentro de los 30 segundos en los que no estan siendo modificadas.
			//Desde aqui podemos acceder a cualquier otra clase, y usar setters para otra clase y guardar estas variables locales en las variables
			//de la clase a la que queramos enviar la informacion, pero es mas recomendable guardarlas en la base de datos y recuperarlas desde alli
			//tambien podemos obtener los datos de usuario y de sesion necesarios para guardar estos datos en la base de datos correctamente.
			
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
			
			pointsCuantity++;

		}
		
	//Agregado por Marlon para prueba	
		public float getLastDistance(){
			return distance;
		}
		public float getLastSpeed(){
			return velocity;
		}
		
		
//		//Modificado Jorge Zambrano
//		
		// no valido
		
//		public Double getLatiude(){
//			return localizacion.getLatitude();
//		}
//
//		public Double getAltitude(){
//			return localizacion.getAltitude();
//		}
//		public Double getLongitude(){
//			return localizacion.getLongitude();
//		}
//		public Double getSpeed(){
//			return localizacion.getSpeed();
//		}
//		public Double getTime(){
//			return localizacion.getTime();
//		}




}
