package com.est.webservice.server;


public class FieldValue {
	private String fieldName;
	private Object fieldValue;
	private int objIndex;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	public int getObjIndex() {
		return objIndex;
	}
	public void setObjIndex(int objIndex) {
		this.objIndex = objIndex;
	}
}
