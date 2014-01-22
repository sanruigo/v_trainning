package project.v_trainning;


import project.database.DataBase_vTrainning;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This class displays a form that the user must fill in to make a training session
 * 
 * @author Various 
 *
 */
@SuppressLint("NewApi")
public class AjustesActivity extends Activity {
	  final int mode = Activity.MODE_PRIVATE;
	  final String MYPREFS_SETTINGS = "MyPreferencesSettings";
	  SharedPreferences myPreferences, myPreferencesRecover;
	  String nombreActividad;
	  int tipoActividad;
	  boolean datosCompletos;
	  EditText txtPrefName, txtPrefAge, txtPrefWeight, txtPrefHeight, txtPrefPulso;
	  Spinner spinActType;
	  String spin;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajustes);
		createWidget();
	}

	/**
	 * Create the activity widgets. 
	 */
	private void createWidget(){
		
		txtPrefName=(EditText)findViewById(R.id.eTextAjustActNombre);
		txtPrefAge= (EditText)findViewById(R.id.eTextAjustActEdad);
		txtPrefWeight =(EditText)findViewById(R.id.eTextAjustActPeso);
		txtPrefHeight =(EditText)findViewById(R.id.eTextAjustActEstatura);
		txtPrefPulso =(EditText)findViewById(R.id.eTextPulso);
		spinActType =  (Spinner) findViewById(R.id.spnAjustActTipoAct);
		ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.ActType, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spinActType.setAdapter(adapter);
		spinActType.setOnItemSelectedListener(new OnItemSelectedListener(){
		public void onItemSelected(AdapterView<?> parentView, View SelectedItemView, int position, long id){
			tipoActividad = position;
			nombreActividad=(String)spinActType.getItemAtPosition(position).toString();
			System.out.println(spinActType.getItemAtPosition(position));
			
			System.out.println(position);
		}
		public void onNothingSelected(AdapterView<?> parentView){
			}
		});
		
	}

	/**
	 * Save the user data in the database. If it's necessary only update the data.
	 * 
	 */
	private void savedDataBase(){
		
		DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
		//Inserta Usuarios
		if (!datosCompletos){
			db.setUsuario(txtPrefName.getText().toString(),Integer.valueOf(txtPrefAge.getText().toString()),Double.valueOf(txtPrefWeight.getText().toString()),Double.valueOf(txtPrefHeight.getText().toString()));
		}else{
			db.updateUsuario(txtPrefName.getText().toString(),Integer.valueOf(txtPrefAge.getText().toString()),Double.valueOf(txtPrefWeight.getText().toString()),Double.valueOf(txtPrefHeight.getText().toString()));
		}
		db.closeDataBase();
	}
	
	/**
	 * Recover the saved data from preferences
	 */
	private void showSavedPreferences() {
		// Auto-generated method stub
		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		txtPrefName.setText(myPreferencesRecover.getString("nombre", ""));
		txtPrefAge.setText(myPreferencesRecover.getString("edad", ""));
		txtPrefWeight.setText(myPreferencesRecover.getString("peso", ""));
		txtPrefHeight.setText(myPreferencesRecover.getString("estatura", ""));
		txtPrefPulso.setText(myPreferencesRecover.getString("frecuencia_basal", ""));
		datosCompletos=Boolean.valueOf(myPreferencesRecover.getString("completo", "false"));
		//spinActType.setSelection(Integer.valueOf(myPreferencesRecover.getString("actividad", "1")));
		spinActType.setSelection(myPreferencesRecover.getInt("actividad", 0));
		
	}
	
	/**
	 * Do the corresponding task when the activity gets focus.
	 * These include showSavedPreferences.
	 */
	@Override
	protected void onResume() {
		// Auto-generated method stub
		super.onResume();
		showSavedPreferences();
		
		if(!txtPrefName.getText().toString().isEmpty() && !txtPrefAge.getText().toString().isEmpty() && !txtPrefHeight.getText().toString().isEmpty() && !txtPrefWeight.getText().toString().isEmpty()){
			DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
			if (!db.equals(null))
				//System.out.println("Usuario:"+db.getUsuarioName(1)+" Edad:"+db.getUsuarioAge(1)+" Peso:"+db.getUsuarioWeight(db.getUsuarioName(1))+" Estatura:"+db.getUsuarioHeight(db.getUsuarioName(1)));
			db.closeDataBase();
		}
	}

	/**
	 * Perform the corresponding tasks when the activities lost focus.
	 * These include savePreferences and verify the input data. 
	 */
	@Override
	protected void onPause(){
		boolean completo=false;
		String temp;
		if(!txtPrefName.getText().toString().isEmpty() && !txtPrefAge.getText().toString().isEmpty() && !txtPrefHeight.getText().toString().isEmpty() && !txtPrefWeight.getText().toString().isEmpty()){
			temp=(txtPrefAge.getText().toString());
			if (isPosInteger(temp)){
				temp=(txtPrefWeight.getText().toString());
				if (isPosInteger(temp)){
					if (isPosDouble((txtPrefHeight.getText().toString()))){
						savedDataBase();
						completo=true;
						if (!isPosInteger(txtPrefPulso.getText().toString())){
							Toast.makeText(getApplicationContext(), R.string.msgErrorPulso, Toast.LENGTH_LONG).show();
						
						}
					}else{
						Toast.makeText(getApplicationContext(), R.string.msgErrorEstatura, Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), R.string.msgErrorPeso, Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), R.string.msgErrorEdad, Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getApplicationContext(), R.string.msgAjustes, Toast.LENGTH_LONG).show();
		}
		savePreferences(completo);
		super.onPause();
		finish();
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
	 * @return true if action is selected.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menuAjusTraining:
	        	System.out.println("menuAjustes");
	        	launchTranningActivity(findViewById(R.id.btnAjustActTrain));
	            return true;
	        case R.id.menuAjusResumen:
	        	System.out.println("menuResumen");
	        	launchResumeActivity(findViewById(R.id.btnAjustActResum));
	            return true;
	        case R.id.menuAjusCreditos:
	        	launchAboutActivity(findViewById(R.id.btnAjustActSetting));
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
/*	
	@Override
	public void onBackPressed() {
		// Auto-generated method stub
		//super.onBackPressed();
	}
*/
	/**
	 * Save the user data in the app preferences.
	 * @param completo
	 * indicates if all the data is complete.
	 */
	private void savePreferences(boolean completo) {
		// Auto-generated method stub
		myPreferences = getSharedPreferences(MYPREFS_SETTINGS, mode);
		Editor myEditor = myPreferences.edit();
		myEditor.putString("nombre", txtPrefName.getText().toString().trim());
		myEditor.putString("edad", txtPrefAge.getText().toString().trim());
		myEditor.putString("peso", txtPrefWeight.getText().toString().trim());
		myEditor.putString("frecuencia_basal", txtPrefPulso.getText().toString().trim());
		myEditor.putString("estatura", txtPrefHeight.getText().toString().trim());
		myEditor.putString("nombre_actividad", nombreActividad);
		//myEditor.putString("actividad", tipoActividad+"");
		myEditor.putInt("actividad", tipoActividad);
		myEditor.putString("completo", String.valueOf(completo));
		myEditor.commit();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajustes, menu);
		return true;
	}
	
	/**
	 * Launch the Training Activity, finishing this.
	 * @param view
	 */
	public void launchTranningActivity(View view){
		
		finish();
	}
	
	/**
	 * Launch the About Activity.
	 * @param view
	 */
	public void launchAboutActivity(View view){
		startActivity(new Intent(AjustesActivity.this,AboutActivity.class));
		
	}
	

	/**
	 * Launch the Resumen Activity.
	 * @param view
	 */
	public void launchResumeActivity(View view){
		startActivity(new Intent(AjustesActivity.this,ResumenActivity.class));
		
	}
	
	/**
	 * Verify if an input string is a positive integer.
	 * @param cadena
	 * @return true if cadena is a integer number greater than zero; false if not.
	 */
	public boolean isPosInteger(String cadena){
		try{
			int dd=Integer.parseInt(cadena);
			if (dd>0){
				return true;
			}else return false;
		}catch (NumberFormatException e){
			return false;
		}
	}

	/**
	 * Verify if an input string is a positive double.
	 * @param cadena
	 * @return true if cadena is a double number greater than zero; false if not.
	 */
	public boolean isPosDouble(String cadena){
		try{
			double dd=Double.parseDouble(cadena);
			if (dd>0){
				return true;
			}else return false;
		}catch (NumberFormatException e){
			return false;
		}
	}
	


}
