package com.app.crewbid.model;

import java.io.Serializable;
import java.util.Comparator;

public class ClsKeyValue implements Comparator<ClsKeyValue>, Serializable,
		Cloneable {

	private static final long serialVersionUID = 8848235319584000662L;
	private Object key;
	private Object value;

	public ClsKeyValue() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public ClsKeyValue(Object key, Object value) {
		super();
		this.setKey(key);
		this.setValue(value);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "key :" + getKey().toString() + " & value: "
				+ getValue().toString();
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public int compare(ClsKeyValue lhs, ClsKeyValue rhs) {
		// TODO Auto-generated method stub
		return lhs.getKey().toString().compareTo(rhs.getKey().toString());
	}

	public static Comparator<ClsKeyValue> sortbyValue = new Comparator<ClsKeyValue>() {
		@Override
		public int compare(ClsKeyValue lhs, ClsKeyValue rhs) {
			// TODO Auto-generated method stub
			return lhs.getValue().toString()
					.compareToIgnoreCase(rhs.getValue().toString());
		}
	};

}
