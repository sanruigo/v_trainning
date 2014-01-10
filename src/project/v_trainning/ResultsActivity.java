package project.v_trainning;

import android.os.Bundle;
import project.chart.ChartPlot;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
	ChartPlot plot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		createWidgets();
		plot= new CPlot(R.id.PlotResult);
		PlotResults();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
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
	}
	
	private void PlotResults(){
		Number[] serie1={10,12,13,12,15,9};
		
		plot.setSerie(serie1);
		plot.setXYSeries(serie1, getResources().getString(R.string.txtVelocidad));
		plot.setSeriesFormat(Color.rgb(10, 200, 10), Color.BLACK, Color.LTGRAY);
		plot.Plot();
	}
	
	private void compartir(){
		String comp="";
		comp=txtNombre.getText().toString()+": He recorrido "+txtDistancia.getText().toString()+"Km en "+txtTiempo.getText().toString()+
				", quemando "+txtCalorias.getText().toString()+", usando V-Training Assistant";
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, comp);
		intent.setType("text/plain");
		startActivity(Intent.createChooser(intent, getResources().getString(R.string.msgCompartir)));
	}
	
	private class CPlot extends ChartPlot{
		
		public CPlot(int idLayout){
			myXYPlot=(XYPlot)findViewById(idLayout);
		}
	}

}
