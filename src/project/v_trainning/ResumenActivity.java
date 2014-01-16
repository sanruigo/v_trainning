package project.v_trainning;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class ResumenActivity extends Activity {
	private Button prueba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resumen);
		System.out.println("Creando Resumen");
		prueba=(Button)findViewById(R.id.btnPrueba);
		prueba.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				startActivity(new Intent(ResumenActivity.this,ResultsActivity.class));
			}	
		});
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		finish();
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
		startActivity(new Intent(ResumenActivity.this,TrainningActivity.class));
		
	}

}
