package com.est.sysinit.sysproperty.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * SysProperty entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysProperty implements java.io.Serializable {

	// Fields

	private Long propertyid;
	private SysProperty sysProperty;
	private String propertyname;
	private String propertycode;
	private Long ordernum;
	private Set sysProperties = new HashSet(0);

	// Constructors

	/** default constructor */
	public SysProperty() {
	}

	/** minimal constructor */
	public SysProperty(String propertycode) {
		this.propertycode = propertycode;
	}

	/** full constructor */
	public SysProperty(SysProperty sysProperty, String propertyname,
			String propertycode, Long ordernum, Set sysProperties) {
		this.sysProperty = sysProperty;
		this.propertyname = propertyname;
		this.propertycode = propertycode;
		this.ordernum = ordernum;
		this.sysProperties = sysProperties;
	}

	// Property accessors

	public Long getPropertyid() {
		return this.propertyid;
	}

	public void setPropertyid(Long propertyid) {
		this.propertyid = propertyid;
	}

	public SysProperty getSysProperty() {
		return this.sysProperty;
	}

	public void setSysProperty(SysProperty sysProperty) {
		this.sysProperty = sysProperty;
	}

	public String getPropertyname() {
		return this.propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	public String getPropertycode() {
		return this.propertycode;
	}

	public void setPropertycode(String propertycode) {
		this.propertycode = propertycode;
	}

	public Long getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(Long ordernum) {
		this.ordernum = ordernum;
	}

	public Set getSysProperties() {
		return this.sysProperties;
	}

	public void setSysProperties(Set sysProperties) {
		this.sysProperties = sysProperties;
	}

}