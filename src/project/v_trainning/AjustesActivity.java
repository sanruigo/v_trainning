package project.v_trainning;

//import android.R;

import project.database.DataBase_vTrainning;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

@SuppressLint("NewApi")
public class AjustesActivity extends Activity {
	  final int mode = Activity.MODE_PRIVATE;
	  final String MYPREFS_SETTINGS = "MyPreferencesSettings";
	  SharedPreferences myPreferences, myPreferencesRecover;
	  String nombreActividad;
	  int tipoActividad;
	  EditText txtPrefName, txtPrefAge, txtPrefWeight, txtPrefHeight, txtPrefPulso;
	  Spinner spinActType;
	  String spin;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajustes);
		createWidget();
	}

	
	private void createWidget(){
		
		txtPrefName=(EditText)findViewById(R.id.eTextAjustActNombre);
		txtPrefAge= (EditText)findViewById(R.id.eTextAjustActEdad);
		txtPrefWeight =(EditText)findViewById(R.id.eTextAjustActPeso);
		txtPrefHeight =(EditText)findViewById(R.id.eTextAjustActEstatura);
		txtPrefPulso =(EditText)findViewById(R.id.eTextAjustActPulso);
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
	
	private void savedDataBase(){
		
		DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
		//Inserta Usuarios
		db.setUsuario(txtPrefName.getText().toString(),Integer.valueOf(txtPrefAge.getText().toString()),Double.valueOf(txtPrefWeight.getText().toString()),Double.valueOf(txtPrefHeight.getText().toString()));
		db.closeDataBase();
	}
	private void showSavedPreferences() {
		// TODO Auto-generated method stub
		myPreferencesRecover = getSharedPreferences(MYPREFS_SETTINGS,mode);
		txtPrefName.setText(myPreferencesRecover.getString("nombre", ""));
		txtPrefAge.setText(myPreferencesRecover.getString("edad", ""));
		txtPrefWeight.setText(myPreferencesRecover.getString("peso", ""));
		txtPrefHeight.setText(myPreferencesRecover.getString("estatura", ""));
		//spinActType.setSelection(Integer.valueOf(myPreferencesRecover.getString("actividad", "1")));
		spinActType.setSelection(myPreferencesRecover.getInt("actividad", 0));
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showSavedPreferences();
		
		if(!txtPrefName.getText().toString().isEmpty() && !txtPrefAge.getText().toString().isEmpty() && !txtPrefHeight.getText().toString().isEmpty() && !txtPrefWeight.getText().toString().isEmpty()){
			DataBase_vTrainning db =new DataBase_vTrainning(this, "DBvTrainning", null, 1);
			if (!db.equals(null))
				//System.out.println("Usuario:"+db.getUsuarioName(1)+" Edad:"+db.getUsuarioAge(1)+" Peso:"+db.getUsuarioWeight(db.getUsuarioName(1))+" Estatura:"+db.getUsuarioHeight(db.getUsuarioName(1)));
			db.closeDataBase();
		}
	}


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

	public void salir(){
		finishFromChild(getParent());
		finish();
	}
	

	public void questionMessage(String title,String text, String nameButton1, String nameButton2, boolean cancelable){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(text)
		        .setTitle(title)
		        .setCancelable(cancelable)
		        .setPositiveButton(nameButton1,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                		salir();
		                		//finish();
		                    }
		                })
		         .setNegativeButton(nameButton2,
		        		 new DialogInterface.OnClickListener() {
		        	 		public void onClick(DialogInterface dialog, int id) {
		        	 		}
                });
		AlertDialog alert = builder.create();
		alert.show();

	}
	
	public boolean onMenuItemClick(MenuItem item) {
		  return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menuOpTraining:
	        	System.out.println("menuAjustes");
	        	launchTranningActivity(findViewById(R.id.btnAjustActTrain));
	            return true;
	        case R.id.menuOpResumen:
	        	System.out.println("menuResumen");
	        	launchResumeActivity(findViewById(R.id.btnAjustActResum));
	            return true;
	        case R.id.menuOpCreditos:
	            
	            return true;
	        case R.id.menuOpSalir:
	            questionMessage(getResources().getString(R.string.title_activity_trainning),getResources().getString(R.string.msgSalir),
	            		getResources().getString(R.string.btnOk),getResources().getString(R.string.btnCancel),false);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
	}


	private void savePreferences(boolean completo) {
		// TODO Auto-generated method stub
		myPreferences = getSharedPreferences(MYPREFS_SETTINGS, mode);
		Editor myEditor = myPreferences.edit();
		myEditor.putString("nombre", txtPrefName.getText().toString().trim());
		myEditor.putString("edad", txtPrefAge.getText().toString().trim());
		myEditor.putString("peso", txtPrefWeight.getText().toString().trim());
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
	
	public void launchTranningActivity(View view){
		
		startActivity(new Intent(AjustesActivity.this,TrainningActivity.class));

	}
	

	public void launchResumeActivity(View view){
		startActivity(new Intent(AjustesActivity.this,ResumenActivity.class));
		
	}
	
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
	
	/*
	private void alertMessage(String title, String text, String nameButton, boolean cancelable){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(text)
		        .setTitle(title)
		        .setCancelable(cancelable)
		        .setNeutralButton(nameButton,
		                new DialogInterface.OnClickListener() {
		                    public void onClick(DialogInterface dialog, int id) {
		                        dialog.cancel();
		                    }
		                });
		AlertDialog alert = builder.create();
		alert.show();
	}
	*/

}
