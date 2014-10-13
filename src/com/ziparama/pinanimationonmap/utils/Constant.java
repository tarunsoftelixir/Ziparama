package com.ziparama.pinanimationonmap.utils;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class Constant {
	public static final String LOGGED_IN = "already_loggedIn";
	public static final String base_url = "http://www.brandtsx.com/mobile_api/";
	public static Bitmap currentImage = null;
	public static String imagePath = null;
	private static Map<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();
	
	public static Bitmap getBitmaps(String key) {
		return bitmaps.get(key);
	}
	
	public static void setBitmaps(String key, Bitmap image) {
		bitmaps.put(key, image);
	}
	
	public static boolean isBitmapAvailable(String key){
		return bitmaps.containsKey(key);
	}
}
