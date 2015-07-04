package com.example.proyecto;

import java.lang.reflect.Array;
import java.sql.SQLException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Actividad_Guardar_Lista extends Activity {
	
	String listas;
	DBAdapter db;
	String[] names_music;
	EditText tv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_guardar_lista);
		
		db = new DBAdapter(this);
		tv= (EditText) findViewById(R.id.Namelista);
        Bundle bundle = getIntent().getExtras();
        
        names_music = new String[bundle.getStringArray("canciones").length];
        names_music = bundle.getStringArray("canciones");
       
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		String nl =tv.getText().toString();
		if (nl.length()>0)
		{
			try {
				db.open();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			if(db.insertLista(nl)>=0)
			{
				int n = names_music.length;
				String id = db.getIdListaString(nl);
				for(int i =0; i < n;i++  ){
					db.insertcancion(id, names_music[i]);
				}
				Toast.makeText(this, "Guardado con Exito", Toast.LENGTH_SHORT).show();
			}
			else
				Toast.makeText(this, "Nombre de lista ya existe", Toast.LENGTH_SHORT).show();
			db.close();
		}
		else
			Toast.makeText(this, "No ingreso Nombre de lista", Toast.LENGTH_SHORT).show();
		
		super.finish();
	}
	

}
