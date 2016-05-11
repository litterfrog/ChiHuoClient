package com.fxp.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitMapUtil {
	public static boolean galleryFlag=false;
	public static boolean cameraFlag=false;
	public static Bitmap bitmap=null;
	public static Bitmap getPicFromBytes(byte[] bytes,BitmapFactory.Options opts){
		if(bytes!=null){
			if(opts!=null){
				return BitmapFactory.decodeByteArray(bytes,0,bytes.length,opts);
			}else{
				return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
			}
		}
		return null;
	}
	public static byte[] readStream(InputStream inSteam)throws Exception{
		byte[] buffer=new byte[1024];
		int len=-1;
		ByteArrayOutputStream outStream=new ByteArrayOutputStream();
		while((len=inSteam.read(buffer))!=-1){
			outStream.write(buffer,0,len);
		}
		byte[] data=outStream.toByteArray();
		outStream.close();
		inSteam.close();
		return data;
	}
}
