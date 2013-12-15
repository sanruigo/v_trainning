package project.v_trainning;

//import android.R;
import java.io.IOException;

import project.database.DataBase_vTrainning;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
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
	  EditText txtPrefName, txtPrefAge, txtPrefWeight, txtPrefHeight;
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
			System.out.println("Usuario:"+db.getUsuarioName(1)+" Edad:"+db.getUsuarioAge(1)+" Peso:"+db.getUsuarioWeight(db.getUsuarioName(1))+" Estatura:"+db.getUsuarioHeight(db.getUsuarioName(1)));
			db.closeDataBase();
		}
	}


	@Override
	protected void onPause(){
		super.onPause();
		savePreferences();
		if(!txtPrefName.getText().toString().isEmpty() && !txtPrefAge.getText().toString().isEmpty() && !txtPrefHeight.getText().toString().isEmpty() && !txtPrefWeight.getText().toString().isEmpty()){
			savedDataBase();
		}
	}

	private void savePreferences() {
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

}
