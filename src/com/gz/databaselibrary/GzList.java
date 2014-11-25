package com.gz.databaselibrary;

import java.util.ArrayList;

/**
 * 
 * @author Rio Rizky Rainey
 *
 * @param <E> Entity
 */
public class GzList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465673761231012931L;
	
	/**
	 * for multiple insert in database
	 */
	public void commit(){
		for (int i = 0; i < size(); i++) {
			E entity = get(i);
			((GzEntity)entity).save();
		}
	}

}
