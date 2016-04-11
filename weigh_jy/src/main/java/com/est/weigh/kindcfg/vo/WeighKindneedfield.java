package com.est.weigh.kindcfg.vo;

/**
 * WeighKindneedfield entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighKindneedfield implements java.io.Serializable {

	// Fields

	private String id;
	private String kindCode;
	private String fieldName;
	private String fieldType;
	private Short readonly;
	private String propertyCode;
	private Short isInput;
	private Short isQuery;
	private Short isShow;

	//=====临时字段=========
	private String kindid;
	// Constructors

	/** default constructor */
	public WeighKindneedfield() {
	}
	
	public String getKindid() {
		return kindid;
	}

	public void setKindid(String kindid) {
		this.id = kindid;
		this.kindid = kindid;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.kindid = id;
		this.id = id;
	}

	public String getKindCode() {
		return this.kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return this.fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}


	public Short getReadonly() {
		return readonly;
	}

	public void setReadonly(Short readonly) {
		this.readonly = readonly;
	}

	public String getPropertyCode() {
		return this.propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public Short getIsInput() {
		return isInput;
	}

	public void setIsInput(Short isInput) {
		this.isInput = isInput;
	}

	public Short getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(Short isQuery) {
		this.isQuery = isQuery;
	}

	public Short getIsShow() {
		return isShow;
	}

	public void setIsShow(Short isShow) {
		this.isShow = isShow;
	}
}