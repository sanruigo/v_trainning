
/**
 * This class lets to show the training results
 *
 * @author MNM
 *  
 * @version 1.0, 07/01/2013
 * 
 */

package project.v_trainning;

import android.os.Bundle;
import project.chart.ChartPlot;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import project.database.DataBase_vTrainning;

//import com.androidplot.xy.LineAndPointFormatter;
//import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
//import com.androidplot.xy.XYSeries;
import android.graphics.Color;

public class ResultsActivity extends Activity {
	
	TextView txtTiempo;
	TextView txtNombre;
	TextView txtDistancia;
	TextView txtCalorias;
	TextView txtFrecOptima;
	Button btnShare;
	Button btnMapa;
	ChartPlot plot;
	String activ="";
	String fecha="";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		createWidgets();
		//CargaDatos();
		plot= new CPlot(R.id.PlotResult);
		PlotResults();
		System.out.println("onCreate results");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		System.out.println("onResume result");
		super.onResume();
		CargaDatos();
		
	}

	public boolean onMenuItemClick(MenuItem item) {
		  return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menuResultCreditos:
	        	launchAboutActivity(findViewById(R.id.btnAjustActSetting));
	            return true;
	        case R.id.menuResultShare:
	        	compartir();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void launchAboutActivity(View view){
		startActivity(new Intent(ResultsActivity.this,AboutActivity.class));
		
	}
	


	private void createWidgets(){
		txtTiempo=(TextView)findViewById(R.id.txtViewTiempo);
		txtCalorias=(TextView)findViewById(R.id.txtViewCalorias);
		txtDistancia=(TextView)findViewById(R.id.txtViewDistRecorrida);
		txtFrecOptima=(TextView)findViewById(R.id.txtViewFrecOptima);
		txtNombre=(TextView)findViewById(R.id.txtViewNombre);
		
		btnShare=(Button)findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				compartir();
			}	
		});
		
		btnMapa=(Button)findViewById(R.id.btnMapa);
		btnMapa.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				mostrarMapa();
			}	
		});
	}
	
	private void mostrarMapa(){
		
	}
	
	private void PlotResults(){
		Number[] serie1={0, 10, 12, 13, 12, 15}; //reemplazar por valores de resultados en Y
		Number[] serie2={0, 1.2, 2.2, 3.3, 4.4, 5.5}; //reemplazar por valores de resultados en X
		
		//plot.setSerie(serie1);
		plot.setXYSeries(serie2, serie1, getResources().getString(R.string.txtVelocidad));
		plot.setSeriesFormat(Color.rgb(10, 200, 10), Color.BLACK, Color.LTGRAY);
		
		plot.Plot(getResources().getString(R.string.txtTiempo), serie1.length);
	}
	
	private void compartir(){
		String comp="";
		if (getResources().getString(R.string.idiomaApp).equals("espa�ol")){
			comp="He recorrido"+txtDistancia.getText().toString()+" durante "+txtTiempo.getText().toString()+
					" entrenando "+activ+", y quem� "+txtCalorias.getText().toString()+". V-Training Assistant "+fecha;
		}else{
			comp="I have raced "+txtDistancia.getText().toString()+" during "+txtTiempo.getText().toString()+
					activ+" training, and I burned "+txtCalorias.getText().toString()+". V-Training Assistant "+fecha;
		}
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, comp);
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.msgCompartir)));
	}
	
	public void CargaDatos(){
		int indice;
		String temp="";
		System.out.println("Empieza carga");
		int mode = Activity.MODE_PRIVATE;
		String MYPREFS_SETTINGS = "MyPreferencesSettings";
		SharedPreferences myPreferencesRecover;
		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		txtNombre.setText(myPreferencesRecover.getString("nombre", ""));

		DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
		// el resto de la carga	
		indice=db.getRows("select id_sesion from sesiones");
		System.out.println(db.toString());
		fecha=db.getSesionPerIdSesion(txtNombre.getText().toString().trim(), indice, 2);
		System.out.println(fecha);
		temp=db.getSesionPerIdSesion(txtNombre.getText().toString().trim(), indice, 5);
		System.out.println(temp);
		txtCalorias.setText(temp);
		temp=db.getSesionPerIdSesion(txtNombre.getText().toString().trim(), indice, 6);
		System.out.println(temp);
		txtDistancia.setText(temp);
		temp=db.getSesionPerIdSesion(txtNombre.getText().toString(), indice, 7);
		System.out.println(temp);
		txtTiempo.setText(temp);
		db.closeDataBase();
	}
	
	/**
	 * This private class inherits CharPlot class and add a constructor function to assign it the right view id
	 * @author MNM
	 * 
	 */
	private class CPlot extends ChartPlot{
		
		public CPlot(int idLayout){
			myXYPlot=(XYPlot)findViewById(idLayout);
		}
	}

}
