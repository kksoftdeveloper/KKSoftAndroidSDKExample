package com.unity3d.player;

import android.content.SharedPreferences;

public class SharePreferenceUtil {
	
	public static void saveAccessToken(String token, SharedPreferences.Editor editor) {
		editor.putString("access_token", token);
		editor.apply();
	}
	
	public static String getAccessToken(SharedPreferences sharedPreferences) {
		return sharedPreferences.getString("access_token", null);
	}
	
	public static void setIsGuest(boolean isGuest, SharedPreferences.Editor editor) {
		editor.putBoolean("is_guest_user", true);
		editor.apply();
	}
	
	public static boolean getIsGuest(SharedPreferences sharedPreferences) {
		return sharedPreferences.getBoolean("is_guest_user", false);
	}
	
	
	public static boolean clear(SharedPreferences sharedPreferences) {
		return sharedPreferences.edit().clear().commit();
	}
	
	
}
