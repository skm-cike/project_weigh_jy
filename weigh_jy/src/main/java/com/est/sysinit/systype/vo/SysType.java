package com.est.sysinit.systype.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * SysType entity.
 * 
 * @author hxing
 */

public class SysType implements java.io.Serializable {

	// Fields
	/**
	 * 合同类型ID
	 */
	private Long typeid;
	/**
	 * 类型编号
	 */
	private String typecode;
	/**
	 * 类型名称
	 */
	private String typename;
	/**
	 * 父类型 单是数据库中保存的是父类型的ID
	 */
	private SysType sysType;
	/**
	 * 内容表达式
	 */
	private String typerule;
	/**
	 * 备注
	 */
	private String typeremark;

	private Set sysTypes = new HashSet(0);

	public SysType() {
		super();
	}

	public SysType(Long typeid, String typecode, String typename,
			SysType sysType, String typerule, String typeremark, Set sysTypes) {
		super();
		this.typeid = typeid;
		this.typecode = typecode;
		this.typename = typename;
		this.sysType = sysType;
		this.typerule = typerule;
		this.typeremark = typeremark;
		this.sysTypes = sysTypes;
	}

	

	public Set getSysTypes() {
		return sysTypes;
	}

	public void setSysTypes(Set sysTypes) {
		this.sysTypes = sysTypes;
	}

	public SysType getSysType() {
		return sysType;
	}

	public String getTypecode() {
		return typecode;
	}

	public Long getTypeid() {
		return typeid;
	}

	public String getTypename() {
		return typename;
	}

	public String getTyperemark() {
		return typeremark;
	}

	public String getTyperule() {
		return typerule;
	}

	

	public void setSysType(SysType sysType) {
		this.sysType = sysType;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public void setTyperemark(String typeremark) {
		this.typeremark = typeremark;
	}

	public void setTyperule(String typerule) {
		this.typerule = typerule;
	}

}