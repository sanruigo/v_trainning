package project.v_trainning;


import java.util.Locale;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
//import android.view.View;

import java.util.Locale;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;

/**
 * This class lets to show the home screen of the application.
 * @author Xemma Vazquez
 */
public class MainActivity extends Activity {
	
    /////TextToSpeech
	private TextToSpeech tts;
	private static int TTS_DATA_CHECK = 1;
	private boolean isTTSInitialized = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//speakBeggin();
		//confirmTTSData();
		esperarYCerrar(MILISEGUNDOS_ESPERA);
		
		//chartExample();
		//dbExample();
		
	}
	
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//speakUserLocale();
		//confirmTTSData();
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
	
	
	private void confirmTTSData()  {
    	Intent intent = new Intent(Engine.ACTION_CHECK_TTS_DATA);
    	startActivityForResult(intent, TTS_DATA_CHECK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == TTS_DATA_CHECK) {
    		if (resultCode == Engine.CHECK_VOICE_DATA_PASS) {
    			//Voice data exists		
    			initializeTTS();
    		}
    		else {
    			Intent installIntent = new Intent(Engine.ACTION_INSTALL_TTS_DATA);
    			startActivity(installIntent);
    		}
    	}
    }
    
    private void initializeTTS() {
    	tts = new TextToSpeech(this, new OnInitListener() {
    		public void onInit(int status) {
    			if (status == TextToSpeech.SUCCESS) {
    				isTTSInitialized = true;
    			}
    			else {
    				//Handle initialization error here
    				isTTSInitialized = false;
    			}
    		}
    	});
    }
    
    private void speakUserLocale() {
    	if(isTTSInitialized) {
    		//Determine User's Locale
    		Locale locale = this.getResources().getConfiguration().locale;
    		
    		System.out.println("locale="+locale);
    		if (tts.isLanguageAvailable(locale) >= 0) 
    			tts.setLanguage(locale);
    		
    		tts.setPitch(0.8f);
    		tts.setSpeechRate(1.1f);
    		
    		//tts.speak(getResources().getString(R.string.msgWelcome), TextToSpeech.QUEUE_ADD, null);
    		tts.speak("Hola", TextToSpeech.QUEUE_ADD, null);
    	}
    }

    private void speakBeggin() {
    	if(isTTSInitialized) {
    		//Determine User's Locale
    		Locale locale = this.getResources().getConfiguration().locale;
    		
    		System.out.println("locale="+locale);
    		if (tts.isLanguageAvailable(locale) >= 0) 
    			tts.setLanguage(locale);
    		
    		tts.setPitch(0.8f);
    		tts.setSpeechRate(1.1f);
    		
    		tts.speak("", TextToSpeech.QUEUE_ADD, null);
    	}
    }
    
    @Override
    public void onDestroy() {
    	if (tts != null) {
    		tts.stop();
    		tts.shutdown();
    	}
    	super.onDestroy();
    }
	

}
