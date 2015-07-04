package com.example.proyecto;

import java.lang.reflect.Array;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Actividad_Eleccion extends Activity {
	
	private static final int OK_RESULT_CODE = 1;
	String[] names_music ={
			"Soda Stereo - Zoom",
			"The Naked and Famous - Girls like you",
			"One Republic - Good life",
			"The Cure - Just like heaven",
			"sajdfjksd-asdasd",
			"pedro-pooo",
			"lala-popo",
			"lospollitos dicen-jerje",
			"la vaca- trucha",
			"elsa elsa - tumama",
			"juan segura -lola",
			"la maca- me la maca"
	};
	
	ListView lstView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actividad_eleccion);
		
		lstView = (ListView)findViewById(R.id.android_List);
		
		lstView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lstView.setTextFilterEnabled(true);        
        lstView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,names_music));                
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
		//Toast.makeText(this,"regresando", Toast.LENGTH_SHORT).show();
		int in=lstView.getCheckedItemPositions().size();
		int j=0;
		//CLista lis = new CLista(in);	
		String[] o= new String[1];
		if(in != 0)
		{	
			o= new String[in];
			SparseBooleanArray b = lstView.getCheckedItemPositions();
			for(int i = 0 ; i < lstView.getCount();i++)
			{
				if(b.get(i))
				{
					o[j]=lstView.getAdapter().getItem(i).toString();
					j++;
				}
			}
		}
        Intent data = new Intent();
        data.putExtra("indice", String.valueOf(j));
        data.putExtra("o", o);
        data.putExtra("b", "0");
        setResult(Activity.RESULT_OK, data);        
		super.finish();
	}
	

}
