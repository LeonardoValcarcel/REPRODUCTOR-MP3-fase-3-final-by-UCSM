package com.example.proyecto;

import java.sql.SQLException;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
	static final String KEY_ID = "_id";
	static final String KEY_NAME_LIST = "name_list";
	static final String KEY_NAME_CANCION = "name_cancion";
	static final String TAG = "DBAdapter";
	
	static final String DATABASE_NAME = "MyDB_List";
	static final String DATABASE_TABLE = "mylist";
	static final String DATABASE_TABLE_C = "canciones";
	static final int DATABASE_VERSION = 1;
	
	static final String DATABASE_CREATE =
				"create table mylist (_id integer primary key, name_list text not null);";
	static final String DATABASE_CREATE_CANCION =
				"create table canciones (_id integer not null, "
				+"name_cancion text not null);";
	final Context context;
	DatabaseHelper DBHelper;
	SQLiteDatabase db;
	public DBAdapter(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}
		
		
		public void onCreate(SQLiteDatabase db)
		{
			try{
				db.execSQL(DATABASE_CREATE);
				db.execSQL(DATABASE_CREATE_CANCION);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
		{
			Log.w(TAG, "Upgrading database from version "+oldVersion+" to "+newVersion+",which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS mylist");
			db.execSQL("DROP TABLE IF EXISTS canciones");
			
			onCreate(db);
		}
		
	}
	
	public DBAdapter open() throws SQLException
	{
		db= DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		DBHelper.close();
	}
	
	public long insertLista(String nl)
	{
		if(db!=null)
		{
			String c = this.getIdListaString(nl);
			if(c.equals("")){
				ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_NAME_LIST, nl);
				return db.insert(DATABASE_TABLE, null, initialValues);
			}
			else
				return -1;

		}
		return -1;
	}
	public void insertcancion(String id,String nc){
		if(db!=null)
		{
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_ID,id);
			initialValues.put(KEY_NAME_CANCION, nc);
			db.insert(DATABASE_TABLE_C, null, initialValues);
		}
	}
	
	public Cursor getAllLista()
	{
		return db.query(DATABASE_TABLE, new String[] {KEY_NAME_LIST}, null, null, null, null, null);
	}

	public String getIdListaString(String nl)
	{
		Cursor a = db.query(DATABASE_TABLE, new String[] {KEY_ID}, KEY_NAME_LIST+"='"+nl+"'", null, null, null, null);
		if (a.moveToFirst())
		{
			return a.getString(0);
		}
		else
			return "";
	}
	
	public Cursor getCanciones(String nl){
		String id = this.getIdListaString(nl); 
		return db.query(DATABASE_TABLE_C, new String[] {KEY_NAME_CANCION}, KEY_ID+"='"+id+"'", null, null, null, null);
	}
	
	public Cursor getCancionesId(String id){
		return db.query(DATABASE_TABLE_C, new String[] {KEY_NAME_CANCION}, KEY_ID+"='"+id+"'", null, null, null, null);
	}
	
	public String getNameList(String id){
		Cursor a = db.query(DATABASE_TABLE, new String[] {KEY_NAME_LIST}, KEY_ID+"='"+id+"'", null, null, null, null);
		if (a!=null)
		{
			a.moveToFirst();
			return a.getString(0);
		}
		else
			return "";
	}
	
	public boolean deleteList(String name) 
	{
		String a =this.getIdListaString(name);
		db.delete(DATABASE_TABLE_C, KEY_ID + "='" + a+"'", null) ;
	    return db.delete(DATABASE_TABLE, KEY_ID + "='" + a+"'", null) > 0;
	}
	

	
}
