package com.est.sysinit.sysmodule.vo;

/**
 * SysModulefield entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysModulefield implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = -4797925429246076272L;
	
	private Long fieldid;
	/** 关联模块表 */
	private SysModule sysModule;
	/** 字段编码，即字段名 */
	private String fieldcode;
	/** 字段中文名称 */
	private String fieldname;
	/** 排序号*/
	private Long ordernum;
	

	// Constructors

	/** default constructor */
	public SysModulefield() {
	}


	// Property accessors

	public Long getFieldid() {
		return this.fieldid;
	}

	public void setFieldid(Long fieldid) {
		this.fieldid = fieldid;
	}

	public SysModule getSysModule() {
		return this.sysModule;
	}

	public void setSysModule(SysModule sysModule) {
		this.sysModule = sysModule;
	}

	public String getFieldcode() {
		return this.fieldcode;
	}

	public void setFieldcode(String fieldcode) {
		this.fieldcode = fieldcode;
	}

	public String getFieldname() {
		return this.fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public Long getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Long ordernum) {
		this.ordernum = ordernum;
	}
	
	

}