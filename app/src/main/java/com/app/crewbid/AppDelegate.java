package com.app.crewbid;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppDelegate extends Application {

	private static SharedPreferences preferences;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
	}

	public static String getString(String key) {
		return preferences.getString(key, null);
	}

	public static boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}

	public static void storeValue(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void storeValue(String key, boolean value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}
