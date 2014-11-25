package com.gz.databaselibrary;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class GzApplication extends Application {
	
	private static GzApplication main;
	private GzDatabase gzDb;
	private String nameDatabase;
	private int versionDatabase;
	
	@Override
	public void onCreate(){
		super.onCreate();
		GzApplication.main = this;
		Log.d("HORAY", "Application start");
		nameDatabase = getMetaDataString("DATABASE");
		if(!nameDatabase.endsWith(".db"))
			nameDatabase+=".db";
		versionDatabase = getMetaDataInt("VERSION");
		GzDatabase.name = nameDatabase;
		GzDatabase.version = versionDatabase;
		gzDb = GzDatabase.getInstance(main);
	}
	/**
	 * get Application
	 * @return Application
	 */
	public static GzApplication getInstance(){
		return main;
	}
	
	public GzDatabase getOpenHelper(){
		return main.gzDb;
	}
	
	private String getMetaDataString(String name) {
        String value = null;
        PackageManager pm = this.getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(this.getPackageName(), 128);
            value = ai.metaData.getString(name);
        } catch (Exception e) {
            Log.e("Hmm...", "Couldn't find value " + name + " on meta data");
        }
        return value;
    }
	
	private Integer getMetaDataInt(String name) {
        Integer value = null;
        PackageManager pm = this.getPackageManager();

        try {
            ApplicationInfo ai = pm.getApplicationInfo(this.getPackageName(), 128);
            value = ai.metaData.getInt(name);
        } catch (Exception e) {
            Log.e("Hmm...", "Couldn't find value " + name + " on meta data");
        }
        return value;
    }

}
