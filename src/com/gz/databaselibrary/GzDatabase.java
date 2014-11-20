package com.gz.databaselibrary;

import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GzDatabase extends SQLiteOpenHelper {

	private static GzDatabase main;
	protected static SQLiteDatabase database;
	private  Context context;
	public static String name;
	public static int version;

	public GzDatabase(Context context) {
		super(context, name, null, version);
		this.context = context;
	}

	public static GzDatabase getInstance(Context context) {
		if (main == null) {
			main = new GzDatabase(context);
			main.open();
		}
		return main;
	}

	public static GzDatabase getInstance() {
		if (main != null) {
			return main;
		}
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("Database", "onCreate");
		Log.d("Database Version", "version:" + database.getVersion());
		
		for (int i = 1; i <= version; i++) {
			String[] query = extractQuery(i + "");
			Log.d("database ", "" + query.length);
			for (int j = 0; j < query.length; j++) {
				database.execSQL(query[j] + ";");
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		for (int i = oldVersion; i <= newVersion; i++) {
			String[] query = extractQuery(i + "");
			Log.d("database ", "" + query.length);
			for (int j = 0; j < query.length; j++) {
				database.execSQL(query[j] + ";");
			}
		}
	}

	public SQLiteDatabase getDatabase() {
		return database;
	}

	public void open() throws SQLException {
		database = getWritableDatabase();
		Log.d("database", "database open");
	}

	public void close() {
		database.close();
	}

	public long insert(String table, ContentValues cv) {
		return database.insert(table, null, cv);
	}

	public long update(String table, ContentValues cv, String whereClause,
			String[] whereArgs) {
		return database.update(table, cv, whereClause, null);
	}

	public long delete(String table, String whereClause, String whereArgs[]) {
		return database.delete(table, whereClause, whereArgs);
	}

	public long deleteTableData(String table) {
		return database.delete(table, null, null);
	}

	/**
	 * Extract query from specified file in the assets folder.
	 * 
	 * @param fileName
	 *            Name of file to be extracted.
	 * @return Extracted query.
	 */
	private String[] extractQuery(String fileName) {
		String[] returnedValue = null;
		try {
			InputStream is = context.getAssets().open(fileName);
			int size = is.available();

			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			String text = new String(buffer);

			returnedValue = text.split(";");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnedValue;
	}

}
