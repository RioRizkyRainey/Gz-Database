package com.gz.databaselibrary;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.gz.databaselibrary.annotation.Column;
import com.gz.databaselibrary.annotation.Id;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Utils {

	public static <T> void setFieldValueFromCursor(Field field, Cursor cursor, T entity) {
		field.setAccessible(true);
		Column column = field.getAnnotation(Column.class);
		if (column != null) {
			String columnName = column.name();
			int columnIndex = cursor.getColumnIndex(columnName);
			Class<?> fieldType = field.getType();
			try {
				if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
					field.set(entity, cursor.getLong(columnIndex));
				} else if (fieldType.equals(String.class)) {
					String val = cursor.getString(columnIndex);
					field.set(entity, val != null && val.equals("null") ? null : val);
				} else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
					field.set(entity, cursor.getDouble(columnIndex));
				} else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
					field.set(entity, cursor.getString(columnIndex).equals("1"));
				} else if (field.getType().getName().equals("[B")) {
					field.set(entity, cursor.getBlob(columnIndex));
				} else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
					field.set(entity, cursor.getInt(columnIndex));
				} else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
					field.set(entity, cursor.getFloat(columnIndex));
				} else if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
					field.set(entity, cursor.getShort(columnIndex));
				} else if (fieldType.equals(Timestamp.class)) {
					long l = cursor.getLong(columnIndex);
					field.set(entity, new Timestamp(l));
				} else if (fieldType.equals(Date.class)) {
					long l = cursor.getLong(columnIndex);
					field.set(entity, new Date(l));
				} else if (fieldType.equals(Calendar.class)) {
					long l = cursor.getLong(columnIndex);
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(l);
					field.set(entity, c);
				} else if (Enum.class.isAssignableFrom(fieldType)) {
					try {
						Method valueOf = field.getType().getMethod("valueOf", String.class);
						String strVal = cursor.getString(columnIndex);
						Object enumVal = valueOf.invoke(field.getType(), strVal);
						field.set(entity, enumVal);
					} catch (Exception e) {
						Log.e("Oh shit..", "Please check the type of field " + field.getName());
					}
				} else
					Log.e("Oh shit..", "Please check the type of field " + field.getName() + "("
							+ field.getType().getName() + ")");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static ContentValues setValueFromField(Field[] attributes, ContentValues values, Object object) {
		for (Field field : attributes) {
			field.setAccessible(true);
			Log.d("FIELD", field.getName());
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				Class<?> columnType = field.getType();
				Object columnValue;
				try {
					columnValue = field.get(object);
					String columnName = column.name();
					if (columnType.equals(String.class)) {
						values.put(columnName, (String) columnValue);
					} else if (columnType.equals(Short.class) || columnType.equals(short.class)) {
						values.put(columnName, (Short) columnValue);
					} else if (columnType.equals(Integer.class) || columnType.equals(int.class)) {
						values.put(columnName, (Integer) columnValue);
					} else if (columnType.equals(Long.class) || columnType.equals(long.class)) {
						values.put(columnName, (Long) columnValue);
					} else if (columnType.equals(Float.class) || columnType.equals(float.class)) {
						values.put(columnName, (Float) columnValue);
					} else if (columnType.equals(Double.class) || columnType.equals(double.class)) {
						values.put(columnName, (Double) columnValue);
					} else if (columnType.equals(Boolean.class) || columnType.equals(boolean.class)) {
						values.put(columnName, (Boolean) columnValue);
					} else if (Date.class.equals(columnType)) {
						try {
							values.put(columnName, ((Date) field.get(object)).getTime());
						} catch (NullPointerException e) {
							values.put(columnName, (Long) null);
						}
					} else if (Calendar.class.equals(columnType)) {
						try {
							values.put(columnName, ((Calendar) field.get(object)).getTimeInMillis());
						} catch (NullPointerException e) {
							values.put(columnName, (Long) null);
						}
					} else {
						if (columnValue == null) {
							values.putNull(columnName);
						} else {
							values.put(columnName, String.valueOf(columnValue));
						}
					}
				} catch (IllegalAccessException e1) {
					Log.e("Oh shit..", e1.getMessage());
				}
			}
		}
		return values;
	}

	public static String fieldToString(Field field, Object object) {
		Class<?> columnType = field.getType();
		Object columnValue;
		try {
			columnValue = field.get(object);
			if (columnType.equals(String.class)) {
				return (String) columnValue;
			} else if (columnType.equals(Short.class) || columnType.equals(short.class)) {
				return ((Short) columnValue).toString();
			} else if (columnType.equals(Integer.class) || columnType.equals(int.class)) {
				return ((Integer) columnValue).toString();
			} else if (columnType.equals(Long.class) || columnType.equals(long.class)) {
				return ((Long) columnValue).toString();
			} else if (columnType.equals(Float.class) || columnType.equals(float.class)) {
				return ((Float) columnValue).toString();
			} else if (columnType.equals(Double.class) || columnType.equals(double.class)) {
				return ((Double) columnValue).toString();
			} else if (columnType.equals(Boolean.class) || columnType.equals(boolean.class)) {
				return ((Boolean) columnValue).toString();
			} else if (Date.class.equals(columnType)) {
				try {
					return String.valueOf(((Date) field.get(object)).getTime());
				} catch (NullPointerException e) {
					return null;
				}
			} else if (Calendar.class.equals(columnType)) {
				try {
					return String.valueOf(((Calendar) field.get(object)).getTimeInMillis());
				} catch (NullPointerException e) {
					return null;
				}
			} else {
				if (columnValue == null) {
					return null;
				} else {
					return String.valueOf(columnValue);
				}
			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public static Field getIdFromClass(Class<?> classType) {
		Field[] fields = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Id idAnnotation = field.getAnnotation(Id.class);
			if (idAnnotation != null) {
				return field;
			}
		}
		throw new Error("No Primary Key on Class: " + classType.getName());
	}

}
