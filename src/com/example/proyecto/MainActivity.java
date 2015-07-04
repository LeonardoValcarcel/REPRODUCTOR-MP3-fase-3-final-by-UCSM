package com.example.proyecto;


import java.sql.SQLException;

import org.xml.sax.helpers.ParserAdapter;
import android.media.MediaPlayer;   
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.app.Notification;
import android.app.NotificationManager;

public class MainActivity extends Activity{
	
	SeekBar seek_bar;    
    Handler seekHandler = new Handler();
	protected static final int REQUEST_CODE = 1;
	CLista names_music= new CLista();
	private int b=0;
	private boolean play=true;

	private MediaPlayer mediaPlayer;
	private int playbackPosition=0;	
	private int id=0;
	private boolean bandera=false; 
	DBAdapter db;
	
	int notificationID = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	Button button =(Button) findViewById(R.id.btn_displaynotif);
	button.setOnClickListener (new Button.OnClickListener(){
		public void onClick(View v){
			displayNotification();
		}
	});	
			
		db= new DBAdapter(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, names_music.names_music);
		AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.txtNames);
		textView.setThreshold(1);
		textView.setAdapter(adapter);	
		ListView lstView = (ListView)findViewById(R.id.android_List);
        lstView.setTextFilterEnabled(true);
        
        lstView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,names_music.names_music));
        lstView.setOnItemClickListener(new OnItemClickListener()
        {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,long arg3) {
				AutoCompleteTextView a = (AutoCompleteTextView)findViewById(R.id.txtNames);
				a.setText(names_music.names_music[position]);
				ImageButton imaB = (ImageButton)findViewById(R.id.imageButtonPlay);
				imaB.setImageResource(android.R.drawable.ic_media_pause);
				b++;
				killMediaPlayer();
				playLocalAudio();
			}
        	
        });
        getInit();
        seekUpdation();
        Toast.makeText(this, namelista(), Toast.LENGTH_SHORT).show();
        if(!(namelista().equals(""))){
        	try {
				db.open();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	Cursor a = db.getCanciones(namelista());
        	int i=0;
			names_music= new CLista(a.getCount());
			if(a.moveToFirst())
			{
				do{
					names_music.names_music[i]=	a.getString(0);
					i++;
				}while(a.moveToNext());
			}
			ponerlista();
        	db.close();
        }
        	
                
        
	}
	
	//
	public void onClickLoad(View view)
	{
		Intent i = new Intent(this,AppPreferenceActivity.class);
		startActivity(i);
	}
	public String namelista()
	{
		SharedPreferences appPrefs=
				getSharedPreferences("com.example.proyecto_preferences",MODE_PRIVATE);
		return appPrefs.getString("editTextPref", "");	
	}
	public void Guardar_Click(View view){
		Intent i = new Intent(this, Actividad_Guardar_Lista.class);
        i.putExtra("canciones", names_music.names_music);
        startActivity(i);
	}
	public void Cargar_Click(View view){
		Intent i = new Intent(this, Actividad_Eleccion_Lista.class);
		startActivityForResult(i, 0);
	}
	public void Borrar_Click(View view){
		Intent i = new Intent(this, Actividad_Borrar_Lista.class);
		startActivity(i);
	}
	public void ponerlista()
	{
		ListView lstView = (ListView)findViewById(R.id.android_List);
		lstView.setTextFilterEnabled(true);
		lstView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,names_music.names_music));
	}
	
	public void onClick(View view){
		ImageButton imaB = (ImageButton)findViewById(R.id.imageButtonPlay);
		if(view.getId()==R.id.imageButtonAdd)
		{			
			Intent i = new Intent(this, Actividad_Eleccion.class);
			startActivityForResult(i, 0);		
		}
		if(names_music.n==0)
			return;
		switch (view.getId()) {
		case R.id.imageButtonPlay:
			play(imaB);
			break;
		case R.id.imageButtonStop:
			stop(imaB);
			break;
		case R.id.imageButtonAdelante:
			siguiente();
			break;
		case R.id.imageButtonAtras:
			anterior();
			break;
		}
	}
	
	public void theClick(View view){
		TextView t = (TextView)findViewById(R.id.textViewPrueba);
		AutoCompleteTextView a = (AutoCompleteTextView)findViewById(R.id.txtNames);
		t.setText(a.getText());
	}



	private void playLocalAudio() {
		AutoCompleteTextView a = (AutoCompleteTextView)findViewById(R.id.txtNames);
		//TextView t = (TextView)findViewById(R.id.textViewPrueba);
		//TextView t2 = (TextView)findViewById(R.id.textViewPrueba2);
		if(a.getText().toString().equals(names_music.lis[0].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_0);
			id=0;
			//t2.setText("0");
		}else if(a.getText().toString().equals(names_music.lis[1].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_1);
			id=1;
			//t2.setText("1");
		}else if(a.getText().toString().equals(names_music.lis[2].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_2);
			id=2;
			//t2.setText("2");
		}else if(a.getText().toString().equals(names_music.lis[3].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_3);
			id=3;
			//t2.setText("3");
		}else if(a.getText().toString().equals(names_music.lis[4].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_4);
			id=4;
			//t2.setText("4");
		}else if(a.getText().toString().equals(names_music.lis[5].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_5);
			id=5;
			//t2.setText("5");
		}else if(a.getText().toString().equals(names_music.lis[6].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_6);
			id=6;
			//t2.setText("6");
		}else if(a.getText().toString().equals(names_music.lis[7].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_7);
			id=7;
			//t2.setText("7");
		}else if(a.getText().toString().equals(names_music.lis[8].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_8);
			id=8;
			//t2.setText("8");
		}else if(a.getText().toString().equals(names_music.lis[9].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_9);
			id=9;
			//t2.setText("9");
		}else if(a.getText().toString().equals(names_music.lis[10].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_10);
			id=10;
			//t2.setText("10");
		}else if(a.getText().toString().equals(names_music.lis[11].toString())){
			mediaPlayer = MediaPlayer.create(this, R.raw.music_file_11);
			id=11;
			//t2.setText("11");
		}	
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.start();
		//t.setText(mediaPlayer.getDuration());
	}
	public void onPrepared(MediaPlayer mp){
		mp.start();
	}
	private void killMediaPlayer(){
		if(mediaPlayer!=null){
			try{
				mediaPlayer.release();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void stop(ImageButton imaB)
	{
		if(mediaPlayer != null){
			mediaPlayer.stop();
			playbackPosition=0;
			b=0;
			imaB.setImageResource(android.R.drawable.ic_media_play);
		}
	}
	private void play(ImageButton imaB)
	{
		try{
			if(b==0){					
				playLocalAudio();
				imaB.setImageResource(android.R.drawable.ic_media_pause);
				b++;
			}else{
				if(play==true){
					imaB.setImageResource(android.R.drawable.ic_media_play);
					if(mediaPlayer != null && mediaPlayer.isPlaying()){
						playbackPosition = mediaPlayer.getCurrentPosition();
						mediaPlayer.pause();
					}
					play=false;
				}
				else{
					imaB.setImageResource(android.R.drawable.ic_media_pause);
					if(mediaPlayer != null && !mediaPlayer.isPlaying()){
						mediaPlayer.seekTo(playbackPosition);
						mediaPlayer.start();
					}
					play=true;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void siguiente()
	{
		ImageButton imaB = (ImageButton)findViewById(R.id.imageButtonPlay);
		if(id < names_music.n-1)
			id=id+1;
		else
			id=0;
		killMediaPlayer();
		AutoCompleteTextView a = (AutoCompleteTextView)findViewById(R.id.txtNames);
		a.setText(names_music.names_music[id]);
		playLocalAudio();
	}
	private void anterior()
	{
		ImageButton imaB = (ImageButton)findViewById(R.id.imageButtonPlay);
		if(id > 0)
			id=id-1;
		else
			id=names_music.n-1;
		killMediaPlayer();
		AutoCompleteTextView a = (AutoCompleteTextView)findViewById(R.id.txtNames);
		a.setText(names_music.names_music[id]);
		playLocalAudio();
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if(data!= null)
		{
			try {
				db.open();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			String b = data.getStringExtra("b");
			if(b.equals("0"))
			{
				int indi =Integer.parseInt(data.getStringExtra("indice"));
				String[] mu = data.getStringArrayExtra("o");
				names_music= new CLista(indi);
				for(int i = 0 ;i <indi ; i++)
					names_music.names_music[i]=mu[i];
			}
			else{
				String mu = data.getStringExtra("o");
				Cursor a = db.getCanciones(mu);
				int i=0;
				names_music= new CLista(a.getCount());
				if(a.moveToFirst())
				{
					do{
						names_music.names_music[i]=	a.getString(0);
						i++;
					}while(a.moveToNext());
				}
			}
			ponerlista();
			db.close();
		}
	}
	
	
	private void playAudio(String url)throws Exception{
		killMediaPlayer();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDataSource(url);
		mediaPlayer.prepareAsync();
	}

	public void getInit() {
        seek_bar = (SeekBar) findViewById(R.id.seek_bar);
		mediaPlayer = MediaPlayer.create(this, R.raw.music_file_0);
        seek_bar.setMax(mediaPlayer.getDuration());
    }
 
    Runnable run = new Runnable() { 
        @Override
        public void run() {
            seekUpdation();
        }
    };
	private NotificationManager nm;
 
    public void seekUpdation() { 
        seek_bar.setProgress(mediaPlayer.getCurrentPosition());
        seekHandler.postDelayed(run, 1000);
    }
    
   protected void displayNotification()
   {
	   Intent i = new Intent(this, NotificationView.class);
	   i.putExtra("notificationID",notificationID);
	   PendingIntent pendingIntent =
			   PendingIntent.getActivity(this,0,i, 0);
	   Notification notif = new Notification(R.drawable.ic_launcher,
			   "AGRADECER NO CUESTA NADA",
			   System.currentTimeMillis());
	   CharSequence from = "ALARMA DEL REPORDUCTOR";
	   CharSequence message = "Reunion con cliente a las 8pm..";
	   notif.setLatestEventInfo(this, from, message, pendingIntent);
	   
	   notif.vibrate = new long[]{100,250,100,500};
	   nm.notify(notificationID,notif);
   }
   
}
