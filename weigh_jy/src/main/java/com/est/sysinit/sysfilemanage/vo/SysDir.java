package com.est.sysinit.sysfilemanage.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * SysDir entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysDir implements java.io.Serializable {

	// Fields

	private Long dirid;
	private SysDir sysDir;
	private String dirname;
	private String dircode;
	private String dirpath;
	private String dirType;
	private Set sysDirs = new HashSet(0);
	private Set sysTemplates = new HashSet(0);
	private Set sysFileinfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public SysDir() {
	}

	// Property accessors

	public Long getDirid() {
		return this.dirid;
	}

	public void setDirid(Long dirid) {
		this.dirid = dirid;
	}

	public SysDir getSysDir() {
		return this.sysDir;
	}

	public void setSysDir(SysDir sysDir) {
		this.sysDir = sysDir;
	}

	public String getDirname() {
		return this.dirname;
	}

	public void setDirname(String dirname) {
		this.dirname = dirname;
	}

	public String getDircode() {
		return this.dircode;
	}

	public void setDircode(String dircode) {
		this.dircode = dircode;
	}

	public String getDirpath() {
		return this.dirpath;
	}

	public void setDirpath(String dirpath) {
		this.dirpath = dirpath;
	}
	

	public Set getSysDirs() {
		return this.sysDirs;
	}

	public void setSysDirs(Set sysDirs) {
		this.sysDirs = sysDirs;
	}

	public Set getSysTemplates() {
		return this.sysTemplates;
	}

	public void setSysTemplates(Set sysTemplates) {
		this.sysTemplates = sysTemplates;
	}

	public Set getSysFileinfos() {
		return this.sysFileinfos;
	}

	public void setSysFileinfos(Set sysFileinfos) {
		this.sysFileinfos = sysFileinfos;
	}

	public String getDirType() {
		return dirType;
	}

	public void setDirType(String dirType) {
		this.dirType = dirType;
	}
	

}