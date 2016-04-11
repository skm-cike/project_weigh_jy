package com.est.sysinit.sysfilemanage.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * SysTemplate entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysTemplate implements java.io.Serializable {

	// Fields

	private Long templateid;
	private SysDir sysDir;
	private String templatename;
	private String templatecode;
	private String updateflag;
	private String validflag;
	private String fileno;
	private Set sysTemplatefunctions = new HashSet(0);
	private Set sysFileinfos = new HashSet(0);
	private Set sysTemplateparams = new HashSet(0);

	// Constructors

	/** default constructor */
	public SysTemplate() {
	}

	/** full constructor */
	public SysTemplate(SysDir sysDir, String templatename, String templatecode,
			String updateflag, String validflag, String fileno,
			Set sysTemplatefunctions, Set sysFileinfos, Set sysTemplateparams) {
		this.sysDir = sysDir;
		this.templatename = templatename;
		this.templatecode = templatecode;
		this.updateflag = updateflag;
		this.validflag = validflag;
		this.fileno = fileno;
		this.sysTemplatefunctions = sysTemplatefunctions;
		this.sysFileinfos = sysFileinfos;
		this.sysTemplateparams = sysTemplateparams;
	}

	// Property accessors

	public Long getTemplateid() {
		return this.templateid;
	}

	public void setTemplateid(Long templateid) {
		this.templateid = templateid;
	}

	public SysDir getSysDir() {
		return this.sysDir;
	}

	public void setSysDir(SysDir sysDir) {
		this.sysDir = sysDir;
	}

	public String getTemplatename() {
		return this.templatename;
	}

	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	public String getTemplatecode() {
		return this.templatecode;
	}

	public void setTemplatecode(String templatecode) {
		this.templatecode = templatecode;
	}

	public String getUpdateflag() {
		return this.updateflag;
	}

	public void setUpdateflag(String updateflag) {
		this.updateflag = updateflag;
	}

	public String getValidflag() {
		return this.validflag;
	}

	public void setValidflag(String validflag) {
		this.validflag = validflag;
	}

	public String getFileno() {
		return this.fileno;
	}

	public void setFileno(String fileno) {
		this.fileno = fileno;
	}

	public Set getSysTemplatefunctions() {
		return this.sysTemplatefunctions;
	}

	public void setSysTemplatefunctions(Set sysTemplatefunctions) {
		this.sysTemplatefunctions = sysTemplatefunctions;
	}

	public Set getSysFileinfos() {
		return this.sysFileinfos;
	}

	public void setSysFileinfos(Set sysFileinfos) {
		this.sysFileinfos = sysFileinfos;
	}

	public Set getSysTemplateparams() {
		return this.sysTemplateparams;
	}

	public void setSysTemplateparams(Set sysTemplateparams) {
		this.sysTemplateparams = sysTemplateparams;
	}

}