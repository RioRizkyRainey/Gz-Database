package com.gz.databaselibrary;

import static com.gz.databaselibrary.GzApplication.getInstance;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.gz.databaselibrary.annotation.Column;
import com.gz.databaselibrary.annotation.Id;
import com.gz.databaselibrary.annotation.Table;

public class GzEntity implements Serializable {

	/**
	 * versionUID
	 */
	private static final long serialVersionUID = -8594432892186377783L;

	/**
	 * save entities to the database. The entity class must contain the
	 * annotation {@link Table}. And has a field with annotation {@link Column}
	 * 
	 * @return id save row
	 */
	public long save() {
		ContentValues values = new ContentValues();
		Field[] attributes = getClass().getDeclaredFields();
		Object object = this;
		values = Utils.setValueFromField(attributes, object);
		return GzDatabase.getInstance().insert(getClass().getAnnotation(Table.class).name(), values);
	}

	/**
	 * update row by id. The entitu class must have a field which contains
	 * annotation {@link Id}
	 * 
	 * @return id update
	 */
	public long update() {
		ContentValues values = new ContentValues();
		Field[] attributes = getClass().getDeclaredFields();
		Field primary = Utils.getIdFromClass(getClass());
		Object object = this;
		values = Utils.setValueFromField(attributes, object);
		return GzDatabase.getInstance().update(getClass().getAnnotation(Table.class).name(), values,
				primary.getAnnotation(Column.class).name() + "=?", new String[] { Utils.fieldToString(primary, this) });
	}

	/**
	 * delete from database
	 * 
	 * @param classType
	 *            class of entity that contains the annotation {@link Table}
	 * @param whereClause
	 *            the optional WHERE clause to apply when deleting. Passing null
	 *            will delete all rows.
	 * @param whereArgs
	 *            You may include ?s in the where clause, which will be replaced
	 *            by the values from whereArgs. The values will be bound as
	 *            Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public final static long delete(Class<?> classType, String whereClause, String... whereArgs) {
		return GzDatabase.getInstance().delete(classType.getAnnotation(Table.class).name(), whereClause, whereArgs);
	}

	/**
	 * select all from the class that contains the annotation {@link Table}
	 * 
	 * @param classType
	 *            class of entity that contains the annotation {@link Table}
	 * @return
	 */
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
				T entity = (T) Utils.setEntityFromCursor(classType, cursor);
				list.add(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return list;
	}

	/**
	 * SELECT entity by id
	 * 
	 * @param classType
	 *            class of entity that contains the annotation {@link Table}
	 * @param id
	 *            String id
	 * @return null if entity not found
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T findById(Class<?> classType, String id) {
		String tableName = Utils.getTableAnnotation(classType).name();
		Field primary = Utils.getIdFromClass(classType);
		String sql = "SELECT * FROM " + tableName + " WHERE " + primary.getAnnotation(Column.class).name() + "=?";
		Cursor cursor = getInstance().getOpenHelper().getDatabase().rawQuery(sql, new String[] { id });
		T entity = null;
		try {
			while (cursor.moveToNext()) {
				entity = (T) Utils.setEntityFromCursor(classType, cursor);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		return entity;
	}

	/**
	 * returns the id field in the entity which contains annotation {@link Id}
	 * 
	 * @return return id of entity.
	 */
	public String getId() {
		Object object = null;
		Field primary = Utils.getIdFromClass(getClass());
		try {
			object = primary.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	/**
	 * 
	 * @param classType
	 *            class of entity that contains the annotation {@link Table}
	 * @param columnName
	 *            name of column argument
	 * @param value
	 * @return <code>true</code> if exist, <code>false</code> if otherwise
	 */
	public static boolean isExist(Class<?> classType, String columnName, String value) {
		String tableName = Utils.getTableAnnotation(classType).name();
		String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + "=?";
		Cursor cursor = GzDatabase.getInstance().getDatabase().rawQuery(sql, new String[] { value });
		int count = cursor.getCount();
		cursor.close();
		return count > 0 ? true : false;
	}

}
