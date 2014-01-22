

/**
 * This class provides information about connection to database SQLite
 *
 * @author Jorge Zambrano
 * @author 
 * @version 1.0, 21/11/2013
 * @since 1.4
 */


package project.database;

import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase_vTrainning extends SQLiteOpenHelper {

	SQLiteDatabase dbRead;
	SQLiteDatabase dbWrite;
	/*Create table*/
	String foreingKeyOn="PRAGMA foreign_keys = ON";
	String sqlCreateTableUsuarios="CREATE TABLE usuarios "
			+ "(id_usuario INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "
			+ "nombre VARCHAR, "
			+ "edad INTEGER, "
			+ "peso DOUBLE," 
			+ "estatura DOUBLE)";
	
	String sqlCreateTableSesiones="CREATE TABLE sesiones "
			+ "(id_sesion INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL ,"
			+ "fecha DATETIME,"
			+ "basal_sistolica DOUBLE,"
			+ "basal_diastolica DOUBLE,"
			+ "calorias_quemadas DOUBLE,"
			+ "velocidad_media DOUBLE,"
			+ "distancia_total DOUBLE, "
			+ "tiempo_total DATETIME,  "
			+ "id_usuario INTEGER,"
			+ "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario))";
			
	String sqlCreateTableGPSS="CREATE TABLE gpss "
			+ "(id_gps INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
			+ "latitud DOUBLE, "
			+ "longitud DOUBLE,  "
			+ "altura_gps DOUBLE,"
			+ "velocidad_parcial DOUBLE,"
			+ "distancia_parcial DOUBLE,"
			+ "tiempo_parcial DOUBLE,"
			+ "id_sesion INTEGER,"
			+ "FOREIGN KEY(id_sesion) REFERENCES sesiones(id_sesion))";

	/*Drop tables*/
	String sqlDropTableGPSS="DROP TABLE IF EXISTS gpss";
	String sqlDropTableSesiones="DROP TABLE IF EXISTS sesiones";
	String sqlDropTableUsuarios="DROP TABLE IF EXISTS usuarios";
	/*Delete Secuence or autoincrement*/
	String sqlDeleteSecuenceGPSS="DELETE FROM sqlite_sequence WHERE name='gpss'";
	String sqlDeleteSecuenceSessiones="DELETE FROM sqlite_sequence WHERE name='sesiones'";
	String sqlDeleteSecuenceUsuarios="DELETE FROM sqlite_sequence WHERE name='usuarios'";
	String foreingKeyOff="PRAGMA foreign_keys = OFF";



	
	/**
	 * SQLite Constructor
	 * @param context
	 * @param dataBase_name
	 * @param factory
	 * @param version
	 * */
	public DataBase_vTrainning(Context context, String dataBase_name,CursorFactory factory, int version) {
	    super(context, dataBase_name, factory, version);
        //System.out.println("DataBase");
        dbRead=getReadableDatabase();
        dbWrite=getWritableDatabase();
	}
	@Override
	/**
	 * Creation DataBase one-time
	 * @param db Database 
	 * */
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//db.execSQL(sqlCreateTable);
		db.execSQL(foreingKeyOn);
		db.execSQL(sqlCreateTableUsuarios);
		db.execSQL(sqlCreateTableSesiones);
		db.execSQL(sqlCreateTableGPSS);
	}
	@Override
	/**
	 * Upgrade if only change version on Database
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 * */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 //Se elimina la versi�n anterior de la tabla
		db.execSQL(sqlDeleteSecuenceGPSS);
		db.execSQL(sqlDeleteSecuenceSessiones);
		db.execSQL(sqlDeleteSecuenceUsuarios);
		db.execSQL(foreingKeyOff);
		
 
        //Se crea la nueva versi�n de la tabla
		db.execSQL(foreingKeyOn);
		db.execSQL(sqlCreateTableUsuarios);
		db.execSQL(sqlCreateTableSesiones);
		db.execSQL(sqlCreateTableGPSS);
	}
	
	/**
	 * Close Database
	 * 
	 * */
    public void closeDataBase(){
    	
        //Cerramos la base de datos
        dbWrite.close();
        //Cerramos la base de datos
        dbRead.close();
    }
    
    /**
     * return us the number of rows, depend each query
     * @param rowQuerry it's a query 
     * 
     * @return rows number of rows
     * */
    public int getRows(String rowQuery){
    	int rows=0;
    	if(dbRead != null)
        { 
    		Cursor cursor=dbRead.rawQuery(rowQuery,null);
    		while(cursor.moveToNext()){
    			rows=Integer.valueOf(cursor.getString(0));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return rows;
    }

    
    /**
     * Insert a user in the Database
     * @param name user's name
     * @param age user's age
     * @param weight user's weight
     * @return isInsert if this transaction was successful
     * */
   
    public boolean setUsuario(String name, int age,double weight,double height){
    	boolean isInsert=true;
       		String rowQuerry="INSERT INTO usuarios (id_usuario,nombre,edad,peso,estatura) VALUES(null,'"+name+"',"+age+","+weight+","+height+")"; 
    		
    		dbWrite.execSQL(rowQuerry);
    		
            //Cerramos la base de datos aqui o al momento de retornar
            //dbWrite.close();
        
    	return isInsert;
    }
    
    /**
     * Update a user in the Database
     * @param name user's name
     * @param age user's age
     * @param weight user's weight
     * @return isUpdate if this transaction was successful
     * */
    public boolean updateUsuario(String name, int age,double weight,double height){
    	boolean isUpdate=true;
    	String rowQuerry="update usuarios set nombre='"+name+"',edad='"+age+"',peso='"+weight+"',estatura='"+height+"' where id_usuario=1";
    	dbWrite.execSQL(rowQuerry);
    	
    	return isUpdate;
    }
    
    /**
     * return user's name
     * @param row user's row
     * @return name user's name
     * */
    public String getUsuarioName(int row){  	
    	String name="";
      	if(dbRead != null)
        {
    		String rowQuerry="select nombre from usuarios where id_usuario="+row; 
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		if(cursor.move(row)){
    			//System.out.println("name:"+cursor.getString(0));
    			name=cursor.getString(0);
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return name;
    }
    
    /**
     * return all user's name
     * @param row user's row
     * @return name user's name
     * */
    public Vector<String> getAllUsuarioName(){  	
    	Vector<String> names=new Vector<String>();
      	if(dbRead != null)
        {
    		String rowQuerry="select nombre from usuarios"; 
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			//System.out.println("name:"+cursor.getString(0));
    			names.add(cursor.getString(0));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return names;
    }
    
    /**
     * return user's age
     * @param row user's row
     * @return name user's age
     * */
    public String getUsuarioAge(int row){  	
    	String age="";
      	if(dbRead != null)
        {
    		String rowQuerry="select edad from usuarios where id_usuario="+row; 
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		if(cursor.move(row)){
    			//System.out.println("name:"+cursor.getString(0));
    			age=cursor.getString(0);
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return age;
    }
    
    /**
     * return user's weight
     * @param name user's name
     * @return weight user's weight
     * */
    public String getUsuarioWeight(String name){  	
    	String weight="";
      	if(dbRead != null)
        {
    		String rowQuerry="select peso from usuarios where nombre='"+name+"'"; 
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			//System.out.println("name:"+cursor.getString(0));
    			weight=cursor.getString(0);
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return weight;
    }
    
    /**
     * return user's weight
     * @param name user's name
     * @return weight user's weight
     * */
    public String getUsuarioHeight(String name){  	
    	String height="";
      	if(dbRead != null)
        {
    		String rowQuerry="select estatura from usuarios where nombre='"+name+"'"; 
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			//System.out.println("name:"+cursor.getString(0));
    			height=cursor.getString(0);
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return height;
    }
    
    /**
     * Insert a user's session in the Database
     * @param name user's name
     * @param date user's date when begun the session
     * @param basal_sistolica systolic blood pressure
     * @param basal_diastolica diastolic pressure
     * @param calories calories burned during the session
     * @param average_speed average velocity in the session
     * @param total_distance total distance in the session
     * @param total_time total time in the session
     * @return isInsert if this transaction was successful
     * */
   
    public boolean setSesion(String name,String date,double basal_sistolica, double basal_diastolica,double calories, double average_speed,double total_distance, String total_time){
    	boolean isInsert=true;
       		String rowQuerry="INSERT INTO sesiones "
       				+ "(id_sesion,fecha,basal_sistolica,basal_diastolica,calorias_quemadas,velocidad_media,distancia_total,tiempo_total,id_usuario) "
       				+ "VALUES(null,'"+date+"',"+basal_sistolica+","+basal_diastolica+","+calories+","+average_speed+","+total_distance+","+total_time+","+getRows("select id_usuario from usuarios where nombre='"+name+"'")+");"; 
       		
       		dbWrite.execSQL(rowQuerry);
       		
    		
            //Cerramos la base de datos aqui o al momento de retornar
            //dbWrite.close();
        
    	return isInsert;
    }
    
    /**
     * return user's session per Id Session
     * @param name user's name
     * @param idSession user's idSession
     * @param column fecha 
     * 				basal_sistolica
     * 				basal_diastolica
     * 				calorias_quemadas
     * 				velocidad_media
     * 				distancia_total
     * 				tiempo_total
     * @return session user's session
     * */
    public String getSesionPerIdSesion(String name,int idSesion,int colum){  	
    	String sesion="";
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.fecha,sesiones.basal_sistolica,sesiones.basal_diastolica,sesiones.calorias_quemadas,sesiones.velocidad_media,sesiones.distancia_total,sesiones.tiempo_total from usuarios,sesiones where usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='"+name+"' and sesiones.id_sesion="+idSesion; 
    		int row=getRows("select count(*) from usuarios,sesiones where usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='"+name+"' and sesiones.id_sesion="+idSesion);
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		if(cursor.move(row)){
    			//System.out.println("name:"+cursor.getString(0));
    			sesion=cursor.getString(colum);
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return sesion;
    }
    
    /**
     * return user's session per User
     * @param name user's name
     * @param column fecha 
     * 				basal_sistolica
     * 				basal_diastolica
     * 				calorias_quemadas
     * 				velocidad_media
     * 				distancia_total
     * 				tiempo_total
     * @return session user's session
     * */
    public String getSesionPerUser(String name,int column){  	
    	String sesion="";
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.fecha,sesiones.basal_sistolica,sesiones.basal_diastolica,sesiones.calorias_quemadas,sesiones.velocidad_media,sesiones.distancia_total,sesiones.tiempo_total "
    				+ "from usuarios,sesiones where usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='"+name+"'"; 
    		int row=getRows("select count(*) from usuarios,sesiones where usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='"+name+"'");
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		if(cursor.move(row)){
    			//System.out.println("name:"+cursor.getString(0));
    			
    			sesion=cursor.getString(column);
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return sesion;
    }
    
    /**
     * return user's ID session per Date
     * @param name user's name
     * @param column fecha 
     * 				basal_sistolica
     * 				basal_diastolica
     * 				calorias_quemadas
     * 				velocidad_media
     * 				distancia_total
     * 				tiempo_total
     * @return idSession user's ID session
     * */
    public Vector<Integer> getSesionPerDate(String name,String date){  	
    	Vector<Integer> idSession=new Vector<Integer>();
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.id_sesion from usuarios,sesiones where usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='"+name+"' and sesiones.fecha='"+date+"'"; 
    		
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			idSession.add(Integer.valueOf(cursor.getString(0)));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return idSession;
    }
    
    /**
     * return all the sessions dates
     * 
     * @return sDates the sessions dates
     * */
    public Vector<String> getAllSesionDate(){  	
    	Vector<String> sDates=new Vector<String>();
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.fecha from sesiones"; 
    		
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			sDates.add(cursor.getString(0).substring(0,10));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return sDates;
    }
    
    /**
     * return all the sessions distances
     * 
     * @return sDistances the sessions distances
     * */
    public Vector<Double> getAllSesionDistance(){  	
    	Vector<Double> sDistances=new Vector<Double>();
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.distancia_total from sesiones"; 
    		
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			sDistances.add(cursor.getDouble(0));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return sDistances;
    }
    
    /**
     * return all the burned calories by sessions 
     * 
     * @return sCalories the burned calories by sessions 
     * */
    public Vector<Double> getAllSesionCal(){  	
    	Vector<Double> sCalories=new Vector<Double>();
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.calorias_quemadas from sesiones"; 
    		
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			sCalories.add(cursor.getDouble(0));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return sCalories;
    }
    
    /**
     * return all the sessions times
     * 
     * @return sDates the sessions dates
     * */
    public Vector<String> getAllSesionTime(){  	
    	Vector<String> sDates=new Vector<String>();
      	if(dbRead != null)
        {
    		String rowQuerry="select sesiones.tiempo_total from sesiones"; 
    		
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			sDates.add(cursor.getString(0));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return sDates;
    }
    
    /**
     * Insert a user's GPS in the Database
     * @param name user's name
     * @param latitude user's location in latitude
     * @param longitude user's location in latitude
     * @param GPS_height height above sea level
     * @param partial_speed partial velocity in the gps
     * @param partial_distance partial distance in the gps
     * @param partial_time partial time in the gps
     * @return isInsert if this transaction was successful
     * */
    public boolean setGPS(String name,double latitude,double longitude,double GPS_height,double partial_speed,double partial_distance,double partial_time){
    	boolean isInsert=true;
       		String rowQuerry="INSERT INTO gpss "
       				+ "(id_gps,latitud,longitud,altura_gps,velocidad_parcial,distancia_parcial,tiempo_parcial,id_sesion) "
       				+ "VALUES(null,"+latitude+","+longitude+","+GPS_height+","+partial_speed+","+partial_distance+","+partial_time+","+getRows("select count(*) from usuarios,sesiones where usuarios.nombre='"+name+"'")+");"; 
       		
       		dbWrite.execSQL(rowQuerry);
    		
       		//id_gps,latitud,longitud,altura_gps,velocidad_parcial,distancia_parcial,tiempo_parcial,id_sesion
       		
       		
            //Cerramos la base de datos aqui o al momento de retornar
            //dbWrite.close();
        
    	return isInsert;
    }
    
    /**
     * return user's session per User
     * @param name user's name
     * @param session number of session
     * @param column latitud,
     * 				longitud,
     * 				altura_gps,
     * 				velocidad_parcial,
     * 				distancia_parcial,
     * 				tiempo_parcial
     * @return session user's session
     * */
    public Vector<Double> getGPS(String name,int session,int column){  	
    	Vector<Double> gps=new Vector<Double>();
      	if(dbRead != null)
        {
    		String rowQuerry="select gpss.latitud,gpss.longitud,gpss.altura_gps,gpss.velocidad_parcial,gpss.distancia_parcial,gpss.tiempo_parcial "
    				+ "from gpss,sesiones,usuarios where sesiones.id_sesion=gpss.id_sesion and usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='"+name+"' and sesiones.id_sesion="+session; 
    		Cursor cursor=dbRead.rawQuery(rowQuerry,null);
    		while(cursor.moveToNext()){
    			gps.add(Double.valueOf(cursor.getString(column)));
    		}
    		//Cerramos el cursor
    		cursor.close();
        }
    	return gps;
    }
    
    /**
     * select * from usuarios,sesiones,gpss where usuarios.id_usuario=sesiones.id_usuario and sesiones.id_sesion=gpss.id_sesion and usuarios.nombre='runner1' and gpss.id_sesion=1
     *
     *select sesiones.fecha,sesiones.basal_sistolica,sesiones.basal_diastolica,sesiones.calorias_quemadas,sesiones.velocidad_media,sesiones.distancia_total,sesiones.tiempo_total from usuarios,sesiones where usuarios.id_usuario=sesiones.id_usuario and usuarios.nombre='runner1' and sesiones.id_sesion=1
     * */
}
