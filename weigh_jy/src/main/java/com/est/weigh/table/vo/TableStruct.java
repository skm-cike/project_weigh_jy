package com.est.weigh.table.vo;

/**
 * TableStruct entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class TableStruct implements java.io.Serializable {

	// Fields

	private String id;
	private String tableName;
	private String tableAttribute;
	private String tableAttrtype;
	private String tableId;
	private String tableDesc;
	// Constructors

	/** default constructor */
	public TableStruct() {
	}


	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableAttribute() {
		return this.tableAttribute;
	}

	public void setTableAttribute(String tableAttribute) {
		this.tableAttribute = tableAttribute;
	}

	public String getTableAttrtype() {
		return this.tableAttrtype;
	}

	public void setTableAttrtype(String tableAttrtype) {
		this.tableAttrtype = tableAttrtype;
	}

	public String getTableId() {
		return this.tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
}