package com.est.weigh.kindcfg.vo;

/**
 * WeighVockind entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WeighVockind implements java.io.Serializable {

	// Fields

	private String id;
	private String kindName;
	private String tableName;
	private String kindDesc;
	private String kindCode;

	// Constructors

	/** default constructor */
	public WeighVockind() {
	}

	/** minimal constructor */
	public WeighVockind(String id) {
		this.id = id;
	}

	/** full constructor */
	public WeighVockind(String id, String kindName, String tableName,
			String kindDesc, String kindCode) {
		this.id = id;
		this.kindName = kindName;
		this.tableName = tableName;
		this.kindDesc = kindDesc;
		this.kindCode = kindCode;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKindName() {
		return this.kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getKindDesc() {
		return this.kindDesc;
	}

	public void setKindDesc(String kindDesc) {
		this.kindDesc = kindDesc;
	}

	public String getKindCode() {
		return this.kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

}