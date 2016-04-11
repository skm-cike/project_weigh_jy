package com.est.sysinit.sysfilemanage.vo;
// default package

import java.util.HashSet;
import java.util.Set;

/**
 * SysTemplatetype entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysTemplatetype implements java.io.Serializable {

	// Fields

	private Long typeid;
	private SysTemplatetype sysTemplatetype;
	private String typename;
	private String typecode;
	private Set sysTemplatetypes = new HashSet(0);
	private Set sysTemplates = new HashSet(0);

	// Constructors

	/** default constructor */
	public SysTemplatetype() {
	}

	/** full constructor */
	public SysTemplatetype(SysTemplatetype sysTemplatetype, String typename,
			String typecode, Set sysTemplatetypes, Set sysTemplates) {
		this.sysTemplatetype = sysTemplatetype;
		this.typename = typename;
		this.typecode = typecode;
		this.sysTemplatetypes = sysTemplatetypes;
		this.sysTemplates = sysTemplates;
	}

	// Property accessors

	public Long getTypeid() {
		return this.typeid;
	}

	public void setTypeid(Long typeid) {
		this.typeid = typeid;
	}

	public SysTemplatetype getSysTemplatetype() {
		return this.sysTemplatetype;
	}

	public void setSysTemplatetype(SysTemplatetype sysTemplatetype) {
		this.sysTemplatetype = sysTemplatetype;
	}

	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getTypecode() {
		return this.typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public Set getSysTemplatetypes() {
		return this.sysTemplatetypes;
	}

	public void setSysTemplatetypes(Set sysTemplatetypes) {
		this.sysTemplatetypes = sysTemplatetypes;
	}

	public Set getSysTemplates() {
		return this.sysTemplates;
	}

	public void setSysTemplates(Set sysTemplates) {
		this.sysTemplates = sysTemplates;
	}

}