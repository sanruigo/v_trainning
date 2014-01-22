package project.v_trainning;


import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
//import android.view.View;

/**
 * This class lets to show the home screen of the application.
 * @author Xemma Vazquez
 */
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		esperarYCerrar(MILISEGUNDOS_ESPERA);
		
		//chartExample();
		//dbExample();
	}
	
	public static int MILISEGUNDOS_ESPERA = 2000;

	/**
	 * This method is responsible for displaying the home screen of the app a certain time
	 *
	 * @param milisegundos
	 * Time that this activity is showed.
	 */
	public void esperarYCerrar(int milisegundos) {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				// acciones que se ejecutan tras los milisegundos
				//setContentView(R.layout.activity_trainning);
				startActivity(new Intent(MainActivity.this,TrainningActivity.class));
				finish();
			}
		}, milisegundos);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

}
