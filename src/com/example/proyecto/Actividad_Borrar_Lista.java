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

public class Actividad_Borrar_Lista extends Activity {
	
	String[] names_listas;
	
	ListView lstView;
	DBAdapter db;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_eleccion_lista);
		db = new DBAdapter(this);
		lstView = (ListView)findViewById(R.id.android_List);
		cargar_lista();
		
		lstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lstView.setTextFilterEnabled(true);        
        lstView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,names_listas));     
	}
	void cargar_lista(){
		try {
			db.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Cursor c = db.getAllLista();
		names_listas = new String[c.getCount()];
		int i = 0;
		if(c.moveToFirst())
		{
			do{
					names_listas[i]=c.getString(0);
					i++;		
			}while(c.moveToNext());
		}
		db.close();
	}
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		int in=lstView.getCheckedItemPosition();

		try {
			db.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (in>=0)
		{
			db.deleteList(lstView.getItemAtPosition(in).toString());
			Toast.makeText(this, "Borrado", Toast.LENGTH_SHORT).show();
		}
		db.close();
		super.finish();
	}
	

}
