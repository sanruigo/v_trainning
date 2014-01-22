package project.v_trainning;


import java.util.Vector;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import project.database.DataBase_vTrainning;

/**
 * This class shows a summary of all the training that the user has done. 
 * @author Various 
 *
 */
public class ResumenActivity extends Activity {
	private DataBase_vTrainning db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resumen);
		System.out.println("Creando Resumen");
		
	}

	
	@Override
	protected void onPause() {
		// Auto-generated method stub
		super.onPause();
		finish();
	}


	@Override
	protected void onResume() {
		// Auto-generated method stub
		super.onResume();
		createTables();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resumen, menu);
		return true;
	}
	
	
	/**
	 * Launch the Settings Activity.
	 * @param view
	 */
	public void launchSettingActivity(View view){
		startActivity(new Intent(ResumenActivity.this,AjustesActivity.class));
		
	}
	
	/**
	 * Launch the Training Activity, finishing this.
	 * @param view
	 */
	public void launchTrainningActivity(View view){
		finish();
		
	}
	
	/**
	 * This method create the results table, loading them form the database.
	 */
	@SuppressLint("NewApi")
	public void createTables(){
		
		int rows;
		Double temp;
		long j;
		TableLayout table = (TableLayout) findViewById(R.id.Table1); 
		db=new DataBase_vTrainning(this, "DBvTrainning", null, 1);
		rows=db.getRows("select count(*) from sesiones");
		TableRow rowLocal = new TableRow(this);
		//Agregar encabezados
		TextView tvLocal = new TextView(this);
		tvLocal.setText(" "+getResources().getString(R.string.txtFecha));
		tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		rowLocal.addView(tvLocal);
		tvLocal = new TextView(this);
		tvLocal.setText(getResources().getString(R.string.txtDistancia));
		tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tvLocal.setPadding(20, 0, 0, 0);
		rowLocal.addView(tvLocal);
		tvLocal = new TextView(this);
		tvLocal.setText(getResources().getString(R.string.txtTiempo));
		tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tvLocal.setPadding(20, 0, 0, 0);
		rowLocal.addView(tvLocal);
		tvLocal = new TextView(this);
		tvLocal.setText(getResources().getString(R.string.txtCalorias));
		tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		tvLocal.setPadding(20, 0, 0, 0);
		rowLocal.addView(tvLocal);
		table.addView(rowLocal);

		//Obtener vectores con resultados
		Vector<Double> vDistancias=db.getAllSesionDistance();
		Vector<Double> vCalorias=db.getAllSesionCal();
		Vector<String> vFechas=db.getAllSesionDate();
		Vector<String> vTiempos=db.getAllSesionTime();
		//Agragar valores de entrenamientos
		for(int i=0; i<rows;i++){
			rowLocal = new TableRow(this);
			
			tvLocal = new TextView(this);
			
			tvLocal.setText(vFechas.get(i));
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			rowLocal.addView(tvLocal);
			
			tvLocal = new TextView(this);
			tvLocal.setText(vDistancias.get(i)+"km");
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			tvLocal.setPadding(20, 0, 0, 0);
			rowLocal.addView(tvLocal);
			
			tvLocal = new TextView(this);
			tvLocal.setText(vTiempos.get(i)+"min");
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			tvLocal.setPadding(20, 0, 0, 0);
			rowLocal.addView(tvLocal);

			tvLocal = new TextView(this);
			temp=vCalorias.get(i);
			temp=temp*100;
			j=Math.round(temp);
			temp=(double) (j/100);
			tvLocal.setText(temp+"Kcal");
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			tvLocal.setPadding(30, 0, 0, 0);
			rowLocal.addView(tvLocal);

			table.addView(rowLocal);
		}
		db.closeDataBase();
	}

}
