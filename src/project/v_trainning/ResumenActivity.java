package project.v_trainning;


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
//import android.widget.Button;
import project.database.DataBase_vTrainning;

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
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		createTables();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.resumen, menu);
		return true;
	}
	
	public void launchSettingActivity(View view){
		startActivity(new Intent(ResumenActivity.this,AjustesActivity.class));
		
	}
	

	public void launchTrainningActivity(View view){
		finish();
		
	}
	@SuppressLint("NewApi")
	public void createTables(){
		///Create row of Table Local
		
		int rows;
		TableLayout table = (TableLayout) findViewById(R.id.Table1); 
		db=new DataBase_vTrainning(this, "DBvTrainning", null, 1);
		rows=db.getRows("select count(*) from usuarios");
		String nombre=db.getUsuarioName(rows);
		rows=db.getRows("select count(*) from sesiones");
		for(int i=0; i<=rows;i++){
			TableRow rowLocal = new TableRow(this);
			
			TextView tvLocal = new TextView(this);
			
			tvLocal.setText(db.getSesionPerUser(nombre,0));
			
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			rowLocal.addView(tvLocal);
			
			tvLocal = new TextView(this);
			tvLocal.setText(db.getSesionPerUser(nombre,5)+"km");
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			tvLocal.setPadding(110, 0, 0, 0);
			rowLocal.addView(tvLocal);
			
			tvLocal = new TextView(this);
			tvLocal.setText(db.getSesionPerUser(nombre,6)+"min");
			tvLocal.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			tvLocal.setPadding(220, 0, 0, 0);
			rowLocal.addView(tvLocal);

			table.addView(rowLocal);
		}
	}

}
