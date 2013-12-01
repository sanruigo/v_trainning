package project.v_trainning;

import com.androidplot.xy.XYPlot;

import project.chart.ChartPlot;
import project.database.DataBase_vTrainning;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;

public class MainActivity extends Activity {

	
	private XYPlot mySimpleXYPlot;//Variable para cargar el Plot
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		plotExample();
        
		//dbExample();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	
	public void plotExample(){
		
		// Creamos dos arrays de prueba. En el caso real debemos reemplazar
        // estos datos por los que realmente queremos mostrar
		Number[] series1Numbers = {1, 8, 5, 2, 7, 4};
		ChartPlot chP=new ChartPlot();
		chP.setSerie1(series1Numbers);
		chP.setXYSeries(chP.getSerie(), "Serie");
		chP.setSeriesFormat(Color.BLUE, Color.RED, 0);
		
		// Inicializamos el objeto XYPlot búscandolo desde el layout:
        mySimpleXYPlot = (XYPlot) findViewById(R.id.mySimpleXYPlot);
        // Una vez definida la serie (datos y estilo), la añadimos al panel
        mySimpleXYPlot.addSeries(chP.getSeries(), chP.getSeriesFormat());
		
	}
	
	
	
	public void dbExample(){
		//Abrimos la base de datos 'DBUsuarios' en modo escritura
		DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
        //Inserta Usuarios
        db.setUsuario("Jorge", 25, 60.4);
//        db.setUsuario("Luis", 25, 60.4);
//        db.setUsuario("Zambrano", 25, 60.4);
        
        
        
//////SESION 1        
        //Inserta Sesion con sus repectivas informaciones
       db.setSesion(db.getUsuarioName(1),"2013-05-30",120.0,60.0,1705.2,10.2,1019.0,"764");
       
        //Inserta la informacion GPS
        db.setGPS(db.getUsuarioName(1),39.4805899205, -0.3472319210 ,1.75,10.2,0,0);
        db.setGPS(db.getUsuarioName(1),39.4823495991, -0.3462663257 ,1.75,10.2,220.0,180);
        db.setGPS(db.getUsuarioName(1),39.4828340204, -0.3461375797 ,1.75,10.2,59.0,44);
        db.setGPS(db.getUsuarioName(1),39.4832418426, -0.3475081885 ,1.75,10.2,130.0,60);
        db.setGPS(db.getUsuarioName(1),39.4826187224, -0.3491443360 ,1.75,10.2,160.0,120);
        db.setGPS(db.getUsuarioName(1),39.4814925377, -0.3506302798 ,1.75,10.2,180.0,120);
        db.setGPS(db.getUsuarioName(1),39.4805899205, -0.3472319210 ,1.75,10.2,300.0,240);
        db.setGPS(db.getUsuarioName(1),39.4805899205, -0.3472319210 ,1.75,10.2,0,0);
        
//////////SESION 2
        
        //Inserta Sesion con sus repectivas informaciones
        db.setSesion(db.getUsuarioName(1),"2013-05-31",121.0,62.0,1805.4,11.2,279.0,"205");
      //Inserta la informacion GPS
        db.setGPS(db.getUsuarioName(1),39.4805899205, -0.3472319210 ,1.75,11.2,0,0);
        db.setGPS(db.getUsuarioName(1),39.4823495991, -0.3462663257 ,1.75,11.2,220.0,171);
        db.setGPS(db.getUsuarioName(1),39.4828340204, -0.3461375797 ,1.75,11.2,59.0,34);
        
        System.out.println("All names:"+db.getAllUsuarioName().toString());
        System.out.println("Usuario:"+db.getUsuarioName(1)+" Edad:"+db.getUsuarioAge(1)+" Peso:"+db.getUsuarioWeight(db.getUsuarioName(1)));
        System.out.println("Sesion 1 (Fecha):"+db.getSesionPerIdSesion(db.getUsuarioName(1), 1,0));
        System.out.println("Sesion por nombre"
        		+ "\nFecha:"+db.getSesionPerUser(db.getUsuarioName(1),0)+""
        		+ "\nbasal_sistolica:"+db.getSesionPerUser(db.getUsuarioName(1),1)+""
      			+ "\nbasal_diastolica:"+db.getSesionPerUser(db.getUsuarioName(1),2)+""
      			+ "\ncalorias_quemadas:"+db.getSesionPerUser(db.getUsuarioName(1),3)+""
      			+ "\nvelocidad_media:"+db.getSesionPerUser(db.getUsuarioName(1),4)+""
      			+ "\ndistancia_total:"+db.getSesionPerUser(db.getUsuarioName(1),5)+""
      			+ "\ntiempo_total:"+db.getSesionPerUser(db.getUsuarioName(1),6)+"");
        System.out.println("Sesion por Fecha (id): "+db.getSesionPerDate(db.getUsuarioName(1), "2013-05-31").toString());
        System.out.println("GPS: "+db.getGPS(db.getUsuarioName(1), db.getSesionPerDate(db.getUsuarioName(1), "2013-05-31").get(0), 1).toString());
	
        System.out.println("inicio Github");
        System.out.println("Otra linea para verificar cambios en github");
	}

	
	
}
