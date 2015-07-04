package com.example.proyecto;

import android.os.Parcel;
import android.os.Parcelable;

public class CLista implements Parcelable
{
	public String[] names_music;
	public int n=0;				//numero de canciones de la lista
	public int[] b;				//
	public String[] lis ={
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
	
	public CLista(){
		names_music= new String[n];
		b = new int[n];
	}
	public CLista(int N){
		n=N;
		names_music= new String[n];
		b = new int[n];
		for(int i = 0 ; i < n ; i++)
		{
			b[i]=0;
		}
	}
	public void set(CLista a)
	{
		n=a.n;
		for(int i = 0 ; i < n ; i++)
		{
			names_music[i]=a.names_music[i];
			b[i]=a.b[i];
		}
	}
	public void quitar(String a)
	{
		for(int i = 0 ; i < n;i++)
			if(this.names_music[i]==a)
			{
				this.names_music[i]="";
				n--;
			}
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(this.n);
		dest.writeArray(this.names_music);
		dest.writeIntArray(this.b);
	}
	
	

}
