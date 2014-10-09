package com.gz.databaselibrary;

import java.util.ArrayList;

public class GzList<T> extends ArrayList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465673761231012931L;
	
	public void commit(){
		for (int i = 0; i < size(); i++) {
			T entity = get(i);
			((GzEntity)entity).save();
		}
	}

}
