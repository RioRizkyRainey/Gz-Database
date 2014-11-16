package com.gz.databaselibrary;

import static com.gz.databaselibrary.GzApplication.getInstance;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.gz.databaselibrary.annotation.Column;
import com.gz.databaselibrary.annotation.Id;
import com.gz.databaselibrary.annotation.Table;

public class GzEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8594432892186377783L;

	/**
	 * @return id save row
	 */
	public long save() {
		ContentValues values = new ContentValues();
		Field[] attributes = getClass().getDeclaredFields();
		Object object = this;
		values = Utils.setValueFromField(attributes, values, object);
		return GzDatabase.getInstance().insert(getClass().getAnnotation(Table.class).name(), values);
	}

	/**
	 * update row by id
	 * @return id update
	 */
	public long update() {
		ContentValues values = new ContentValues();
		Field[] attributes = getClass().getDeclaredFields();
		Field primary = Utils.getIdFromClass(getClass());
		Object object = this;
		values = Utils.setValueFromField(attributes, values, object);
		return GzDatabase.getInstance().update(getClass().getAnnotation(Table.class).name(), values,
				primary.getAnnotation(Column.class).name() + "=?", new String[] {Utils.fieldToString(primary, this)});
	}

	public final static long delete(Class<?> classType, String whereClause, String... whereArgs) {
		return GzDatabase.getInstance().delete(classType.getAnnotation(Table.class).name(), whereClause, whereArgs);
	}

	public final static <T> ArrayList<T> toArrayList(Class<?> classType) {
		String tableName = classType.getAnnotation(Table.class).name();
		String sql = "SELECT * FROM " + tableName;
		Log.d(classType.getName(), "toArrayList");
		Log.d(classType.getName(), getInstance().getOpenHelper().toString());
		Cursor cursor = getInstance().getOpenHelper().getDatabase().rawQuery(sql, null);
		ArrayList<T> list = new ArrayList<T>();
		try {
			while (cursor.moveToNext()) {
				@SuppressWarnings("unchecked")
				T entity = (T) classType.getDeclaredConstructor().newInstance();
				Field[] attributes = entity.getClass().getDeclaredFields();
				for (Field field : attributes) {
					Utils.setFieldValueFromCursor(field, cursor, entity);
				}
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}

	public final static <T> T findById(Class<?> classType, String id) {
		String tableName = classType.getAnnotation(Table.class).name();
		Field primary = Utils.getIdFromClass(classType);
		String sql = "SELECT * FROM " + tableName + " WHERE " + primary.getAnnotation(Column.class).name() + "=?";
		Cursor cursor = getInstance().getOpenHelper().getDatabase().rawQuery(sql, new String[] { id });
		T entity = null;
		try {
			entity = (T) classType.getDeclaredConstructor().newInstance();
			while (cursor.moveToNext()) {
				Field[] attributes = entity.getClass().getDeclaredFields();
				for (Field field : attributes) {
					Utils.setFieldValueFromCursor(field, cursor, entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return entity;
	}

	public String getId(){
		Object object = null;
		Field primary = Utils.getIdFromClass(getClass());
		try {
			object = primary.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString();
	}
}
