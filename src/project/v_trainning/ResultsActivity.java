

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

/**
 * This class lets to show the training results
 *
 * @author Marlon Navia
 *  
 * @version 1.0, 07/01/2013
 * 
 */
public class ResultsActivity extends Activity {
	
	TextView txtTiempo;
	TextView txtNombre;
	TextView txtDistancia;
	TextView txtCalorias;
	TextView txtFrecOptima;
	Button btnShare;
	//Button btnMapa;
	ChartPlot plot;
	String activ="";
	String fecha="";
	private Number[] serie1; //resultados graficados en Y
	private Number[] serie2; //resultados graficados en X
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		createWidgets();
		//CargaDatos();
		plot= new CPlot(R.id.PlotResult);
		System.out.println("onCreate results");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}
	/**
	 * Do the corresponding task when the activity gets focus.
	 * These include CargaDatos and PlotResults.
	 */
	@Override
	protected void onResume() {
		// Auto-generated method stub
		System.out.println("onResume result");
		super.onResume();
		CargaDatos();
		PlotResults();
		
	}

	/**
	 * The default action on menu select item.
	 * @param item
	 * @return true
	 */
	public boolean onMenuItemClick(MenuItem item) {
		  return true;
	}
	
	/**
	 * Define the menu actions for this activity.
	 * @param item
	 * The menu selected item.
	 * 
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
        case R.id.menuResultResum:
        	launchResumenActivity(findViewById(R.id.btnAjustActResum));
            return true;
        case R.id.menuResultTrain:
        	launchTrainningActivity(findViewById(R.id.btnAjustActTrain));
            return true;
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
	
	/**
	 * Launch the About Activity.
	 * @param view
	 */
	public void launchAboutActivity(View view){
		startActivity(new Intent(ResultsActivity.this,AboutActivity.class));
		
	}
	
	/**
	 * Launch the Resumen Activity.
	 * @param view
	 */
	public void launchResumenActivity(View view){
		finish();
		startActivity(new Intent(ResultsActivity.this,ResumenActivity.class));
		
	}
	
	/**
	 * Launch the Training Activity, finishing this.
	 * @param view
	 */
	public void launchTrainningActivity(View view){
		finish();
	}

	/**
	 * Create the activity widgets. 
	 */
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
		
		/*btnMapa=(Button)findViewById(R.id.btnMapa);
		btnMapa.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				mostrarMapa();
			}	
		});*/
	}
	
	/*private void mostrarMapa(){
		
	}*/
	
	/**
	 * Draw the graphic in the respective frame, using the CPlot class. 
	 */
	private void PlotResults(){
		//Number[] serie1={0, 10, 12, 13, 12, 15}; //reemplazar por valores de resultados en Y
		//Number[] serie2={0, 1.2, 2.2, 3.3, 4.4, 5.5}; //reemplazar por valores de resultados en X
		
		//plot.setSerie(serie1);
		plot.setXYSeries(serie2, serie1, getResources().getString(R.string.txtVelocidad));
		plot.setSeriesFormat(Color.rgb(10, 200, 10), Color.BLACK, Color.LTGRAY);
		
		plot.Plot(getResources().getString(R.string.txtTiempo), serie1.length);
	}
	
	/**
	 * Let share a brief of the training result by message.
	 */
	private void compartir(){
		String comp="";
		if (getResources().getString(R.string.idiomaApp).equals("español")){
			comp="He recorrido "+txtDistancia.getText().toString()+"km durante "+txtTiempo.getText().toString()+
					" entrenando "+activ+", y quemé "+txtCalorias.getText().toString()+". V-Training Assistant "+fecha;
		}else{
			comp="I have raced "+txtDistancia.getText().toString()+"km during "+txtTiempo.getText().toString()+
					activ+" training, and I burned "+txtCalorias.getText().toString()+". V-Training Assistant "+fecha;
		}
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, comp);
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.msgCompartir)));
	}
	
	/**
	 * Load the results data of the last training, including a graphic for the speed. 
	 */
	public void CargaDatos(){
		int indice;
		String temp="",nombre;
		System.out.println("Empieza carga");
		int mode = Activity.MODE_PRIVATE;
		String MYPREFS_SETTINGS = "MyPreferencesSettings";
		SharedPreferences myPreferencesRecover,myPreferencesRecover2;
		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		txtNombre.setText(myPreferencesRecover.getString("nombre", ""));
		activ=myPreferencesRecover.getString("nombre_actividad", "");
				
		serie1=new Number[9];
		serie2=new Number[9];
		myPreferencesRecover2 = getSharedPreferences("MyPreferencesTrainning",mode);
		serie1[0]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio0", "0.0"));
		serie1[1]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio1", "0.0"));
		serie1[2]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio2", "0.0"));
		serie1[3]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio3", "0.0"));
		serie1[4]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio4", "0.0"));
		serie1[5]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio5", "0.0"));
		serie1[6]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio6", "0.0"));
		serie1[7]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio7", "0.0"));
		serie1[8]=Double.parseDouble(myPreferencesRecover2.getString("velocidadPromedio8", "0.0"));
		
		DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
		// el resto de la carga	
		indice=db.getRows("select count(*) from usuarios");
		nombre=db.getUsuarioName(indice);
		fecha=db.getSesionPerUser(nombre, 0);
		//System.out.println(":"+fecha);
		
		temp=db.getSesionPerUser(nombre, 3);
		double dd=Double.parseDouble(temp);
		dd=dd*100;
		long i=Math.round(dd);
		//System.out.println(temp);
		txtCalorias.setText((i/100)+"Kcal");
		
		temp=db.getSesionPerUser(nombre, 5);
		System.out.println(temp);
		txtDistancia.setText(temp);
		
		temp=db.getSesionPerUser(nombre, 2);
		System.out.println(temp);
		txtFrecOptima.setText(temp+"ppm");
		
		temp=db.getSesionPerUser(nombre, 6);
		//System.out.println(temp);
		txtTiempo.setText(db.getSesionPerUser(nombre, 6)+"min");
		//System.out.println(db.toString());
		
		db.closeDataBase();
		
		if (temp.isEmpty() || temp.equals(""))
			temp="1";
		for (indice=0;indice<9;indice++){
			serie2[indice]=(indice*(Double.parseDouble(temp)/8));
			System.out.println(serie1[indice]);
		}
	}
	
	
	
	/**
	 * This private class inherits CharPlot class and add a constructor function to assign it the right view id
	 * @author MNM
	 * 
	 */
	private class CPlot extends ChartPlot{
	/**
	 * Initialize the CPlot object.
	 * @param idLayout: It's the Layout id where the graphics will be ploted.
	 *  
	 */
		public CPlot(int idLayout){
			myXYPlot=(XYPlot)findViewById(idLayout);
		}
	}

}
